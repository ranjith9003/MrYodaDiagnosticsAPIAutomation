@echo off
echo ================================================
echo MrYoda Diagnostics API Smoke Test Execution
echo ================================================
echo.

echo Running Smoke Tests...
call mvn clean test -DsuiteXmlFile=testng-smoke.xml

echo.
echo ================================================
echo Smoke Test Execution Completed!
echo ================================================
pause
