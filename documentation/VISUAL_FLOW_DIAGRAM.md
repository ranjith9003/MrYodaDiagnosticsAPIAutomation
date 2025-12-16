# 🎯 Visual Flow Diagram - How It All Works

## 📊 Complete Flow Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    USER CONFIGURATION                            │
│  GlobalSearchAPITest.java (Line 45)                             │
│                                                                  │
│  String[] testsToSearch = {                                     │
│      "Blood Coagulation",      ← YOU CONFIGURE THESE            │
│      "Complete Blood Count",   ← ADD AS MANY AS YOU WANT        │
│      "Lipid Profile"           ← NO LIMIT!                      │
│  };                                                              │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                   MEMBER FLOW (User Type 1)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Step 1: LOGIN                                                   │
│  ├─ API: /otps/getOtp (Request OTP)                             │
│  ├─ API: /otps/verifyOtp (Verify & Get Token)                   │
│  └─ STORED:                                                      │
│      ├─ RequestContext.setMemberToken(token)                    │
│      ├─ RequestContext.setMemberUserId(userId)                  │
│      ├─ RequestContext.setMemberFirstName(firstName)            │
│      └─ RequestContext.setMemberLastName(lastName)              │
│                                                                  │
│  Step 2: GET LOCATIONS                                           │
│  ├─ API: /tests/getlocations                                    │
│  └─ STORED:                                                      │
│      ├─ RequestContext.storeLocation("Madhapur", "id123")       │
│      ├─ RequestContext.storeLocation("Guntur", "id456")         │
│      └─ ... (All 6 locations)                                    │
│                                                                  │
│  Step 3: GET BRANDS                                              │
│  ├─ API: /brand/getAllBrands                                    │
│  └─ STORED:                                                      │
│      ├─ RequestContext.storeBrand("Diagnostics", "brandId1")    │
│      ├─ RequestContext.storeBrand("DNA Decoder", "brandId2")    │
│      └─ ... (All 4 brands)                                       │
│                                                                  │
│  Step 4: GLOBAL SEARCH (FINDS YOUR CONFIGURED TESTS)            │
│  ├─ API: /tests/adminTests                                      │
│  ├─ SEARCHES FOR: "Blood Coagulation"                           │
│  │                 "Complete Blood Count"                        │
│  │                 "Lipid Profile"                              │
│  └─ STORED:                                                      │
│      ├─ RequestContext.storeTest("Blood Coagulation", {         │
│      │      _id: "675921110856fe1e1e992ec9",                    │
│      │      price: 25000,                                        │
│      │      type: "diagnostics",                                │
│      │      ... 40+ more fields                                 │
│      │  })                                                       │
│      ├─ RequestContext.storeTest("Complete Blood Count", {...}) │
│      └─ RequestContext.storeTest("Lipid Profile", {...})        │
│                                                                  │
│  Step 5: ADD TO CART (ADDS ALL TESTS DYNAMICALLY!)              │
│  ├─ API: /carts/v2/addCart                                      │
│  ├─ BUILDS PAYLOAD:                                             │
│  │   {                                                           │
│  │     user_id: "userId",                                       │
│  │     lab_location_id: "locationId",                           │
│  │     product_details: [                                       │
│  │       {                                                       │
│  │         product_id: "675921110856fe1e1e992ec9", ← Blood Coag │
│  │         quantity: 1,                                         │
│  │         brand_id: "brandId",                                 │
│  │         family_member_id: ["userId"],                        │
│  │         location_id: "locationId"                            │
│  │       },                                                      │
│  │       {                                                       │
│  │         product_id: "testId2", ← Complete Blood Count        │
│  │         quantity: 1,                                         │
│  │         ...                                                   │
│  │       },                                                      │
│  │       {                                                       │
│  │         product_id: "testId3", ← Lipid Profile               │
│  │         quantity: 1,                                         │
│  │         ...                                                   │
│  │       }                                                       │
│  │     ]  ← ALL TESTS ADDED AUTOMATICALLY!                      │
│  │   }                                                           │
│  └─ STORED:                                                      │
│      ├─ RequestContext.setMemberCartId(cartGuid)                │
│      ├─ RequestContext.setMemberCartNumericId(cartId)           │
│      └─ RequestContext.setMemberTotalAmount(totalAmount)        │
│                                                                  │
│  ✅ VALIDATION:                                                  │
│      Expected Items: 3                                           │
│      Actual Items: 3                                             │
│      Status: PASSED ✅                                           │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              EXISTING MEMBER FLOW (User Type 2)                  │
├─────────────────────────────────────────────────────────────────┤
│  [Same steps as Member Flow but with different user]            │
│                                                                  │
│  STORED IN DIFFERENT VARIABLES:                                 │
│  ├─ RequestContext.setExistingMemberToken(token)                │
│  ├─ RequestContext.setExistingMemberUserId(userId)              │
│  ├─ RequestContext.setExistingMemberCartId(cartGuid)            │
│  └─ RequestContext.setExistingMemberTotalAmount(amount)         │
│                                                                  │
│  ✅ Adds ALL configured tests to cart                           │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                 NEW USER FLOW (User Type 3)                      │
├─────────────────────────────────────────────────────────────────┤
│  Step 0: REGISTER NEW USER                                       │
│  ├─ API: /users/addUser                                         │
│  ├─ GENERATES: Random mobile number                             │
│  └─ STORED: RequestContext.setUserId(userId)                    │
│             RequestContext.setMobile(mobile)                     │
│                                                                  │
│  [Then follows same flow as Member]                             │
│                                                                  │
│  STORED IN DIFFERENT VARIABLES:                                 │
│  ├─ RequestContext.setNewUserToken(token)                       │
│  ├─ RequestContext.setNewUserUserId(userId)                     │
│  ├─ RequestContext.setNewUserCartId(cartGuid)                   │
│  └─ RequestContext.setNewUserTotalAmount(amount)                │
│                                                                  │
│  ✅ Adds ALL configured tests to cart                           │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 Dynamic Test Addition Flow

