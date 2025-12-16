package com.mryoda.diagnostics.api.utils;

import com.mryoda.diagnostics.api.config.ConfigLoader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Razorpay Payment Verification Utility
 * Simulates payment verification using Razorpay signature algorithm
 * Since there's no API endpoint available, this class handles verification locally
 */
public class RazorpayPaymentVerifier {
    
    private static String getRazorpaySecret() {
        return ConfigLoader.getConfig().razorpaySecret();
    }
    
    /**
     * Verify Razorpay payment signature
     * Algorithm: HMAC SHA256 of (order_id|payment_id) using secret key
     * 
     * @param orderId Razorpay order ID
     * @param paymentId Razorpay payment ID
     * @param signature Razorpay signature to verify
     * @return true if signature is valid, false otherwise
     */
    public static boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            // Create the message: order_id|payment_id
            String message = orderId + "|" + paymentId;
            
            // Generate HMAC SHA256 signature
            String generatedSignature = generateHmacSHA256(message, getRazorpaySecret());
            
            // Compare signatures
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            System.err.println("❌ Error verifying payment signature: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate simulated payment signature for testing
     * This is used to create valid test signatures
     * 
     * @param orderId Razorpay order ID
     * @param paymentId Razorpay payment ID
     * @return Generated HMAC SHA256 signature
     */
    public static String generatePaymentSignature(String orderId, String paymentId) {
        try {
            String message = orderId + "|" + paymentId;
            return generateHmacSHA256(message, getRazorpaySecret());
        } catch (Exception e) {
            System.err.println("❌ Error generating payment signature: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Generate HMAC SHA256 hash
     * 
     * @param message Message to hash
     * @param secret Secret key
     * @return Hex encoded HMAC SHA256 hash
     */
    private static String generateHmacSHA256(String message, String secret) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hash = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }
    
    /**
     * Convert byte array to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * Simulate complete payment verification flow
     * This replaces the API call for payment verification
     * 
     * @param orderCreationId Original order ID
     * @param razorpayPaymentId Payment ID from Razorpay
     * @param razorpayOrderId Order ID from Razorpay
     * @param razorpaySignature Signature from Razorpay
     * @param mobile User mobile number
     * @param userId User ID
     * @return PaymentVerificationResult object
     */
    public static PaymentVerificationResult verifyPayment(
            String orderCreationId,
            String razorpayPaymentId,
            String razorpayOrderId,
            String razorpaySignature,
            String mobile,
            String userId) {
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🔐 PAYMENT VERIFICATION (Java Code)                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        PaymentVerificationResult result = new PaymentVerificationResult();
        
        // Step 1: Verify order IDs match
        System.out.println("📋 Step 1: Verifying Order IDs");
        if (!orderCreationId.equals(razorpayOrderId)) {
            System.out.println("   ❌ Order ID mismatch!");
            System.out.println("      Expected: " + orderCreationId);
            System.out.println("      Received: " + razorpayOrderId);
            result.setSuccess(false);
            result.setMessage("Order ID mismatch");
            return result;
        }
        System.out.println("   ✅ Order IDs match: " + orderCreationId);
        
        // Step 2: Verify payment signature
        System.out.println("\n🔐 Step 2: Verifying Payment Signature");
        System.out.println("   Order ID: " + razorpayOrderId);
        System.out.println("   Payment ID: " + razorpayPaymentId);
        System.out.println("   Signature: " + razorpaySignature);
        
        // Generate expected signature
        String expectedSignature = generatePaymentSignature(razorpayOrderId, razorpayPaymentId);
        System.out.println("   Expected Signature: " + expectedSignature);
        
        boolean signatureValid = verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
        
        if (!signatureValid) {
            System.out.println("   ❌ Invalid payment signature!");
            result.setSuccess(false);
            result.setMessage("Invalid payment signature");
            return result;
        }
        System.out.println("   ✅ Payment signature verified successfully!");
        
        // Step 3: Validate user details
        System.out.println("\n👤 Step 3: Validating User Details");
        if (mobile == null || mobile.isEmpty()) {
            System.out.println("   ❌ Mobile number is required");
            result.setSuccess(false);
            result.setMessage("Mobile number is required");
            return result;
        }
        System.out.println("   ✅ Mobile: " + mobile);
        
        if (userId == null || userId.isEmpty()) {
            System.out.println("   ❌ User ID is required");
            result.setSuccess(false);
            result.setMessage("User ID is required");
            return result;
        }
        System.out.println("   ✅ User ID: " + userId);
        
        // Step 4: Generate simulated order ID (like backend would do)
        System.out.println("\n📦 Step 4: Generating Order Details");
        String simulatedOrderGuid = java.util.UUID.randomUUID().toString();
        System.out.println("   ✅ Order GUID: " + simulatedOrderGuid);
        
        // All verifications passed
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ PAYMENT VERIFICATION SUCCESS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Payment ID: " + razorpayPaymentId);
        System.out.println("║  Order ID: " + razorpayOrderId);
        System.out.println("║  User ID: " + userId);
        System.out.println("║  Mobile: " + mobile);
        System.out.println("║  Order GUID: " + simulatedOrderGuid);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        result.setSuccess(true);
        result.setMessage("Payment verified successfully");
        result.setOrderId(razorpayOrderId);
        result.setPaymentId(razorpayPaymentId);
        result.setOrderGuid(simulatedOrderGuid);
        result.setUserId(userId);
        result.setMobile(mobile);
        
        return result;
    }
    
    /**
     * Payment Verification Result class
     */
    public static class PaymentVerificationResult {
        private boolean success;
        private String message;
        private String orderId;
        private String paymentId;
        private String orderGuid;
        private String userId;
        private String mobile;
        
        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getOrderId() {
            return orderId;
        }
        
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        
        public String getPaymentId() {
            return paymentId;
        }
        
        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }
        
        public String getOrderGuid() {
            return orderGuid;
        }
        
        public void setOrderGuid(String orderGuid) {
            this.orderGuid = orderGuid;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public String getMobile() {
            return mobile;
        }
        
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
