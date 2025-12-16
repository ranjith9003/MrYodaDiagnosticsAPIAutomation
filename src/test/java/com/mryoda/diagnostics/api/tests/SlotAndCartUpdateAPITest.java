package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.*;

public class SlotAndCartUpdateAPITest extends BaseTest {

    // -------------------------------
    // STEP 1: GET SLOT COUNT BY TIME (POST) - Find Available Slot with non-null count
    // -------------------------------
    @Test(priority = 18, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GetCentersByAddAPITest.testGetCentersByAdd_ForNonMember")
    public void testGetSlotCountByTime_FetchDates() {
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     GET SLOT COUNT BY TIME - FIND AVAILABLE SLOT         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        String token = RequestContext.getNonMemberToken();
        String addressGuid = RequestContext.getNonMemberAddressId();
        
        System.out.println("\n📅 SEARCHING FOR AVAILABLE SLOTS...");
        System.out.println("   Address GUID: " + addressGuid);
        System.out.println("   Order Type: home");
        
        // Get today's date in format: yyyy-MM-dd
        java.time.LocalDate today = java.time.LocalDate.now();
        
        String selectedSlotGuid = null;
        String selectedSlotDate = null;
        int daysChecked = 0;
        int maxDaysToCheck = 7; // Check up to 7 days ahead
        
        // Try to find an available slot starting from today
        while (selectedSlotGuid == null && daysChecked < maxDaysToCheck) {
            String checkDate = today.plusDays(daysChecked).toString();
            System.out.println("\n🔍 Checking date: " + checkDate + " (Day " + (daysChecked + 1) + ")");
            
            // Build payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("slot_start_time", checkDate);
            payload.put("page", 1);
            payload.put("limit", 100);
            payload.put("type", "home");
            payload.put("addressguid", addressGuid);
            
            Response response = new RequestBuilder()
                    .setEndpoint(APIEndpoints.GET_SLOT_COUNT_BY_TIME)
                    .addHeader("Authorization", token)
                    .setRequestBody(payload)
                    .post();
            
            AssertionUtil.verifyEquals(response.getStatusCode(), 200, "HTTP status should be 200");
            
            // Validate response
            Boolean success = response.jsonPath().getBoolean("success");
            AssertionUtil.verifyTrue(success, "API success flag should be true");
            
            // Extract slots
            List<Map<String, Object>> slots = response.jsonPath().getList("data");
            
            if (slots != null && !slots.isEmpty()) {
                System.out.println("   ✔ Found " + slots.size() + " slots for " + checkDate);
                
                // Print first slot for debugging
                if (slots.size() > 0) {
                    Map<String, Object> firstSlot = slots.get(0);
                    System.out.println("   🔍 DEBUG - First slot data: " + firstSlot);
                }
                
                // Find first slot with available count
                for (int i = 0; i < slots.size(); i++) {
                    Map<String, Object> slot = slots.get(i);
                    
                    String slotGuid = (String) slot.get("guid");
                    String slotStartTime = (String) slot.get("starttime");
                    String slotEndTime = (String) slot.get("endtime");
                    Object slotCountObj = slot.get("count");  // Fixed: field name is "count" not "slot_count"
                    
                    // Print debug info for first 3 slots
                    if (i < 3) {
                        System.out.println("   🔍 Slot " + (i+1) + " - GUID: " + slotGuid + 
                                         ", count: " + slotCountObj + 
                                         " (type: " + (slotCountObj != null ? slotCountObj.getClass().getSimpleName() : "null") + ")");
                    }
                    
                    // Check if slot is available - handle all possible data types
                    boolean isAvailable = false;
                    Integer slotCount = null;
                    
                    if (slotCountObj != null) {
                        try {
                            if (slotCountObj instanceof Integer) {
                                slotCount = (Integer) slotCountObj;
                            } else if (slotCountObj instanceof String) {
                                slotCount = Integer.parseInt((String) slotCountObj);
                            } else if (slotCountObj instanceof Double) {
                                slotCount = ((Double) slotCountObj).intValue();
                            } else {
                                slotCount = Integer.parseInt(slotCountObj.toString());
                            }
                            
                            isAvailable = slotCount > 0;
                        } catch (Exception e) {
                            System.out.println("   ⚠️  Could not parse count: " + slotCountObj);
                        }
                    }
                    
                    // Select slot ONLY if it has available count > 0
                    if (slotGuid != null && slotStartTime != null && isAvailable) {
                        selectedSlotGuid = slotGuid;
                        selectedSlotDate = checkDate;
                        
                        System.out.println("\n   ✅ FOUND AVAILABLE SLOT!");
                        System.out.println("   ━━━━━━━━━━━━━━━━━━━━━━");
                        System.out.println("   ✔ Date: " + checkDate);
                        System.out.println("   ✔ Slot GUID: " + slotGuid);
                        System.out.println("   ✔ Time: " + slotStartTime + " - " + slotEndTime);
                        System.out.println("   ✔ Available Count: " + slotCount);
                        
                        // Store slot GUID for all user types
                        RequestContext.setNonMemberSlotGuid(selectedSlotGuid);
                        RequestContext.setMemberSlotGuid(selectedSlotGuid);
                        RequestContext.setNewUserSlotGuid(selectedSlotGuid);
                        RequestContext.setSlotStartDate(selectedSlotDate);
                        
                        System.out.println("   ✔ Stored in RequestContext for all user types");
                        break;
                    }
                }
                
                if (selectedSlotGuid == null) {
                    System.out.println("   ⚠️  All slots full for " + checkDate + ", checking next day...");
                }
            } else {
                System.out.println("   ⚠️  No slots found for " + checkDate);
            }
            
            daysChecked++;
        }
        
        AssertionUtil.verifyTrue(selectedSlotGuid != null, "Should find at least one available slot");
        
        System.out.println("\n========================================");
        System.out.println("SLOT SEARCH COMPLETED SUCCESSFULLY");
        System.out.println("========================================");
        System.out.println("Selected Date: " + selectedSlotDate);
        System.out.println("Slot GUID: " + selectedSlotGuid);
        System.out.println("Days Checked: " + daysChecked);
        System.out.println("========================================\n");
    }
    
    // -------------------------------
    // STEP 2: UPDATE CART WITH SLOT (PUT/POST)
    // -------------------------------
    private void updateCartWithSlot(String token, String userId, String slotGuid, String userType) {
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     UPDATE CART WITH SLOT - " + userType);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n📦 PREPARING CART UPDATE...");
        System.out.println("   User ID: " + userId);
        System.out.println("   Slot GUID: " + slotGuid);
        
        // Get product details from stored tests
        List<Map<String, Object>> productDetails = new ArrayList<>();
        Map<String, Map<String, Object>> selectedTests = RequestContext.getAllTests();
        
        for (Map.Entry<String, Map<String, Object>> entry : selectedTests.entrySet()) {
            Map<String, Object> testData = entry.getValue();
            
            String productId = (String) testData.get("_id");
            String brandId = RequestContext.getSelectedBrandId();
            String locationId = RequestContext.getSelectedLocationId();
            
            System.out.println("\n   🔍 DEBUG Product Details:");
            System.out.println("     Test Name: " + entry.getKey());
            System.out.println("     Product ID: " + productId + " (type: " + (productId != null ? productId.getClass().getSimpleName() : "null") + ")");
            System.out.println("     Brand ID: " + brandId + " (type: " + (brandId != null ? brandId.getClass().getSimpleName() : "null") + ")");
            System.out.println("     Location ID: " + locationId + " (type: " + (locationId != null ? locationId.getClass().getSimpleName() : "null") + ")");
            
            Map<String, Object> product = new HashMap<>();
            product.put("product_id", productId);  // API expects 'product_id'
            product.put("quantity", 1);
            product.put("type", testData.get("type"));
            product.put("brand_id", brandId);
            product.put("location_id", locationId);
            
            productDetails.add(product);
        }
        
        // Build payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("product_details", productDetails);
        payload.put("slot_guid", slotGuid);
        
        System.out.println("\n📦 CART UPDATE PAYLOAD:");
        System.out.println("   user_id: " + userId);
        System.out.println("   product_details: " + productDetails.size() + " products");
        System.out.println("   slot_guid: " + slotGuid);
        
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.ADD_TO_CART)
                .addHeader("Authorization", token)
                .setRequestBody(payload)
                .post();  // Using POST to create/update cart with slot
        
