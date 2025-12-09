package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.payloads.UserPayloadBuilder;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import com.mryoda.diagnostics.api.utils.RandomDataUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class UserCreateAPITest extends BaseTest {

	@Test(priority = 1)
	public void testUserRegistration_CreateNewUser() {

	    System.out.println("\n========== USER REGISTRATION START ==========");

	    String mobile = "9" + RandomDataUtil.getRandomMobile().substring(1);
	    RequestContext.setMobile(mobile);

	    JSONObject req = UserPayloadBuilder.buildNewUserPayload();
	    System.out.println("📩 Registration Request:\n" + req.toString(2));

	    Response response =
	            given()
	                    .contentType("application/json")
	                    .log().all()
	                    .body(req.toString())
	            .when()
	                    .post(APIEndpoints.USER_CREATE)
	            .then()
	                    .log().all()
	                    .statusCode(anyOf(is(201), is(200)))
	                    .extract().response();

	    AssertionUtil.verifyEquals(
	            response.jsonPath().getString("data.mobile"),
	            mobile,
	            "Mobile Number"
	    );

	    String guid = response.jsonPath().getString("data.guid");
	    AssertionUtil.verifyNotNull(guid, "GUID");
	    RequestContext.setUserGuid(guid);

	    String userId = response.jsonPath().getString("data.id");
	    AssertionUtil.verifyNotNull(userId, "User ID");
	    RequestContext.setUserId(userId);

	    System.out.println("🟢 USER REGISTERED SUCCESSFULLY");
	    System.out.println("GUID: " + guid);
	    System.out.println("UserId: " + userId);
	    System.out.println("========== USER REGISTRATION END ==========\n");
	}

}
