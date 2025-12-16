# GlobalSearchHelper - Quick Reference Guide

## All Available Helper Methods

### Basic Fields
```java
String testId = GlobalSearchHelper.getTestId(testName);
String productId = GlobalSearchHelper.getProductId(testName);
String slug = GlobalSearchHelper.getSlug(testName);
String status = GlobalSearchHelper.getTestStatus(testName);
String type = GlobalSearchHelper.getTestType(testName);
```

### Pricing Fields
```java
double price = GlobalSearchHelper.getTestPrice(testName);
double originalPrice = GlobalSearchHelper.getOriginalPrice(testName);
Double b2bPrice = GlobalSearchHelper.getB2BPrice(testName);
double discountPercentage = GlobalSearchHelper.getDiscountPercentage(testName);
String discountRate = GlobalSearchHelper.getDiscountRate(testName);
String rewardsPercentage = GlobalSearchHelper.getRewardsPercentage(testName);
double membershipDiscount = GlobalSearchHelper.getMembershipDiscount(testName);
double courierCharges = GlobalSearchHelper.getCourierCharges(testName);
double cptPrice = GlobalSearchHelper.getCPTPrice(testName);
double actualCprtPrice = GlobalSearchHelper.getActualCPRTPrice(testName);
String cptComment = GlobalSearchHelper.getCPTComment(testName);
```

### Detail Fields
```java
String homeCollection = GlobalSearchHelper.getHomeCollection(testName);
String specimen = GlobalSearchHelper.getSpecimen(testName);
Object turnAroundTime = GlobalSearchHelper.getTurnAroundTime(testName);
String preTestInfo = GlobalSearchHelper.getPreTestInformation(testName);
String description = GlobalSearchHelper.getDescription(testName);
String comment = GlobalSearchHelper.getComment(testName);
String usage = GlobalSearchHelper.getUsage(testName);
String resultInterpretation = GlobalSearchHelper.getResultInterpretation(testName);
```

### Boolean Flags
```java
boolean isPopular = GlobalSearchHelper.isPopular(testName);
boolean isSpecialityTest = GlobalSearchHelper.isSpecialityTest(testName);
boolean isFrequentlyBooked = GlobalSearchHelper.isFrequentlyBooked(testName);
```

### Array/List Fields
```java
List<String> genders = GlobalSearchHelper.getGenders(testName);
List<String> businessType = GlobalSearchHelper.getBusinessType(testName);
List<String> locations = GlobalSearchHelper.getLocations(testName);
List<Object> components = GlobalSearchHelper.getComponents(testName);
List<Object> stability = GlobalSearchHelper.getStability(testName);
List<Object> method = GlobalSearchHelper.getMethod(testName);
List<Object> organ = GlobalSearchHelper.getOrgan(testName);
List<Object> diseases = GlobalSearchHelper.getDiseases(testName);
List<String> searchKeywords = GlobalSearchHelper.getSearchKeywords(testName);
List<String> otherNames = GlobalSearchHelper.getOtherNames(testName);
List<Object> faqs = GlobalSearchHelper.getFrequentlyAskedQuestions(testName);
List<Map<String, Object>> department = GlobalSearchHelper.getDepartment(testName);
List<Object> doctorSpeciality = GlobalSearchHelper.getDoctorSpeciality(testName);
List<Object> doctorsSpeciality = GlobalSearchHelper.getDoctorsSpeciality(testName);
```

### Timestamp Fields
```java
String createdAt = GlobalSearchHelper.getCreatedAt(testName);
String updatedAt = GlobalSearchHelper.getUpdatedAt(testName);
Integer index = GlobalSearchHelper.getIndex(testName);
```

### Raw Data Access
```java
// Get raw test object (complete JSON data)
Map<String, Object> rawData = GlobalSearchHelper.getRawTestData(testName);

// Get any custom field directly
Object customField = GlobalSearchHelper.getTestField(testName, "field_name");
```

### Utility Methods
```java
// Search tests by full names
Response response = GlobalSearchHelper.searchTestsByFullNames(testNames, locationName);

// Extract and store tests from response
GlobalSearchHelper.extractAndStoreTests(response, requiredTests);

// Print complete test details
GlobalSearchHelper.printTestDetails(testName);
```

