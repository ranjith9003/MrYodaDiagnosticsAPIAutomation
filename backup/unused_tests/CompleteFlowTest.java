package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.utils.LoggerUtil;
import org.testng.annotations.Test;

/**
 * Complete Flow Test - Orchestrates full end-to-end flow for all user types
 * 
 * FLOWS:
 * 1. MEMBER FLOW: Login → Location → Brand → Global Search → Add to Cart
 * 2. EXISTING MEMBER FLOW: Login → Location → Brand → Global Search → Add to Cart
 * 3. NEW USER FLOW: Create User → Login → Location → Brand → Global Search → Add to Cart
 * 
 * Note: Each flow stores and validates response data at every step
 */
public class CompleteFlowTest extends BaseTest {

    // ============================================================
    // MEMBER FLOW - Complete execution with validations
    // ============================================================
    @Test(priority = 1, groups = {"member-flow", "complete-flow"})
    public void testCompleteMemberFlow() {
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║           🎯 MEMBER FLOW - COMPLETE EXECUTION 🎯               ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Step 1: Member Login
            LoggerUtil.info("📍 Step 1/5: Member Login");
            new LoginAPITest().testLoginWithOTP();
            validateMemberLoginData();
            
            // Step 2: Fetch Locations
            LoggerUtil.info("📍 Step 2/5: Fetch Locations");
            new LocationAPITest().testGetLocations_ForMember();
            validateLocationData();
            
            // Step 3: Fetch Brands
            LoggerUtil.info("📍 Step 3/5: Fetch Brands");
            new BrandAPITest().testGetAllBrands_ForMember();
            validateBrandData();
            
            // Step 4: Global Search
            LoggerUtil.info("📍 Step 4/5: Global Search");
            new GlobalSearchAPITest().testGlobalSearchAndStore();
            validateGlobalSearchData();
            
            // Step 5: Add to Cart
            LoggerUtil.info("📍 Step 5/5: Add to Cart");
            new AddToCartAPITest().testAddToCart_ForMember();
            validateMemberCartData();
            
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                                ║");
            System.out.println("║           ✅ MEMBER FLOW COMPLETED SUCCESSFULLY ✅             ║");
            System.out.println("║                                                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            printMemberFlowSummary();
            
        } catch (Exception e) {
            LoggerUtil.error("❌ MEMBER FLOW FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ============================================================
    // EXISTING MEMBER FLOW - Complete execution with validations
    // ============================================================
    @Test(priority = 2, groups = {"existing-member-flow", "complete-flow"})
    public void testCompleteExistingMemberFlow() {
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║      🎯 EXISTING MEMBER FLOW - COMPLETE EXECUTION 🎯           ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Step 1: Existing Member Login
            LoggerUtil.info("📍 Step 1/5: Existing Member Login");
            new LoginAPITest().testLoginWithOTP_ExistingMember();
            validateExistingMemberLoginData();
            
            // Step 2: Fetch Locations
            LoggerUtil.info("📍 Step 2/5: Fetch Locations");
            new LocationAPITest().testGetLocations_ForExistingMember();
            validateLocationData();
            
            // Step 3: Fetch Brands
            LoggerUtil.info("📍 Step 3/5: Fetch Brands");
            new BrandAPITest().testGetAllBrands_ForExistingMember();
            validateBrandData();
            
            // Step 4: Global Search (uses same stored data)
            LoggerUtil.info("📍 Step 4/5: Global Search (reusing stored data)");
            // No need to call again, data already stored from Member flow
            
            // Step 5: Add to Cart
            LoggerUtil.info("📍 Step 5/5: Add to Cart");
            new AddToCartAPITest().testAddToCart_ForExistingMember();
            validateExistingMemberCartData();
            
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                                ║");
            System.out.println("║      ✅ EXISTING MEMBER FLOW COMPLETED SUCCESSFULLY ✅         ║");
            System.out.println("║                                                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            printExistingMemberFlowSummary();
            
        } catch (Exception e) {
            LoggerUtil.error("❌ EXISTING MEMBER FLOW FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ============================================================
    // NEW USER FLOW - Complete execution with validations
    // ============================================================
    @Test(priority = 3, groups = {"new-user-flow", "complete-flow"})
    public void testCompleteNewUserFlow() {
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║         🎯 NEW USER FLOW - COMPLETE EXECUTION 🎯               ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Step 1: User Registration
            LoggerUtil.info("📍 Step 1/6: User Registration");
            new UserCreateAPITest().testUserRegistration_CreateNewUser();
            validateNewUserRegistrationData();
            
            // Step 2: New User Login
            LoggerUtil.info("📍 Step 2/6: New User Login");
            new LoginAPITest().testLoginWithOTP_NewlyRegisteredUser();
            validateNewUserLoginData();
            
            // Step 3: Fetch Locations
            LoggerUtil.info("📍 Step 3/6: Fetch Locations");
            new LocationAPITest().testGetLocations_ForNewUser();
            validateLocationData();
            
            // Step 4: Fetch Brands
            LoggerUtil.info("📍 Step 4/6: Fetch Brands");
            new BrandAPITest().testGetAllBrands_ForNewUser();
            validateBrandData();
            
            // Step 5: Global Search (uses same stored data)
            LoggerUtil.info("📍 Step 5/6: Global Search (reusing stored data)");
            // No need to call again, data already stored
            
            // Step 6: Add to Cart
            LoggerUtil.info("📍 Step 6/6: Add to Cart");
            new AddToCartAPITest().testAddToCart_ForNewUser();
            validateNewUserCartData();
            
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                                ║");
            System.out.println("║         ✅ NEW USER FLOW COMPLETED SUCCESSFULLY ✅             ║");
            System.out.println("║                                                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            printNewUserFlowSummary();
            
        } catch (Exception e) {
            LoggerUtil.error("❌ NEW USER FLOW FAILED: " + e.getMessage());
            throw e;
        }
    }

    // ============================================================
    // VALIDATION METHODS
    // ============================================================
    
    private void validateMemberLoginData() {
        if (RequestContext.getMemberToken() == null || RequestContext.getMemberToken().isEmpty()) {
            throw new AssertionError("❌ Member token is null or empty!");
        }
        if (RequestContext.getMemberUserId() == null || RequestContext.getMemberUserId().isEmpty()) {
            throw new AssertionError("❌ Member user ID is null or empty!");
        }
        LoggerUtil.info("✅ Member login data validated successfully");
    }
    
    private void validateExistingMemberLoginData() {
        if (RequestContext.getExistingMemberToken() == null || RequestContext.getExistingMemberToken().isEmpty()) {
            throw new AssertionError("❌ Existing Member token is null or empty!");
        }
        if (RequestContext.getExistingMemberUserId() == null || RequestContext.getExistingMemberUserId().isEmpty()) {
            throw new AssertionError("❌ Existing Member user ID is null or empty!");
        }
        LoggerUtil.info("✅ Existing Member login data validated successfully");
    }
    
    private void validateNewUserRegistrationData() {
        if (RequestContext.getUserId() == null || RequestContext.getUserId().isEmpty()) {
            throw new AssertionError("❌ New User ID is null or empty!");
        }
        if (RequestContext.getMobile() == null || RequestContext.getMobile().isEmpty()) {
            throw new AssertionError("❌ New User mobile is null or empty!");
        }
        LoggerUtil.info("✅ New User registration data validated successfully");
    }
    
    private void validateNewUserLoginData() {
        if (RequestContext.getNewUserToken() == null || RequestContext.getNewUserToken().isEmpty()) {
            throw new AssertionError("❌ New User token is null or empty!");
        }
        if (RequestContext.getNewUserUserId() == null || RequestContext.getNewUserUserId().isEmpty()) {
            throw new AssertionError("❌ New User user ID is null or empty!");
        }
        LoggerUtil.info("✅ New User login data validated successfully");
    }
    
    private void validateLocationData() {
        if (RequestContext.getAllLocations() == null || RequestContext.getAllLocations().isEmpty()) {
            throw new AssertionError("❌ No locations stored!");
        }
        LoggerUtil.info("✅ Location data validated successfully - " + RequestContext.getAllLocations().size() + " locations stored");
    }
    
    private void validateBrandData() {
        if (RequestContext.getAllBrands() == null || RequestContext.getAllBrands().isEmpty()) {
            throw new AssertionError("❌ No brands stored!");
        }
        LoggerUtil.info("✅ Brand data validated successfully - " + RequestContext.getAllBrands().size() + " brands stored");
    }
    
    private void validateGlobalSearchData() {
        if (RequestContext.getTest("Blood Coagulation") == null) {
            throw new AssertionError("❌ Test 'Blood Coagulation' not found in stored data!");
        }
        LoggerUtil.info("✅ Global search data validated successfully");
    }
    
    private void validateMemberCartData() {
        if (RequestContext.getMemberCartId() == null || RequestContext.getMemberCartId().isEmpty()) {
            throw new AssertionError("❌ Member cart ID is null or empty!");
        }
        LoggerUtil.info("✅ Member cart data validated successfully");
    }
    
    private void validateExistingMemberCartData() {
        if (RequestContext.getExistingMemberCartId() == null || RequestContext.getExistingMemberCartId().isEmpty()) {
            throw new AssertionError("❌ Existing Member cart ID is null or empty!");
        }
        LoggerUtil.info("✅ Existing Member cart data validated successfully");
    }
    
    private void validateNewUserCartData() {
        if (RequestContext.getNewUserCartId() == null || RequestContext.getNewUserCartId().isEmpty()) {
            throw new AssertionError("❌ New User cart ID is null or empty!");
        }
        LoggerUtil.info("✅ New User cart data validated successfully");
    }

    // ============================================================
    // SUMMARY METHODS
    // ============================================================
    
    private void printMemberFlowSummary() {
        System.out.println("\n📊 =============== MEMBER FLOW SUMMARY ===============");
        System.out.println("👤 User ID       : " + RequestContext.getMemberUserId());
        System.out.println("👤 First Name    : " + RequestContext.getMemberFirstName());
        System.out.println("👤 Last Name     : " + RequestContext.getMemberLastName());
        System.out.println("🔑 Token         : " + (RequestContext.getMemberToken() != null ? "Generated ✅" : "Missing ❌"));
        System.out.println("📍 Locations     : " + RequestContext.getAllLocations().size() + " stored");
        System.out.println("🏷️  Brands        : " + RequestContext.getAllBrands().size() + " stored");
        System.out.println("🛒 Cart ID       : " + RequestContext.getMemberCartId());
        System.out.println("💰 Total Amount  : ₹" + RequestContext.getMemberTotalAmount());
        System.out.println("======================================================\n");
    }
    
    private void printExistingMemberFlowSummary() {
        System.out.println("\n📊 ========= EXISTING MEMBER FLOW SUMMARY ============");
        System.out.println("👤 User ID       : " + RequestContext.getExistingMemberUserId());
        System.out.println("👤 First Name    : " + RequestContext.getExistingMemberFirstName());
        System.out.println("👤 Last Name     : " + RequestContext.getExistingMemberLastName());
        System.out.println("🔑 Token         : " + (RequestContext.getExistingMemberToken() != null ? "Generated ✅" : "Missing ❌"));
        System.out.println("📍 Locations     : " + RequestContext.getAllLocations().size() + " stored");
        System.out.println("🏷️  Brands        : " + RequestContext.getAllBrands().size() + " stored");
        System.out.println("🛒 Cart ID       : " + RequestContext.getExistingMemberCartId());
        System.out.println("💰 Total Amount  : ₹" + RequestContext.getExistingMemberTotalAmount());
        System.out.println("======================================================\n");
    }
    
    private void printNewUserFlowSummary() {
        System.out.println("\n📊 ============= NEW USER FLOW SUMMARY ===============");
        System.out.println("👤 User ID       : " + RequestContext.getNewUserUserId());
        System.out.println("👤 First Name    : " + RequestContext.getNewUserFirstName());
        System.out.println("👤 Last Name     : " + RequestContext.getNewUserLastName());
        System.out.println("📱 Mobile        : " + RequestContext.getMobile());
        System.out.println("🔑 Token         : " + (RequestContext.getNewUserToken() != null ? "Generated ✅" : "Missing ❌"));
        System.out.println("📍 Locations     : " + RequestContext.getAllLocations().size() + " stored");
        System.out.println("🏷️  Brands        : " + RequestContext.getAllBrands().size() + " stored");
        System.out.println("🛒 Cart ID       : " + RequestContext.getNewUserCartId());
        System.out.println("💰 Total Amount  : ₹" + RequestContext.getNewUserTotalAmount());
        System.out.println("======================================================\n");
    }
}