        // Log response for debugging
        System.out.println("\n📋 API RESPONSE:");
        System.out.println("   Status Code: " + response.getStatusCode());
        System.out.println("   Response Body: " + response.getBody().asString());
        
        AssertionUtil.verifyEquals(response.getStatusCode(), 200, "HTTP status should be 200");
        System.out.println("\n   ✅ HTTP Status: " + response.getStatusCode());
        
        // Validate response
        System.out.println("\n✅ STEP 1: Validating Cart Update Response");
        
        Boolean success = response.jsonPath().getBoolean("success");
        AssertionUtil.verifyTrue(success, "API success flag should be true");
        System.out.println("   ✔ Success flag: " + success);
        
        String message = response.jsonPath().getString("msg");
        System.out.println("   ✔ Message: " + message);
        
        // Extract and validate cart data
        Map<String, Object> data = response.jsonPath().getMap("data");
        
        String cartGuid = (String) data.get("guid");
        System.out.println("   ✔ Cart GUID: " + cartGuid);
        
        // Validate slot_guid in response
        String responseSlotGuid = (String) data.get("slot_guid");
        if (responseSlotGuid != null) {
            AssertionUtil.verifyEquals(responseSlotGuid, slotGuid, "Slot GUID should match");
            System.out.println("   ✔ Slot GUID verified: " + responseSlotGuid);
        } else {
            System.out.println("   ℹ️  Slot GUID not in response (expected behavior)");
        }
        
