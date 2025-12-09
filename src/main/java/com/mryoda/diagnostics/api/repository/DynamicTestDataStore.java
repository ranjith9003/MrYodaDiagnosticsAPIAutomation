package com.mryoda.diagnostics.api.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dynamic Test Data Store - Thread-safe storage for runtime test data
 * Stores dynamic data generated during test execution like IDs, tokens, etc.
 * This class acts as a session-level data repository for test execution
 */
public class DynamicTestDataStore {
    
    // Thread-safe map to store test data per thread (for parallel execution)
    private static final ThreadLocal<Map<String, Object>> testDataStore = 
        ThreadLocal.withInitial(ConcurrentHashMap::new);
    
    // Shared data across all threads (for cross-test data sharing)
    private static final Map<String, Object> sharedDataStore = new ConcurrentHashMap<>();
    
    // Common keys for storing dynamic data - MrYoda Diagnostics
    public static final String KEY_CREATED_USER_ID = "created_user_id";
    public static final String KEY_GENERATED_OTP = "generated_otp";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_PATIENT_ID = "patient_id";
    public static final String KEY_DOCTOR_ID = "doctor_id";
    public static final String KEY_LAB_ID = "lab_id";
    public static final String KEY_TEST_ID = "test_id";
    public static final String KEY_REPORT_ID = "report_id";
    public static final String KEY_APPOINTMENT_ID = "appointment_id";
    public static final String KEY_RESPONSE_TIME = "response_time";
    public static final String KEY_STATUS_CODE = "status_code";
    public static final String KEY_RESPONSE_BODY = "response_body";
    public static final String KEY_REQUEST_ID = "request_id";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    
    /**
     * Store data in thread-local storage
     * @param key The key to store the data
     * @param value The value to store
     */
    public static void set(String key, Object value) {
        testDataStore.get().put(key, value);
    }
    
    /**
     * Retrieve data from thread-local storage
     * @param key The key to retrieve the data
     * @return The stored value or null if not found
     */
    public static Object get(String key) {
        return testDataStore.get().get(key);
    }
    
    /**
     * Retrieve data as String from thread-local storage
     * @param key The key to retrieve the data
     * @return The stored value as String or null if not found
     */
    public static String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Retrieve data as Integer from thread-local storage
     * @param key The key to retrieve the data
     * @return The stored value as Integer or null if not found
     */
    public static Integer getInteger(String key) {
        Object value = get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }
    
    /**
     * Check if key exists in thread-local storage
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public static boolean containsKey(String key) {
        return testDataStore.get().containsKey(key);
    }
    
    /**
     * Remove data from thread-local storage
     * @param key The key to remove
     */
    public static void remove(String key) {
        testDataStore.get().remove(key);
    }
    
    /**
     * Clear all data from thread-local storage
     */
    public static void clear() {
        testDataStore.get().clear();
    }
    
    /**
     * Clear thread-local storage and remove thread local reference
     */
    public static void clearAll() {
        testDataStore.remove();
    }
    
    /**
     * Store data in shared storage (accessible across all threads)
     * @param key The key to store the data
     * @param value The value to store
     */
    public static void setShared(String key, Object value) {
        sharedDataStore.put(key, value);
    }
    
    /**
     * Retrieve data from shared storage
     * @param key The key to retrieve the data
     * @return The stored value or null if not found
     */
    public static Object getShared(String key) {
        return sharedDataStore.get(key);
    }
    
    /**
     * Retrieve data as String from shared storage
     * @param key The key to retrieve the data
     * @return The stored value as String or null if not found
     */
    public static String getSharedString(String key) {
        Object value = getShared(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Check if key exists in shared storage
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public static boolean containsSharedKey(String key) {
        return sharedDataStore.containsKey(key);
    }
    
    /**
     * Remove data from shared storage
     * @param key The key to remove
     */
    public static void removeShared(String key) {
        sharedDataStore.remove(key);
    }
    
    /**
     * Clear all data from shared storage
     */
    public static void clearShared() {
        sharedDataStore.clear();
    }
    
    /**
     * Get all data from thread-local storage
     * @return Map of all stored data
     */
    public static Map<String, Object> getAllData() {
        return new HashMap<>(testDataStore.get());
    }
    
    /**
     * Get all data from shared storage
     * @return Map of all shared data
     */
    public static Map<String, Object> getAllSharedData() {
        return new HashMap<>(sharedDataStore);
    }
    
    /**
     * Print all thread-local data (for debugging)
     */
    public static void printAllData() {
        System.out.println("Thread-Local Test Data:");
        testDataStore.get().forEach((key, value) -> 
            System.out.println(key + " = " + value));
    }
    
    /**
     * Print all shared data (for debugging)
     */
    public static void printAllSharedData() {
        System.out.println("Shared Test Data:");
        sharedDataStore.forEach((key, value) -> 
            System.out.println(key + " = " + value));
    }
    
    private DynamicTestDataStore() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
