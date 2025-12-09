package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RandomDataUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class LoginAPITest extends BaseTest {

    @Test(priority = 1)
    public void testLoginWithOTP() {

        String mobile = ConfigLoader.getConfig().memberMobile();
        String countryCode = ConfigLoader.getConfig().countryCode();
        String otp = ConfigLoader.getConfig().staticOtp();

        System.out.println("\n==== LOGIN USING BDD FLOW START ====");

        // 1️⃣ Request OTP - BDD format
        JSONObject request1 = new JSONObject();
        request1.put("mobile", mobile);
        request1.put("country_code", countryCode);

        Response otpResponse =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .body(request1.toString())
                        .log().all()
                .when()
                .post(APIEndpoints.OTP_REQUEST)
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        System.out.println("🟢 OTP Sent Successfully!");

        // 2️⃣ Verify OTP → Token - BDD format
        JSONObject request2 = new JSONObject();
        request2.put("mobile", mobile);
        request2.put("country_code", countryCode);
        request2.put("otp", otp);

        Response tokenResponse =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .body(request2.toString())
                        .log().all()
                .when()
                        .post("/otps/getOtp")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        System.out.println("🟢 OTP Verified Successfully!");

        // Extract important response values
        String token = tokenResponse.jsonPath().getString("data.access_token");
        String firstName = tokenResponse.jsonPath().getString("data.first_name");
        String lastName = tokenResponse.jsonPath().getString("data.last_name");
        String actualMobile = tokenResponse.jsonPath().getString("data.mobile");

        // 🔍 Field Validations
        AssertionUtil.verifyNotNull(token, "Token");
        AssertionUtil.verifyNotNull(firstName, "First Name");
        AssertionUtil.verifyNotNull(lastName, "Last Name");
        AssertionUtil.verifyEquals(actualMobile, mobile, "Mobile Number Validation");

        System.out.println("\n🔍 RESPONSE VALIDATION RESULTS:");
        System.out.println("✔ Token stored");
        System.out.println("✔ Name: " + firstName + " " + lastName);
        System.out.println("✔ Mobile verified: " + actualMobile);

        // Save token for next tests
        RequestContext.setToken(token);

        System.out.println("🔑 TOKEN SAVED FOR FUTURE TEST CASES");
        System.out.println("==== LOGIN USING BDD FLOW END ====\n");
    }
    
    @Test(priority = 1)
    public void testLoginWithOTP_ExistingMember() {

        String mobile = ConfigLoader.getConfig().nonMemberMobile();
        String countryCode = ConfigLoader.getConfig().countryCode();
        String otp = ConfigLoader.getConfig().staticOtp();

        System.out.println("\n==== LOGIN FLOW - EXISTING MEMBER ====");

        JSONObject request1 = new JSONObject();
        request1.put("mobile", mobile);
        request1.put("country_code", countryCode);

        Response otpResponse =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .body(request1.toString())
                        .log().all()
                .when()
                        .post(APIEndpoints.OTP_REQUEST)
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        JSONObject request2 = new JSONObject();
        request2.put("mobile", mobile);
        request2.put("country_code", countryCode);
        request2.put("otp", otp);

        Response tokenResponse =
                given()
                        .contentType("application/json")
                        .accept("application/json")
                        .body(request2.toString())
                        .log().all()
                .when()
                        .post("/otps/getOtp")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        String token = tokenResponse.jsonPath().getString("data.access_token");
        String actualMobile = tokenResponse.jsonPath().getString("data.mobile");

        AssertionUtil.verifyNotNull(token, "Token");
        AssertionUtil.verifyEquals(actualMobile, mobile, "Registered member mobile matches");

        RequestContext.setToken(token);

        System.out.println("==== MEMBER LOGIN SUCCESS ====\n");
    }
    @Test(priority = 2,
    	      dependsOnMethods = "com.mryoda.diagnostics.api.tests.UserCreateAPITest.testUserRegistration_CreateNewUser")
    	public void testLoginWithOTP_NewlyRegisteredUser() {

    	    System.out.println("\n========== LOGIN WITH OTP - NEW USER ==========");

    	    String mobile = RequestContext.getMobile();
    	    String countryCode = ConfigLoader.getConfig().countryCode();
    	    String otp = ConfigLoader.getConfig().staticOtp();

    	    JSONObject otpReq = new JSONObject();
    	    otpReq.put("mobile", mobile);
    	    otpReq.put("country_code", countryCode);

    	    Response otpResponse =
    	            given()
    	                    .contentType("application/json")
    	                    .log().all()
    	                    .body(otpReq.toString())
    	            .when()
    	                    .post(APIEndpoints.OTP_REQUEST)
    	            .then()
    	                    .log().all()
    	                    .statusCode(200)
    	                    .extract().response();

    	    JSONObject verifyReq = new JSONObject();
    	    verifyReq.put("mobile", mobile);
    	    verifyReq.put("country_code", countryCode);
    	    verifyReq.put("otp", otp);

    	    Response verifyResponse =
    	            given()
    	                    .contentType("application/json")
    	                    .log().all()
    	                    .body(verifyReq.toString())
    	            .when()
    	                    .post(APIEndpoints.OTP_REQUEST)
    	            .then()
    	                    .log().all()
    	                    .statusCode(200)
    	                    .extract().response();

    	    String token = verifyResponse.jsonPath().getString("data.access_token");
    	    String guid = verifyResponse.jsonPath().getString("data.guid");
    	    String userId = verifyResponse.jsonPath().getString("data.id");

    	    AssertionUtil.verifyNotNull(token, "Access Token");
    	    AssertionUtil.verifyNotNull(guid, "GUID");
    	    AssertionUtil.verifyNotNull(userId, "User ID");

    	    RequestContext.setToken(token);
    	    RequestContext.setUserGuid(guid);
    	    RequestContext.setUserId(userId);

    	    System.out.println("🔐 New User Logged In Successfully");
    	    System.out.println("GUID: " + guid);
    	    System.out.println("UserId: " + userId);
    	    System.out.println("========== LOGIN FLOW END ==========\n");
    	}

}