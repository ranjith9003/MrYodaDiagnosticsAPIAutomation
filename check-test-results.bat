@echo off
echo Checking test results for membership price validation...
echo.
type test_output_latest.log | findstr /C:"membershipPrice" /C:"Discount Rate" /C:"MEMBERSHIP PRICE" /C:"Using for total" /C:"Tests run" /C:"BUILD SUCCESS" /C:"BUILD FAILURE"
echo.
echo ========================================
echo Checking validation errors log...
echo ========================================
type validation_errors.log
echo.
pause
