@echo off
cd /d C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
echo ========================================
echo Running Payment Validation Tests
echo ========================================
mvn clean test -Dtest=PaymentValidationAPITest
pause
