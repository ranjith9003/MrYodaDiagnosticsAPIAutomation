package com.mryoda.diagnostics.api.endpoints;

/**
 * API Endpoints - Centralized repository for all API endpoints used in framework
 * All API endpoints are defined here to maintain consistency and easy maintenance
 */
public class APIEndpoints {
    
    // ========== AUTHENTICATION & USER MANAGEMENT ==========
    /**
     * OTP Request Endpoint - Get OTP for mobile number
     * POST /otps/getOtp
     */
    public static final String OTP_REQUEST = "/otps/getOtp";
    
    /**
     * OTP Verify Endpoint - Verify OTP for mobile number
     * POST /otps/getOtp
     */
    public static final String OTP_VERIFY = "/otps/getOtp";
    
    /**
     * User Creation Endpoint - Create new user
     * POST /users/addUser
     */
    public static final String USER_CREATE = "/users/addUser";
    
    /**
     * User Profile Endpoint - Get user profile
     * GET /user/profile
     */
    public static final String USER_PROFILE = "/user/profile";
    
    /**
     * Update Profile Endpoint - Update user profile
     * PUT /user/update
     */
    public static final String UPDATE_PROFILE = "/user/update";
    
    // ========== LOCATION & SEARCH ==========
    /**
     * Get Locations Endpoint - Fetch all available locations
     * GET /tests/getlocations
     */
    public static final String GET_LOCATION = "/tests/getlocations";
    
    /**
     * Global Search Endpoint - Search for tests
     * POST tests/adminTests
     */
    public static final String GLOBAL_SEARCH = "tests/adminTests";
    
    // ========== BRAND & MEMBERSHIP ==========
    /**
     * Get All Brands Endpoint - Fetch all available brands
     * External API: GET https://staging-api-membership.yodaprojects.com/brand/getAllBrands
     */
    public static final String GET_ALL_BRANDS = "https://staging-api-membership.yodaprojects.com/brand/getAllBrands";
    
    // ========== CART MANAGEMENT ==========
    /**
     * Add to Cart Endpoint - Add items to cart (v2)
     * POST /carts/v2/addCart
     */
    public static final String ADD_TO_CART = "/carts/v2/addCart";
    
    /**
     * Get Cart By ID Endpoint - Retrieve cart details by user ID (v2)
     * GET /carts/v2/getCartById/{user_id}?order_type=home&location={location_id}
     */
    public static final String GET_CART_BY_ID = "/carts/v2/getCartById/{user_id}";
    
    // ========== ADDRESS MANAGEMENT ==========
    /**
     * Add Address Endpoint - Add new address for user
     * POST /address/addAddress
     */
    public static final String ADD_ADDRESS = "/address/addAddress";
    
    /**
     * Get Address By User ID Endpoint - Fetch all addresses for a user
     * GET /address/getAddressByUserId/{user_id}
     */
    public static final String GET_ADDRESS_BY_USER_ID = "/address/getAddressByUserId/{user_id}";
    
    // ========== SLOT & CENTER MANAGEMENT ==========
    /**
     * Get Centers By Address Endpoint - Fetch service centers near address
     * POST /slot/getCentersByadd
     */
    public static final String GET_CENTERS_BY_ADD = "/slot/getCentersByadd";
    
    /**
     * Get Slot Count By Time Endpoint - Fetch available slots by time
     * POST /slot/getSlotCountByTime
     */
    public static final String GET_SLOT_COUNT_BY_TIME = "/slot/getSlotCountByTime";
    
    // ========== ORDER MANAGEMENT ==========
    /**
     * Create Order Endpoint - Create new order (v2)
     * POST /gateway/v2/CreateOrder
     */
    public static final String CREATE_ORDER = "/gateway/v2/CreateOrder";
    
    /**
     * Verify Payment Endpoint - Verify Razorpay payment
     * POST /gateway/v2/VerifyPayment (Note: Capital V)
     */
    public static final String VERIFY_PAYMENT = "/gateway/v2/VerifyPayment";
    
    
}
