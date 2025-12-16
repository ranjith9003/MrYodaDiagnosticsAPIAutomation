# Quick Start Guide: Cross-API Validation

## 🚀 How to Run Tests

### Option 1: Run Complete Test Suite (Recommended)
```bash
mvn clean test
```

### Option 2: Run via TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Option 3: In Eclipse IDE
1. Right-click on `testng.xml`
2. Select **Run As** → **TestNG Suite**

---

## 📋 Test Execution Order

The tests run in this specific order (DO NOT change priorities):

1. **LoginAPITest** (Priority 1-2)
   - Creates tokens for MEMBER, EXISTING_MEMBER, NEW_USER
   
2. **UserCreateAPITest** (Priority 3-4)
   - Registers new user
   
3. **LocationAPITest** (Priority 5-6)
   - Fetches and stores location data
   - **✅ Validates**: "Madhapur" location exists
   
4. **BrandAPITest** (Priority 6-7)
   - Fetches and stores brand data
   - **✅ Validates**: "Diagnostics" brand exists
   
5. **GlobalSearchAPITest** (Priority 7)
   - Searches for tests at specific location
   - **✅ Cross-validates**: Location from LocationAPI
   - Stores test details (ID, price, status, etc.)
   
6. **AddToCartAPITest** (Priority 8)
   - Adds tests to cart
   - **✅ Cross-validates**: Brand from BrandAPI
   - **✅ Cross-validates**: Location from LocationAPI
   - **✅ Cross-validates**: Tests from GlobalSearchAPI
   - Stores complete cart response
   
7. **GetCartByIdAPITest** (Priority 9)
   - Retrieves cart by ID
   - **✅ Cross-validates**: All cart data from AddToCartAPI
   - **✅ Cross-validates**: Cart items from GlobalSearchAPI

---

## 🔍 What Gets Validated

### Cross-API Validations Implemented:

| Source API | Target API | Fields Validated |
|------------|-----------|------------------|
| LocationAPI | GlobalSearchAPI | location_id |
| LocationAPI | AddToCartAPI | location_id |
| BrandAPI | AddToCartAPI | brand_id |
| GlobalSearchAPI | AddToCartAPI | test _id, price, status |
| AddToCartAPI | GetCartByIdAPI | cart_guid, cart_id, total_amount, user_id, location_id, status, order_type |
| GlobalSearchAPI | GetCartByIdAPI | product_id, price, test_type, test_status (per item) |

---

## ✅ Expected Output

When all validations pass, you'll see:

```
╔══════════════════════════════════════════════════════════╗
║        GLOBAL SEARCH API TEST - COMPLETE FLOW           ║
╚══════════════════════════════════════════════════════════╝

🔍 STEP 1: Validating Location from Previous API (LocationAPI)
   ✅ Location 'Madhapur' validated from LocationAPI: <location_id>
   ✅ This location will be used for Global Search

---

╔══════════════════════════════════════════════════════════╗
║        ADD TO CART API — MEMBER (ALL TESTS)              ║
╚══════════════════════════════════════════════════════════╝

🔍 CROSS-API VALIDATION: Verifying Brand and Location from Previous APIs
   ✅ Brand 'Diagnostics' validated from BrandAPI: <brand_id>
   ✅ Location 'Madhapur' validated from LocationAPI: <location_id>
   ✅ Tests validated from GlobalSearchAPI: 2 tests found

---

╔══════════════════════════════════════════════════════════╗
║     COMPREHENSIVE GET CART VALIDATION - MEMBER           ║
╚══════════════════════════════════════════════════════════╝

🔍 STEP 3: Comparing with Add to Cart Response
   ✅ Cart GUID matches Add to Cart: <guid>
   ✅ Cart ID matches Add to Cart: <id>
   ✅ Total Amount matches Add to Cart: ₹1500

🔄 Cross-validating comprehensive cart data with AddToCart response:
   ✅ Lab Location ID matches: <location_id>
   ✅ User ID matches: <user_id>
   ✅ Cart Status matches: DRAFT
   ✅ Order Type matches: HOME
```

---

## 🛠️ Troubleshooting

