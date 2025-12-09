@echo off
echo ================================================
echo MrYoda Diagnostics API Test Execution
echo ================================================
echo.

echo Cleaning previous build...
call mvn clean

echo.
echo Running API Tests...
call mvn test

echo.
echo ================================================
echo Test Execution Completed!
echo ================================================
echo Check reports in: test-output/reports/
echo.
pause
