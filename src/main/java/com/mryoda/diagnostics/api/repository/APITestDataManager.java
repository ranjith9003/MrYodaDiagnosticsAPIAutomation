package com.mryoda.diagnostics.api.repository;

/**
 * API Test Data Manager - Centralized manager for all test data operations
 * Provides convenient methods to access both static and dynamic test data
 * for MrYoda Diagnostics API
 */
public class APITestDataManager {
    
    // ============ Mobile/OTP Management ============
    
    /**
     * Get test mobile number (static)
     */
    public static String getTestMobile() {
        return TestDataRepository.TEST_MOBILE;
    }
    
    /**
     * Get test country code (static)
     */
    public static String getTestCountryCode() {
        return TestDataRepository.TEST_COUNTRY_CODE;
    }
    
    /**
     * Get test OTP (static)
     */
    public static String getTestOtp() {
        return TestDataRepository.TEST_OTP;
    }
    
    /**
     * Get invalid mobile for negative testing
     */
    public static String getInvalidMobile() {
        return TestDataRepository.INVALID_MOBILE;
    }
    
    /**
     * Get invalid OTP for negative testing
     */
    public static String getInvalidOtp() {
        return TestDataRepository.INVALID_OTP;
    }
    
    // ============ User Management ============
    
    /**
     * Get test user ID (static)
     */
    public static String getTestUserId() {
        return TestDataRepository.TEST_USER_ID;
    }
    
    /**
     * Get created user ID (dynamic)
     */
    public static String getCreatedUserId() {
        return DynamicTestDataStore.getString(DynamicTestDataStore.KEY_CREATED_USER_ID);
    }
    
    /**
     * Set created user ID (dynamic)
     */
    public static void setCreatedUserId(String userId) {
        DynamicTestDataStore.set(DynamicTestDataStore.KEY_CREATED_USER_ID, userId);
    }
    
    /**
     * Get test patient ID (static)
     */
    public static String getTestPatientId() {
        return TestDataRepository.TEST_PATIENT_ID;
    }
    
    /**
     * Get test doctor ID (static)
     */
    public static String getTestDoctorId() {
        return TestDataRepository.TEST_DOCTOR_ID;
    }
    
    /**
     * Get invalid user ID for negative testing
     */
    public static String getInvalidUserId() {
        return TestDataRepository.INVALID_USER_ID;
    }
    
    // ============ Diagnostics Management ============
    
    /**
     * Get test lab ID (static)
     */
    public static String getTestLabId() {
        return TestDataRepository.TEST_LAB_ID;
    }
    
    /**
     * Get test report ID (static)
     */
    public static String getTestReportId() {
        return TestDataRepository.TEST_REPORT_ID;
    }
    
    /**
     * Get test appointment ID (static)
     */
    public static String getTestAppointmentId() {
        return TestDataRepository.TEST_APPOINTMENT_ID;
    }
    
    // ============ Authentication Management ============
    
    /**
     * Get auth token (dynamic if set, else from static)
     */
    public static String getAuthToken() {
        String dynamicToken = DynamicTestDataStore.getString(DynamicTestDataStore.KEY_AUTH_TOKEN);
        return dynamicToken != null ? dynamicToken : TestDataRepository.VALID_AUTH_TOKEN;
    }
    
    /**
     * Set auth token (dynamic)
     */
    public static void setAuthToken(String token) {
        DynamicTestDataStore.set(DynamicTestDataStore.KEY_AUTH_TOKEN, token);
    }
    
    /**
     * Get valid auth token (static)
     */
    public static String getValidAuthToken() {
        return TestDataRepository.VALID_AUTH_TOKEN;
    }
    
    /**
     * Get invalid auth token (static - for negative testing)
     */
    public static String getInvalidAuthToken() {
        return TestDataRepository.INVALID_AUTH_TOKEN;
    }
    
    /**
     * Get expired auth token (static - for negative testing)
     */
    public static String getExpiredAuthToken() {
        return TestDataRepository.EXPIRED_AUTH_TOKEN;
    }
    
    // ============ Status Management ============
    
    /**
     * Get pending status
     */
    public static String getPendingStatus() {
        return TestDataRepository.STATUS_PENDING;
    }
    
    /**
     * Get completed status
     */
    public static String getCompletedStatus() {
        return TestDataRepository.STATUS_COMPLETED;
    }
    
    /**
     * Get active status
     */
    public static String getActiveStatus() {
        return TestDataRepository.STATUS_ACTIVE;
    }
    
    /**
     * Get inactive status
     */
    public static String getInactiveStatus() {
        return TestDataRepository.STATUS_INACTIVE;
    }
    
    // ============ Invalid Data for Testing ============
    
    /**
     * Get invalid email for validation testing
     */
    public static String getInvalidEmail() {
        return TestDataRepository.INVALID_EMAIL;
    }
    
    /**
     * Get empty string for testing
     */
    public static String getEmptyString() {
        return TestDataRepository.EMPTY_STRING;
    }
    
    /**
     * Get SQL injection string for security testing
     */
    public static String getSqlInjection() {
        return TestDataRepository.SQL_INJECTION;
    }
    
    /**
     * Get XSS attack string for security testing
     */
    public static String getXssAttack() {
        return TestDataRepository.XSS_ATTACK;
    }
    
    // ============ Dynamic Data Management ============
    
    /**
     * Clear all dynamic test data
     */
    public static void clearDynamicData() {
        DynamicTestDataStore.clear();
    }
    
    /**
     * Clear all test data (thread-local and shared)
     */
    public static void clearAllData() {
        DynamicTestDataStore.clearAll();
        DynamicTestDataStore.clearShared();
    }
    
    /**
     * Store response time for reporting
     */
    public static void setResponseTime(long responseTime) {
        DynamicTestDataStore.set(DynamicTestDataStore.KEY_RESPONSE_TIME, responseTime);
    }
    
    /**
     * Get stored response time
     */
    public static Long getResponseTime() {
        return (Long) DynamicTestDataStore.get(DynamicTestDataStore.KEY_RESPONSE_TIME);
    }
    
    /**
     * Store status code for validation
     */
    public static void setStatusCode(int statusCode) {
        DynamicTestDataStore.set(DynamicTestDataStore.KEY_STATUS_CODE, statusCode);
    }
    
    /**
     * Get stored status code
     */
    public static Integer getStatusCode() {
        return DynamicTestDataStore.getInteger(DynamicTestDataStore.KEY_STATUS_CODE);
    }
    
    /**
     * Store generated OTP (dynamic)
     */
    public static void setGeneratedOtp(String otp) {
        DynamicTestDataStore.set("generated_otp", otp);
    }
    
    /**
     * Get generated OTP (dynamic)
     */
    public static String getGeneratedOtp() {
        return DynamicTestDataStore.getString("generated_otp");
    }
    
    private APITestDataManager() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
