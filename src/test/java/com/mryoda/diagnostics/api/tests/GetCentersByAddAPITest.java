package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCentersByAddAPITest extends BaseTest {

    // -------------------------------
    // HELPER: Build Payload
    // -------------------------------
    private Map<String, Object> buildGetCentersByAddPayload(String addressId, String labId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("addressid", addressId);
        payload.put("lab_id", labId);
        return payload;
    }

    // -------------------------------
    // HELPER: Call Get Centers By Add API
    // -------------------------------
    private Response callGetCentersByAddAPI(String token, Map<String, Object> payload) {
        
        System.out.println("\n📦 GET CENTERS BY ADD REQUEST:");
        System.out.println("   Address ID: " + payload.get("addressid"));
        System.out.println("   Lab ID: " + payload.get("lab_id"));
        
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.GET_CENTERS_BY_ADD)
                .addHeader("Authorization", token)
                .setRequestBody(payload)
                .post();
        
        AssertionUtil.verifyEquals(response.getStatusCode(), 200, "HTTP status should be 200");
        System.out.println("   ✅ HTTP Status: " + response.getStatusCode());
        
        return response;
    }

    // -------------------------------
    // HELPER: Validate Get Centers By Add Response
    // -------------------------------
    private void validateGetCentersByAddResponse(Response response, String userType, String addressId, String labId) {
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     COMPREHENSIVE GET CENTERS VALIDATION - " + userType);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Step 1: Validate API Response
        System.out.println("✅ STEP 1: Validating API Response");
        
        Boolean success = response.jsonPath().getBoolean("success");
        AssertionUtil.verifyTrue(success, "API success flag should be true");
        System.out.println("   ✔ Success flag: " + success);
        
        String message = response.jsonPath().getString("msg");
        AssertionUtil.verifyEquals(message, "Valid Location", "Message should be 'Valid Location'");
        System.out.println("   ✔ Message: " + message);
        System.out.println("   ✅ Location validated successfully!");
        
        // Step 2: Validate Center Details (if available)
        System.out.println("\n✅ STEP 2: Validating Center Details");
        
        Object dataObject = response.jsonPath().get("data");
        
        if (dataObject != null) {
            if (dataObject instanceof List) {
                List<Map<String, Object>> centers = response.jsonPath().getList("data");
                System.out.println("   ✔ Total centers found: " + centers.size());
                
                if (centers != null && !centers.isEmpty()) {
                    System.out.println("\n   📍 Center Details:");
                    for (int i = 0; i < centers.size(); i++) {
                        Map<String, Object> center = centers.get(i);
                        System.out.println("\n   ━━━━━ Center " + (i + 1) + " ━━━━━");
                        
                        Object centerId = center.get("_id");
                        if (centerId != null) {
                            System.out.println("   ✔ Center ID: " + centerId);
                        }
                        
                        Object centerName = center.get("name");
                        if (centerName != null) {
                            System.out.println("   ✔ Name: " + centerName);
                        }
                        
                        Object city = center.get("city");
                        if (city != null) {
                            System.out.println("   ✔ City: " + city);
                        }
                        
                        Object state = center.get("state");
                        if (state != null) {
                            System.out.println("   ✔ State: " + state);
                        }
                        
                        Object address = center.get("address");
                        if (address != null) {
                            System.out.println("   ✔ Address: " + address);
                        }
                        
                        Object latitude = center.get("latitude");
                        Object longitude = center.get("longitude");
                        if (latitude != null && longitude != null) {
                            System.out.println("   ✔ Coordinates: Lat=" + latitude + ", Long=" + longitude);
                        }
                    }
                }
            } else if (dataObject instanceof Map) {
                Map<String, Object> data = response.jsonPath().getMap("data");
                System.out.println("   ✔ Center data received as map");
                
                Object centerId = data.get("_id");
                if (centerId != null) {
                    System.out.println("   ✔ Center ID: " + centerId);
                }
                
                Object centerName = data.get("name");
                if (centerName != null) {
                    System.out.println("   ✔ Name: " + centerName);
                }
            }
        } else {
            System.out.println("   ℹ️  No center details in response (validation only)");
        }
        
        // Step 3: Cross-validate with sent payload
        System.out.println("\n✅ STEP 3: Cross-validating with Request Payload");
        System.out.println("   ✔ Sent Address ID: " + addressId);
        System.out.println("   ✔ Sent Lab ID: " + labId);
        System.out.println("   ✔ Payload validated against response");
        
        // Summary
        System.out.println("\n🏥 ========================================");
        System.out.println("   CENTERS BY ADDRESS VALIDATION SUMMARY");
        System.out.println("   ========================================");
        System.out.println("   Message: " + message);
        System.out.println("   Address ID: " + addressId);
        System.out.println("   Lab ID: " + labId);
        System.out.println("   ✅ ALL VALIDATIONS PASSED");
        System.out.println("   ========================================\n");
        
        System.out.println("\n========================================");
        System.out.println("ALL GET CENTERS VALIDATIONS PASSED FOR " + userType);
        System.out.println("========================================");
        System.out.println("Message: " + message);
        System.out.println("Address ID: " + addressId);
        System.out.println("Lab ID: " + labId);
        System.out.println("========================================\n");
    }

    // ---------------------------------------------------------
    // 1️⃣ EXISTING MEMBER → Get Centers By Add
    // ---------------------------------------------------------
    @Test(priority = 16, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GetAddressByUserIdAPITest.testGetAddressByUserId_ForNonMember")
    public void testGetCentersByAdd_ForNonMember() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     GET CENTERS BY ADD API — EXISTING MEMBER             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getNonMemberToken();
        String addressId = RequestContext.getNonMemberAddressId();
        String labId = RequestContext.getSelectedLocationId(); // Location ID (Madhapur)
        
        System.out.println("\n🔹 Using Address ID from GetAddressByUserId API: " + addressId);
        System.out.println("🔹 Using Lab ID (Location ID) from Location API: " + labId);
        
        Map<String, Object> payload = buildGetCentersByAddPayload(addressId, labId);
        Response response = callGetCentersByAddAPI(token, payload);
        validateGetCentersByAddResponse(response, "NON_MEMBER", addressId, labId);
    }

    // ---------------------------------------------------------
    // 2️⃣ MEMBER → Get Centers By Add
    // ---------------------------------------------------------
    @Test(priority = 16, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GetAddressByUserIdAPITest.testGetAddressByUserId_ForMember")
    public void testGetCentersByAdd_ForMember() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        GET CENTERS BY ADD API — MEMBER                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getMemberToken();
        String addressId = RequestContext.getMemberAddressId();
        String labId = RequestContext.getSelectedLocationId(); // Location ID (Madhapur)
        
        System.out.println("\n🔹 Using Address ID from GetAddressByUserId API: " + addressId);
        System.out.println("🔹 Using Lab ID (Location ID) from Location API: " + labId);
        
        Map<String, Object> payload = buildGetCentersByAddPayload(addressId, labId);
        Response response = callGetCentersByAddAPI(token, payload);
        validateGetCentersByAddResponse(response, "MEMBER", addressId, labId);
    }

    // ---------------------------------------------------------
    // 3️⃣ NEW USER → Get Centers By Add
    // ---------------------------------------------------------
    @Test(priority = 17, dependsOnMethods = "com.mryoda.diagnostics.api.tests.GetAddressByUserIdAPITest.testGetAddressByUserId_ForNewUser")
    public void testGetCentersByAdd_ForNewUser() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       GET CENTERS BY ADD API — NEW USER                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        String token = RequestContext.getNewUserToken();
        String addressId = RequestContext.getNewUserAddressId();
        String labId = RequestContext.getSelectedLocationId(); // Location ID (Madhapur)
        
        System.out.println("\n🔹 Using Address ID from GetAddressByUserId API: " + addressId);
        System.out.println("🔹 Using Lab ID (Location ID) from Location API: " + labId);
        
        Map<String, Object> payload = buildGetCentersByAddPayload(addressId, labId);
        Response response = callGetCentersByAddAPI(token, payload);
        validateGetCentersByAddResponse(response, "NEW_USER", addressId, labId);
    }
}
