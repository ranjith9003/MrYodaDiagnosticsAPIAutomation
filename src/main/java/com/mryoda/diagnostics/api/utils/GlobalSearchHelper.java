package com.mryoda.diagnostics.api.utils;

import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import io.restassured.response.Response;

import java.util.*;

public class GlobalSearchHelper {

    /**
     * Perform global test search using search string and selected location
     */
    public static Response searchTests(String searchString, String locationTitle) {

        RequestContext.setSelectedLocation(locationTitle);
        String locationId = RequestContext.getSelectedLocationId();
        
        // Get token from RequestContext
        String token = RequestContext.getMemberToken();
        if (token == null) {
            token = RequestContext.getToken();
        }
        
        // Verify token is not null
        if (token == null) {
            throw new RuntimeException("❌ Token is null! Please login first to generate a token.");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("page", 1);
        body.put("limit", 50);
        body.put("search_string", searchString);
        body.put("sort_by", "Type");
        body.put("location", locationId);

        System.out.println("\n🔍 SEARCHING TESTS → '" + searchString + "' @ Location: " + locationTitle);

        return new RequestBuilder()
                .setEndpoint(APIEndpoints.GLOBAL_SEARCH)
                .addHeader("Authorization", "Bearer " + token)
                .setRequestBody(body)
                .expectStatus(200)
                .post();
    }

    /**
     * Extract only the required tests & store them in RequestContext
     * Maps all fields from API response JSON structure
     */
    public static void extractAndStoreTests(Response response, String[] requiredTests) {

        // Null check for response
        if (response == null) {
            throw new RuntimeException("❌ Response is null!");
        }

        List<Map<String, Object>> allTests = response.jsonPath().getList("data");

        // Null check for allTests
        if (allTests == null) {
            System.out.println("⚠️ No tests found in response (data field is null)");
            allTests = new ArrayList<>();
        }

        System.out.println("📦 TOTAL TESTS RECEIVED FROM API: " + allTests.size());
        RequestContext.storeGlobalTests(allTests);

        for (String testName : requiredTests) {

            Map<String, Object> found = allTests.stream()
                    .filter(t -> {
                        Object testNameObj = t.get("test_name");
                        return testNameObj != null && testNameObj.toString().equalsIgnoreCase(testName);
                    })
                    .findFirst()
                    .orElse(null);

            if (found == null) {
                System.out.println("⚠️ Test not found in search results: " + testName);
                AssertionUtil.verifyTrue(false, "Test not found in search results: " + testName);
                continue;
            }

            // Extract all fields from the JSON response
            Map<String, Object> storeData = new HashMap<>();
            
            // Basic fields
            storeData.put("_id", found.get("_id"));
            storeData.put("test_id", found.get("test_id"));
            storeData.put("test_name", found.get("test_name"));
            storeData.put("slug", found.get("slug"));
            storeData.put("Type", found.get("Type"));
            storeData.put("status", found.get("status"));
            
            // Pricing fields
            storeData.put("price", found.get("price") != null ? found.get("price") : 0);
            storeData.put("original_price", found.get("original_price") != null ? found.get("original_price") : 0);
            storeData.put("b2b_price", found.get("b2b_price"));
            storeData.put("discount_percentage", found.get("discount_percentage") != null ? found.get("discount_percentage") : 0);
            storeData.put("discount_rate", found.get("discount_rate"));
            storeData.put("rewards_percentage", found.get("rewards_percentage"));
            storeData.put("membership_discount", found.get("membership_discount") != null ? found.get("membership_discount") : 0);
            storeData.put("courier_charges", found.get("courier_charges") != null ? found.get("courier_charges") : 0);
            storeData.put("cpt_price", found.get("cpt_price") != null ? found.get("cpt_price") : 0);
            storeData.put("actual_cprt_price", found.get("actual_cprt_price") != null ? found.get("actual_cprt_price") : 0);
            storeData.put("cpt_comment", found.get("cpt_comment"));
            
            // Details fields
            storeData.put("specimen", found.get("specimen"));
            storeData.put("turn_around_time", found.get("turn_around_time"));
            storeData.put("home_collection", found.get("home_collection"));
            storeData.put("pre_test_information", found.get("pre_test_information"));
            storeData.put("description", found.get("description"));
            storeData.put("comment", found.get("comment"));
            storeData.put("usage", found.get("usage"));
            storeData.put("result_interpretation", found.get("result_interpretation"));
            
            // Boolean flags
            storeData.put("popular", found.get("popular") != null ? found.get("popular") : false);
            storeData.put("speciality_tests", found.get("speciality_tests") != null ? found.get("speciality_tests") : false);
            storeData.put("frequently_booked", found.get("frequently_booked") != null ? found.get("frequently_booked") : false);
            
            // Array/List fields
            storeData.put("components", found.get("components"));
            storeData.put("locations", found.get("locations"));
            storeData.put("genders", found.get("genders"));
            storeData.put("business_type", found.get("business_type"));
            storeData.put("stability", found.get("stability"));
            storeData.put("method", found.get("method"));
            storeData.put("organ", found.get("organ"));
            storeData.put("diseases", found.get("diseases"));
            storeData.put("search_keywords", found.get("search_keywords"));
            storeData.put("other_names", found.get("other_names"));
            storeData.put("frequently_asked_questions", found.get("frequently_asked_questions"));
            storeData.put("department", found.get("department"));
            storeData.put("doctor_speciality", found.get("doctor_speciality"));
            
            // Timestamps
            storeData.put("createdAt", found.get("createdAt"));
            storeData.put("updatedAt", found.get("updatedAt"));
            
            // Other fields
            storeData.put("index", found.get("index"));
            
            // Store the complete raw object for reference
            storeData.put("raw", found);

            RequestContext.storeTest(testName, storeData);

            System.out.println("\n🎯 MATCHED & STORED TEST: " + testName);
            System.out.println("   Test ID       : " + storeData.get("test_id"));
            System.out.println("   Product ID    : " + storeData.get("_id"));
            System.out.println("   Price         : ₹" + storeData.get("price"));
            System.out.println("   Original Price: ₹" + storeData.get("original_price"));
            System.out.println("   Type          : " + storeData.get("Type"));
            System.out.println("   Status        : " + storeData.get("status"));
            System.out.println("   Home Collection: " + storeData.get("home_collection"));
        }
        
        System.out.println("\n✅ All requested tests extracted and stored successfully!");
    }
    public static Response searchTestsByFullNames(String[] fullTestNames, String locationName) {

        if (fullTestNames == null || fullTestNames.length == 0) {
            throw new RuntimeException("❌ No test names provided to search!");
        }

        // Extract keyword from first test — backend doesn't support full-name search
        String searchKeyword = fullTestNames[0].split(" ")[0].toLowerCase().trim();

        System.out.println("🔍 SEARCH KEYWORD SELECTED FROM FULL NAME: " + searchKeyword);

        // Set and fetch the location ID
        RequestContext.setSelectedLocation(locationName);
        String locationId = RequestContext.getSelectedLocationId();

        System.out.println("📌 SEARCH LOCATION: " + locationName + " → " + locationId);

        // Get token from RequestContext
        String token = RequestContext.getMemberToken();
        if (token == null) {
            token = RequestContext.getToken();
        }
        
        // Verify token is not null
        if (token == null) {
            throw new RuntimeException("❌ Token is null! Please login first to generate a token.");
        }

        // Call Global Search POST API
        return new RequestBuilder()
                .setEndpoint(APIEndpoints.GLOBAL_SEARCH)
                .addHeader("Authorization", "Bearer " + token)
                .addBodyParam("page", 1)
                .addBodyParam("limit", 50)
                .addBodyParam("search_string", searchKeyword)
                .addBodyParam("sort_by", "Type")
                .addBodyParam("location", locationId)
                .expectStatus(200)
                .post();
    }
    
    // ============================================================
    // HELPER METHODS TO RETRIEVE STORED TEST FIELDS
    // ============================================================
    
    /**
     * Get test field value with type safety
     */
    public static Object getTestField(String testName, String fieldName) {
        Map<String, Object> test = RequestContext.getTest(testName);
        return test != null ? test.get(fieldName) : null;
    }
    
    /**
     * Get test ID (e.g., GEN110)
     */
    public static String getTestId(String testName) {
        Object value = getTestField(testName, "test_id");
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get product ID (MongoDB _id)
     */
    public static String getProductId(String testName) {
        Object value = getTestField(testName, "_id");
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get test price
     */
    public static double getTestPrice(String testName) {
        Object value = getTestField(testName, "price");
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Get original price
     */
    public static double getOriginalPrice(String testName) {
        Object value = getTestField(testName, "original_price");
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Get home collection availability
     */
    public static String getHomeCollection(String testName) {
        Object value = getTestField(testName, "home_collection");
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get test status (ACTIVE/INACTIVE)
     */
    public static String getTestStatus(String testName) {
        Object value = getTestField(testName, "status");
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get test type (diagnostics, etc.)
     */
    public static String getTestType(String testName) {
        Object value = getTestField(testName, "Type");
        return value != null ? value.toString() : null;
    }
    
    /**
     * Print all details of a stored test
     */
    public static void printTestDetails(String testName) {
        Map<String, Object> test = RequestContext.getTest(testName);
        
        if (test == null) {
            System.out.println("❌ Test not found: " + testName);
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  TEST DETAILS: " + testName);
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("🆔 Product ID        : " + test.get("_id"));
        System.out.println("🔢 Test ID           : " + test.get("test_id"));
        System.out.println("🧪 Test Name         : " + test.get("test_name"));
        System.out.println("🔗 Slug              : " + test.get("slug"));
        System.out.println("💰 Price             : ₹" + test.get("price"));
        System.out.println("💵 Original Price    : ₹" + test.get("original_price"));
        System.out.println("💳 B2B Price         : " + (test.get("b2b_price") != null ? "₹" + test.get("b2b_price") : "N/A"));
        System.out.println("🏷️  Discount %        : " + test.get("discount_percentage") + "%");
        System.out.println("💸 Discount Rate     : ₹" + test.get("discount_rate"));
        System.out.println("🎁 Rewards %         : " + test.get("rewards_percentage") + "%");
        System.out.println("👥 Membership Disc.  : " + test.get("membership_discount") + "%");
        System.out.println("📦 Courier Charges   : ₹" + test.get("courier_charges"));
        System.out.println("🏠 Home Collection   : " + test.get("home_collection"));
        System.out.println("🧬 Specimen          : " + test.get("specimen"));
        System.out.println("⏰ Turn Around Time  : " + test.get("turn_around_time"));
        System.out.println("📍 Status            : " + test.get("status"));
        System.out.println("🏷️  Type              : " + test.get("Type"));
        System.out.println("⭐ Popular           : " + test.get("popular"));
        System.out.println("🔬 Speciality Tests  : " + test.get("speciality_tests"));
        System.out.println("📊 Frequently Booked : " + test.get("frequently_booked"));
        System.out.println("👫 Genders           : " + test.get("genders"));
        System.out.println("🏢 Business Type     : " + test.get("business_type"));
        System.out.println("📍 Locations Count   : " + (test.get("locations") instanceof List ? ((List<?>)test.get("locations")).size() : 0));
        System.out.println("🧩 Components Count  : " + (test.get("components") instanceof List ? ((List<?>)test.get("components")).size() : 0));
        System.out.println("📝 Description       : " + (test.get("description") != null && !test.get("description").toString().isEmpty() ? test.get("description") : "N/A"));
        System.out.println("ℹ️  Pre-Test Info     : " + (test.get("pre_test_information") != null && !test.get("pre_test_information").toString().isEmpty() ? test.get("pre_test_information") : "N/A"));
        System.out.println("════════════════════════════════════════════════════════\n");
    }
}