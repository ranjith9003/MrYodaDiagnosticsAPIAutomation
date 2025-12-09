package com.mryoda.diagnostics.api.repository;

/**
 * Test Data Repository - Centralized storage for test data variables
 * This class follows the POM pattern for API testing by storing reusable test data
 * for MrYoda Diagnostics API
 */
public class TestDataRepository {
    
    // Mobile/OTP Test Data
    public static final String TEST_MOBILE = "9876543210";
    public static final String TEST_MOBILE_2 = "9876543211";
    public static final String INVALID_MOBILE = "123";
    public static final String TEST_COUNTRY_CODE = "+91";
    public static final String INVALID_COUNTRY_CODE = "+999";
    public static final String TEST_OTP = "123456";
    public static final String INVALID_OTP = "000000";
    public static final String EXPIRED_OTP = "999999";
    
    // Authentication Test Data
    public static final String VALID_AUTH_TOKEN = "Bearer your-actual-token-here";
    public static final String INVALID_AUTH_TOKEN = "Bearer invalid-token";
    public static final String EXPIRED_AUTH_TOKEN = "Bearer expired-token";
    
    // User Test Data
    public static final String TEST_USER_ID = "12345";
    public static final String TEST_PATIENT_ID = "PAT001";
    public static final String TEST_DOCTOR_ID = "DOC001";
    public static final String INVALID_USER_ID = "999999999";
    
    // Diagnostics Test Data
    public static final String TEST_LAB_ID = "LAB001";
    public static final String TEST_TEST_ID = "TEST001";
    public static final String TEST_REPORT_ID = "REP001";
    public static final String TEST_APPOINTMENT_ID = "APT001";
    
    // Invalid Test Data - For Negative Testing
    public static final String INVALID_EMAIL = "invalid-email";
    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = null;
    public static final String SPECIAL_CHARS = "!@#$%^&*()";
    public static final String LONG_STRING = "a".repeat(1000);
    public static final String SQL_INJECTION = "' OR '1'='1";
    public static final String XSS_ATTACK = "<script>alert('xss')</script>";
    
    // Boundary Test Data
    public static final int ZERO_VALUE = 0;
    public static final int NEGATIVE_VALUE = -1;
    public static final int MIN_MOBILE_LENGTH = 10;
    public static final int MAX_MOBILE_LENGTH = 10;
    public static final int OTP_LENGTH = 6;
    
    // Date Test Data
    public static final String PAST_DATE = "2020-01-01T00:00:00.000+05:30";
    public static final String CURRENT_DATE = "2025-12-08T00:00:00.000+05:30";
    public static final String FUTURE_DATE = "2030-12-31T00:00:00.000+05:30";
    public static final String INVALID_DATE_FORMAT = "2025/12/08";
    
    // Status Values
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    
    // Error Messages - Expected Error Messages
    public static final String ERROR_UNAUTHORIZED = "Authentication failed";
    public static final String ERROR_NOT_FOUND = "Resource not found";
    public static final String ERROR_BAD_REQUEST = "Invalid request";
    public static final String ERROR_VALIDATION = "Validation failed";
    public static final String ERROR_INVALID_OTP = "Invalid OTP";
    public static final String ERROR_EXPIRED_OTP = "OTP expired";
    public static final String ERROR_INVALID_MOBILE = "Invalid mobile number";
    
    // Test User Credentials
    public static final String ADMIN_USERNAME = "admin@mryoda.com";
    public static final String ADMIN_PASSWORD = "Admin@123";
    public static final String USER_USERNAME = "user@mryoda.com";
    public static final String USER_PASSWORD = "User@123";
    public static final String PATIENT_USERNAME = "patient@mryoda.com";
    public static final String PATIENT_PASSWORD = "Patient@123";
    
    // Timeout Values (in milliseconds)
    public static final long DEFAULT_TIMEOUT = 30000;
    public static final long LONG_TIMEOUT = 60000;
    public static final long SHORT_TIMEOUT = 10000;
    public static final long OTP_EXPIRY_TIME = 300000; // 5 minutes
    
    // API Version Test Data
    public static final String API_VERSION_V1 = "v1";
    public static final String API_VERSION_V2 = "v2";
    
    private TestDataRepository() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
