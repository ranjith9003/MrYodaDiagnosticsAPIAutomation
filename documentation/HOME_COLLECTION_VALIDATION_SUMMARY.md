# Home Collection Validation - Implementation Summary

## Overview
Enhanced the GetCartByIdAPI test to validate home collection availability for all tests in the cart when `order_type=home` is specified.

## Changes Made

### 1. **RequestBuilder Enhancement**
   - **File**: `src/main/java/com/mryoda/diagnostics/api/builders/RequestBuilder.java`
   - **Changes**:
     - Changed `queryParams` from `Map<String, ?>` to `Map<String, Object>` for mutability
     - Added `addQueryParam(String key, String value)` method
     - Added `addQueryParam(String key, Object value)` method
     - Updated `setQueryParams()` to use `putAll()` instead of assignment

### 2. **RequestContext Enhancement**
   - **File**: `src/main/java/com/mryoda/diagnostics/api/utils/RequestContext.java`
   - **Changes**:
     - Added `clearAllTests()` method to allow clearing stored tests before filtering

### 3. **GetCartByIdAPITest Enhancement**
   - **File**: `src/test/java/com/mryoda/diagnostics/api/tests/GetCartByIdAPITest.java`
   - **Changes**:
     - Updated `callGetCartByIdAPI()` to accept `locationName` parameter
     - Added query parameters: `order_type=home` and `location={location_id}`
     - Added comprehensive home collection validation in `validateGetCartByIdResponse()`
     - Added home collection counters: `homeCollectionCount` and `nonHomeCollectionCount`
     - Added validation to ensure ALL tests in cart have `home_collection=true`
     - Added detailed logging for home collection status of each test
     - Added home collection summary section in validation output
     - Updated all test methods to pass location parameter ("Madhapur")

### 4. **GlobalSearchAPITest Enhancement**
   - **File**: `src/test/java/com/mryoda/diagnostics/api/tests/GlobalSearchAPITest.java`
   - **Changes**:
     - Added home collection filtering after test extraction
     - Only stores tests with `home_collection=true` for cart operations
     - Added comprehensive logging showing:
       - Which tests have home collection enabled
       - Which tests are excluded (no home collection)
       - Summary statistics
     - Handles multiple home_collection field formats:
       - Boolean: `true/false`
       - String: `"true"/"yes"/"1"`

## API Endpoint Updated

### GetCartById API
```
GET https://staging-api-diagnostics.yodaprojects.com/carts/getCartById/{userId}
    ?order_type=home
    &location={location_id}
```

## Validation Flow

### 1. Global Search Phase (Priority 7)
   - Search for configured tests
   - Extract test details including `home_collection` flag
   - **Filter tests**: Only keep tests with `home_collection=true`
   - Store filtered tests in RequestContext

### 2. Add to Cart Phase (Priority 8-9)
   - Add ONLY home collection tests to cart
   - Cart automatically contains only valid tests for home orders

### 3. Get Cart By ID Phase (Priority 10-11)
   - Call GetCartById API with `order_type=home` and location
   - Validate EVERY test in cart has `home_collection=true`
   - Count and report any invalid tests
   - **FAIL test if any test lacks home collection support**

## Validation Checks

### For Each Cart Item:
1. ✅ Extract test details from cart response
2. ✅ Cross-validate with stored GlobalSearch data
3. ✅ Check `home_collection` field
4. ✅ Verify `home_collection=true` (required for home orders)
5. ✅ Count valid vs invalid tests
6. ✅ Display detailed status for each test

### Summary Validation:
- Total tests in cart
- Tests with home collection enabled
- Tests WITHOUT home collection (should be 0)
- **FAIL if any test lacks home collection**

## Sample Output

```
🏠 STEP 4.1: Validating Home Collection Availability
   Since order_type=home, all tests in cart MUST have home_collection = true

   ━━━━━ Item 1 Validation ━━━━━
   🆔 Product ID: 64a123...
   📝 Test Name: Blood Coagulation
   🏠 Home Collection: true
   ✅ Test supports home collection (VALID for home order)

   ━━━━━ Item 2 Validation ━━━━━
   🆔 Product ID: 64b456...
   📝 Test Name: CBC(COMPLETE BLOOD COUNT)
   🏠 Home Collection: true
   ✅ Test supports home collection (VALID for home order)

🏠 ========================================
   HOME COLLECTION VALIDATION SUMMARY
   ========================================
   Total tests in cart: 2
   Tests with home collection: 2
   Tests WITHOUT home collection: 0
   ✅ ALL TESTS SUPPORT HOME COLLECTION
   ========================================
```

## How to Run

### Run Complete Test Suite:
```bash
cd C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
mvn clean test
```

### Run via TestNG XML:
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### In Eclipse:
1. Right-click `testng.xml`
2. Select `Run As` → `TestNG Suite`

## Test Execution Order

1. **LoginAPITest** (Priority 1-3) - Generate tokens for all user types
2. **UserCreateAPITest** (Priority 4-6) - Create new user
3. **LocationAPITest** (Priority 7) - Fetch locations
4. **BrandAPITest** (Priority 7) - Fetch brands
5. **GlobalSearchAPITest** (Priority 7) - Search & filter home collection tests ✅
6. **AddToCartAPITest** (Priority 8-9) - Add filtered tests to cart
7. **GetCartByIdAPITest** (Priority 10-11) - Validate home collection ✅
8. **AddressAPITest** (Priority 11-13) - Add delivery addresses

## Key Benefits

1. **Automated Filtering**: Only home collection tests are added to cart
2. **Comprehensive Validation**: Every cart item is validated for home collection
3. **Detailed Reporting**: Clear visibility of which tests are valid/invalid
4. **Early Detection**: Tests without home collection are filtered in GlobalSearch
5. **Fail-Fast**: Test fails immediately if invalid tests are found in cart
6. **Cross-API Validation**: Validates data consistency across GlobalSearch and GetCartById APIs

## Notes

- Default location used: **Madhapur**
- Tests are filtered at GlobalSearch phase to prevent invalid tests from reaching cart
- GetCartById provides additional validation layer to ensure cart integrity
- All validations are logged with clear emoji indicators for easy debugging
- Supports multiple boolean formats: Boolean, String ("true"/"yes"/"1")
