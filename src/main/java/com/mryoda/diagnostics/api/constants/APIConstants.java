package com.mryoda.diagnostics.api.constants;

/**
 * API Constants - All constant values used across framework
 */
public class APIConstants {
    
    // HTTP Status Codes
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_UNPROCESSABLE_ENTITY = 422;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    
    // Content Types
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    
    // Headers
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    
    // Common Test Data
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    
    // Timeout Values (in milliseconds)
    public static final long DEFAULT_TIMEOUT = 30000;
    public static final long LONG_TIMEOUT = 60000;
    public static final long SHORT_TIMEOUT = 10000;
    
    // Retry Settings
    public static final int MAX_RETRY_COUNT = 3;
    public static final int RETRY_DELAY = 2000;
    
    private APIConstants() {
        // Private constructor to prevent instantiation
    }
}
