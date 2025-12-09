package com.mryoda.diagnostics.api.utils;

import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import java.util.List;
import java.util.Map;

/**
 * Response Utility for extracting and processing API responses
 */
public class ResponseUtil {
    
    private ResponseUtil() {
        // Private constructor
    }
    
    /**
     * Log complete response details
     */
    public static void logResponseDetails(Response response) {
        LoggerUtil.info("========== RESPONSE DETAILS ==========");
        LoggerUtil.info("Status Code: " + response.getStatusCode());
        LoggerUtil.info("Status Line: " + response.getStatusLine());
        LoggerUtil.info("Response Time: " + response.getTime() + "ms");
        LoggerUtil.info("Content Type: " + response.getContentType());
        LoggerUtil.info("Response Body: \n" + response.getBody().asPrettyString());
        LoggerUtil.info("======================================");
    }
    
    /**
     * Get JsonPath from response
     */
    public static JsonPath getJsonPath(Response response) {
        return response.jsonPath();
    }
    
    /**
     * Extract string value from response
     */
    public static String extractString(Response response, String path) {
        String value = response.jsonPath().getString(path);
        LoggerUtil.info("Extracted String from '" + path + "': " + value);
        return value;
    }
    
    /**
     * Extract integer value from response
     */
    public static Integer extractInt(Response response, String path) {
        Integer value = response.jsonPath().getInt(path);
        LoggerUtil.info("Extracted Integer from '" + path + "': " + value);
        return value;
    }
    
    /**
     * Extract list from response
     */
    public static <T> List<T> extractList(Response response, String path) {
        List<T> list = response.jsonPath().getList(path);
        LoggerUtil.info("Extracted List from '" + path + "', Size: " + (list != null ? list.size() : 0));
        return list;
    }
    
    /**
     * Extract map from response
     */
    public static <K, V> Map<K, V> extractMap(Response response, String path) {
        Map<K, V> map = response.jsonPath().getMap(path);
        LoggerUtil.info("Extracted Map from '" + path + "', Size: " + (map != null ? map.size() : 0));
        return map;
    }
    
    /**
     * Extract boolean value from response
     */
    public static Boolean extractBoolean(Response response, String path) {
        Boolean value = response.jsonPath().getBoolean(path);
        LoggerUtil.info("Extracted Boolean from '" + path + "': " + value);
        return value;
    }
    
    /**
     * Get response body as string
     */
    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }
    
    /**
     * Get pretty printed response body
     */
    public static String getPrettyResponseBody(Response response) {
        return response.getBody().asPrettyString();
    }
    
    /**
     * Get status code
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }
    
    /**
     * Get response time
     */
    public static long getResponseTime(Response response) {
        return response.getTime();
    }
    
    /**
     * Get header value
     */
    public static String getHeaderValue(Response response, String headerName) {
        return response.getHeader(headerName);
    }
    
    /**
     * Check if field exists in response
     */
    public static boolean isFieldPresent(Response response, String fieldPath) {
        try {
            Object value = response.jsonPath().get(fieldPath);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get all cookies from response
     */
    public static Map<String, String> getAllCookies(Response response) {
        return response.getCookies();
    }
    
    /**
     * Get specific cookie value
     */
    public static String getCookieValue(Response response, String cookieName) {
        return response.getCookie(cookieName);
    }
}
