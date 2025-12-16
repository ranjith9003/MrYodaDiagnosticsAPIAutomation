package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.payloads.UserPayloadBuilder;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RandomDataUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

/**
 * User Create API Test - Tests user registration functionality
 */
public class UserCreateAPITest extends BaseTest {

    @Test(priority = 1)
    public void testUserRegistration_CreateNewUser() {

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           USER REGISTRATION TEST                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        // Generate random mobile
        String mobile = "9" + RandomDataUtil.getRandomMobile().substring(1);
        RequestContext.setMobile(mobile);

        System.out.println("\n📱 Generated Mobile: " + mobile);

        // Build user payload
        JSONObject req = UserPayloadBuilder.buildNewUserPayload();
        req.put("mobile", mobile);

        System.out.println("\n➡️ USER REGISTRATION REQUEST:");
        System.out.println(req.toString(2));

        // Make registration API call
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.USER_CREATE)
                .setRequestBody(req.toString())
                .expectStatus(201)
                .post();

        System.out.println("\n✅ USER REGISTRATION RESPONSE RECEIVED");

        // ---------------------------------------------------
        // EXTRACT ALL PARAMETERS from response
        // ---------------------------------------------------
        String userId = response.jsonPath().getString("data.guid");
        String firstName = response.jsonPath().getString("data.first_name");
        String lastName = response.jsonPath().getString("data.last_name");
        String email = response.jsonPath().getString("data.email");
        String gender = response.jsonPath().getString("data.gender");
        String dob = response.jsonPath().getString("data.dob");
        String responseMobile = response.jsonPath().getString("data.mobile");
        String countryCode = response.jsonPath().getString("data.country_code");
        String status = response.jsonPath().getString("data.status");
        String createdAt = response.jsonPath().getString("data.createdAt");
        String updatedAt = response.jsonPath().getString("data.updatedAt");

        // Print all extracted parameters
        System.out.println("\n🔍 ===== EXTRACTED USER REGISTRATION DATA =====");
        System.out.println("🆔 User ID (GUID)  : " + userId);
        System.out.println("👤 First Name      : " + firstName);
        System.out.println("👤 Last Name       : " + lastName);
        System.out.println("📧 Email           : " + email);
        System.out.println("⚧  Gender          : " + gender);
        System.out.println("🎂 DOB             : " + dob);
        System.out.println("📱 Mobile          : " + responseMobile);
        System.out.println("🌍 Country Code    : " + countryCode);
        System.out.println("📍 Status          : " + status);
        System.out.println("📅 Created At      : " + createdAt);
        System.out.println("📅 Updated At      : " + updatedAt);
        System.out.println("==============================================\n");

        // ---------------------------------------------------
        // VALIDATIONS
        // ---------------------------------------------------
        AssertionUtil.verifyEquals(responseMobile, mobile, "Mobile Number must match");

        // ---------------------------------------------------
        // STORE ALL PARAMETERS in RequestContext (Generic)
        // ---------------------------------------------------
        RequestContext.setUserId(userId);
        RequestContext.setFirstName(firstName);
        RequestContext.setLastName(lastName);

        System.out.println("💾 STORED IN RequestContext (GENERIC):");
        System.out.println("✔ User ID: " + userId);
        System.out.println("✔ First Name: " + firstName);
        System.out.println("✔ Last Name: " + lastName);
        System.out.println("✔ Mobile: " + mobile + " (already stored)");

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        USER REGISTRATION COMPLETED ✅                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
