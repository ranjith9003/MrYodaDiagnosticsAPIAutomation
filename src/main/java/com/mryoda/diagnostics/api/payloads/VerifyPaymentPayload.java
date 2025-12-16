package com.mryoda.diagnostics.api.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Verify Payment Payload
 * Matches the frontend structure exactly with Razorpay response field names
 * 
 * Frontend handler response structure:
 * {
 *   "orderCreationId": "order_xxx",
 *   "razorpay_payment_id": "pay_xxx",
 *   "razorpay_order_id": "order_xxx",
 *   "razorpay_signature": "signature_xxx",
 *   "mobile": 1234567890,
 *   "user_id": "user_guid"
 * }
 */
public class VerifyPaymentPayload {
    private String orderCreationId;
    
    @JsonProperty("razorpay_payment_id")
    private String razorpayPaymentId;
    
    @JsonProperty("razorpay_order_id")
    private String razorpayOrderId;
    
    @JsonProperty("razorpay_signature")
    private String razorpaySignature;
    
    private Long mobile;  // Changed to Long to match Number() in frontend
    private String user_id;
    
    // Constructor
    public VerifyPaymentPayload() {
    }
    
    // Builder pattern for easy construction
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private VerifyPaymentPayload payload = new VerifyPaymentPayload();
        
        public Builder orderCreationId(String orderCreationId) {
            payload.orderCreationId = orderCreationId;
            return this;
        }
        
        public Builder razorpayPaymentId(String razorpayPaymentId) {
            payload.razorpayPaymentId = razorpayPaymentId;
            return this;
        }
        
        public Builder razorpayOrderId(String razorpayOrderId) {
            payload.razorpayOrderId = razorpayOrderId;
            return this;
        }
        
        public Builder razorpaySignature(String razorpaySignature) {
            payload.razorpaySignature = razorpaySignature;
            return this;
        }
        
        public Builder mobile(String mobile) {
            payload.mobile = Long.parseLong(mobile);
            return this;
        }
        
        public Builder mobile(Long mobile) {
            payload.mobile = mobile;
            return this;
        }
        
        public Builder userId(String userId) {
            payload.user_id = userId;
            return this;
        }
        
        public VerifyPaymentPayload build() {
            return payload;
        }
    }
    
    // Getters and Setters
    public String getOrderCreationId() {
        return orderCreationId;
    }
    
    public void setOrderCreationId(String orderCreationId) {
        this.orderCreationId = orderCreationId;
    }
    
    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }
    
    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
    
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
    
    public String getRazorpaySignature() {
        return razorpaySignature;
    }
    
    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }
    
    public Long getMobile() {
        return mobile;
    }
    
    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }
    
    public String getUser_id() {
        return user_id;
    }
    
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    
    @Override
    public String toString() {
        return "VerifyPaymentPayload{" +
                "orderCreationId='" + orderCreationId + '\'' +
                ", razorpayPaymentId='" + razorpayPaymentId + '\'' +
                ", razorpayOrderId='" + razorpayOrderId + '\'' +
                ", razorpaySignature='" + razorpaySignature + '\'' +
                ", mobile=" + mobile +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
