package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.utils.GlobalSearchHelper;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.utils.AssertionUtil;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class GlobalSearchAPITest extends BaseTest {

	@Test(priority = 7, dependsOnMethods = {
	        "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForMember",
	        "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForExistingMember",
	        "com.mryoda.diagnostics.api.tests.LocationAPITest.testGetLocations_ForNewUser"
	})
	public void testGlobalSearchAndStore() {

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║        GLOBAL SEARCH API TEST - COMPLETE FLOW           ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    System.out.println("📌 DEBUG → STORED LOCATIONS: " + RequestContext.getAllLocations());

	    // SELECT MADHAPUR
	    String location = "Madhapur";

	    // FULL TEST NAMES (NO SPLITTING)
	    String[] testsToSearch = {
	            "Blood Coagulation",
	            "Complete Blood Count"
	    };

	    System.out.println("\n🎯 Tests to Search: " + String.join(", ", testsToSearch));
	    
	    // AUTO-SEARCH full test names → helper will handle keyword splitting internally
	    Response res = GlobalSearchHelper.searchTestsByFullNames(testsToSearch, location);

	    // Extract each test and store it with ALL fields from JSON response
	    GlobalSearchHelper.extractAndStoreTests(res, testsToSearch);

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║              VALIDATION & FIELD RETRIEVAL                ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    // Validation - Test exists
	    AssertionUtil.verifyNotNull(
	            RequestContext.getTest("Blood Coagulation"),
	            "Stored test 'Blood Coagulation' must not be null"
	    );
	    
	    AssertionUtil.verifyNotNull(
	            RequestContext.getTest("Complete Blood Count"),
	            "Stored test 'Complete Blood Count' must not be null"
	    );

	    // === DEMONSTRATE HELPER METHODS ===
	    
	    // Test 1: Blood Coagulation
	    String testName1 = "Blood Coagulation";
	    System.out.println("\n🧪 Retrieving fields for: " + testName1);
	    
	    String testId1 = GlobalSearchHelper.getTestId(testName1);
	    String productId1 = GlobalSearchHelper.getProductId(testName1);
	    double price1 = GlobalSearchHelper.getTestPrice(testName1);
	    double originalPrice1 = GlobalSearchHelper.getOriginalPrice(testName1);
	    String homeCollection1 = GlobalSearchHelper.getHomeCollection(testName1);
	    String status1 = GlobalSearchHelper.getTestStatus(testName1);
	    String type1 = GlobalSearchHelper.getTestType(testName1);
	    
	    System.out.println("   ✅ Test ID         : " + testId1);
	    System.out.println("   ✅ Product ID      : " + productId1);
	    System.out.println("   ✅ Price           : ₹" + price1);
	    System.out.println("   ✅ Original Price  : ₹" + originalPrice1);
	    System.out.println("   ✅ Home Collection : " + homeCollection1);
	    System.out.println("   ✅ Status          : " + status1);
	    System.out.println("   ✅ Type            : " + type1);
	    
	    // Validate extracted values
	    AssertionUtil.verifyNotNull(testId1, "Test ID should not be null");
	    AssertionUtil.verifyNotNull(productId1, "Product ID should not be null");
	    AssertionUtil.verifyTrue(price1 > 0, "Price should be greater than 0");
	    AssertionUtil.verifyEquals(status1, "ACTIVE", "Test should be ACTIVE");
	    
	    // Test 2: Complete Blood Count
	    String testName2 = "Complete Blood Count";
	    System.out.println("\n🧪 Retrieving fields for: " + testName2);
	    
	    String testId2 = GlobalSearchHelper.getTestId(testName2);
	    double price2 = GlobalSearchHelper.getTestPrice(testName2);
	    String status2 = GlobalSearchHelper.getTestStatus(testName2);
	    
	    System.out.println("   ✅ Test ID         : " + testId2);
	    System.out.println("   ✅ Price           : ₹" + price2);
	    System.out.println("   ✅ Status          : " + status2);
	    
	    // Print complete details using helper method
	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║           COMPLETE TEST DETAILS (ALL FIELDS)             ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    GlobalSearchHelper.printTestDetails(testName1);
	    GlobalSearchHelper.printTestDetails(testName2);
	    
	    // Access any custom field using getTestField
	    Object genders1 = GlobalSearchHelper.getTestField(testName1, "genders");
	    Object businessType1 = GlobalSearchHelper.getTestField(testName1, "business_type");
	    Object department1 = GlobalSearchHelper.getTestField(testName1, "department");
	    
	    System.out.println("\n📋 Additional Custom Fields:");
	    System.out.println("   Genders        : " + genders1);
	    System.out.println("   Business Type  : " + businessType1);
	    System.out.println("   Department     : " + department1);

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║                  TEST COMPLETED ✅                        ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    System.out.println("🟢 GLOBAL SEARCH TEST COMPLETED SUCCESSFULLY!");
	    System.out.println("📊 All fields extracted from JSON response and stored");
	    System.out.println("✅ Helper methods working correctly");
	    System.out.println("✅ Data ready for use in subsequent tests\n");
	}

}
