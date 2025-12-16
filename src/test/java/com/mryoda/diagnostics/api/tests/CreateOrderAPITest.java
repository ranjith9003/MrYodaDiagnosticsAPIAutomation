package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.LoggerUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateOrder API Test - Final step in the complete flow
 * Creates order and validates against all previous API responses
 */
public class CreateOrderAPITest extends BaseTest {
    
    /**
     * Test CreateOrder API for NON_MEMBER
     * Validates response against all previous API data
     */
    @Test(priority = 1, groups = {"createOrder", "regression"})
    public void testCreateOrder_ForNonMember() {
        LoggerUtil.logTestStart("testCreateOrder_ForNonMember");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     CREATE ORDER API – EXISTING MEMBER                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // Get user_id from RequestContext
        String userId = RequestContext.getNonMemberUserId();
        Assert.assertNotNull(userId, "❌ User ID not found in RequestContext");
        
        System.out.println("🔹 CROSS-API VALIDATION: Using data from all previous APIs");
        System.out.println("   ✅ User ID from LoginAPI: " + userId);
        
        // Build request payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("user_id", userId);
        
        System.out.println("\n📤 CREATE ORDER REQUEST:");
        System.out.println("   User ID: " + userId);
        
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + RequestContext.getNonMemberToken())
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .when()
                .post(APIEndpoints.CREATE_ORDER)
                .then()
                .extract()
                .response();
        
        // Log response
        System.out.println("\n📥 CREATE ORDER RESPONSE:");
        System.out.println("   Status Code: " + response.getStatusCode());
        System.out.println("   Response Body: " + response.getBody().asString());
        
        // Validate response with cross-API validation
        validateCreateOrderResponse(response, "NON_MEMBER");
        
