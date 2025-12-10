package com.mryoda.diagnostics.api.endpoints;

/**
 * API Endpoints - All API endpoints used in framework
 */
public class APIEndpoints {
    
    // OTP Endpoints
    public static final String OTP_REQUEST = "/otps/getOtp";
    public static final String OTP_VERIFY = "/otps/getOtp";
    public static final String USER_CREATE = "/users/addUser";
    public static final String GET_LOCATION = "/tests/getlocations";
    public static final String GLOBAL_SEARCH = "tests/adminTests";


    
    // User/Profile Endpoints
    public static final String USER_PROFILE = "/user/profile";
    public static final String UPDATE_PROFILE = "/user/update";
    
    // Appointment Endpoints
    public static final String APPOINTMENTS = "/appointments";
    public static final String BOOK_APPOINTMENT = "/appointments/book";
    public static final String CANCEL_APPOINTMENT = "/appointments/cancel";
    
    // Lab/Diagnostic Endpoints
    public static final String LABS = "/labs";
    public static final String LAB_TESTS = "/labs/tests";
    public static final String BOOK_TEST = "/labs/book";
    
    // Report Endpoints
    public static final String REPORTS = "/reports";
    public static final String REPORT_BY_ID = "/reports/{id}";
    public static final String DOWNLOAD_REPORT = "/reports/{id}/download";

    private APIEndpoints() {
        // Private constructor to prevent instantiation
    }
}
