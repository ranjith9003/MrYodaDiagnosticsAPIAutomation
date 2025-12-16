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

public class BrandAPITest extends BaseTest {

    private void validateAndStoreBrands(Response response) {
        AssertionUtil.verifyTrue(response.jsonPath().getBoolean("success"), "API success flag");

        List<Map<String, Object>> brands = response.jsonPath().getList("data");
        AssertionUtil.verifyTrue(brands != null && brands.size() > 0, "Brand count > 0");
        
        System.out.println("\n🏷️ Total Brands Found: " + brands.size());

        for (int i = 0; i < brands.size(); i++) {
            String brandName = response.jsonPath().getString("data[" + i + "].title");
            String brandId = response.jsonPath().getString("data[" + i + "].Guid");
            String brandStatus = response.jsonPath().getString("data[" + i + "].is_active");
            
            // Validate brand is ACTIVE before storing
            if (brandStatus != null) {
                System.out.println("   Brand: " + brandName + " | Status: " + brandStatus + " | ID: " + brandId);
            }
            
            RequestContext.storeBrand(brandName, brandId);
            System.out.println("✔ Stored: " + brandName + " → brand_id: " + brandId);
        }
        
        // Verify critical brand exists (Diagnostics brand is used in tests)
        String diagnosticsBrandId = RequestContext.getBrandId("Diagnostics");
        if (diagnosticsBrandId != null) {
            System.out.println("\n✅ Critical Brand 'Diagnostics' found and stored: " + diagnosticsBrandId);
            System.out.println("   This brand will be used in AddToCart API");
            // Set as selected brand for cart operations
            RequestContext.setSelectedBrand("Diagnostics");
            System.out.println("   ✔ Set as selected brand");
        } else {
            System.out.println("\n⚠️  Warning: 'Diagnostics' brand not found in response");
        }
        
        System.out.println("\n🟢 Brands stored for reuse in next APIs\n");
    }

    private Response callGetAllBrandsAPI(String token, int page) {
        System.out.println("📄 Requesting page: " + page);
        return new RequestBuilder()
                .setEndpoint(APIEndpoints.GET_ALL_BRANDS)
                .addHeader("Authorization", token)
                .addBodyParam("page", page)
                .expectStatus(200)
                .post();
    }

    @Test(priority = 6, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForMember")
    public void testGetAllBrands_ForMember() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          GET ALL BRANDS API — MEMBER                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        String token = RequestContext.getMemberToken();
        Response response = callGetAllBrandsAPI(token, 1);
        validateAndStoreBrands(response);
    }

    @Test(priority = 6, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForNonMember")
    public void testGetAllBrands_ForNonMember() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       GET ALL BRANDS API — EXISTING MEMBER               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        String token = RequestContext.getNonMemberToken();
        Response response = callGetAllBrandsAPI(token, 1);
        validateAndStoreBrands(response);
    }

    @Test(priority = 7, dependsOnMethods = "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForNewUser")
    public void testGetAllBrands_ForNewUser() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         GET ALL BRANDS API — NEW USER                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        String token = RequestContext.getNewUserToken();
        Response response = callGetAllBrandsAPI(token, 1);
        validateAndStoreBrands(response);
    }

    public static Response getAllBrands(String token, int page) {
        return new RequestBuilder()
                .setEndpoint(APIEndpoints.GET_ALL_BRANDS)
                .addHeader("Authorization", token)
                .addBodyParam("page", page)
                .expectStatus(200)
                .post();
    }
}

