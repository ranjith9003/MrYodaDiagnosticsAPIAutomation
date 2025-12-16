@echo off
cd /d C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
echo ========================================
echo Running Complete Test Suite with Payment Verification
echo ========================================
mvn clean test -DsuiteXmlFile=testng.xml
pause
