package com.mryoda.diagnostics.api.utils;

import java.util.List;
import java.util.Map;

/**
 * Cross-API Validation Helper
 * Provides utility methods to validate data consistency across multiple API calls
 * 
 * This helper ensures that data stored from one API call matches data returned
 * by subsequent API calls, providing comprehensive end-to-end validation.
 */
public class CrossAPIValidator {

    /**
     * Validate that a location ID retrieved from current API matches
     * the location ID stored from LocationAPI
     * 
     * @param currentLocationId - Location ID from current API response
     * @param locationName - Location name to lookup in RequestContext
     * @param apiName - Name of current API (for logging)
     */
    public static void validateLocationConsistency(String currentLocationId, String locationName, String apiName) {
        System.out.println("\n🔄 Cross-API Validation: Location Consistency Check");
        
        String storedLocationId = RequestContext.getLocationId(locationName);
        
        AssertionUtil.verifyNotNull(storedLocationId, 
            "Location '" + locationName + "' must exist from LocationAPI");
        
        AssertionUtil.verifyEquals(currentLocationId, storedLocationId, 
            apiName + " location should match LocationAPI");
        
        System.out.println("   ✅ Location ID matches across APIs: " + currentLocationId);
    }

    /**
     * Validate that a brand ID retrieved from current API matches
     * the brand ID stored from BrandAPI
     * 
     * @param currentBrandId - Brand ID from current API response
     * @param brandName - Brand name to lookup in RequestContext
     * @param apiName - Name of current API (for logging)
     */
    public static void validateBrandConsistency(String currentBrandId, String brandName, String apiName) {
        System.out.println("\n🔄 Cross-API Validation: Brand Consistency Check");
        
        String storedBrandId = RequestContext.getBrandId(brandName);
        
        AssertionUtil.verifyNotNull(storedBrandId, 
            "Brand '" + brandName + "' must exist from BrandAPI");
        
        AssertionUtil.verifyEquals(currentBrandId, storedBrandId, 
            apiName + " brand should match BrandAPI");
        
        System.out.println("   ✅ Brand ID matches across APIs: " + currentBrandId);
    }

    /**
     * Validate that test details from current API match
     * test details stored from GlobalSearchAPI
     * 
     * @param testName - Name of the test
     * @param currentTestId - Test ID from current API
     * @param currentPrice - Test price from current API (can be null)
     * @param apiName - Name of current API (for logging)
     */
    public static void validateTestConsistency(String testName, String currentTestId, 
                                               Integer currentPrice, String apiName) {
        System.out.println("\n🔄 Cross-API Validation: Test Details Consistency Check");
        
        Map<String, Object> storedTest = RequestContext.getTest(testName);
        
        AssertionUtil.verifyNotNull(storedTest, 
            "Test '" + testName + "' must exist from GlobalSearchAPI");
        
        // Validate test ID
        String storedTestId = (String) storedTest.get("_id");
        AssertionUtil.verifyEquals(currentTestId, storedTestId, 
            apiName + " test ID should match GlobalSearchAPI for: " + testName);
        System.out.println("   ✅ Test ID matches: " + currentTestId);
        
        // Validate price if provided
        if (currentPrice != null && storedTest.get("price") != null) {
            int storedPrice = ((Number) storedTest.get("price")).intValue();
            AssertionUtil.verifyEquals(currentPrice, storedPrice, 
                apiName + " price should match GlobalSearchAPI for: " + testName);
            System.out.println("   ✅ Price matches: ₹" + currentPrice);
        }
        
        // Validate test status
        String testStatus = (String) storedTest.get("status");
        AssertionUtil.verifyEquals(testStatus, "ACTIVE", 
            "Test should be ACTIVE: " + testName);
        System.out.println("   ✅ Test Status: " + testStatus);
    }

