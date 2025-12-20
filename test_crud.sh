#!/bin/bash

BASE_URL="https://work-1-htrapqnjhsfnuual.prod-runtime.all-hands.dev/expense-control"

echo "=== TESTING CRUD FUNCTIONALITY ==="
echo

echo "1. Testing Login Page Access..."
LOGIN_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/login.xhtml")
echo "Login page status: $LOGIN_STATUS"

echo
echo "2. Testing Users Page Access..."
USERS_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/admin/users.xhtml")
echo "Users page status: $USERS_STATUS"

echo
echo "3. Testing Dashboard Access..."
DASHBOARD_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/dashboard.xhtml")
echo "Dashboard page status: $DASHBOARD_STATUS"

echo
echo "4. Checking if users page loads content..."
USERS_CONTENT=$(curl -s "$BASE_URL/admin/users.xhtml" | grep -c "Gestión de Usuarios")
echo "Users page contains title: $USERS_CONTENT times"

echo
echo "5. Checking if users table is present..."
USERS_TABLE=$(curl -s "$BASE_URL/admin/users.xhtml" | grep -c "usersTable")
echo "Users table found: $USERS_TABLE times"

echo
echo "6. Checking for PrimeFaces components..."
PF_COMPONENTS=$(curl -s "$BASE_URL/admin/users.xhtml" | grep -c "p:dataTable\|p:commandButton\|p:dialog")
echo "PrimeFaces components found: $PF_COMPONENTS"

echo
echo "7. Checking for JavaScript errors in page..."
JS_ERRORS=$(curl -s "$BASE_URL/admin/users.xhtml" | grep -c "javascript.*error\|js.*error" || echo "0")
echo "JavaScript errors found: $JS_ERRORS"

echo
echo "=== CRUD TEST SUMMARY ==="
if [ "$LOGIN_STATUS" = "200" ] && [ "$USERS_STATUS" = "200" ] && [ "$DASHBOARD_STATUS" = "200" ]; then
    echo "✅ All pages are accessible (HTTP 200)"
else
    echo "❌ Some pages are not accessible"
fi

if [ "$USERS_CONTENT" -gt "0" ] && [ "$USERS_TABLE" -gt "0" ]; then
    echo "✅ Users page content is loading correctly"
else
    echo "❌ Users page content is not loading properly"
fi

if [ "$PF_COMPONENTS" -gt "0" ]; then
    echo "✅ PrimeFaces components are present"
else
    echo "❌ PrimeFaces components are missing"
fi

echo
echo "=== TESTING COMPLETE ==="