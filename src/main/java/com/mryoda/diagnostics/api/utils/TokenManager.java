package com.mryoda.diagnostics.api.utils;

import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import io.restassured.response.Response;
import org.json.JSONObject;

public class TokenManager {

    // User type constants
    public static final String MEMBER = "MEMBER";
    public static final String EXISTING_MEMBER = "EXISTING_MEMBER";
    public static final String NEW_USER = "NEW_USER";
    public static final String GENERIC = "GENERIC";

    /**
     * Generate token with user type - stores all fields in appropriate RequestContext fields
     */
    public static String generateToken(String mobile, String userType) {

        System.out.println("\n==================================================");
        System.out.println("========== TOKEN GENERATION START (" + userType + ") ==========");
        System.out.println("==================================================");

        String countryCode = ConfigLoader.getConfig().countryCode();
        String otp = ConfigLoader.getConfig().staticOtp();

        System.out.println("📌 DEBUG → User Type     : " + userType);
        System.out.println("📌 DEBUG → Mobile        : " + mobile);
        System.out.println("📌 DEBUG → Country Code  : " + countryCode);
        System.out.println("📌 DEBUG → Static OTP    : " + otp);

        // ---------------------------------------------------
        // STEP 1️⃣: REQUEST OTP
        // ---------------------------------------------------
        System.out.println("\n📩 STEP 1: REQUESTING OTP…");

        JSONObject otpReq = new JSONObject();
        otpReq.put("mobile", mobile);
        otpReq.put("country_code", countryCode);

        System.out.println("➡️ OTP REQUEST BODY:");
        System.out.println(otpReq.toString(2));

        Response otpResponse = new RequestBuilder()
                .setEndpoint(APIEndpoints.OTP_REQUEST)
                .setRequestBody(otpReq.toString())
                .expectStatus(200)
                .post();

        System.out.println("🟢 OTP REQUEST SUCCESS → Status: " 
                + otpResponse.getStatusCode());

        // ---------------------------------------------------
        // STEP 2️⃣: VERIFY OTP
        // ---------------------------------------------------
        System.out.println("\n🔐 STEP 2: VERIFYING OTP & FETCHING TOKEN…");

        JSONObject verifyReq = new JSONObject();
        verifyReq.put("mobile", mobile);
        verifyReq.put("country_code", countryCode);
        verifyReq.put("otp", otp);

        System.out.println("➡️ OTP VERIFY BODY:");
        System.out.println(verifyReq.toString(2));

        Response verifyResponse = new RequestBuilder()
                .setEndpoint(APIEndpoints.OTP_VERIFY)
                .setRequestBody(verifyReq.toString())
                .expectStatus(200)
                .post();

        System.out.println("🟢 OTP VERIFIED → Status: " 
                + verifyResponse.getStatusCode());

        // ---------------------------------------------------
        // Extract ALL fields from response
        // ---------------------------------------------------
        String token = verifyResponse.jsonPath().getString("data.access_token");
        String firstName = verifyResponse.jsonPath().getString("data.first_name");
        String lastName = verifyResponse.jsonPath().getString("data.last_name");
        String actualMobile = verifyResponse.jsonPath().getString("data.mobile");
        String userId = verifyResponse.jsonPath().getString("data.guid");
        String email = verifyResponse.jsonPath().getString("data.email");
        String gender = verifyResponse.jsonPath().getString("data.gender");
        String dob = verifyResponse.jsonPath().getString("data.dob");
        String countryCodeFromResponse = verifyResponse.jsonPath().getString("data.country_code");

        // Debug Print EVERYTHING cleanly
        System.out.println("\n🔍 ===== DEBUG: EXTRACTED USER DETAILS =====");
        System.out.println("🔑 Access Token   : " + token);
        System.out.println("👤 First Name     : " + firstName);
        System.out.println("👤 Last Name      : " + lastName);
        System.out.println("📱 Mobile         : " + actualMobile);
        System.out.println("🆔 User ID (GUID) : " + userId);
        System.out.println("📧 Email          : " + email);
        System.out.println("⚧  Gender         : " + gender);
        System.out.println("🎂 DOB            : " + dob);
        System.out.println("🌍 Country Code   : " + countryCodeFromResponse);
        System.out.println("=============================================\n");

        // ---------------------------------------------------
        // VALIDATIONS
        // ---------------------------------------------------
        AssertionUtil.verifyNotNull(token, "Token must not be null");
        AssertionUtil.verifyEquals(actualMobile, mobile, "Mobile must match login mobile");
        AssertionUtil.verifyNotNull(firstName, "First Name");
        AssertionUtil.verifyNotNull(lastName, "Last Name");
        AssertionUtil.verifyNotNull(userId, "User ID");

        // ---------------------------------------------------
        // SAVE INTO REQUEST CONTEXT BASED ON USER TYPE
        // ---------------------------------------------------
        switch (userType) {
            case MEMBER:
                RequestContext.setMemberToken(token);
                RequestContext.setMemberFirstName(firstName);
                RequestContext.setMemberLastName(lastName);
                RequestContext.setMemberUserId(userId);
                
                System.out.println("💾 STORED INTO RequestContext (MEMBER):");
                System.out.println("✔ Member Token");
                System.out.println("✔ Member First Name: " + firstName);
                System.out.println("✔ Member Last Name: " + lastName);
                System.out.println("✔ Member User ID: " + userId);
                break;

            case EXISTING_MEMBER:
                RequestContext.setExistingMemberToken(token);
                RequestContext.setExistingMemberFirstName(firstName);
                RequestContext.setExistingMemberLastName(lastName);
                RequestContext.setExistingMemberUserId(userId);
                
                System.out.println("💾 STORED INTO RequestContext (EXISTING_MEMBER):");
                System.out.println("✔ Existing Member Token");
                System.out.println("✔ Existing Member First Name: " + firstName);
                System.out.println("✔ Existing Member Last Name: " + lastName);
                System.out.println("✔ Existing Member User ID: " + userId);
                break;

            case NEW_USER:
                RequestContext.setNewUserToken(token);
                RequestContext.setNewUserFirstName(firstName);
                RequestContext.setNewUserLastName(lastName);
                RequestContext.setNewUserUserId(userId);
                
                System.out.println("💾 STORED INTO RequestContext (NEW_USER):");
                System.out.println("✔ New User Token");
                System.out.println("✔ New User First Name: " + firstName);
                System.out.println("✔ New User Last Name: " + lastName);
                System.out.println("✔ New User User ID: " + userId);
                break;

            case GENERIC:
            default:
                RequestContext.setToken(token);
                RequestContext.setFirstName(firstName);
                RequestContext.setLastName(lastName);
                RequestContext.setUserId(userId);
                
                System.out.println("💾 STORED INTO RequestContext (GENERIC):");
                System.out.println("✔ Token");
                System.out.println("✔ First Name: " + firstName);
                System.out.println("✔ Last Name: " + lastName);
                System.out.println("✔ User ID: " + userId);
                break;
        }

        System.out.println("\n==================================================");
        System.out.println("========== TOKEN GENERATION END (" + userType + ") ===========");
        System.out.println("==================================================\n");

        return token;
    }

    /**
     * Backward compatible method - uses GENERIC user type
     */
    public static String generateToken(String mobile) {
        return generateToken(mobile, GENERIC);
    }
}
