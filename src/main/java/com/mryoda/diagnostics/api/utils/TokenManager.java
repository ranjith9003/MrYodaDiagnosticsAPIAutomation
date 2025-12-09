package com.mryoda.diagnostics.api.utils;

import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import io.restassured.response.Response;
import org.json.JSONObject;

public class TokenManager {

    public static String generateToken(String mobile) {

        System.out.println("\n================ TOKEN GENERATION START =================");

        String countryCode = ConfigLoader.getConfig().countryCode();
        String otp = ConfigLoader.getConfig().staticOtp();

        System.out.println("📌 Using Mobile: " + mobile);
        System.out.println("📌 Country Code: " + countryCode);
        System.out.println("📌 Static OTP: " + otp);

        // Step 1️⃣ Request OTP
        System.out.println("\n📩 STEP-1: Requesting OTP…");

        JSONObject request1 = new JSONObject();
        request1.put("mobile", mobile);
        request1.put("country_code", countryCode);

        Response otpResponse = new RequestBuilder()
                .setEndpoint(APIEndpoints.OTP_REQUEST)
                .setRequestBody(request1.toString())
                .post();

        System.out.println("🟢 OTP Request Status: " + otpResponse.getStatusCode());

        // Step 2️⃣ Verify OTP → Token
        System.out.println("\n🔐 STEP-2: Verifying OTP & Fetching Token…");

        request1.put("otp", otp);

        Response verifyResponse = new RequestBuilder()
                .setEndpoint(APIEndpoints.OTP_REQUEST)
                .setRequestBody(request1.toString())
                .post();

        System.out.println("🟢 Token API Status: " + verifyResponse.getStatusCode());

        String token = verifyResponse.jsonPath().getString("data.access_token");

        if (token == null || token.isEmpty()) {
            System.out.println("❌ ERROR: Token not received from API!!");
            throw new RuntimeException("Token missing! Check backend API response.");
        }

        System.out.println("🔑 ACCESS TOKEN: " + token);

        RequestContext.setToken(token);

        System.out.println("================ TOKEN GENERATION END =================\n");

        return token;
    }
}
