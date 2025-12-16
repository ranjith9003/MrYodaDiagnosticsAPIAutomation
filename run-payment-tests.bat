@echo off
REM ============================================================
REM Payment Validation Test Execution Script
REM ============================================================
REM 
REM This script runs the payment validation tests for Mr. Yoda
REM Diagnostics API automation framework.
REM 
REM Prerequisites:
REM   - All previous tests must pass (Login, Cart, CreateOrder)
REM   - Maven must be installed and in PATH
REM   - Java 8+ must be installed
REM 
REM ============================================================

echo.
echo ╔══════════════════════════════════════════════════════════╗
echo ║     MR. YODA DIAGNOSTICS - PAYMENT VALIDATION TESTS      ║
echo ╚══════════════════════════════════════════════════════════╝
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

echo ✅ Maven detected: 
mvn --version | findstr "Apache Maven"
echo.

REM Ask user which test to run
echo Select test execution mode:
echo.
echo [1] Run ONLY Payment Validation Tests
echo [2] Run Complete Flow (All tests including Payment)
echo [3] Run Payment Validation for Specific User Type
echo [4] Exit
echo.

set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto run_payment_only
if "%choice%"=="2" goto run_complete_flow
if "%choice%"=="3" goto run_specific_user
if "%choice%"=="4" goto end

echo Invalid choice. Exiting...
goto end

:run_payment_only
echo.
echo ══════════════════════════════════════════════════════════
echo Running ONLY Payment Validation Tests...
echo ══════════════════════════════════════════════════════════
echo.
echo ⚠️  WARNING: This requires CreateOrder tests to have run first!
echo.
timeout /t 3 /nobreak >nul

mvn test -Dgroups=payment

goto end

:run_complete_flow
echo.
echo ══════════════════════════════════════════════════════════
echo Running Complete Test Flow (Including Payment)...
echo ══════════════════════════════════════════════════════════
echo.
echo This will run:
echo   1. Login API
echo   2. Brand API
echo   3. Location API
echo   4. Global Search API
echo   5. Add to Cart API
echo   6. Address API
echo   7. Slot API
echo   8. Get Cart by ID API
echo   9. Create Order API
echo  10. Payment Validation API ^<-- NEW
echo.
timeout /t 3 /nobreak >nul

mvn test -DsuiteXmlFile=testng.xml

goto end

:run_specific_user
echo.
echo Select User Type:
echo.
echo [1] NON_MEMBER (Mobile: 8220220227)
echo [2] MEMBER (Mobile: 9003730394)
echo [3] NEW_USER (Dynamically created)
echo.

set /p userChoice="Enter your choice (1-3): "

if "%userChoice%"=="1" (
    echo.
    echo Running Payment Validation for NON_MEMBER...
    mvn test -Dtest=PaymentValidationAPITest#testPaymentValidation_ForNonMember
)

if "%userChoice%"=="2" (
    echo.
    echo Running Payment Validation for MEMBER...
    mvn test -Dtest=PaymentValidationAPITest#testPaymentValidation_ForMember
)

if "%userChoice%"=="3" (
    echo.
    echo Running Payment Validation for NEW_USER...
    mvn test -Dtest=PaymentValidationAPITest#testPaymentValidation_ForNewUser
)

goto end

:end
echo.
echo ══════════════════════════════════════════════════════════
echo Test execution completed!
echo ══════════════════════════════════════════════════════════
echo.
echo 📊 Check test results:
echo    - Console output above
echo    - Test reports in: target\surefire-reports
echo    - Logs in project root directory
echo.
pause
