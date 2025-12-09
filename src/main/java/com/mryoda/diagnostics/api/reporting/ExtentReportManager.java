package com.mryoda.diagnostics.api.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extent Report Manager for generating HTML reports
 */
public class ExtentReportManager {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    /**
     * Initialize Extent Reports
     */
    public static void initReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportPath = ConfigLoader.getConfig().reportPath() + "/ExtentReport_" + timestamp + ".html";
            
            // Create directory if not exists
            new File(ConfigLoader.getConfig().reportPath()).mkdirs();
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("MrYoda Diagnostics API Test Report");
            sparkReporter.config().setReportName("API Automation Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "MrYoda Diagnostics API");
            extent.setSystemInfo("Environment", ConfigLoader.getConfig().environment());
            extent.setSystemInfo("Base URL", ConfigLoader.getConfig().baseUrl());
            extent.setSystemInfo("Tester", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
    }
    
    /**
     * Create a test in the report
     */
    public static void createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);
    }
    
    /**
     * Get current test
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Log info message
     */
    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().log(Status.INFO, message);
        }
    }
    
    /**
     * Log pass message
     */
    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().log(Status.PASS, message);
        }
    }
    
    /**
     * Log fail message
     */
    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().log(Status.FAIL, message);
        }
    }
    
    /**
     * Log skip message
     */
    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().log(Status.SKIP, message);
        }
    }
    
    /**
     * Log warning message
     */
    public static void logWarning(String message) {
        if (getTest() != null) {
            getTest().log(Status.WARNING, message);
        }
    }
    
    /**
     * Assign category to test
     */
    public static void assignCategory(String... categories) {
        if (getTest() != null) {
            getTest().assignCategory(categories);
        }
    }
    
    /**
     * Assign author to test
     */
    public static void assignAuthor(String... authors) {
        if (getTest() != null) {
            getTest().assignAuthor(authors);
        }
    }
    
    /**
     * Flush reports
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
