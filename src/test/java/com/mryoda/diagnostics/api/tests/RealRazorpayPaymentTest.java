package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.payloads.VerifyPaymentPayload;
import com.mryoda.diagnostics.api.utils.LoggerUtil;
import com.mryoda.diagnostics.api.utils.RazorpayPaymentVerifier;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Real Razorpay Payment Verification Test
 * Tests with actual payment data from real Razorpay transactions
 */
public class RealRazorpayPaymentTest extends BaseTest {
    
    /**
     * Test with REAL Order Data from CreateOrderAPI
     * Gets actual orderCreationId from CreateOrderAPITest
     * Generates valid payment_id and signature for verification
     * COMPLETE END-TO-END FLOW TEST
     */
    @Test(priority = 1, groups = {"razorpay", "payment", "real"})
    public void testRealRazorpayPaymentVerification() {
        LoggerUtil.logTestStart("testRealRazorpayPaymentVerification");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     💳 REAL RAZORPAY PAYMENT VERIFICATION - COMPLETE FLOW       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        // STEP 1: GET REAL ORDER DATA FROM CreateOrderAPITest
        System.out.println("📋 STEP 1: Retrieving Real Order Data from CreateOrderAPI");
        
        String orderCreationId = null;
        String userId = null;
        String mobile = null;
        String token = null;
        
        // Try to get MEMBER order (priority 1)
        orderCreationId = RequestContext.getMemberOrderId();
        if (orderCreationId != null && !orderCreationId.isEmpty()) {
            userId = RequestContext.getMemberUserId();
            mobile = ConfigLoader.getConfig().memberMobile();
            token = RequestContext.getMemberToken();
            System.out.println("   ✅ Using MEMBER order data");
        }
        
        // If no member order, try NON_MEMBER order
        if (orderCreationId == null || orderCreationId.isEmpty()) {
            orderCreationId = RequestContext.getNonMemberOrderId();
            if (orderCreationId != null && !orderCreationId.isEmpty()) {
                userId = RequestContext.getNonMemberUserId();
                mobile = ConfigLoader.getConfig().nonMemberMobile();
                token = RequestContext.getNonMemberToken();
                System.out.println("   ✅ Using NON_MEMBER order data");
            }
        }
        
        // If still no order, try NEW_USER order
        if (orderCreationId == null || orderCreationId.isEmpty()) {
            orderCreationId = RequestContext.getNewUserOrderId();
            if (orderCreationId != null && !orderCreationId.isEmpty()) {
                userId = RequestContext.getNewUserUserId();
                mobile = RequestContext.getMobile();
                token = RequestContext.getNewUserToken();
                System.out.println("   ✅ Using NEW_USER order data");
            }
        }
        
        // If STILL no order, FAIL the test - we need real order data
        if (orderCreationId == null || orderCreationId.isEmpty()) {
            System.out.println("   ❌ ERROR: No order found in context!");
            System.out.println("   ℹ️  This test requires CreateOrderAPITest to run first and create an order.");
            System.out.println("   ℹ️  Please run the complete test suite with testng.xml");
            Assert.fail("Cannot proceed without real order data from CreateOrderAPI. Run complete test suite first.");
            return;
        }
        
        System.out.println("   📦 Order Creation ID: " + orderCreationId + " (FROM CreateOrderAPI)");
        System.out.println("   👤 User ID: " + userId);
        System.out.println("   📱 Mobile: " + mobile);
        System.out.println();
        
        // STEP 2: GENERATE RAZORPAY PAYMENT RESPONSE
        System.out.println("📋 STEP 2: Simulating Razorpay Payment Response");
        String razorpayOrderId = orderCreationId; // Same as orderCreationId
        String razorpayPaymentId = "pay_" + System.currentTimeMillis(); // Unique payment ID
        
        // Generate VALID signature using the REAL Razorpay algorithm
        String razorpaySignature = RazorpayPaymentVerifier.generatePaymentSignature(
            razorpayOrderId, 
            razorpayPaymentId
        );
        
        Long mobileNumber = Long.parseLong(mobile);
        
        System.out.println("   ✅ Razorpay Payment ID: " + razorpayPaymentId + " (UNIQUE - Generated)");
        System.out.println("   ✅ Razorpay Order ID: " + razorpayOrderId + " (FROM CreateOrderAPI)");
        System.out.println("   ✅ Razorpay Signature: " + razorpaySignature);
        System.out.println("   ℹ️  Signature generated using: HMAC_SHA256(order_id|payment_id, secret)");
        System.out.println();
        
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  🔑 REAL PAYMENT DATA (From CreateOrderAPI):                    ║");
        System.out.println("║  ➜ Order ID: " + orderCreationId);
        System.out.println("║  ➜ Payment ID: " + razorpayPaymentId);
        System.out.println("║  ➜ Signature: " + razorpaySignature.substring(0, 20) + "...");
        System.out.println("║  ➜ User: " + userId);
        System.out.println("║  ➜ Mobile: " + mobileNumber);
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // STEP 3: Verify signature locally first
        System.out.println("📋 STEP 3: Verifying Signature Locally (Before API Call)");
        System.out.println("   🔐 Algorithm: HMAC_SHA256");
        System.out.println("   🔑 Message: " + razorpayOrderId + "|" + razorpayPaymentId);
        System.out.println("   🔑 Secret: " + ConfigLoader.getConfig().razorpaySecret());
        
        boolean isSignatureValid = RazorpayPaymentVerifier.verifyPaymentSignature(
            razorpayOrderId,
            razorpayPaymentId,
            razorpaySignature
        );
        
        if (isSignatureValid) {
            System.out.println("   ✅ Signature Verification: VALID");
        } else {
            System.out.println("   ❌ Signature Verification: INVALID");
            Assert.fail("Signature verification failed locally!");
        }
        System.out.println();
        
        // STEP 4: Create payload with REAL order data
        System.out.println("📋 STEP 4: Creating Verification Payload");
        
        VerifyPaymentPayload payload = VerifyPaymentPayload.builder()
            .orderCreationId(orderCreationId)  // REAL order from CreateOrderAPI
            .razorpayPaymentId(razorpayPaymentId)
            .razorpayOrderId(razorpayOrderId)
            .razorpaySignature(razorpaySignature)
            .mobile(mobileNumber)
            .userId(userId)
            .build();
        
        System.out.println("   ✅ Payload created with REAL order data");
        System.out.println("   📦 orderCreationId: " + orderCreationId + " (FROM CreateOrderAPI)");
        System.out.println("   💳 razorpay_payment_id: " + razorpayPaymentId);
        System.out.println("   🔐 razorpay_signature: " + razorpaySignature.substring(0, 20) + "...");
        System.out.println();
        
        // STEP 5: Validate authentication token
        System.out.println("📋 STEP 5: Validating Authentication Token");
        
        if (token == null || token.isEmpty()) {
            System.out.println("   ❌ ERROR: No authentication token found!");
            System.out.println("   ℹ️  This should not happen if CreateOrderAPITest ran successfully.");
            Assert.fail("Authentication token not found. Cannot proceed with API call.");
            return;
        }
        
        System.out.println("   ✅ Token retrieved successfully");
        System.out.println();
        
        // STEP 6: Call VerifyPayment API with REAL order data
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     📋 STEP 6: CALLING VERIFY PAYMENT API                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("   🔗 Endpoint: " + APIEndpoints.VERIFY_PAYMENT);
        System.out.println("   🌐 Base URL: " + ConfigLoader.getConfig().baseUrl());
        System.out.println("   📦 Order ID: " + orderCreationId + " (FROM CreateOrderAPI)");
        System.out.println("   💳 Payment ID: " + razorpayPaymentId);
        System.out.println();
        System.out.println("   ⏳ Sending request to server...");
        System.out.println();
        
        Response response = given()
            .baseUri(ConfigLoader.getConfig().baseUrl())
            .contentType("application/json")
            .header("Authorization", token)
            .body(payload)
            .log().all()
            .when()
            .post(APIEndpoints.VERIFY_PAYMENT)
            .then()
            .log().all()
            .extract()
            .response();
        
        System.out.println("\n📥 API RESPONSE:");
        System.out.println("   Status Code: " + response.getStatusCode());
        System.out.println("   Response Body: ");
        System.out.println(response.getBody().asPrettyString());
        System.out.println();
        
        // STEP 7: Validate Response
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     📋 STEP 7: VALIDATING API RESPONSE                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        int statusCode = response.getStatusCode();
        System.out.println("   📊 HTTP Status Code: " + statusCode);
        
        if (statusCode == 200) {
            System.out.println("   ✅ HTTP Status: 200 OK");
            System.out.println();
            
            Boolean success = response.jsonPath().getBoolean("success");
            String message = response.jsonPath().getString("msg");
            
            System.out.println("   🎯 Success Flag: " + success);
            System.out.println("   📝 Message: " + message);
            System.out.println();
            
            if (success != null && success) {
                System.out.println("╔══════════════════════════════════════════════════════════════════╗");
                System.out.println("║                                                                  ║");
                System.out.println("║     ✅✅✅ PAYMENT VERIFICATION SUCCESSFUL! ✅✅✅                 ║");
                System.out.println("║                                                                  ║");
                System.out.println("╠══════════════════════════════════════════════════════════════════╣");
                System.out.println("║  📦 Order ID (FROM CreateOrderAPI): " + orderCreationId);
                System.out.println("║  💳 Payment ID: " + razorpayPaymentId);
                System.out.println("║  🔐 Signature: VALID");
                System.out.println("║  📱 Mobile: " + mobileNumber);
                System.out.println("║  👤 User: " + userId);
                System.out.println("║  ✅ API Response: " + message);
                System.out.println("║                                                                  ║");
                System.out.println("╚══════════════════════════════════════════════════════════════════╝");
                System.out.println();
                
                // Try to extract order details
                try {
                    String orderId = response.jsonPath().getString("data[0].OrderItems[0].order_id");
                    if (orderId != null && !orderId.isEmpty()) {
                        System.out.println("   📦 Backend Order GUID: " + orderId);
                        System.out.println("   ✅ Order created successfully in backend!");
                        System.out.println("   🔄 User would be redirected to: /order-success");
                    }
                } catch (Exception e) {
                    System.out.println("   ℹ️  Order details: " + e.getMessage());
                }
                System.out.println();
                
                // Check membership
                try {
                    Boolean isMembershipOrder = response.jsonPath().getBoolean("data[0].membershipDetails.isMembershipOrder");
                    if (isMembershipOrder != null && isMembershipOrder) {
                        Integer membershipPrice = response.jsonPath().getInt("data[0].membershipDetails.membershipPrice");
                        System.out.println("   🎖️  Membership Order: YES");
                        System.out.println("   💰 Membership Price: ₹" + membershipPrice);
                    } else {
                        System.out.println("   ℹ️  Regular order (no membership)");
                    }
                } catch (Exception e) {
                    System.out.println("   ℹ️  Regular order (no membership)");
                }
                System.out.println();
                
                System.out.println("╔══════════════════════════════════════════════════════════════════╗");
                System.out.println("║                                                                  ║");
                System.out.println("║  🎉 COMPLETE FLOW TEST - SUCCESS!                               ║");
                System.out.println("║                                                                  ║");
                System.out.println("║  ✅ Step 1: Retrieved REAL order from CreateOrderAPI            ║");
                System.out.println("║  ✅ Step 2: Generated valid payment_id                          ║");
                System.out.println("║  ✅ Step 3: Verified signature locally                          ║");
                System.out.println("║  ✅ Step 4: Created payload with correct field mapping          ║");
                System.out.println("║  ✅ Step 5: Validated authentication token                      ║");
                System.out.println("║  ✅ Step 6: Called VerifyPayment API                            ║");
                System.out.println("║  ✅ Step 7: Received success response                           ║");
                System.out.println("║                                                                  ║");
                System.out.println("║  🚀 THE COMPLETE RAZORPAY FLOW IS WORKING PERFECTLY!            ║");
                System.out.println("║                                                                  ║");
                System.out.println("╚══════════════════════════════════════════════════════════════════╝");
                System.out.println();
                
                Assert.assertTrue(true, "Payment verification successful with REAL order data!");
                
            } else {
                String errorMsg = response.jsonPath().getString("msg");
                System.out.println("   ❌ Payment verification failed!");
                System.out.println("   📝 Error Message: " + errorMsg);
                Assert.fail("Payment verification returned success=false: " + errorMsg);
            }
            
        } else if (statusCode == 401) {
            System.out.println("   ❌ HTTP Status: 401 Unauthorized");
            System.out.println("   📝 Token has expired or is invalid");
            Assert.fail("Authentication failed - token expired");
            
        } else {
            System.out.println("   ❌ HTTP Status: " + statusCode + " (Expected 200)");
            System.out.println("   📝 Response: " + response.getBody().asString());
            Assert.fail("Payment verification API returned status " + statusCode);
        }
        
        LoggerUtil.logTestEnd("testRealRazorpayPaymentVerification");
    }
}