        System.out.println("\n✅ STEP 2: Cart Update Summary");
        System.out.println("   ✔ Cart updated successfully");
        System.out.println("   ✔ Cart GUID: " + cartGuid);
        System.out.println("   ✔ Products: " + productDetails.size());
        System.out.println("   ✔ Slot GUID: " + slotGuid);
        
        System.out.println("\n========================================");
        System.out.println("CART UPDATE COMPLETED - " + userType);
        System.out.println("========================================");
        System.out.println("Cart GUID: " + cartGuid);
        System.out.println("Slot GUID: " + slotGuid);
        System.out.println("Message: " + message);
        System.out.println("========================================\n");
    }
    
    // -------------------------------
    // COMPLETE FLOW: EXISTING MEMBER
    // -------------------------------
    @Test(priority = 19, dependsOnMethods = "testGetSlotCountByTime_FetchDates")
    public void testCompleteSlotFlow_ExistingMember() {
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     UPDATE CART WITH SLOT - EXISTING MEMBER              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        String token = RequestContext.getNonMemberToken();
        String userId = RequestContext.getNonMemberUserId();
        String slotGuid = RequestContext.getNonMemberSlotGuid();
        
        // Update cart with slot
        updateCartWithSlot(token, userId, slotGuid, "NON_MEMBER");
        
        System.out.println("\n🎉 ========================================");
        System.out.println("   COMPLETE FLOW SUCCESS - NON_MEMBER");
        System.out.println("   ========================================");
    }
    
    // -------------------------------
    // COMPLETE FLOW: MEMBER
    // -------------------------------
    @Test(priority = 19, dependsOnMethods = "testGetSlotCountByTime_FetchDates")
    public void testCompleteSlotFlow_Member() {
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     UPDATE CART WITH SLOT - MEMBER                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        String token = RequestContext.getMemberToken();
        String userId = RequestContext.getMemberUserId();
        String slotGuid = RequestContext.getMemberSlotGuid();
        
        // Update cart with slot
        updateCartWithSlot(token, userId, slotGuid, "MEMBER");
        
        System.out.println("\n🎉 ========================================");
        System.out.println("   COMPLETE FLOW SUCCESS - MEMBER");
        System.out.println("   ========================================");
    }
    
    // -------------------------------
    // COMPLETE FLOW: NEW USER
    // -------------------------------
    @Test(priority = 20, dependsOnMethods = "testGetSlotCountByTime_FetchDates")
    public void testCompleteSlotFlow_NewUser() {
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     UPDATE CART WITH SLOT - NEW USER                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        String token = RequestContext.getNewUserToken();
        String userId = RequestContext.getNewUserUserId();
        String slotGuid = RequestContext.getNewUserSlotGuid();
        
        // Update cart with slot
        updateCartWithSlot(token, userId, slotGuid, "NEW_USER");
        
        System.out.println("\n🎉 ========================================");
        System.out.println("   COMPLETE FLOW SUCCESS - NEW_USER");
        System.out.println("   ========================================");
    }
}
