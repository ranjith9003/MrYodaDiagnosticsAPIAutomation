# ✅ Global Search Helper - Complete Rewrite Based on JSON Response

## 🎯 Overview
Completely rewrote the `GlobalSearchHelper.extractAndStoreTests()` method to map **ALL fields** from the actual JSON API response, making all test data available for reuse in subsequent tests.

---

## 📋 What Changed

### **1. Complete Field Mapping**
Previously, only a few fields were extracted:
- ❌ **OLD:** Only `test_name`, `test_id`, `_id`, `price`, `original_price`, `Type`, `locations`

Now, **ALL fields** from the JSON response are extracted and stored:
- ✅ **NEW:** 40+ fields including pricing, details, flags, arrays, timestamps, and more

---

## 🗂️ Complete Field List Now Stored

### **Basic Fields**
```java
- _id              // Product ID (MongoDB)
- test_id          // Test ID (e.g., GEN110)
- test_name        // Test Name (e.g., Blood Coagulation)
- slug             // URL slug
- Type             // Type (diagnostics)
- status           // Status (ACTIVE/INACTIVE)
```

### **Pricing Fields**
```java
- price                  // Current price
- original_price         // Original price
- b2b_price             // B2B price
- discount_percentage    // Discount percentage
- discount_rate         // Discount rate
- rewards_percentage    // Rewards percentage
- membership_discount   // Membership discount
- courier_charges       // Courier charges
- cpt_price            // CPT price
- actual_cprt_price    // Actual CPRT price
- cpt_comment          // CPT comment
```

### **Details Fields**
```java
- specimen              // Specimen type
- turn_around_time      // Turn around time
- home_collection       // Home collection availability
- pre_test_information  // Pre-test information
- description           // Test description
- comment               // Comments
- usage                 // Usage information
- result_interpretation // Result interpretation
```

### **Boolean Flags**
```java
- popular            // Is popular test
- speciality_tests   // Is speciality test
- frequently_booked  // Is frequently booked
```

### **Array/List Fields**
```java
- components                    // List of components
- locations                     // List of location IDs
- genders                       // List of genders (MALE, FEMALE)
- business_type                 // List (B2B, B2C)
- stability                     // List of stability info
- method                        // List of methods
- organ                         // List of organs
- diseases                      // List of diseases
- search_keywords               // List of search keywords
- other_names                   // List of alternate names
- frequently_asked_questions    // List of FAQs
- department                    // List of departments
- doctor_speciality            // List of doctor specialities
```

### **Timestamps**
```java
- createdAt  // Created timestamp
- updatedAt  // Updated timestamp
```

### **Other Fields**
```java
- index  // Index number
- raw    // Complete raw JSON object (for reference)
```

---

## 🛠️ New Helper Methods Added

### **1. Generic Field Retrieval**
```java
Object value = GlobalSearchHelper.getTestField("Blood Coagulation", "genders");
```

### **2. Typed Field Getters**
```java
String testId = GlobalSearchHelper.getTestId("Blood Coagulation");
String productId = GlobalSearchHelper.getProductId("Blood Coagulation");
double price = GlobalSearchHelper.getTestPrice("Blood Coagulation");
double originalPrice = GlobalSearchHelper.getOriginalPrice("Blood Coagulation");
String homeCollection = GlobalSearchHelper.getHomeCollection("Blood Coagulation");
String status = GlobalSearchHelper.getTestStatus("Blood Coagulation");
String type = GlobalSearchHelper.getTestType("Blood Coagulation");
```

### **3. Pretty Print Method**
```java
GlobalSearchHelper.printTestDetails("Blood Coagulation");
```