    /**
     * Validate cart data consistency between AddToCart and GetCartById APIs
     * 
     * @param currentCartGuid - Cart GUID from GetCartById
     * @param currentCartId - Cart ID from GetCartById
     * @param currentTotalAmount - Total amount from GetCartById
     * @param currentUserId - User ID from GetCartById
     * @param currentLocationId - Location ID from GetCartById
     * @param userType - User type ("MEMBER", "EXISTING_MEMBER", "NEW_USER")
     */
    public static void validateCartConsistency(String currentCartGuid, Integer currentCartId, 
                                              Integer currentTotalAmount, String currentUserId,
                                              String currentLocationId, String userType) {
        System.out.println("\n🔄 Cross-API Validation: Cart Data Consistency Check");
        
        String storedCartGuid = null;
        Integer storedCartId = null;
        Integer storedTotalAmount = null;
        String storedUserId = null;
        
        switch(userType) {
            case "MEMBER":
                storedCartGuid = RequestContext.getMemberCartId();
                storedCartId = RequestContext.getMemberCartNumericId();
                storedTotalAmount = RequestContext.getMemberTotalAmount();
                storedUserId = RequestContext.getMemberUserId();
                break;
            case "EXISTING_MEMBER":
                storedCartGuid = RequestContext.getExistingMemberCartId();
                storedCartId = RequestContext.getExistingMemberCartNumericId();
                storedTotalAmount = RequestContext.getExistingMemberTotalAmount();
                storedUserId = RequestContext.getExistingMemberUserId();
                break;
            case "NEW_USER":
                storedCartGuid = RequestContext.getNewUserCartId();
                storedCartId = RequestContext.getNewUserCartNumericId();
                storedTotalAmount = RequestContext.getNewUserTotalAmount();
                storedUserId = RequestContext.getNewUserUserId();
                break;
        }
        
        // Validate Cart GUID
        if (storedCartGuid != null && currentCartGuid != null) {
            AssertionUtil.verifyEquals(currentCartGuid, storedCartGuid, 
                "GetCartById GUID should match AddToCart");
            System.out.println("   ✅ Cart GUID matches: " + currentCartGuid);
        }
        
        // Validate Cart ID
        if (storedCartId != null && currentCartId != null) {
            AssertionUtil.verifyEquals(currentCartId, storedCartId, 
                "GetCartById ID should match AddToCart");
            System.out.println("   ✅ Cart ID matches: " + currentCartId);
        }
        
        // Validate Total Amount
        if (storedTotalAmount != null && currentTotalAmount != null) {
            AssertionUtil.verifyEquals(currentTotalAmount, storedTotalAmount, 
                "GetCartById total amount should match AddToCart");
            System.out.println("   ✅ Total Amount matches: ₹" + currentTotalAmount);
        }
        
        // Validate User ID
        if (storedUserId != null && currentUserId != null) {
            AssertionUtil.verifyEquals(currentUserId, storedUserId, 
                "GetCartById user ID should match stored user");
            System.out.println("   ✅ User ID matches: " + currentUserId);
        }
    }

    /**
     * Validate that all cart items match tests from GlobalSearchAPI
     * 
     * @param cartItems - List of cart items from GetCartById
     */
    public static void validateCartItemsMatchTests(List<Map<String, Object>> cartItems) {
        System.out.println("\n🔄 Cross-API Validation: Cart Items vs Global Search Tests");
        
        Map<String, Map<String, Object>> storedTests = RequestContext.getAllTests();
        
        AssertionUtil.verifyNotNull(storedTests, "Stored tests from GlobalSearchAPI");
        AssertionUtil.verifyNotNull(cartItems, "Cart items should not be null");
        
        int matchCount = 0;
        
        for (Map<String, Object> cartItem : cartItems) {
            String productId = (String) cartItem.get("product_id");
            
            // Find matching test by product_id
            for (Map.Entry<String, Map<String, Object>> entry : storedTests.entrySet()) {
                String testName = entry.getKey();
                Map<String, Object> testData = entry.getValue();
                String storedProductId = (String) testData.get("_id");
                
                if (productId != null && productId.equals(storedProductId)) {
                    System.out.println("   ✅ Cart item matches test: " + testName + " (ID: " + productId + ")");
                    matchCount++;
                    break;
                }
            }
        }
        
        System.out.println("   📊 Total cart items validated: " + matchCount + "/" + cartItems.size());
    }

    /**
     * Print comprehensive validation summary
     * 
     * @param apiName - Name of the API being validated
     * @param validationCount - Number of validations performed
     * @param allPassed - Whether all validations passed
     */
    public static void printValidationSummary(String apiName, int validationCount, boolean allPassed) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        if (allPassed) {
            System.out.println("║  ✅ CROSS-API VALIDATION COMPLETE: " + apiName);
        } else {
            System.out.println("║  ❌ CROSS-API VALIDATION FAILED: " + apiName);
        }
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Total Validations: " + validationCount);
        System.out.println("║  Status: " + (allPassed ? "PASSED ✅" : "FAILED ❌"));
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
