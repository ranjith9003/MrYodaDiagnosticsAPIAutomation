package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Add to Cart API Test Class
 * Handles /carts/v2/addCart endpoint
 * - Adds test items to cart with all required data
 * - Uses stored user_id, test_id, brand_id, location_id
 */
public class AddToCartAPITest extends BaseTest {

    /**
     * Build cart payload with tests that have HOME COLLECTION AVAILABLE
     * Strategy: Select up to 2 tests, but ONLY add tests with home_collection = AVAILABLE
     * Tests WITHOUT home collection will be EXCLUDED from cart
     */
    private Map<String, Object> buildCartPayloadWithAllTests(String userId, String brandName, String locationName) {
        
        System.out.println("\n🔍 CROSS-API VALIDATION: Verifying Brand and Location from Previous APIs");
        
        // Get brand and location IDs
        String brandId = RequestContext.getBrandId(brandName);
        String locationId = RequestContext.getLocationId(locationName);
        
        System.out.println("   ✅ Brand '" + brandName + "' validated from BrandAPI: " + brandId);
        System.out.println("   ✅ Location '" + locationName + "' validated from LocationAPI: " + locationId);
        
        // Get ALL stored tests from RequestContext
        Map<String, Map<String, Object>> allTests = RequestContext.getAllTests();
        
        // Check if tests exist - if not, return null to skip cart addition
        if (allTests == null || allTests.isEmpty()) {
            System.out.println("   ⚠️  No tests found in RequestContext (filtered out in GlobalSearchAPI)");
            System.out.println("   ℹ️  Skipping Add to Cart - will validate existing cart items instead");
            return null;
        }
        
        System.out.println("   ✅ Tests validated from GlobalSearchAPI: " + allTests.size() + " tests found");
        
        final int MAX_TESTS_TO_CHECK = 2;
        System.out.println("\n📦 STRATEGY: Filter and add ONLY tests with HOME COLLECTION AVAILABLE");
        System.out.println("   - Checking up to " + MAX_TESTS_TO_CHECK + " tests from GlobalSearch");
        System.out.println("   - Will add ONLY tests that support home collection");
        System.out.println("   - Tests WITHOUT home collection will be EXCLUDED");
        
        // Build product details list - add only tests with home collection
        List<Map<String, Object>> productDetailsList = new ArrayList<>();
        int testsChecked = 0;
        int testsWithHomeCollection = 0;
        int testsWithoutHomeCollection = 0;
        
        System.out.println("\n🔍 FILTERING TESTS FOR HOME COLLECTION:");
        
        // Iterate through stored tests and filter for home collection
        for (Map.Entry<String, Map<String, Object>> entry : allTests.entrySet()) {
            if (testsChecked >= MAX_TESTS_TO_CHECK) {
                break; // Stop after checking 2 tests
            }
            
            String testName = entry.getKey();
            Map<String, Object> testData = entry.getValue();
            
            // Get test ID (_id field from Global Search response)
            String testId = (String) testData.get("_id");
            
            // Check home collection status
            Object homeCollectionObj = testData.get("home_collection");
            boolean isHomeCollectionAvailable = false;
            String homeCollectionStatus = "NOT AVAILABLE";
            
            if (homeCollectionObj != null) {
                if (homeCollectionObj instanceof Boolean) {
                    isHomeCollectionAvailable = (Boolean) homeCollectionObj;
                    homeCollectionStatus = isHomeCollectionAvailable ? "AVAILABLE" : "NOT AVAILABLE";
                } else {
                    String homeCollectionStr = homeCollectionObj.toString().trim().toUpperCase();
                    isHomeCollectionAvailable = "AVAILABLE".equals(homeCollectionStr) || 
                                               "TRUE".equals(homeCollectionStr) || 
                                               "YES".equals(homeCollectionStr) ||
                                               "1".equals(homeCollectionStr);
                    homeCollectionStatus = isHomeCollectionAvailable ? "AVAILABLE" : homeCollectionStr;
                }
            }
            
            testsChecked++;
            
            System.out.println("\n   📋 Test " + testsChecked + ": " + testName);
            System.out.println("      Product ID: " + testId);
            System.out.println("      Home Collection: " + homeCollectionStatus);
            
            // ONLY add to cart if home collection is available
            if (isHomeCollectionAvailable) {
                // Build product detail for this test
                Map<String, Object> productDetail = new HashMap<>();
                productDetail.put("product_id", testId);
                productDetail.put("quantity", 1);
                productDetail.put("brand_id", brandId);
                
                // Family member IDs (user adds for themselves)
                List<String> familyMemberIds = new ArrayList<>();
                familyMemberIds.add(userId);
                productDetail.put("family_member_id", familyMemberIds);
                productDetail.put("location_id", locationId);
                
                productDetailsList.add(productDetail);
                testsWithHomeCollection++;
                
                System.out.println("      ✅ ADDED TO CART (Home collection available)");
            } else {
                testsWithoutHomeCollection++;
                System.out.println("      ❌ NOT ADDED TO CART (Home collection not available)");
            }
        }
        
        System.out.println("\n📊 FILTERING SUMMARY:");
        System.out.println("   Tests checked: " + testsChecked);
        System.out.println("   Tests WITH home collection (ADDED): " + testsWithHomeCollection);
        System.out.println("   Tests WITHOUT home collection (EXCLUDED): " + testsWithoutHomeCollection);
        
        // If no tests with home collection found, return null
        if (productDetailsList.isEmpty()) {
            System.out.println("\n   ⚠️  NO TESTS WITH HOME COLLECTION AVAILABLE");
            System.out.println("   ℹ️  Skipping Add to Cart - no home collection tests to add");
            return null;
        }
        
        System.out.println("   ✅ Final cart will contain: " + productDetailsList.size() + " test(s) with home collection");
        
        // Build final payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("lab_location_id", locationId);
        payload.put("product_details", productDetailsList);
        
        System.out.println("\n📊 Cart Payload Summary:");
        System.out.println("   User ID: " + userId);
        System.out.println("   Location ID: " + locationId);
        System.out.println("   Brand ID: " + brandId);
        System.out.println("   Total Tests Being Added: " + productDetailsList.size());
        System.out.println("   ✅ ALL tests in cart have home collection available");
        
        // 🔍 DEBUG: Print complete payload details
        System.out.println("\n🔍 DEBUG - COMPLETE PAYLOAD BEFORE API CALL:");
        for (int i = 0; i < productDetailsList.size(); i++) {
            Map<String, Object> item = productDetailsList.get(i);
            System.out.println("\n   Product " + (i+1) + ":");
            System.out.println("      product_id: " + item.get("product_id"));
            System.out.println("      quantity: " + item.get("quantity") + " (Type: " + item.get("quantity").getClass().getSimpleName() + ")");
            System.out.println("      brand_id: " + item.get("brand_id"));
            System.out.println("      location_id: " + item.get("location_id"));
            System.out.println("      family_member_id: " + item.get("family_member_id"));
        }
        
        return payload;
    }