**Output:**
```
╔════════════════════════════════════════════════════════╗
║  TEST DETAILS: Blood Coagulation
╚════════════════════════════════════════════════════════╝
🆔 Product ID        : 675921110856fe1e1e992ec9
🔢 Test ID           : GEN110
🧪 Test Name         : Blood Coagulation
🔗 Slug              : blood-coagulation
💰 Price             : ₹25000
💵 Original Price    : ₹25000
💳 B2B Price         : N/A
🏷️  Discount %        : 0%
💸 Discount Rate     : ₹22500
🎁 Rewards %         : 1125%
👥 Membership Disc.  : 10%
📦 Courier Charges   : ₹0
🏠 Home Collection   : NOT AVAILABLE
🧬 Specimen          : 
⏰ Turn Around Time  : null
📍 Status            : ACTIVE
🏷️  Type              : diagnostics
⭐ Popular           : false
🔬 Speciality Tests  : false
📊 Frequently Booked : false
👫 Genders           : [MALE, FEMALE]
🏢 Business Type     : [B2B, B2C]
📍 Locations Count   : 16
🧩 Components Count  : 0
📝 Description       : N/A
ℹ️  Pre-Test Info     : N/A
════════════════════════════════════════════════════════
```

---

## 📝 Updated Code

### **GlobalSearchHelper.java**

#### **extractAndStoreTests() Method**
```java
public static void extractAndStoreTests(Response response, String[] requiredTests) {
    // Null checks
    if (response == null) {
        throw new RuntimeException("❌ Response is null!");
    }

    List<Map<String, Object>> allTests = response.jsonPath().getList("data");
    if (allTests == null) {
        System.out.println("⚠️ No tests found in response");
        allTests = new ArrayList<>();
    }

    // Loop through required tests
    for (String testName : requiredTests) {
        Map<String, Object> found = allTests.stream()
            .filter(t -> {
                Object testNameObj = t.get("test_name");
                return testNameObj != null && 
                       testNameObj.toString().equalsIgnoreCase(testName);
            })
            .findFirst()
            .orElse(null);

        if (found == null) {
            System.out.println("⚠️ Test not found: " + testName);
            continue;
        }

        // Extract ALL fields from JSON
        Map<String, Object> storeData = new HashMap<>();
        
        // Basic fields
        storeData.put("_id", found.get("_id"));
        storeData.put("test_id", found.get("test_id"));
        storeData.put("test_name", found.get("test_name"));
        // ... (40+ fields total)
        
        // Store in RequestContext
        RequestContext.storeTest(testName, storeData);
    }
}
```

#### **Helper Methods**
```java
// Get specific field
public static Object getTestField(String testName, String fieldName) {
    Map<String, Object> test = RequestContext.getTest(testName);
    return test != null ? test.get(fieldName) : null;
}

// Get Test ID
public static String getTestId(String testName) {
    Object value = getTestField(testName, "test_id");
    return value != null ? value.toString() : null;
}

// Get Price (with type conversion)
public static double getTestPrice(String testName) {
    Object value = getTestField(testName, "price");
    if (value instanceof Number) {
        return ((Number) value).doubleValue();
    }
    return 0.0;
}

// ... (more helper methods)

// Pretty print
public static void printTestDetails(String testName) {
    Map<String, Object> test = RequestContext.getTest(testName);
    // ... formatted output
}
```

---

## 🧪 Test Class Usage Example

### **GlobalSearchAPITest.java**

```java
@Test
public void testGlobalSearchAndStore() {
    // 1. Search tests
    String[] testsToSearch = {"Blood Coagulation", "Complete Blood Count"};
    Response res = GlobalSearchHelper.searchTestsByFullNames(testsToSearch, "Madhapur");
    
    // 2. Extract and store ALL fields
    GlobalSearchHelper.extractAndStoreTests(res, testsToSearch);
    
    // 3. Use helper methods to retrieve data
    String testId = GlobalSearchHelper.getTestId("Blood Coagulation");
    String productId = GlobalSearchHelper.getProductId("Blood Coagulation");
    double price = GlobalSearchHelper.getTestPrice("Blood Coagulation");
    String status = GlobalSearchHelper.getTestStatus("Blood Coagulation");
    
    // 4. Validate
    AssertionUtil.verifyNotNull(testId, "Test ID should not be null");
    AssertionUtil.verifyTrue(price > 0, "Price should be > 0");
    AssertionUtil.verifyEquals(status, "ACTIVE", "Should be ACTIVE");
    
    // 5. Print complete details
    GlobalSearchHelper.printTestDetails("Blood Coagulation");
    
    // 6. Access any custom field
    Object genders = GlobalSearchHelper.getTestField("Blood Coagulation", "genders");
    Object businessType = GlobalSearchHelper.getTestField("Blood Coagulation", "business_type");
}
```

