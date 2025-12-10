package com.mryoda.diagnostics.api.utils;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RequestContext {

    // ============================================================
    // TOKENS (MEMBER / EXISTING MEMBER / NEW USER)
    // ============================================================
    private static String memberToken;
    private static String existingMemberToken;
    private static String newUserToken;

    // ============================================================
    // USER DETAILS (Stored per user type)
    // ============================================================
    private static String memberFirstName, memberLastName, memberUserId;
    private static String existingMemberFirstName, existingMemberLastName, existingMemberUserId;
    private static String newUserFirstName, newUserLastName, newUserUserId;

    // Generic (used by TokenManager)
    private static String token;
    private static String firstName;
    private static String lastName;
    private static String userId;
    private static String mobile;

    // ============================================================
    // LOCATION STORAGE
    // ============================================================
    private static final Map<String, String> locations = new HashMap<>();
    private static String selectedLocationId;

    public static void storeLocation(String title, String id) {
        locations.put(title, id);
    }

    public static String getLocationId(String title) {
        return locations.get(title);
    }

    public static Map<String, String> getAllLocations() {
        return locations;
    }

    public static void setSelectedLocation(String title) {
        String id = locations.get(title);
        if (id == null) {
            throw new RuntimeException("❌ Location not found in RequestContext: " + title);
        }
        selectedLocationId = id;
    }

    public static String getSelectedLocationId() {
        return selectedLocationId;
    }

    // ============================================================
    // GLOBAL SEARCH TEST STORAGE
    // ============================================================
    /**
     * Each testName → details map
     * details = { id, test_id, price, original_price, discount_percentage, type }
     */
    private static final Map<String, Map<String, Object>> storedTests = new HashMap<>();

    public static void storeTestDetails(String testName,
                                        String id,
                                        String testId,
                                        int price,
                                        int originalPrice,
                                        int discountPercentage,
                                        String type) {

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("test_id", testId);
        data.put("price", price);
        data.put("original_price", originalPrice);
        data.put("discount_percentage", discountPercentage);
        data.put("type", type);

        storedTests.put(testName, data);
    }

    public static Map<String, Object> getTestDetails(String testName) {
        return storedTests.get(testName);
    }

    public static Map<String, Map<String, Object>> getAllStoredTests() {
        return storedTests;
    }

    // ============================================================
    // SETTERS
    // ============================================================
    public static void setMemberToken(String v) { memberToken = v; }
    public static void setMemberFirstName(String v) { memberFirstName = v; }
    public static void setMemberLastName(String v) { memberLastName = v; }
    public static void setMemberUserId(String v) { memberUserId = v; }

    public static void setExistingMemberToken(String v) { existingMemberToken = v; }
    public static void setExistingMemberFirstName(String v) { existingMemberFirstName = v; }
    public static void setExistingMemberLastName(String v) { existingMemberLastName = v; }
    public static void setExistingMemberUserId(String v) { existingMemberUserId = v; }

    public static void setNewUserToken(String v) { newUserToken = v; }
    public static void setNewUserFirstName(String v) { newUserFirstName = v; }
    public static void setNewUserLastName(String v) { newUserLastName = v; }
    public static void setNewUserUserId(String v) { newUserUserId = v; }

    public static void setMobile(String v) { mobile = v; }

    public static void setToken(String v) { token = v; }
    public static void setFirstName(String v) { firstName = v; }
    public static void setLastName(String v) { lastName = v; }
    public static void setUserId(String v) { userId = v; }

    // ============================================================
    // GETTERS
    // ============================================================
    public static String getMemberToken() { return memberToken; }
    public static String getMemberFirstName() { return memberFirstName; }
    public static String getMemberLastName() { return memberLastName; }
    public static String getMemberUserId() { return memberUserId; }

    public static String getExistingMemberToken() { return existingMemberToken; }
    public static String getExistingMemberFirstName() { return existingMemberFirstName; }
    public static String getExistingMemberLastName() { return existingMemberLastName; }
    public static String getExistingMemberUserId() { return existingMemberUserId; }

    public static String getNewUserToken() { return newUserToken; }
    public static String getNewUserFirstName() { return newUserFirstName; }
    public static String getNewUserLastName() { return newUserLastName; }
    public static String getNewUserUserId() { return newUserUserId; }

    public static String getMobile() { return mobile; }

    public static String getToken() { return token; }
    public static String getFirstName() { return firstName; }
    public static String getLastName() { return lastName; }
    public static String getUserId() { return userId; }
 // ----------------------------------------------------
 // GLOBAL SEARCH TEST STORAGE
 // ----------------------------------------------------
 private static List<Map<String, Object>> globalTests = new ArrayList<>();

 public static void storeGlobalTests(List<Map<String, Object>> tests) {
     globalTests.clear();
     globalTests.addAll(tests);
 }

 public static List<Map<String, Object>> getGlobalTests() {
     return globalTests;
 }
//Stores selected tests from global search
private static final Map<String, Map<String, Object>> selectedTests = new HashMap<>();
//----------------------------------------------------
//SELECTED TEST STORAGE
//----------------------------------------------------
public static void storeTest(String testName, Map<String, Object> testData) {
 selectedTests.put(testName, testData);
}

public static Map<String, Object> getTest(String testName) {
 return selectedTests.get(testName);
}

public static Map<String, Map<String, Object>> getAllTests() {
 return selectedTests;
}

}
