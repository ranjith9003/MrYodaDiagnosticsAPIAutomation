@echo off
cd /d C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║     Running MrYoda Diagnostics API Tests                       ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
mvn clean test -DsuiteXmlFile=testng.xml
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║     Test Execution Complete                                    ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
pause