---

## 📊 Console Output

### **During Extraction**
```
📦 TOTAL TESTS RECEIVED FROM API: 213

🎯 MATCHED & STORED TEST: Blood Coagulation
   Test ID       : GEN110
   Product ID    : 675921110856fe1e1e992ec9
   Price         : ₹25000
   Original Price: ₹25000
   Type          : diagnostics
   Status        : ACTIVE
   Home Collection: NOT AVAILABLE

🎯 MATCHED & STORED TEST: Complete Blood Count
   Test ID       : GEN120
   Product ID    : 675921110856fe1e1e992ecb
   Price         : ₹1500
   Original Price: ₹1500
   Type          : diagnostics
   Status        : ACTIVE
   Home Collection: AVAILABLE

✅ All requested tests extracted and stored successfully!
```

### **During Field Retrieval**
```
╔══════════════════════════════════════════════════════════╗
║              VALIDATION & FIELD RETRIEVAL                ║
╚══════════════════════════════════════════════════════════╝

🧪 Retrieving fields for: Blood Coagulation
   ✅ Test ID         : GEN110
   ✅ Product ID      : 675921110856fe1e1e992ec9
   ✅ Price           : ₹25000.0
   ✅ Original Price  : ₹25000.0
   ✅ Home Collection : NOT AVAILABLE
   ✅ Status          : ACTIVE
   ✅ Type            : diagnostics
```

---

## ✅ Benefits

1. **Complete Data Access** - All 40+ fields from JSON are now accessible
2. **Type-Safe Helpers** - Dedicated methods for common fields with type conversion
3. **Flexible Retrieval** - Generic `getTestField()` for any custom field
4. **Pretty Printing** - Formatted output for debugging and validation
5. **Null Safety** - All methods handle null values gracefully
6. **Reusable** - Store once, use anywhere in subsequent tests

---

## 🔧 How to Use in Your Tests

### **Step 1: Search and Store**
```java
String[] tests = {"Blood Coagulation", "Complete Blood Count"};
Response res = GlobalSearchHelper.searchTestsByFullNames(tests, "Madhapur");
GlobalSearchHelper.extractAndStoreTests(res, tests);
```

### **Step 2: Retrieve Data**
```java
// Using typed helpers
String testId = GlobalSearchHelper.getTestId("Blood Coagulation");
double price = GlobalSearchHelper.getTestPrice("Blood Coagulation");

// Using generic getter
Object genders = GlobalSearchHelper.getTestField("Blood Coagulation", "genders");
```

### **Step 3: Validate**
```java
AssertionUtil.verifyNotNull(testId, "Test ID");
AssertionUtil.verifyTrue(price > 0, "Price > 0");
```

### **Step 4: Debug**
```java
GlobalSearchHelper.printTestDetails("Blood Coagulation");
```

---

## 🎯 Summary

| Feature | Status |
|---------|--------|
| Complete field extraction (40+ fields) | ✅ **DONE** |
| Typed helper methods | ✅ **DONE** |
| Generic field retrieval | ✅ **DONE** |
| Pretty print functionality | ✅ **DONE** |
| Null safety | ✅ **DONE** |
| Documentation | ✅ **DONE** |
| No compilation errors | ✅ **VERIFIED** |

---

**Status: ✅ COMPLETE - All JSON fields mapped and accessible via helper methods!**

**Date:** December 10, 2025

**Files Modified:**
1. `GlobalSearchHelper.java` - Complete rewrite of `extractAndStoreTests()` + 9 new helper methods
2. `GlobalSearchAPITest.java` - Updated with comprehensive usage examples

**Run your tests now - all fields from JSON response are now accessible!** 🎉
