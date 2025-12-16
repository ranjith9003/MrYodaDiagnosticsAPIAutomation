package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.builders.RequestBuilder;
import com.mryoda.diagnostics.api.utils.RequestContext;
import io.restassured.response.Response;
import org.testng.annotations.Test;

/**
 * Standalone Brand API Endpoint Tester
 * Tests different endpoint variations to find the correct one
 */
public class BrandEndpointTester extends BaseTest {

    @Test(priority = 1, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LoginAPITest.testLoginWithOTP")
    public void testBrandEndpointVariations() {
        
        String token = RequestContext.getMemberToken();
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       TESTING BRAND API ENDPOINT VARIATIONS              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        String[] endpointsToTry = {
            "/brand/getAllBrands",      // Original
            "/brands/getAllBrands",     // Plural
            "/brand/getallbrands",      // Lowercase
            "/brands/getallbrands",     // Plural lowercase
            "brand/getAllBrands",       // Without leading slash
            "brands/getAllBrands"       // Plural without leading slash
        };
        
        for (String endpoint : endpointsToTry) {
            System.out.println("\n📍 Testing endpoint: " + endpoint);
            
            // Try POST with body
            System.out.println("  → Trying POST with body...");
            try {
                Response response = new RequestBuilder()
                        .setEndpoint(endpoint)
                        .addHeader("Authorization", token)
                        .addBodyParam("page", 1)
                        .post();
                
                int statusCode = response.getStatusCode();
                System.out.println("    ✓ POST Status: " + statusCode);
                
                if (statusCode == 200) {
                    System.out.println("    ✅ SUCCESS! POST works for this endpoint!");
                    System.out.println("    📄 Response: " + response.asString().substring(0, Math.min(300, response.asString().length())));
                    return;
                }
                
            } catch (Exception e) {
                System.out.println("    ✗ POST Error: " + e.getMessage());
            }
            
            // Try GET with query params
            System.out.println("  → Trying GET with query params...");
            try {
                Response response = new RequestBuilder()
                        .setEndpoint(endpoint)
                        .addHeader("Authorization", token)
                        .setQueryParams(java.util.Map.of("page", 1))
                        .get();
                
                int statusCode = response.getStatusCode();
                System.out.println("    ✓ GET Status: " + statusCode);
                
                if (statusCode == 200) {
                    System.out.println("    ✅ SUCCESS! GET works for this endpoint!");
                    System.out.println("    📄 Response: " + response.asString().substring(0, Math.min(300, response.asString().length())));
                    return;
                }
                
            } catch (Exception e) {
                System.out.println("    ✗ GET Error: " + e.getMessage());
            }
        }
        
        System.out.println("\n╚══════════════════════════════════════════════════════════╝\n");
    }
}
