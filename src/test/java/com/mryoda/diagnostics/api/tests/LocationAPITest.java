package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class LocationAPITest extends BaseTest {

    // -------------------------------
    // COMMON REUSABLE VALIDATION LOGIC
    // -------------------------------
    private void validateAndStoreLocations(Response response) {

        AssertionUtil.verifyTrue(response.jsonPath().getBoolean("success"), "API success flag");

        List<Map<String, Object>> locations = response.jsonPath().getList("data");
        AssertionUtil.verifyTrue(locations.size() > 0, "Location count > 0");

        System.out.println("\n📍 Total Locations Found: " + locations.size());

        for (int i = 0; i < locations.size(); i++) {
            String id = response.jsonPath().getString("data[" + i + "]._id");
            String title = response.jsonPath().getString("data[" + i + "].title");

            AssertionUtil.verifyNotNull(id, "Location ID " + i);
            AssertionUtil.verifyNotNull(title, "Location Title " + i);

            RequestContext.storeLocation(title, id);

            System.out.println("✔ Stored: " + title + " → " + id);
        }

        System.out.println("\n🟢 Locations stored for reuse in next APIs\n");
    }

    private Response callLocationAPI(String token) {

        AssertionUtil.verifyNotNull(token, "Token must NOT be null!");

        return new RequestBuilder()
                .setEndpoint(APIEndpoints.GET_LOCATION)
                .addHeader("Authorization", "Bearer " + token)
                .expectStatus(200)
                .post();   // yes, endpoint is POST
    }

    // ---------------------------------------------------------
    // 1️⃣ MEMBER → Location API
    // ---------------------------------------------------------
    @Test(priority = 5, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP")
    public void testGetLocations_ForMember() {

        System.out.println("\n===== LOCATION API — MEMBER =====");

        String token = RequestContext.getMemberToken();
        Response response = callLocationAPI(token);

        validateAndStoreLocations(response);
    }

    // ---------------------------------------------------------
    // 2️⃣ EXISTING MEMBER → Location API
    // ---------------------------------------------------------
    @Test(priority = 5, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP_ExistingMember")
    public void testGetLocations_ForExistingMember() {

        System.out.println("\n===== LOCATION API — EXISTING MEMBER =====");

        String token = RequestContext.getExistingMemberToken();
        Response response = callLocationAPI(token);

        validateAndStoreLocations(response);
    }

    // ---------------------------------------------------------
    // 3️⃣ NEW USER → Location API
    // ---------------------------------------------------------
    @Test(priority = 6, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP_NewlyRegisteredUser")
    public void testGetLocations_ForNewUser() {

        System.out.println("\n===== LOCATION API — NEW USER =====");

        String token = RequestContext.getNewUserToken();
        Response response = callLocationAPI(token);

        validateAndStoreLocations(response);
    }
}