```
USER CONFIGURES:
┌─────────────────────────────────────┐
│ Tests to Search:                    │
│ 1. Blood Coagulation                │
│ 2. Complete Blood Count             │
│ 3. Lipid Profile                    │
└─────────────────────────────────────┘
              ↓
GLOBAL SEARCH API CALL:
┌─────────────────────────────────────┐
│ Searches for each test              │
│ Stores ALL found tests              │
│ with COMPLETE data                  │
└─────────────────────────────────────┘
              ↓
REQUESTCONTEXT STORAGE:
┌─────────────────────────────────────┐
│ Map<String, Map<String, Object>>    │
│                                     │
│ "Blood Coagulation" → {             │
│    _id: "testId1",                  │
│    price: 25000,                    │
│    type: "diagnostics",             │
│    ... all fields                   │
│ }                                   │
│                                     │
│ "Complete Blood Count" → {          │
│    _id: "testId2",                  │
│    price: 500,                      │
│    ... all fields                   │
│ }                                   │
│                                     │
│ "Lipid Profile" → {                 │
│    _id: "testId3",                  │
│    price: 800,                      │
│    ... all fields                   │
│ }                                   │
└─────────────────────────────────────┘
              ↓
ADD TO CART PAYLOAD BUILDING:
┌─────────────────────────────────────┐
│ Loop through ALL stored tests:      │
│                                     │
│ for (test in allTests) {            │
│    productDetail = {                │
│       product_id: test._id,         │
│       quantity: 1,                  │
│       brand_id: brandId,            │
│       location_id: locationId,      │
│       family_member_id: [userId]    │
│    }                                │
│    add to productDetailsList        │
│ }                                   │
│                                     │
│ Result:                             │
│ product_details = [                 │
│    {test1 details},                 │
│    {test2 details},                 │
│    {test3 details}                  │
│ ]                                   │
└─────────────────────────────────────┘
              ↓
ADD TO CART API CALL:
┌─────────────────────────────────────┐
│ POST /carts/v2/addCart              │
│                                     │
│ Payload contains:                   │
│ - user_id                           │
│ - lab_location_id                   │
│ - product_details (ALL 3 tests!)    │
└─────────────────────────────────────┘
              ↓
API RESPONSE:
┌─────────────────────────────────────┐
│ {                                   │
│   success: true,                    │
│   data: {                           │
│     guid: "cartGuid",               │
│     id: 123,                        │
│     cart_items: [                   │
│       {                             │
│         test_name: "Blood Coag",    │
│         price: 25000,               │
│         quantity: 1                 │
│       },                            │
│       {                             │
│         test_name: "CBC",           │
│         price: 500,                 │
│         quantity: 1                 │
│       },                            │
│       {                             │
│         test_name: "Lipid",         │
│         price: 800,                 │
│         quantity: 1                 │
│       }                             │
│     ]                               │
│   },                                │
│   total_amount: 26300               │
│ }                                   │
└─────────────────────────────────────┘
              ↓
VALIDATION:
┌─────────────────────────────────────┐
│ Expected Items: 3                   │
│ Actual Items: 3                     │
│ ✅ MATCH!                           │
│                                     │
│ All test names verified:            │
│ ✅ Blood Coagulation - ₹25000       │
│ ✅ Complete Blood Count - ₹500      │
│ ✅ Lipid Profile - ₹800             │
│                                     │
│ Total Amount: ₹26300 ✅             │
└─────────────────────────────────────┘
```