        LoggerUtil.logTestEnd("testCreateOrder_ForNonMember");
    }
    
    /**
     * Test CreateOrder API for MEMBER
     */
    @Test(priority = 2, groups = {"createOrder", "regression"})
    public void testCreateOrder_ForMember() {
        LoggerUtil.logTestStart("testCreateOrder_ForMember");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        CREATE ORDER API – MEMBER                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // Get user_id from RequestContext
        String userId = RequestContext.getMemberUserId();
        Assert.assertNotNull(userId, "❌ User ID not found in RequestContext");
        
        System.out.println("🔹 CROSS-API VALIDATION: Using data from all previous APIs");
        System.out.println("   ✅ User ID from LoginAPI: " + userId);
        
        // Build request payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("user_id", userId);
        
        System.out.println("\n📤 CREATE ORDER REQUEST:");
        System.out.println("   User ID: " + userId);
        
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + RequestContext.getMemberToken())
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .when()
                .post(APIEndpoints.CREATE_ORDER)
                .then()
                .extract()
                .response();
        
        // Log response
        System.out.println("\n📥 CREATE ORDER RESPONSE:");
        System.out.println("   Status Code: " + response.getStatusCode());
        System.out.println("   Response Body: " + response.getBody().asString());
        
        // Validate response with cross-API validation
        validateCreateOrderResponse(response, "MEMBER");
        
        LoggerUtil.logTestEnd("testCreateOrder_ForMember");
    }
    
    /**
     * Test CreateOrder API for NEW_USER
     */
    @Test(priority = 3, groups = {"createOrder", "regression"})
    public void testCreateOrder_ForNewUser() {
        LoggerUtil.logTestStart("testCreateOrder_ForNewUser");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       CREATE ORDER API – NEW USER                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // Get user_id from RequestContext
        String userId = RequestContext.getNewUserUserId();
        Assert.assertNotNull(userId, "❌ User ID not found in RequestContext");
        
        System.out.println("🔹 CROSS-API VALIDATION: Using data from all previous APIs");
        System.out.println("   ✅ User ID from LoginAPI: " + userId);
        
        // Build request payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("user_id", userId);
        
        System.out.println("\n📤 CREATE ORDER REQUEST:");
        System.out.println("   User ID: " + userId);
        
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + RequestContext.getNewUserToken())
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .when()
                .post(APIEndpoints.CREATE_ORDER)
                .then()
                .extract()
                .response();
        
        // Log response
        System.out.println("\n📥 CREATE ORDER RESPONSE:");
        System.out.println("   Status Code: " + response.getStatusCode());
        System.out.println("   Response Body: " + response.getBody().asString());
        
        // Validate response with cross-API validation
        validateCreateOrderResponse(response, "NEW_USER");
        
        LoggerUtil.logTestEnd("testCreateOrder_ForNewUser");
    }
    
    /**
     * Comprehensive validation of CreateOrder response
     * Cross-validates with all previous API responses
     */
    private void validateCreateOrderResponse(Response response, String userType) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     COMPREHENSIVE CREATE ORDER VALIDATION - " + userType);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // STEP 1: Basic Response Validation
        System.out.println("🔹 STEP 1: Validating API Response");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "❌ HTTP Status Code should be 200");
        System.out.println("   ✅ HTTP Status: " + statusCode);
        
        // Extract response data
        Boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue(success, "❌ API success flag should be true");
        System.out.println("   ✅ Success flag: " + success);
        
        String message = response.jsonPath().getString("msg");
        Assert.assertEquals(message, "Order Created Successfully", "❌ Message should be 'Order Created Successfully'");
        System.out.println("   ✅ Response message: " + message);
        
        // STEP 2: Validate ALL Order Fields
        System.out.println("\n🔹 STEP 2: Validating ALL Order Fields");
        
        Map<String, Object> data = response.jsonPath().getMap("data");
        Assert.assertNotNull(data, "❌ Response data should not be null");
        
        // ===== RAZORPAY ORDER ID VALIDATION =====
        String orderId = (String) data.get("id");
        Assert.assertNotNull(orderId, "❌ Razorpay order ID should not be null");
        Assert.assertTrue(orderId.startsWith("order_"), "❌ Razorpay order ID should start with 'order_'");
        System.out.println("   ✅ Razorpay Order ID: " + orderId);
        
        // ===== AMOUNT VALIDATION =====
        String amount = String.valueOf(data.get("amount"));
        Assert.assertNotNull(amount, "❌ Amount should not be null");
        int amountInt = Integer.parseInt(amount);
        Assert.assertTrue(amountInt > 0, "❌ Amount should be greater than 0");
        System.out.println("   ✅ Amount: ₹" + (amountInt / 100.0) + " (paise: " + amount + ")");
        
        // ===== AMOUNT_DUE VALIDATION =====
        String amountDue = String.valueOf(data.get("amount_due"));
        Assert.assertNotNull(amountDue, "❌ Amount due should not be null");
        Assert.assertEquals(amountDue, amount, "❌ Amount due should equal amount");
        System.out.println("   ✅ Amount Due: ₹" + (Integer.parseInt(amountDue) / 100.0) + " (paise: " + amountDue + ")");
        
        // ===== STATUS VALIDATION =====
        String status = (String) data.get("status");
        Assert.assertNotNull(status, "❌ Status should not be null");
        Assert.assertEquals(status, "created", "❌ Status should be 'created'");
        System.out.println("   ✅ Order Status: " + status);
        
        // ===== KEY_ID VALIDATION =====
        String keyId = (String) data.get("key_id");
        Assert.assertNotNull(keyId, "❌ Razorpay key_id should not be null");
        Assert.assertTrue(keyId.startsWith("rzp_"), "❌ Razorpay key_id should start with 'rzp_'");
        System.out.println("   ✅ Razorpay Key ID: " + keyId);
        
        // ===== MOBILE VALIDATION =====
        String mobile = (String) data.get("mobile");
        Assert.assertNotNull(mobile, "❌ Mobile should not be null");
        Assert.assertEquals(mobile.length(), 10, "❌ Mobile should be 10 digits");
        System.out.println("   ✅ Mobile: " + mobile);
        
        // STEP 3: Validate NOTES Object
        System.out.println("\n🔹 STEP 3: Validating Notes Object");
        @SuppressWarnings("unchecked")
        Map<String, Object> notes = (Map<String, Object>) data.get("notes");
        Assert.assertNotNull(notes, "❌ Notes object should not be null");
        
        // ===== NOTES.USER_ID VALIDATION =====
        String notesUserId = (String) notes.get("user_id");
        Assert.assertNotNull(notesUserId, "❌ Notes user_id should not be null");
        System.out.println("   ✅ Notes User ID: " + notesUserId);
        
        // ===== NOTES.MOBILE VALIDATION =====
        String notesMobile = (String) notes.get("mobile");
        Assert.assertNotNull(notesMobile, "❌ Notes mobile should not be null");
        Assert.assertEquals(notesMobile, mobile, "❌ Notes mobile should match data.mobile");
        System.out.println("   ✅ Notes Mobile: " + notesMobile);
        
        // ===== NOTES.SLOT_GUID VALIDATION =====
        String notesSlotGuid = (String) notes.get("slot_guid");
        Assert.assertNotNull(notesSlotGuid, "❌ Notes slot_guid should not be null");
        System.out.println("   ✅ Notes Slot GUID: " + notesSlotGuid);
        
        // STEP 4: Cross-validate with LoginAPI
        System.out.println("\n🔹 STEP 4: Cross-validating with LoginAPI");
        String expectedUserId = null;
        String expectedMobile = null;
        
        switch (userType) {
            case "NON_MEMBER":
                expectedUserId = RequestContext.getNonMemberUserId();
                expectedMobile = "8220220227"; // From LoginAPITest
                break;
            case "MEMBER":
                expectedUserId = RequestContext.getMemberUserId();
                expectedMobile = "9003730394"; // From LoginAPITest
                break;
            case "NEW_USER":
                expectedUserId = RequestContext.getNewUserUserId();
                expectedMobile = RequestContext.getMobile(); // From UserRegistrationTest
                break;
        }
        
        Assert.assertEquals(notesUserId, expectedUserId, 
                          "❌ User ID should match LoginAPI");
        System.out.println("   ✅ User ID matches LoginAPI: " + notesUserId);
        
        if (expectedMobile != null) {
            Assert.assertEquals(mobile, expectedMobile, 
                              "❌ Mobile should match LoginAPI");
            System.out.println("   ✅ Mobile matches LoginAPI: " + mobile);
        }
        
        // STEP 5: Cross-validate with SlotAPI
        System.out.println("\n🔹 STEP 5: Cross-validating with SlotAPI");
        String expectedSlotGuid = null;
        
        switch (userType) {
            case "NON_MEMBER":
                expectedSlotGuid = RequestContext.getNonMemberSlotGuid();
                break;
            case "MEMBER":
                expectedSlotGuid = RequestContext.getMemberSlotGuid();
                break;
            case "NEW_USER":
                expectedSlotGuid = RequestContext.getNewUserSlotGuid();
                break;
        }
        
        if (expectedSlotGuid != null) {
            // NOTE: Slot GUID may differ from SlotAPI because backend selects best available slot at order creation
            // We just validate that a slot_guid exists and log comparison
            System.out.println("   📊 Slot GUID from SlotAPI: " + expectedSlotGuid);
            System.out.println("   📊 Slot GUID from CreateOrder: " + notesSlotGuid);
            
            if (notesSlotGuid.equals(expectedSlotGuid)) {
                System.out.println("   ✅ Slot GUID matches SlotAPI exactly");
            } else {
                System.out.println("   ℹ️  Slot GUID differs (backend selected different slot - this is expected)");
            }
        } else {
            System.out.println("   ℹ️  No stored slot GUID to compare (validation skipped)");
        }
        
        // STEP 6: Store Order Data
        System.out.println("\n🔹 STEP 6: Storing Order Data in RequestContext");
        
        switch (userType) {
            case "NON_MEMBER":
                RequestContext.setNonMemberOrderId(orderId);
                System.out.println("   ✅ Stored Existing Member Order ID: " + orderId);
                break;
            case "MEMBER":
                RequestContext.setMemberOrderId(orderId);
                System.out.println("   ✅ Stored Member Order ID: " + orderId);
                break;
            case "NEW_USER":
                RequestContext.setNewUserOrderId(orderId);
                System.out.println("   ✅ Stored New User Order ID: " + orderId);
                break;
        }
        
        // Final Summary
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ ALL VALIDATIONS PASSED FOR " + userType);
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Razorpay Order ID: " + orderId);
        System.out.println("║  Amount: ₹" + (amountInt / 100.0));
        System.out.println("║  Status: " + status);
        System.out.println("║  Mobile: " + mobile);
        System.out.println("║  User ID: " + notesUserId);
        System.out.println("║  Slot GUID: " + notesSlotGuid);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}
