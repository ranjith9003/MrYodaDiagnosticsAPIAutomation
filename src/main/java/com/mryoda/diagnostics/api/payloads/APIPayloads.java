package com.mryoda.diagnostics.api.payloads;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized Payloads Class for Mr. Yoda Diagnostics API Tests
 * 
 * This class contains all request payload builders for various API endpoints.
 * Having centralized payloads makes it easier to maintain and update request structures.
 * 
 * @author Mr. Yoda Diagnostics API Team
 * @version 1.0.0
 */
public class APIPayloads {

    // ==================== LOGIN API PAYLOADS ====================
    
    /**
     * Create login payload for mobile-based authentication
     * 
     * @param mobile User's mobile number (10 digits)
     * @return Map containing login request payload
     */
    public static Map<String, Object> createLoginPayload(String mobile) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("mobile", mobile);
        return payload;
    }

    // ==================== USER REGISTRATION API PAYLOADS ====================
    
    /**
     * Create user registration payload
     * 
     * @param firstName User's first name
     * @param lastName User's last name
     * @param mobile User's mobile number (10 digits)
     * @param email User's email address
     * @param gender User's gender
     * @return Map containing user registration payload
     */
    public static Map<String, Object> createUserRegistrationPayload(
            String firstName, String lastName, String mobile, String email, String gender) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("first_name", firstName);
        payload.put("last_name", lastName);
        payload.put("mobile", mobile);
        payload.put("email", email);
        payload.put("gender", gender);
        return payload;
    }

    // ==================== ADD TO CART API PAYLOADS ====================
    
    /**
     * Create add to cart payload
     * 
     * @param userId User's ID
     * @param productId Product/Test ID to add
     * @param brandId Brand ID
     * @param locationId Location ID
     * @param orderType Order type (e.g., "home")
     * @return Map containing add to cart payload
     */
    public static Map<String, Object> createAddToCartPayload(
            String userId, String productId, String brandId, String locationId, String orderType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("product_id", productId);
        payload.put("brand_id", brandId);
        payload.put("location_id", locationId);
        payload.put("order_type", orderType);
        payload.put("quantity", 1); // Default quantity
        return payload;
    }
    
    /**
     * Create add to cart payload with custom quantity
     * 
     * @param userId User's ID
     * @param productId Product/Test ID to add
     * @param brandId Brand ID
     * @param locationId Location ID
     * @param orderType Order type (e.g., "home")
     * @param quantity Quantity of items
     * @return Map containing add to cart payload
     */
    public static Map<String, Object> createAddToCartPayload(
            String userId, String productId, String brandId, String locationId, String orderType, int quantity) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("product_id", productId);
        payload.put("brand_id", brandId);
        payload.put("location_id", locationId);
        payload.put("order_type", orderType);
        payload.put("quantity", quantity);
        return payload;
    }

    // ==================== ADDRESS API PAYLOADS ====================
    
    /**
     * Create address payload
     * 
     * @param userId User's ID
     * @param addressLine1 Address line 1
     * @param addressLine2 Address line 2
     * @param city City name
     * @param state State name
     * @param pincode Postal code
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return Map containing address payload
     */
    public static Map<String, Object> createAddressPayload(
            String userId, String addressLine1, String addressLine2, 
            String city, String state, String pincode, 
            String latitude, String longitude) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("address_line1", addressLine1);
        payload.put("address_line2", addressLine2);
        payload.put("city", city);
        payload.put("state", state);
        payload.put("pincode", pincode);
        payload.put("latitude", latitude);
        payload.put("longitude", longitude);
        payload.put("is_default", true);
        return payload;
    }
    
    /**
     * Create address payload with minimal fields
     * 
     * @param userId User's ID
     * @param addressLine1 Address line 1
     * @param city City name
     * @param state State name
     * @param pincode Postal code
     * @return Map containing address payload
     */
    public static Map<String, Object> createMinimalAddressPayload(
            String userId, String addressLine1, String city, String state, String pincode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("address_line1", addressLine1);
        payload.put("city", city);
        payload.put("state", state);
        payload.put("pincode", pincode);
        payload.put("is_default", true);
        return payload;
    }

    // ==================== SLOT UPDATE API PAYLOADS ====================
    
    /**
     * Create slot update payload for cart
     * 
     * @param userId User's ID
     * @param slotGuid Slot GUID to assign
     * @param cartId Cart ID
     * @return Map containing slot update payload
     */
    public static Map<String, Object> createSlotUpdatePayload(
            String userId, String slotGuid, String cartId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("slot_guid", slotGuid);
        payload.put("cart_id", cartId);
        return payload;
    }

    // ==================== CREATE ORDER API PAYLOADS ====================
    
    /**
     * Create order payload for Razorpay
     * 
     * @param userId User's ID
     * @return Map containing create order payload
     */
    public static Map<String, Object> createOrderPayload(String userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        return payload;
    }
    
    /**
     * Create order payload with amount
     * 
     * @param userId User's ID
     * @param amount Total amount for order
     * @return Map containing create order payload
     */
    public static Map<String, Object> createOrderPayload(String userId, int amount) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("amount", amount);
        return payload;
    }

    // ==================== PAYMENT VALIDATION PAYLOADS ====================
    
    /**
     * Create Razorpay payment options payload (for frontend)
     * This represents the configuration that would be sent to Razorpay
     * 
     * @param razorpayKey Razorpay API key
     * @param amount Amount in paise (rupees * 100)
     * @param currency Currency code (e.g., "INR")
     * @param orderId Razorpay order ID
     * @param name Merchant name
     * @param description Order description
     * @param mobile User's mobile number
     * @return Map containing Razorpay options
     */
    public static Map<String, Object> createRazorpayOptionsPayload(
            String razorpayKey, int amount, String currency, String orderId,
            String name, String description, String mobile) {
        Map<String, Object> options = new HashMap<>();
        options.put("key", razorpayKey);
        options.put("amount", String.valueOf(amount));
        options.put("currency", currency);
        options.put("order_id", orderId);
        options.put("name", name);
        options.put("description", description);
        options.put("redirect", true);
        options.put("timeout", 600); // 10 minutes
        
        // Prefill information
        Map<String, Object> prefill = new HashMap<>();
        prefill.put("contact", mobile);
        options.put("prefill", prefill);
        
        return options;
    }

    // ==================== SEARCH API PAYLOADS ====================
    
    /**
     * Create global search payload
     * 
     * @param searchQuery Search query string
     * @param brandId Brand ID to filter by
     * @param locationId Location ID to filter by
     * @return Map containing search payload
     */
    public static Map<String, Object> createSearchPayload(
            String searchQuery, String brandId, String locationId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", searchQuery);
        payload.put("brand_id", brandId);
        payload.put("location_id", locationId);
        return payload;
    }
    
    /**
     * Create search payload with pagination
     * 
     * @param searchQuery Search query string
     * @param brandId Brand ID to filter by
     * @param locationId Location ID to filter by
     * @param page Page number
     * @param limit Results per page
     * @return Map containing search payload
     */
    public static Map<String, Object> createSearchPayload(
            String searchQuery, String brandId, String locationId, int page, int limit) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", searchQuery);
        payload.put("brand_id", brandId);
        payload.put("location_id", locationId);
        payload.put("page", page);
        payload.put("limit", limit);
        return payload;
    }

    // ==================== CART UPDATE API PAYLOADS ====================
    
    /**
     * Create cart update payload
     * 
     * @param userId User's ID
     * @param cartId Cart ID
     * @param productId Product ID
     * @param quantity New quantity
     * @return Map containing cart update payload
     */
    public static Map<String, Object> createCartUpdatePayload(
            String userId, String cartId, String productId, int quantity) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("cart_id", cartId);
        payload.put("product_id", productId);
        payload.put("quantity", quantity);
        return payload;
    }

    // ==================== GENERIC PAYLOADS ====================
    
    /**
     * Create a generic payload with single key-value pair
     * 
     * @param key Payload key
     * @param value Payload value
     * @return Map containing single key-value
     */
    public static Map<String, Object> createGenericPayload(String key, Object value) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(key, value);
        return payload;
    }
    
    /**
     * Create empty payload
     * 
     * @return Empty Map
     */
    public static Map<String, Object> createEmptyPayload() {
        return new HashMap<>();
    }
    
    /**
     * Add field to existing payload
     * 
     * @param payload Existing payload
     * @param key Key to add
     * @param value Value to add
     * @return Updated payload
     */
    public static Map<String, Object> addFieldToPayload(
            Map<String, Object> payload, String key, Object value) {
        if (payload == null) {
            payload = new HashMap<>();
        }
        payload.put(key, value);
        return payload;
    }
    
    /**
     * Remove field from payload
     * 
     * @param payload Existing payload
     * @param key Key to remove
     * @return Updated payload
     */
    public static Map<String, Object> removeFieldFromPayload(
            Map<String, Object> payload, String key) {
        if (payload != null) {
            payload.remove(key);
        }
        return payload;
    }

    // ==================== PAYMENT VERIFICATION PAYLOADS ====================
    
    /**
     * Create payment verification payload
     * Used to verify payment after Razorpay callback
     * 
     * @param orderId Razorpay order ID
     * @param paymentId Razorpay payment ID
     * @param signature Razorpay signature for verification
     * @return Map containing verification payload
     */
    public static Map<String, Object> createPaymentVerificationPayload(
            String orderId, String paymentId, String signature) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("razorpay_order_id", orderId);
        payload.put("razorpay_payment_id", paymentId);
        payload.put("razorpay_signature", signature);
        return payload;
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Clone a payload (create a deep copy)
     * 
     * @param originalPayload Original payload to clone
     * @return Cloned payload
     */
    public static Map<String, Object> clonePayload(Map<String, Object> originalPayload) {
        if (originalPayload == null) {
            return new HashMap<>();
        }
        return new HashMap<>(originalPayload);
    }
    
    /**
     * Merge two payloads (second payload overwrites first on conflicts)
     * 
     * @param payload1 First payload
     * @param payload2 Second payload
     * @return Merged payload
     */
    public static Map<String, Object> mergePayloads(
            Map<String, Object> payload1, Map<String, Object> payload2) {
        Map<String, Object> merged = new HashMap<>();
        if (payload1 != null) {
            merged.putAll(payload1);
        }
        if (payload2 != null) {
            merged.putAll(payload2);
        }
        return merged;
    }
}