## Usage Examples

### Example 1: Extract Pricing Information
```java
String testName = "Blood Coagulation";
double price = GlobalSearchHelper.getTestPrice(testName);
double originalPrice = GlobalSearchHelper.getOriginalPrice(testName);
double discount = GlobalSearchHelper.getDiscountPercentage(testName);

System.out.println("Test: " + testName);
System.out.println("Price: ₹" + price);
System.out.println("Original: ₹" + originalPrice);
System.out.println("Discount: " + discount + "%");
```

### Example 2: Check Test Availability
```java
String testName = "Complete Blood Count";
String homeCollection = GlobalSearchHelper.getHomeCollection(testName);
List<String> locations = GlobalSearchHelper.getLocations(testName);
List<String> genders = GlobalSearchHelper.getGenders(testName);

System.out.println("Home Collection: " + homeCollection);
System.out.println("Available at " + locations.size() + " locations");
System.out.println("Applicable for: " + genders);
```

### Example 3: Department and Speciality Info
```java
String testName = "Blood Coagulation";
List<Map<String, Object>> departments = GlobalSearchHelper.getDepartment(testName);

if (!departments.isEmpty()) {
    Map<String, Object> dept = departments.get(0);
    System.out.println("Department: " + dept.get("name"));
    System.out.println("Active: " + dept.get("active"));
}
```

### Example 4: Complete Test Validation
```java
String testName = "Blood Coagulation";

// Validate basic fields
AssertionUtil.verifyNotNull(GlobalSearchHelper.getTestId(testName), "Test ID should exist");
AssertionUtil.verifyEquals(GlobalSearchHelper.getTestStatus(testName), "ACTIVE", "Test should be ACTIVE");

// Validate pricing
AssertionUtil.verifyTrue(GlobalSearchHelper.getTestPrice(testName) > 0, "Price should be positive");

// Validate lists
AssertionUtil.verifyTrue(GlobalSearchHelper.getGenders(testName).size() > 0, "Genders should not be empty");
AssertionUtil.verifyTrue(GlobalSearchHelper.getLocations(testName).size() > 0, "Locations should not be empty");

// Print all details
GlobalSearchHelper.printTestDetails(testName);
```

### Example 5: Access Raw Data for Custom Fields
```java
String testName = "Blood Coagulation";
Map<String, Object> rawData = GlobalSearchHelper.getRawTestData(testName);

// Access any field from raw JSON
Object customField = rawData.get("any_custom_field");

// Or use getTestField for direct access
Object value = GlobalSearchHelper.getTestField(testName, "any_field_name");
```

## Test Execution Flow

1. **Search Tests**
   ```java
   String[] tests = {"Blood Coagulation", "Complete Blood Count"};
   Response response = GlobalSearchHelper.searchTestsByFullNames(tests, "Madhapur");
   ```

2. **Extract and Store**
   ```java
   GlobalSearchHelper.extractAndStoreTests(response, tests);
   ```

3. **Retrieve and Validate**
   ```java
   String testId = GlobalSearchHelper.getTestId("Blood Coagulation");
   AssertionUtil.verifyNotNull(testId, "Test ID should not be null");
   ```

4. **Print Details**
   ```java
   GlobalSearchHelper.printTestDetails("Blood Coagulation");
   ```

## Key Features

✅ **Type Safety** - All methods return appropriate types (String, double, List, etc.)  
✅ **Null Safety** - Methods handle null values gracefully  
✅ **Complete Coverage** - 40+ fields from API response  
✅ **Easy Access** - Simple, intuitive method names  
✅ **Raw Data Access** - Access any field via getRawTestData()  
✅ **Print Utility** - Pretty-print all test details with printTestDetails()

## Files Location
- **Helper Class**: `src/main/java/com/mryoda/diagnostics/api/utils/GlobalSearchHelper.java`
- **Test Class**: `src/test/java/com/mryoda/diagnostics/api/tests/GlobalSearchAPITest.java`
- **Documentation**: `GLOBAL_SEARCH_COMPLETE_FIELD_MAPPING.md`
