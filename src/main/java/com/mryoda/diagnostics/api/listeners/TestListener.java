package com.mryoda.diagnostics.api.listeners;

import org.testng.*;
import com.mryoda.diagnostics.api.reporting.ExtentReportManager;
import com.mryoda.diagnostics.api.utils.LoggerUtil;

/**
 * TestNG Listener for test execution events and reporting
 */
public class TestListener implements ITestListener, ISuiteListener {
    
    @Override
    public void onStart(ISuite suite) {
        LoggerUtil.info("========================================");
        LoggerUtil.info("TEST SUITE STARTED: " + suite.getName());
        LoggerUtil.info("========================================");
        ExtentReportManager.initReports();
    }
    
    @Override
    public void onFinish(ISuite suite) {
        LoggerUtil.info("========================================");
        LoggerUtil.info("TEST SUITE FINISHED: " + suite.getName());
        LoggerUtil.info("========================================");
        ExtentReportManager.flushReports();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        LoggerUtil.logTestStart(testName);
        ExtentReportManager.createTest(testName, description != null ? description : testName);
        ExtentReportManager.logInfo("Test execution started: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.info("TEST PASSED: " + testName);
        ExtentReportManager.logPass("Test passed: " + testName);
        LoggerUtil.logTestEnd(testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        LoggerUtil.error("TEST FAILED: " + testName);
        LoggerUtil.error("Failure Reason: " + throwable.getMessage(), throwable);
        
        ExtentReportManager.logFail("Test failed: " + testName);
        ExtentReportManager.logFail("Failure Reason: " + throwable.getMessage());
        ExtentReportManager.logFail("Stack Trace: <pre>" + getStackTrace(throwable) + "</pre>");
        
        LoggerUtil.logTestEnd(testName);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.warn("TEST SKIPPED: " + testName);
        ExtentReportManager.logSkip("Test skipped: " + testName);
        
        if (result.getThrowable() != null) {
            ExtentReportManager.logSkip("Skip Reason: " + result.getThrowable().getMessage());
        }
        
        LoggerUtil.logTestEnd(testName);
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LoggerUtil.warn("TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.error("TEST FAILED WITH TIMEOUT: " + testName);
        ExtentReportManager.logFail("Test failed with timeout: " + testName);
        onTestFailure(result);
    }
    
    /**
     * Get stack trace as string
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