### Test Dependency Errors
**Problem**: Tests fail with "NullPointerException" or "data not found"

**Solution**: Always run the complete suite. Individual tests depend on previous tests storing data in `RequestContext`.

```bash
# ❌ DON'T DO THIS (will fail)
mvn test -Dtest=AddToCartAPITest

# ✅ DO THIS (will succeed)
mvn clean test
```

---

### Configuration Issues
**Problem**: Tests can't find "Madhapur" location or "Diagnostics" brand

**Solution**: Check that these exist in the API response:
1. Check `LocationAPITest` output for available locations
2. Check `BrandAPITest` output for available brands
3. Update test names in `GlobalSearchAPITest` if needed

---

### Validation Failures
**Problem**: Cross-API validation fails (data mismatch)

**Solution**: This indicates a real issue! Check:
1. API response logs to see what data is returned
2. RequestContext storage to see what was saved
3. API might have changed - update test accordingly

---

## 📊 RequestContext Storage

All data is stored in `RequestContext` for cross-validation:

```java
// User Data (per user type)
RequestContext.getMemberToken()
RequestContext.getMemberUserId()
RequestContext.getMemberFirstName()

// Location Data
RequestContext.getLocationId("Madhapur")
RequestContext.getAllLocations()

// Brand Data
RequestContext.getBrandId("Diagnostics")
RequestContext.getAllBrands()

// Test Data (from Global Search)
RequestContext.getTest("Blood Coagulation")
RequestContext.getAllTests()

// Cart Data (per user type)
RequestContext.getMemberCartId()
RequestContext.getMemberCartNumericId()
RequestContext.getMemberTotalAmount()
RequestContext.getMemberAddToCartResponse()
RequestContext.getMemberCartItems()
```

---

## 🎯 Adding New Tests

### To add new tests to search and add to cart:

1. **Edit GlobalSearchAPITest.java**:
```java
String[] testsToSearch = {
    "Blood Coagulation",
    "CBC(COMPLETE BLOOD COUNT)",
    "YOUR NEW TEST NAME"  // ← Add here
};
```

2. **Run tests**:
```bash
mvn clean test
```

3. **Verify**: Check logs to confirm test was found and added to cart

---

## 🔄 Using CrossAPIValidator Helper

New helper class available for simplified validation:

```java
// Validate location consistency
CrossAPIValidator.validateLocationConsistency(
    currentLocationId, 
    "Madhapur", 
    "AddToCartAPI"
);

// Validate brand consistency
CrossAPIValidator.validateBrandConsistency(
    currentBrandId, 
    "Diagnostics", 
    "AddToCartAPI"
);

// Validate test consistency
CrossAPIValidator.validateTestConsistency(
    testName, 
    currentTestId, 
    currentPrice, 
    "GetCartByIdAPI"
);

// Validate cart consistency
CrossAPIValidator.validateCartConsistency(
    cartGuid, cartId, totalAmount, 
    userId, locationId, "MEMBER"
);
```

---

## 📝 Test Reports

After running tests, check:

1. **Console Output**: Real-time validation messages
2. **TestNG Report**: `test-output/index.html`
3. **Allure Report** (if configured): `allure-results/`

---

## ✨ Key Features

✅ **Complete Data Flow Validation**: Every API validates against previous APIs
✅ **Comprehensive Coverage**: Not just IDs, but all common fields
✅ **Clear Logging**: Detailed validation messages for debugging
✅ **User Type Separation**: Independent validation for MEMBER, EXISTING_MEMBER, NEW_USER
✅ **Helper Classes**: Reusable validation utilities

---

## 🎓 Best Practices

1. ✅ Always run `mvn clean test` for complete validation
2. ✅ Check console output for validation messages
3. ✅ Update test names if API data changes
4. ✅ Use CrossAPIValidator for new validations
5. ✅ Don't modify test priorities without understanding dependencies

---

## 📞 Support

For issues or questions:
1. Check console output for detailed error messages
2. Review `CROSS_API_VALIDATION_SUMMARY.md` for implementation details
3. Check individual test files for specific validations
4. Verify API responses haven't changed

---

**Last Updated**: December 11, 2025
**Version**: 1.0
