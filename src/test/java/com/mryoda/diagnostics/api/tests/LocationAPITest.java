package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LocationAPITest extends BaseTest {

    @Test(priority = 4,
          description = "Verify location list and store all IDs & Titles",
          dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP")
    public void testGetLocations() {

        String token = RequestContext.getToken();
        AssertionUtil.verifyNotNull(token, "Token missing — Login test MUST run first!");

        Response response =
                given()
                        .header("Authorization",  token)
                        .log().all()
                .when()
                        .post(APIEndpoints.GET_LOCATION)
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        // Validate API success
        AssertionUtil.verifyTrue(response.jsonPath().getBoolean("success"), "API success flag");
        List<Map<String, Object>> locations = response.jsonPath().getList("data");
        AssertionUtil.verifyTrue(locations.size() > 0, "Locations count > 0");

        System.out.println("\n📍 Total Locations Found: " + locations.size());

        // Extract & Store ALL: { _id → title }
        for (int i = 0; i < locations.size(); i++) {
            String id = response.jsonPath().getString("data[" + i + "]._id");
            String title = response.jsonPath().getString("data[" + i + "].title");

            AssertionUtil.verifyNotNull(id, "Location ID for index " + i);
            AssertionUtil.verifyNotNull(title, "Location Title for index " + i);

            RequestContext.storeLocation(id, title);
            System.out.println("✔ Location Stored: " + title + " → " + id);
        }

        System.out.println("\n🟢 Locations fetched & stored successfully in RequestContext\n");
    }
}