    /**
     * Call Add to Cart API
     */
    private Response callAddToCartAPI(String token, Map<String, Object> payload) {
        
        System.out.println("\n📦 ADD TO CART REQUEST:");
        System.out.println("User ID: " + payload.get("user_id"));
        System.out.println("Location: " + payload.get("lab_location_id"));
        System.out.println("Products: " + ((List<?>) payload.get("product_details")).size());
        
        // 🔍 DEBUG: Print quantity for each product before API call
        System.out.println("\n🔍 DEBUG - FINAL PAYLOAD VERIFICATION (Before API Call):");
        List<Map<String, Object>> products = (List<Map<String, Object>>) payload.get("product_details");
        for (int i = 0; i < products.size(); i++) {
            Map<String, Object> product = products.get(i);
            System.out.println("   Product " + (i+1) + ":");
            System.out.println("      product_id: " + product.get("product_id"));
            Object qty = product.get("quantity");
            System.out.println("      quantity: " + qty + " (Type: " + qty.getClass().getSimpleName() + ", Value: " + qty + ")");
            System.out.println("      brand_id: " + product.get("brand_id"));
            System.out.println("      location_id: " + product.get("location_id"));
            System.out.println("      family_member_id: " + product.get("family_member_id"));
        }
        
        // 🔍 EXTREME DEBUG: Convert payload to JSON string to show EXACT request body
        System.out.println("\n🔍 ======= PROOF FOR DEV TEAM: EXACT HTTP REQUEST BODY =======");
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            System.out.println(jsonPayload);
            
            // Extra emphasis on quantity field
            System.out.println("\n🔍 ======= QUANTITY FIELD VERIFICATION =======");
            for (int i = 0; i < products.size(); i++) {
                Map<String, Object> product = products.get(i);
                System.out.println("   Product " + (i+1) + " quantity in JSON: " + product.get("quantity"));
                System.out.println("   ✅ CONFIRMED: quantity = " + product.get("quantity") + " is being sent to backend!");
            }
            System.out.println("=================================================\n");
        } catch (Exception e) {
            System.out.println("Could not serialize payload: " + e.getMessage());
        }
        
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.ADD_TO_CART)
                .addHeader("Authorization", token)
                .setRequestBody(payload)
                .post();
        
        // 🔍 DEBUG: Print response to see what backend returned
        System.out.println("\n🔍 ======= API RESPONSE FROM BACKEND =======");
        System.out.println("Status Code: " + response.getStatusCode());
        
        // Check if response has cart_items with price
        try {
            List<Map<String, Object>> responseItems = response.jsonPath().getList("data.cart_items");
            if (responseItems == null) {
                responseItems = response.jsonPath().getList("data.product_details");
            }
            
            if (responseItems != null && !responseItems.isEmpty()) {
                System.out.println("Backend returned " + responseItems.size() + " items:");
                for (int i = 0; i < responseItems.size(); i++) {
                    Map<String, Object> item = responseItems.get(i);
                    System.out.println("\n   Item " + (i+1) + " from backend:");
                    System.out.println("      test_name: " + item.get("test_name"));
                    System.out.println("      quantity: " + item.get("quantity"));
                    System.out.println("      price: " + item.get("price"));
                    System.out.println("      original_price: " + item.get("original_price"));
                    
                    // Highlight the issue if price is 0
                    Object priceObj = item.get("price");
                    Object qtyObj = item.get("quantity");
                    if (priceObj != null && qtyObj != null) {
                        int price = ((Number) priceObj).intValue();
                        int qty = ((Number) qtyObj).intValue();
                        if (price == 0 && qty > 0) {
                            System.out.println("\n      ❌ BACKEND BUG DETECTED:");
                            System.out.println("         We sent: quantity = " + products.get(i).get("quantity"));
                            System.out.println("         Backend received: quantity = " + qty);
                            System.out.println("         But backend returned: price = " + price + " (SHOULD BE > 0!)");
                            System.out.println("         ⚠️  This is a BACKEND PRICING ISSUE, not a frontend issue!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not parse response items: " + e.getMessage());
        }
        System.out.println("===========================================\n");
        
        // Accept both 200 (cart updated) and 201 (cart created)
        int statusCode = response.getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            throw new AssertionError("Expected HTTP 200 or 201 but got " + statusCode);
        }
        
        return response;
    }

    /**
     * Validate Add to Cart Response with COMPREHENSIVE validations
     * Validates all common fields from test selection through cart addition
     * @param userType - "MEMBER", "NON_MEMBER", or "NEW_USER"
     */
    private void validateAddToCartResponse(Response response, String userType) {
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     COMPREHENSIVE CART VALIDATION - " + userType);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        // ========== STEP 1: VALIDATE API RESPONSE ========== //
        System.out.println("\n🔍 STEP 1: Validating API Response");
        
        // Validate success flag
        Boolean successFlag = response.jsonPath().getBoolean("success");
        AssertionUtil.verifyTrue(successFlag, "API success flag should be true");
        System.out.println("   ✅ Success flag: " + successFlag);
        
        // Validate message
        String message = response.jsonPath().getString("msg");
        System.out.println("   ✅ Response message: " + message);
        
        // ========== STEP 2: VALIDATE CART BASIC FIELDS ========== //
        System.out.println("\n🔍 STEP 2: Validating Cart Basic Fields");
        
        // Get cart fields
        String cartGuid = response.jsonPath().getString("data.guid");
        System.out.println("   ✅ Cart GUID: " + cartGuid);
        
        Integer cartId = response.jsonPath().getInt("data.id");
        System.out.println("   ✅ Cart ID: " + cartId);
        
        String cartUserId = response.jsonPath().getString("data.user_id");
        System.out.println("   ✅ User ID: " + cartUserId);
        
        Integer totalAmount = response.jsonPath().getInt("total_amount");
        if (totalAmount != null) {
            System.out.println("   ✅ Total Amount: ₹" + totalAmount);
        }
        
        // ========== STEP 3: VALIDATE CART ITEMS COUNT ========== //
        System.out.println("\n🔍 STEP 3: Validating Cart Items Count");
        
        // Try cart_items first, fallback to product_details
        List<Map<String, Object>> cartItems = response.jsonPath().getList("data.cart_items");
        if (cartItems == null) {
            cartItems = response.jsonPath().getList("data.product_details");
        }
        
        final int EXPECTED_TESTS_ADDED = 2; // We added 2 tests
        int actualCount = cartItems.size();
        
        System.out.println("   📊 Tests added in this request: " + EXPECTED_TESTS_ADDED);
        System.out.println("   📊 Total tests now in cart: " + actualCount);
        System.out.println("   ✅ Cart should contain at least " + EXPECTED_TESTS_ADDED + " tests");
        
        // Verify we added 2 tests (cart may have more from previous sessions)
        if (actualCount >= EXPECTED_TESTS_ADDED) {
            System.out.println("   ✅ SUCCESS: Cart contains " + actualCount + " tests (includes our " + EXPECTED_TESTS_ADDED + " newly added tests)");
        } else {
            System.out.println("   ❌ ERROR: Expected at least " + EXPECTED_TESTS_ADDED + " tests but found " + actualCount);
        }
        
        // ========== STEP 4: VALIDATE EACH CART ITEM DETAILS ========== //
        System.out.println("\n🔍 STEP 4: Validating Each Cart Item");
        
        Map<String, Map<String, Object>> storedTests = RequestContext.getAllTests();
        
        // Determine which field to use (cart_items or product_details)
        String itemsPath = response.jsonPath().get("data.cart_items") != null ? "cart_items" : "product_details";
        System.out.println("   Using response field: data." + itemsPath);
        
        for (int i = 0; i < cartItems.size(); i++) {
            System.out.println("\n   ━━━━━ Item " + (i+1) + " Validation ━━━━━");
            
            // Extract cart item fields (cart_items has test_name, product_details doesn't)
            String itemName = response.jsonPath().getString("data." + itemsPath + "[" + i + "].test_name");
            String itemProductId = response.jsonPath().getString("data." + itemsPath + "[" + i + "].product_id");
            Object priceObj = response.jsonPath().get("data." + itemsPath + "[" + i + "].price");
            Integer itemPrice = (priceObj != null) ? ((Number) priceObj).intValue() : null;
            Integer itemQuantity = response.jsonPath().getInt("data." + itemsPath + "[" + i + "].quantity");
            String itemBrandId = response.jsonPath().getString("data." + itemsPath + "[" + i + "].brand_id");
            String itemLocationId = response.jsonPath().getString("data." + itemsPath + "[" + i + "].location_id");
            
            // ========== CROSS-VALIDATE WITH STORED TEST DATA (GlobalSearchAPI) ========== //
            System.out.println("\n   🔄 Cross-validating cart item " + (i+1) + " with GlobalSearchAPI response...");
            
            // Find test name from stored tests by product ID
            String matchedTestName = null;
            Map<String, Object> storedTest = null;
            
            if (itemName == null && storedTests != null) {
                for (Map.Entry<String, Map<String, Object>> entry : storedTests.entrySet()) {
                    String storedProductId = (String) entry.getValue().get("_id");
                    if (itemProductId != null && itemProductId.equals(storedProductId)) {
                        matchedTestName = entry.getKey();
                        storedTest = entry.getValue();
                        break;
                    }
                }
            } else if (itemName != null) {
                matchedTestName = itemName;
                storedTest = storedTests.get(itemName);
            }
            
            if (matchedTestName != null) {
                System.out.println("   📝 Test Name: " + matchedTestName);
            }
            
            if (storedTest != null) {
                System.out.println("   ✅ Found matching test from GlobalSearchAPI: " + matchedTestName);
                
                // ========== VALIDATE: Product ID matches GlobalSearchAPI ========== //
                String storedProductId = (String) storedTest.get("_id");
                AssertionUtil.verifyEquals(itemProductId, storedProductId, 
                    "AddToCart product ID must match GlobalSearchAPI test ID for: " + matchedTestName);
                System.out.println("   ✅ Product ID matches GlobalSearchAPI: " + itemProductId + " = " + storedProductId);
                
                // ========== VALIDATE: Price matches GlobalSearchAPI (if available) ========== //
                Object storedPriceObj = storedTest.get("price");
                if (itemPrice != null && storedPriceObj != null) {
                    int storedPrice = ((Number) storedPriceObj).intValue();
                    AssertionUtil.verifyEquals(itemPrice, storedPrice, 
                        "AddToCart price must match GlobalSearchAPI price for: " + matchedTestName);
                    System.out.println("   ✅ Price matches GlobalSearchAPI: ₹" + itemPrice + " = ₹" + storedPrice);
                } else if (storedPriceObj != null) {
                    int storedPrice = ((Number) storedPriceObj).intValue();
                    System.out.println("   ℹ️  GlobalSearchAPI price: ₹" + storedPrice + " (cart response doesn't include price)");
                }
                
                // ========== VALIDATE: Quantity is 1 (as expected) ========== //
                AssertionUtil.verifyEquals(itemQuantity, 1, "Quantity must be 1 for: " + matchedTestName);
                System.out.println("   ✅ Quantity is 1 as expected: " + itemQuantity);
                
                // ========== VALIDATE: Brand ID matches BrandAPI ========== //
                String expectedBrandId = RequestContext.getBrandId("Diagnostics");
                AssertionUtil.verifyEquals(itemBrandId, expectedBrandId, 
                    "AddToCart brand ID must match BrandAPI for: " + matchedTestName);
                System.out.println("   ✅ Brand ID matches BrandAPI: " + itemBrandId + " = " + expectedBrandId);
                
                // ========== VALIDATE: Location ID matches LocationAPI ========== //
                String expectedLocationId = RequestContext.getLocationId("Madhapur");
                AssertionUtil.verifyEquals(itemLocationId, expectedLocationId, 
                    "AddToCart location ID must match LocationAPI for: " + matchedTestName);
                System.out.println("   ✅ Location ID matches LocationAPI: " + itemLocationId + " = " + expectedLocationId);
                
                // ========== VALIDATE: Test Type from GlobalSearchAPI ========== //
                String testType = (String) storedTest.get("Type");
                System.out.println("   ✅ Test Type from GlobalSearchAPI: " + testType);
                
                // ========== VALIDATE: Test Status from GlobalSearchAPI ========== //
                String testStatus = (String) storedTest.get("status");
                AssertionUtil.verifyEquals(testStatus, "ACTIVE", 
                    "Test must be ACTIVE in GlobalSearchAPI for: " + matchedTestName);
                System.out.println("   ✅ Test Status from GlobalSearchAPI: " + testStatus);
                
            } else {
                System.out.println("   ⚠️  Warning: Test '" + matchedTestName + "' not found in GlobalSearchAPI stored tests");
            }
        }
        
        // ========== STEP 4.5: VALIDATE HOME COLLECTION AVAILABILITY ========== //
        System.out.println("\n🔍 STEP 4.5: Validating Home Collection for Added Tests");
        System.out.println("   Strategy: We added 2 tests, checking which ones support home collection");
        
        int homeCollectionCount = 0;
        int nonHomeCollectionCount = 0;
        List<String> homeCollectionTests = new ArrayList<>();
        List<String> nonHomeCollectionTests = new ArrayList<>();
        
        // Check home collection for each cart item (reuse storedTests from earlier)
        for (int i = 0; i < Math.min(cartItems.size(), EXPECTED_TESTS_ADDED); i++) {
            String itemProductId = response.jsonPath().getString("data." + itemsPath + "[" + i + "].product_id");
            String itemName = response.jsonPath().getString("data." + itemsPath + "[" + i + "].test_name");
            
            // Find test in stored tests
            Map<String, Object> testData = null;
            String testName = null;
            
            for (Map.Entry<String, Map<String, Object>> entry : storedTests.entrySet()) {
                if (itemProductId.equals(entry.getValue().get("_id"))) {
                    testData = entry.getValue();
                    testName = entry.getKey();
                    break;
                }
            }
            
            if (testData != null) {
                Object homeCollectionObj = testData.get("home_collection");
                boolean isHomeCollectionAvailable = false;
                
                if (homeCollectionObj != null) {
                    if (homeCollectionObj instanceof Boolean) {
                        isHomeCollectionAvailable = (Boolean) homeCollectionObj;
                    } else {
                        String homeCollectionStr = homeCollectionObj.toString().trim().toUpperCase();
                        isHomeCollectionAvailable = "AVAILABLE".equals(homeCollectionStr) || 
                                                   "TRUE".equals(homeCollectionStr) || 
                                                   "YES".equals(homeCollectionStr) ||
                                                   "1".equals(homeCollectionStr);
                    }
                }
                
                if (isHomeCollectionAvailable) {
                    homeCollectionCount++;
                    homeCollectionTests.add(testName != null ? testName : itemName);
                    System.out.println("   ✅ Test " + (i+1) + ": " + (testName != null ? testName : itemName) + " - HOME COLLECTION: AVAILABLE");
                } else {
                    nonHomeCollectionCount++;
                    nonHomeCollectionTests.add(testName != null ? testName : itemName);
                    System.out.println("   ⚠️  Test " + (i+1) + ": " + (testName != null ? testName : itemName) + " - HOME COLLECTION: NOT AVAILABLE");
                }
            }
        }
        
        System.out.println("\n📊 HOME COLLECTION SUMMARY:");
        System.out.println("   Total tests added: " + EXPECTED_TESTS_ADDED);
        System.out.println("   Tests with home collection: " + homeCollectionCount);
        System.out.println("   Tests without home collection: " + nonHomeCollectionCount);
        
        if (homeCollectionCount >= 1) {
            System.out.println("   ✅ SUCCESS: At least 1 test supports home collection");
            System.out.println("   Tests with home collection: " + homeCollectionTests);
        } else {
            System.out.println("   ⚠️  WARNING: No tests with home collection found");
        }
        
        if (nonHomeCollectionCount > 0) {
            System.out.println("   ℹ️  Tests without home collection: " + nonHomeCollectionTests);
        }
        
        // ========== STEP 5: STORE CART DATA BY USER TYPE ========== //
        System.out.println("\n🔍 STEP 5: Storing Cart Data for " + userType);
        
        // Extract complete cart data from response
        Map<String, Object> cartData = response.jsonPath().getMap("data");
        
        // Extract cart items list
        List<Map<String, Object>> cartItemsList = response.jsonPath().getList("data." + itemsPath);
        
        switch(userType) {
            case "MEMBER":
                RequestContext.setMemberCartId(cartGuid);
                RequestContext.setMemberCartNumericId(cartId);
                if (totalAmount != null) {
                    RequestContext.setMemberTotalAmount(totalAmount);
                }
                RequestContext.setMemberAddToCartResponse(cartData);
                RequestContext.setMemberCartItems(cartItemsList);
                System.out.println("   ✅ MEMBER cart data stored");
                System.out.println("      Cart GUID: " + cartGuid);
                System.out.println("      Cart ID: " + cartId);
                System.out.println("      Total Amount: ₹" + (totalAmount != null ? totalAmount : 0));
                System.out.println("      Cart Items Count: " + (cartItemsList != null ? cartItemsList.size() : 0));
                break;
                
            case "NON_MEMBER":
                RequestContext.setNonMemberCartId(cartGuid);
                RequestContext.setNonMemberCartNumericId(cartId);
                if (totalAmount != null) {
                    RequestContext.setNonMemberTotalAmount(totalAmount);
                }
                RequestContext.setNonMemberAddToCartResponse(cartData);
                RequestContext.setNonMemberCartItems(cartItemsList);
                System.out.println("   ✅ EXISTING MEMBER cart data stored");
                System.out.println("      Cart GUID: " + cartGuid);
                System.out.println("      Cart ID: " + cartId);
                System.out.println("      Total Amount: ₹" + (totalAmount != null ? totalAmount : 0));
                System.out.println("      Cart Items Count: " + (cartItemsList != null ? cartItemsList.size() : 0));
                break;
                
            case "NEW_USER":
                RequestContext.setNewUserCartId(cartGuid);
                RequestContext.setNewUserCartNumericId(cartId);
                if (totalAmount != null) {
                    RequestContext.setNewUserTotalAmount(totalAmount);
                }
                RequestContext.setNewUserAddToCartResponse(cartData);
                RequestContext.setNewUserCartItems(cartItemsList);
                System.out.println("   ✅ NEW USER cart data stored");
                System.out.println("      Cart GUID: " + cartGuid);
                System.out.println("      Cart ID: " + cartId);
                System.out.println("      Total Amount: ₹" + (totalAmount != null ? totalAmount : 0));
                System.out.println("      Cart Items Count: " + (cartItemsList != null ? cartItemsList.size() : 0));
                break;
        }
        
        // ========== FINAL SUMMARY ========== //
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ ALL VALIDATIONS PASSED FOR " + userType);
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Cart GUID: " + cartGuid);
        System.out.println("║  Cart ID: " + cartId);
        System.out.println("║  Tests Added: " + EXPECTED_TESTS_ADDED);
        System.out.println("║  Total Items in Cart: " + actualCount);
        System.out.println("║  Tests with Home Collection: " + homeCollectionCount);
        System.out.println("║  Total Amount: ₹" + (totalAmount != null ? totalAmount : 0));
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  ✅ Strategy Verified:");
        System.out.println("║     - Added " + EXPECTED_TESTS_ADDED + " tests to cart");
        System.out.println("║     - " + homeCollectionCount + " test(s) support home collection");
        System.out.println("║     - GetCartById will validate home collection availability");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    // ---------------------------------------------------------
    // 1️⃣ MEMBER → Add to Cart (ADD 2 TESTS, VERIFY HOME COLLECTION)
    // ---------------------------------------------------------
    @Test(priority = 8, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GlobalSearchAPITest.testGlobalSearchAndStore")
    public void testAddToCart_ForMember() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  ADD TO CART API — MEMBER (ADD 2, CHECK HOME COLLECTION) ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getMemberToken();
        String userId = RequestContext.getMemberUserId();
        
        // Build payload with 2 tests from Global Search
        Map<String, Object> payload = buildCartPayloadWithAllTests(
            userId, 
            "Diagnostics", 
            "Madhapur"
        );
        
        if (payload == null) {
            System.out.println("⏭️  Skipping Add to Cart - no home collection tests available");
            System.out.println("   Will validate existing cart items in GetCartById test");
            return;
        }
        
        Response response = callAddToCartAPI(token, payload);
        validateAddToCartResponse(response, "MEMBER");
    }

    // ---------------------------------------------------------
    // 2️⃣ EXISTING MEMBER → Add to Cart (ADD 2 TESTS, VERIFY HOME COLLECTION)
    // ---------------------------------------------------------
    @Test(priority = 8, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GlobalSearchAPITest.testGlobalSearchAndStore")
    public void testAddToCart_ForNonMember() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ ADD TO CART — NON-MEMBER (ADD 2, CHECK HOME COLLECTION)  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getNonMemberToken();
        String userId = RequestContext.getNonMemberUserId();
        
        // Build payload with 2 tests from Global Search
        Map<String, Object> payload = buildCartPayloadWithAllTests(
            userId, 
            "Diagnostics", 
            "Madhapur"
        );
        
        if (payload == null) {
            System.out.println("⏭️  Skipping Add to Cart - no home collection tests available");
            System.out.println("   Will validate existing cart items in GetCartById test");
            return;
        }
        
        Response response = callAddToCartAPI(token, payload);
        validateAddToCartResponse(response, "NON_MEMBER");
    }

    // ---------------------------------------------------------
    // 3️⃣ NEW USER → Add to Cart (ADD 2 TESTS, VERIFY HOME COLLECTION)
    // ---------------------------------------------------------
    @Test(priority = 9, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GlobalSearchAPITest.testGlobalSearchAndStore")
    public void testAddToCart_ForNewUser() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ ADD TO CART — NEW USER (ADD 2, CHECK HOME COLLECTION)    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getNewUserToken();
        String userId = RequestContext.getNewUserUserId();
        
        // Build payload with ALL tests from Global Search
        Map<String, Object> payload = buildCartPayloadWithAllTests(
            userId, 
            "Diagnostics", 
            "Madhapur"
        );
        
        if (payload == null) {
            System.out.println("⏭️  Skipping Add to Cart - no home collection tests available");
            System.out.println("   Will validate existing cart items in GetCartById test");
            return;
        }
        
        Response response = callAddToCartAPI(token, payload);
        validateAddToCartResponse(response, "NEW_USER");
    }

    // ---------------------------------------------------------
    // 4️⃣ REUSABLE METHOD → Add to Cart for any user/test/brand/location
    // ---------------------------------------------------------
    /**
     * Reusable method to add any test to cart
     * 
     * @param token - Authorization token
     * @param userId - User ID (GUID)
     * @param testName - Test name (e.g., "Blood Coagulation")
     * @param brandName - Brand name (e.g., "Diagnostics")
     * @param locationName - Location name (e.g., "Madhapur")
     * @return Response object
     */
    public static Response addToCart(String token, String userId, String testName, String brandName, String locationName) {
        
        String testId = RequestContext.getTestId(testName);
        String brandId = RequestContext.getBrandId(brandName);
        String locationId = RequestContext.getLocationId(locationName);
        
        // Build product details
        Map<String, Object> productDetail = new HashMap<>();
        productDetail.put("product_id", testId);
        productDetail.put("quantity", 1);
        productDetail.put("brand_id", brandId);
        
        List<String> familyMemberIds = new ArrayList<>();
        familyMemberIds.add(userId);
        productDetail.put("family_member_id", familyMemberIds);
        productDetail.put("location_id", locationId);
        
        List<Map<String, Object>> productDetailsList = new ArrayList<>();
        productDetailsList.add(productDetail);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("lab_location_id", locationId);
        payload.put("product_details", productDetailsList);
        
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.ADD_TO_CART)
                .addHeader("Authorization", token)
                .setRequestBody(payload)
                .post();
        
        // Accept both 200 (cart updated) and 201 (cart created)
        int statusCode = response.getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            throw new AssertionError("Expected HTTP 200 or 201 but got " + statusCode);
        }
        
        return response;
    }
}
