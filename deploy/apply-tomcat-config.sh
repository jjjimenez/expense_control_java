#!/usr/bin/env bash
# Apply Tomcat conf templates (CORS + RemoteIpValve) idempotently
# Usage:
#   ./deploy/apply-tomcat-config.sh -c /path/to/tomcat -o "https://work-1-...,.https://work-2-..."
# Options:
#   -c  CATALINA_BASE (Tomcat base dir containing conf/)
#   -o  Allowed origin patterns (comma-separated) to replace @@ALLOWED_ORIGIN_PATTERNS@@
#   -n  Dry run (print what would change)

set -euo pipefail

CATALINA_BASE=""
ALLOWED_ORIGIN_PATTERNS=""
DRY_RUN=0

while getopts ":c:o:n" opt; do
  case $opt in
    c) CATALINA_BASE="$OPTARG" ;;
    o) ALLOWED_ORIGIN_PATTERNS="$OPTARG" ;;
    n) DRY_RUN=1 ;;
    :) echo "Missing option value for -$OPTARG" >&2; exit 2 ;;
    \?) echo "Unknown option: -$OPTARG" >&2; exit 2 ;;
  esac
done

if [[ -z "${CATALINA_BASE}" ]]; then
  echo "Error: -c CATALINA_BASE is required" >&2
  exit 2
fi

CONF_DIR="${CATALINA_BASE%/}/conf"
WEB_XML="$CONF_DIR/web.xml"
SERVER_XML="$CONF_DIR/server.xml"

if [[ ! -f "$WEB_XML" || ! -f "$SERVER_XML" ]]; then
  echo "Error: Could not find conf/web.xml or conf/server.xml under $CONF_DIR" >&2
  exit 2
fi

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
CORS_SNIPPET_TMPL="$SCRIPT_DIR/tomcat/web.xml.cors"
REMOTEIP_SNIPPET="$SCRIPT_DIR/tomcat/server.xml.remoteip"

if [[ ! -f "$CORS_SNIPPET_TMPL" || ! -f "$REMOTEIP_SNIPPET" ]]; then
  echo "Error: Missing template files under $SCRIPT_DIR/tomcat" >&2
  exit 2
fi

backup_file() {
  local f="$1"
  local ts
  ts="$(date +%Y%m%d%H%M%S)"
  cp "$f" "${f}.bak.${ts}"
  echo "Backed up $f -> ${f}.bak.${ts}"
}

apply_cors() {
  if grep -q "BEGIN OPENHANDS CORS" "$WEB_XML"; then
    echo "CORS snippet already present in web.xml; skipping"
    return
  fi
  if [[ -z "$ALLOWED_ORIGIN_PATTERNS" ]]; then
    echo "Warning: -o ALLOWED_ORIGIN_PATTERNS not provided; inserting placeholder as-is" >&2
  fi
  local tmp_snip
  tmp_snip="$(mktemp)"
  sed "s#@@ALLOWED_ORIGIN_PATTERNS@@#${ALLOWED_ORIGIN_PATTERNS//#/\\#}#g" "$CORS_SNIPPET_TMPL" > "$tmp_snip"
  if [[ $DRY_RUN -eq 1 ]]; then
    echo "[DRY RUN] Would append CORS snippet to $WEB_XML"
  else
    backup_file "$WEB_XML"
    printf '\n\n' >> "$WEB_XML"
    cat "$tmp_snip" >> "$WEB_XML"
    echo "Appended CORS snippet to web.xml"
  fi
  rm -f "$tmp_snip"
}

apply_remoteip() {
  if grep -q "BEGIN OPENHANDS REMOTEIPVALVE" "$SERVER_XML"; then
    echo "RemoteIpValve snippet already present in server.xml; skipping"
    return
  fi
  if [[ $DRY_RUN -eq 1 ]]; then
    echo "[DRY RUN] Would insert RemoteIpValve into $SERVER_XML after <Engine name=\"Catalina\">"
    return
  fi
  backup_file "$SERVER_XML"
  # Insert snippet after the <Engine name="Catalina" ...> line
  local tmp_out
  tmp_out="$(mktemp)"
  # sed inserts the file content after the first match
  sed "/<Engine[[:space:]]*name=\"Catalina\"[^">]*>/,/<Engine[[:space:]]*name=\"Catalina\"[^">]*>/ !b;" "$SERVER_XML" >/dev/null 2>&1 || true
  sed "/<Engine[[:space:]]*name=\"Catalina\"[^"]*>/r $REMOTEIP_SNIPPET" "$SERVER_XML" > "$tmp_out"
  mv "$tmp_out" "$SERVER_XML"
  echo "Inserted RemoteIpValve into server.xml"
}

apply_cors
apply_remoteip

echo "Done. Restart Tomcat to apply changes: $CATALINA_BASE/bin/shutdown.sh && $CATALINA_BASE/bin/startup.sh"