## 📦 Data Storage Map

```
┌──────────────────────────────────────────────────────────────────┐
│                    REQUEST CONTEXT STORAGE                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────────────────────────────────┐         │
│  │  MEMBER DATA                                        │         │
│  ├─────────────────────────────────────────────────────┤         │
│  │  Token: eyJhbGciOiJIUzI1NiIs...                     │         │
│  │  User ID: 2592eebe-cc3d-471a-99f9-56757ff76ea3      │         │
│  │  First Name: Ranjith                                │         │
│  │  Last Name: Kumar                                   │         │
│  │  Cart GUID: d134189f-9e03-4125-bc32-ff0fd3874595    │         │
│  │  Cart ID: 535                                       │         │
│  │  Total Amount: 26300                                │         │
│  └─────────────────────────────────────────────────────┘         │
│                                                                   │
│  ┌─────────────────────────────────────────────────────┐         │
│  │  EXISTING MEMBER DATA                               │         │
│  ├─────────────────────────────────────────────────────┤         │
│  │  Token: eyJhbGciOiJIUzI1NiIs...                     │         │
│  │  User ID: 74518065-cc4b-4d9e-a24b-32e331e1963d      │         │
│  │  First Name: Ranjith                                │         │
│  │  Last Name: A                                       │         │
│  │  Cart GUID: fd0d7d71-3903-4445-b0bc-a81dd37a08a8    │         │
│  │  Cart ID: 506                                       │         │
│  │  Total Amount: 26300                                │         │
│  └─────────────────────────────────────────────────────┘         │
│                                                                   │
│  ┌─────────────────────────────────────────────────────┐         │
│  │  NEW USER DATA                                      │         │
│  ├─────────────────────────────────────────────────────┤         │
│  │  Token: eyJhbGciOiJIUzI1NiIs...                     │         │
│  │  User ID: ce709527-16bf-4773-816c-c60dd3214014      │         │
│  │  First Name: David                                  │         │
│  │  Last Name: Wilson                                  │         │
│  │  Mobile: 9387309880 (auto-generated)               │         │
│  │  Cart GUID: a6f8914f-aadb-4bda-9b31-69918153bba9    │         │
│  │  Cart ID: 979                                       │         │
│  │  Total Amount: 26300                                │         │
│  └─────────────────────────────────────────────────────┘         │
│                                                                   │
│  ┌─────────────────────────────────────────────────────┐         │
│  │  SHARED DATA (All Users)                            │         │
│  ├─────────────────────────────────────────────────────┤         │
│  │  LOCATIONS:                                         │         │
│  │  ├─ "Madhapur" → "676a5fa720093d2807af03a5"         │         │
│  │  ├─ "Ameerpet (HQ)" → "64870066842708a0d5ae6c77"    │         │
│  │  ├─ "Guntur" → "64870066842708a0d5ae6c74"           │         │
│  │  └─ ... (6 total)                                   │         │
│  │                                                      │         │
│  │  BRANDS:                                            │         │
│  │  ├─ "Diagnostics" → "967a5f02-2e38-47c8-b850-..."   │         │
│  │  ├─ "DNA Decoder" → "e4041fd4-ee8d-43c6-87ef-..."   │         │
│  │  └─ ... (4 total)                                   │         │
│  │                                                      │         │
│  │  TESTS:                                             │         │
│  │  ├─ "Blood Coagulation" → {                         │         │
│  │  │     _id: "675921110856fe1e1e992ec9",             │         │
│  │  │     price: 25000,                                │         │
│  │  │     original_price: 25000,                       │         │
│  │  │     type: "diagnostics",                         │         │
│  │  │     status: "ACTIVE",                            │         │
│  │  │     ... 40+ fields                               │         │
│  │  │  }                                               │         │
│  │  ├─ "Complete Blood Count" → {...}                  │         │
│  │  └─ "Lipid Profile" → {...}                         │         │
│  └─────────────────────────────────────────────────────┘         │
└──────────────────────────────────────────────────────────────────┘
```

