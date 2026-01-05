Deployment configuration templates for Tomcat 8.5 (Java 8)

This folder contains example configuration snippets you can copy into your Tomcat instance.

Templates
- tomcat/web.xml.cors: Global CORS filter for permissive access limited to your runtime hosts
- tomcat/server.xml.remoteip: RemoteIpValve to respect X-Forwarded-* headers behind a proxy

Usage
1) Edit placeholders (marked @@...@@) to match your environment.
2) Append content to your Tomcat conf files (do not replace existing content).

Example placeholders
- @@ALLOWED_ORIGIN_PATTERNS@@: comma-separated list of allowed origin patterns (e.g., https://work-1-....prod-runtime.all-hands.dev,https://work-2-....prod-runtime.all-hands.dev)

Notes
- Do not commit your live Tomcat conf directory; use these templates instead.
- Keep credentials out of these files.
