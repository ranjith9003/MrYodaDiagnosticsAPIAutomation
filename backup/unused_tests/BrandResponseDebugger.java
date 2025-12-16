package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.endpoints.APIEndpoints;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BrandResponseDebugger extends BaseTest {

    @Test(priority = 1, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP")
    public void debugBrandResponse() {
        String token = RequestContext.getMemberToken();
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       BRAND API RESPONSE DEBUGGER                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        Response response = new RequestBuilder()
                .setEndpoint(APIEndpoints.GET_ALL_BRANDS)
                .addHeader("Authorization", token)
                .addBodyParam("page", 1)
                .post();
        
        System.out.println("📊 Status Code: " + response.getStatusCode());
        System.out.println("\n📄 FULL RESPONSE:");
        System.out.println(response.asPrettyString());
        
        System.out.println("\n╚══════════════════════════════════════════════════════════╝\n");
    }
}
