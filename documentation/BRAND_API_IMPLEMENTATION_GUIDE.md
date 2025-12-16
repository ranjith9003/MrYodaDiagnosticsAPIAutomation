# Brand API Implementation - Setup & Usage Guide

## Overview
The Brand API (`/brand/getAllBrands`) has been implemented in the framework following the same pattern as the Location API. The API is designed to:
- Fetch all brands with pagination
- Extract and store `title` and `guid` (as `brand_id`) for each brand
- Make the data reusable across other test classes

## Files Created/Modified

### 1. **APIEndpoints.java** - Added Brand Endpoint
```java
public static final String GET_ALL_BRANDS = "/brands/getAllBrands";
```

### 2. **RequestContext.java** - Added Brand Storage
```java
// Brand storage methods
public static void storeBrand(String title, String brandId)
public static String getBrandId(String title)
public static Map<String, String> getAllBrands()
public static void setSelectedBrand(String title)
public static String getSelectedBrandId()
```

### 3. **BrandAPITest.java** - Main Test Class
Contains 3 test methods for different user types:
- `testGetAllBrands_ForMember()`
- `testGetAllBrands_ForExistingMember()` 
- `testGetAllBrands_ForNewUser()`

Plus 2 reusable static methods:
- `getAllBrands(String token, int page)` - Can be called from other test classes
- `validateAndStoreBrandsStatic(Response response)` - Reusable validation

### 4. **testng.xml** - Updated Test Suite
Added `BrandAPITest` class to the test suite execution order.

## Current Status

⚠️ **IMPORTANT NOTE**: The API endpoint is currently returning **404 - Not Found** with the message:
```
Cannot POST /brand/getAllBrands
```

This indicates one of the following issues:
1. The endpoint path might be incorrect (e.g., should be `/brands/` instead of `/brand/`)
2. The HTTP method might be wrong (should be GET instead of POST)
3. The endpoint might require different authentication or headers
4. The endpoint might not be available in the staging environment

## How to Fix the Endpoint Issue

### Option 1: Verify the Correct Endpoint
Check the API documentation or Postman collection to confirm:
- Exact endpoint path
- HTTP method (GET vs POST)
- Required headers
- Request payload structure

### Option 2: Test Different Variations
A `BrandEndpointTester.java` utility class has been created that tests multiple endpoint variations:
- `/brand/getAllBrands`
- `/brands/getAllBrands`
- `/brand/getallbrands`
- `/brands/getallbrands`
- `brand/getAllBrands` (without leading slash)
- `brands/getAllBrands` (without leading slash)

To run the tester:
```bash
mvn test -Dtest=LoginAPITest,BrandEndpointTester
```

### Option 3: Change to GET Method
If the endpoint requires GET instead of POST, update BrandAPITest.java:
```java
return new RequestBuilder()
        .setEndpoint(APIEndpoints.GET_ALL_BRANDS)
        .addHeader("Authorization", token)
        .setQueryParams(Map.of("page", page))  // Query param for GET
        .expectStatus(200)
        .get();  // Changed from post() to get()
```

## How to Use (Once Endpoint is Fixed)

### 1. Run Tests via TestNG Suite
```bash
mvn clean test
```

The BrandAPITest will run after LocationAPITest and before GlobalSearchAPITest.

### 2. Access Stored Brand Data in Other Tests
```java
// Get all brands
Map<String, String> allBrands = RequestContext.getAllBrands();

// Get specific brand ID by title
String brandId = RequestContext.getBrandId("BrandName");

// Set and get selected brand
RequestContext.setSelectedBrand("BrandName");
String selectedId = RequestContext.getSelectedBrandId();
```

### 3. Use Reusable Methods from Other Classes
```java
// Call from any test class
Response response = BrandAPITest.getAllBrands(token, 1);
BrandAPITest.validateAndStoreBrandsStatic(response);
```

## Expected API Response Structure

Based on the implementation, the expected response structure is:
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "guid": "brand-id-1",
        "title": "Brand Name 1"
      },
      {
        "guid": "brand-id-2",
        "title": "Brand Name 2"
      }
    ]
  }
}
```

The code extracts:
- `data.data[i].guid` → stored as `brand_id`
- `data.data[i].title` → stored as brand title (key for lookup)

## Request Payload

```json
{
  "page": 1
}
```

## Next Steps to Make it Work

1. **Verify the endpoint with Postman or API documentation**
2. **Update `APIEndpoints.java` with the correct path** if different
3. **Change HTTP method** in `BrandAPITest.java` if needed (POST → GET)
4. **Adjust response parsing** in `validateAndStoreBrands()` if response structure is different
5. **Run tests** to confirm it works

## Test Execution Order

```
LoginAPITest (generates tokens)
  ↓
UserCreateAPITest (creates new user)
  ↓
LocationAPITest (fetches & stores locations)
  ↓
BrandAPITest (fetches & stores brands) ← NEW
  ↓
GlobalSearchAPITest (uses locations & brands)
```

## Files Location

```
MrYodaDiagnosticsAPI/
├── src/
│   ├── main/java/com/mryoda/diagnostics/api/
│   │   ├── endpoints/APIEndpoints.java (modified)
│   │   └── utils/RequestContext.java (modified)
│   └── test/java/com/mryoda/diagnostics/api/tests/
│       ├── BrandAPITest.java (new)
│       └── BrandEndpointTester.java (new - utility)
└── testng.xml (modified)
```

## Contact/Support

Once you verify the correct endpoint details:
1. Update the endpoint in `APIEndpoints.java`
2. Update the HTTP method in `BrandAPITest.java` if needed
3. Run `mvn clean test` to execute all tests
4. All brand data will be available in `RequestContext` for subsequent API calls

---

**Created**: December 11, 2025  
**Status**: ⚠️ Awaiting correct endpoint verification  
**Framework**: Ready and implemented, just needs correct API endpoint details