## 🎯 Execution Flow Timeline

```
Time: 0s
├─ START: Test Suite Execution
│
├─ [0-2s] MEMBER LOGIN
│  └─ ✅ Token Generated & Stored
│
├─ [2-3s] EXISTING MEMBER LOGIN  
│  └─ ✅ Token Generated & Stored
│
├─ [3-4s] NEW USER REGISTRATION
│  └─ ✅ User Created & Stored
│
├─ [4-5s] NEW USER LOGIN
│  └─ ✅ Token Generated & Stored
│
├─ [5-6s] GET LOCATIONS (All Users)
│  └─ ✅ 6 Locations Stored
│
├─ [6-7s] GET BRANDS (All Users)
│  └─ ✅ 4 Brands Stored
│
├─ [7-8s] GLOBAL SEARCH
│  ├─ Search: "Blood Coagulation" ✅ Found
│  ├─ Search: "Complete Blood Count" ✅ Found  
│  ├─ Search: "Lipid Profile" ✅ Found
│  └─ ✅ 3 Tests Stored with Full Data
│
├─ [8s] ADD TO CART - MEMBER
│  ├─ Build Payload with 3 Tests
│  ├─ Call API
│  └─ ✅ Cart Created: ID 535, Amount ₹26300
│
├─ [8s] ADD TO CART - EXISTING MEMBER
│  ├─ Build Payload with 3 Tests
│  ├─ Call API
│  └─ ✅ Cart Created: ID 506, Amount ₹26300
│
├─ [9s] ADD TO CART - NEW USER
│  ├─ Build Payload with 3 Tests
│  ├─ Call API
│  └─ ✅ Cart Created: ID 979, Amount ₹26300
│
└─ [9s] END: All Tests Passed ✅
   Total Time: ~9 seconds
   Tests: 14
   Failures: 0
```

## 💡 Key Insights

### Why This Works:

1. **No Hardcoding**
   ```
   ❌ BAD: product_id = "675921110856fe1e1e992ec9"
   ✅ GOOD: product_id = test.get("_id")
   ```

2. **Dynamic Looping**
   ```
   ❌ BAD: Add test 1, Add test 2, Add test 3 (hardcoded)
   ✅ GOOD: for (test in allTests) { add test }
   ```

3. **Smart Validation**
   ```
   ❌ BAD: Assert cart has 3 items (hardcoded)
   ✅ GOOD: Assert cart.size() == configuredTests.size()
   ```

4. **Separate Storage**
   ```
   ❌ BAD: Single cartId for all users (overwrite!)
   ✅ GOOD: memberCartId, existingMemberCartId, newUserCartId
   ```

## 🚀 Quick Reference

### To Add More Tests:
```java
// File: GlobalSearchAPITest.java (Line 45)
String[] testsToSearch = {
    "Blood Coagulation",
    "Complete Blood Count",
    "Lipid Profile",          // ← ADD HERE
    "Your Test Name Here"     // ← AND HERE
};
```

### To Run:
```cmd
execute-tests.bat
```

### To Verify:
Look for this in console:
```
✅ Validation: Expected X items, Got X items
```

---

**That's it! Framework handles everything else automatically!** 🎉
