# 🐛 API BUGS DETECTED - Error Log

**Generated:** 2025-12-13 16:15:26  
**Test Suite:** MrYoda Diagnostics API  
**Framework:** Comprehensive API Validation Framework  

---

## 📊 Bug Summary

| Bug ID | API | User Type | Item | Bug Type | Status |
|--------|-----|-----------|------|----------|--------|
| BUG-001 | GetCartById | EXISTING_MEMBER | Blood Coagulation | Price is ZERO | ❌ Failed |
| BUG-002 | GetCartById | EXISTING_MEMBER | Blood Coagulation | Original Price is ZERO | ❌ Failed |
| BUG-003 | GetCartById | EXISTING_MEMBER | Blood Coagulation | MembershipPrice is ZERO | ❌ Failed |
| BUG-004 | GetCartById | MEMBER | Blood Coagulation | Price is ZERO | ❌ Failed |
| BUG-005 | GetCartById | MEMBER | Blood Coagulation | Original Price is ZERO | ❌ Failed |
| BUG-006 | GetCartById | MEMBER | Blood Coagulation | MembershipPrice is ZERO | ❌ Failed |
| BUG-007 | GetCartById | NEW_USER | CBC(COMPLETE BLOOD COUNT) | Price is ZERO | ❌ Failed |
| BUG-008 | GetCartById | NEW_USER | CBC(COMPLETE BLOOD COUNT) | Original Price is ZERO | ❌ Failed |

---

## 🔍 Detailed Bug Reports

### **BUG-001, BUG-002, BUG-003: EXISTING_MEMBER - Blood Coagulation**

**Test:** `testGetCartById_ForExistingMember`  
**API Endpoint:** `GET /gateway/v2/getCartById`  
**User Type:** EXISTING_MEMBER  
**User ID:** 74518065-cc4b-4d9e-a24b-32e331e1963d  
**Item:** Blood Coagulation  

#### **Issues Detected:**

```
❌ BUG: price is ZERO for item 'Blood Coagulation'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0

❌ BUG: original_price is ZERO for item 'Blood Coagulation'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0

❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation'
   EXPECTED: ₹0 (90% discount of original price)
   ACTUAL: ₹0
```

#### **Context:**
- User has **MEMBERSHIP** (isMember: YES)
- Item is in cart but shows:
  - `quantity: 1` ✅
  - `price: 0` ❌
  - `original_price: 0` ❌
  - `membershipPrice: 0` ❌

#### **Expected Behavior:**
For items in the cart with `quantity > 0`:
- `price` should be a positive value (e.g., ₹310)
- `original_price` should be a positive value
- `membershipPrice` should be 90% of original_price for members

#### **Impact:**
- HIGH - Affects pricing calculation for members
- Prevents accurate total calculation
- May cause payment issues

---

### **BUG-004, BUG-005, BUG-006: MEMBER - Blood Coagulation**

**Test:** `testGetCartById_ForMember`  
**API Endpoint:** `GET /gateway/v2/getCartById`  
**User Type:** MEMBER  
**User ID:** 2592eebe-cc3d-471a-99f9-56757ff76ea3  
**Item:** Blood Coagulation  

#### **Issues Detected:**

```
❌ BUG: price is ZERO for item 'Blood Coagulation'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0

❌ BUG: original_price is ZERO for item 'Blood Coagulation'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0

❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation'
   EXPECTED: ₹0 (90% discount of original price)
   ACTUAL: ₹0
```

#### **Context:**
- User has **MEMBERSHIP** (isMember: YES)
- Same issue as EXISTING_MEMBER
- Affects all member users

#### **Impact:**
- HIGH - Affects all members
- Consistent bug across member types
- Payment calculation failure

---

### **BUG-007, BUG-008: NEW_USER - CBC(COMPLETE BLOOD COUNT)**

**Test:** `testGetCartById_ForNewUser`  
**API Endpoint:** `GET /gateway/v2/getCartById`  
**User Type:** NEW_USER  
**User ID:** a77f010e-d0c8-4a22-8197-b1dd299e0428  
**Item:** CBC(COMPLETE BLOOD COUNT)  

#### **Issues Detected:**

```
❌ BUG: price is ZERO for item 'CBC(COMPLETE BLOOD COUNT)'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0

❌ BUG: original_price is ZERO for item 'CBC(COMPLETE BLOOD COUNT)'
   EXPECTED: positive amount (e.g., ₹310)
   ACTUAL: ₹0
```

#### **Context:**
- User is **NON-MEMBER** (isMember: NO)
- Item shows in cart but:
  - `quantity: 1` ✅
  - `price: 0` ❌
  - `original_price: 0` ❌
  - `membershipPrice: 0` ⚠️ (expected for non-members)

#### **Expected Behavior:**
For non-member users:
- `price` should equal `original_price`
- Both should be positive values
- `membershipPrice` can be 0 (not applicable for non-members)

#### **Impact:**
- HIGH - Affects new user conversions
- Cart shows ₹0 total when it should show actual amount
- May prevent order completion

---

## 🔧 Recommended Fixes

### **Fix #1: Backend API - GetCartById**

**Location:** `/gateway/v2/getCartById` endpoint

**Issue:** Price fields return 0 instead of actual values

**Suggested Fix:**
```javascript
// Ensure price fields are populated from product catalog
if (cartItem.price === 0 || cartItem.price === null) {
    cartItem.price = productCatalog.getPrice(cartItem.product_id);
    cartItem.original_price = productCatalog.getOriginalPrice(cartItem.product_id);
    
    // Calculate membership price if user is a member
    if (user.isMember) {
        cartItem.membershipPrice = cartItem.original_price * 0.9; // 10% discount
    }
}
```

### **Fix #2: Data Validation**

Add validation before sending cart response:
```javascript
function validateCartItem(item) {
    if (item.quantity > 0) {
        if (item.price === 0 || item.price === null) {
            throw new Error(`Price is null/zero for item: ${item.name}`);
        }
        if (item.original_price === 0 || item.original_price === null) {
            throw new Error(`Original price is null/zero for item: ${item.name}`);
        }
    }
}
```

---

## 📝 Validation Rules Applied

### **For ALL Users:**
1. ✅ `quantity` must NOT be null and NOT be zero when item is in cart
2. ✅ `price` must NOT be null and NOT be zero when quantity > 0
3. ✅ `original_price` must NOT be null and NOT be zero when quantity > 0

### **For MEMBER/EXISTING_MEMBER Users:**
4. ✅ `membershipPrice` must NOT be null and NOT be zero when user has membership
5. ✅ `membershipPrice` must equal 90% of `original_price` (10% discount)

### **For NEW_USER (Non-Members):**
4. ⚠️ `membershipPrice` can be 0 (not applicable)

---

## 🎯 Test Coverage

| Scenario | Test | Status |
|----------|------|--------|
| EXISTING_MEMBER with membership | ✅ Tested | ❌ Bug Found |
| MEMBER with membership | ✅ Tested | ❌ Bug Found |
| NEW_USER without membership | ✅ Tested | ❌ Bug Found |

**All user scenarios are affected by this pricing bug.**

---

## 📊 Test Execution Details

**Full Test Output:**
```
[ERROR] GetCartByIdAPITest.testGetCartById_ForExistingMember:1040->validateGetCartByIdResponse:651
❌ VALIDATION FAILED: Found 3 bug(s) in item 'Blood Coagulation':
   1. ❌ BUG: price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
   2. ❌ BUG: original_price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
   3. ❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation' | EXPECTED: ₹0 (90% discount) | ACTUAL: ₹0

[ERROR] GetCartByIdAPITest.testGetCartById_ForMember:1058->validateGetCartByIdResponse:651
❌ VALIDATION FAILED: Found 3 bug(s) in item 'Blood Coagulation':
   1. ❌ BUG: price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
   2. ❌ BUG: original_price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
   3. ❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation' | EXPECTED: ₹0 (90% discount) | ACTUAL: ₹0

[ERROR] GetCartByIdAPITest.testGetCartById_ForNewUser:1076->validateGetCartByIdResponse:651
❌ VALIDATION FAILED: Found 2 bug(s) in item 'CBC(COMPLETE BLOOD COUNT)':
   1. ❌ BUG: price is ZERO for item 'CBC(COMPLETE BLOOD COUNT)' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
   2. ❌ BUG: original_price is ZERO for item 'CBC(COMPLETE BLOOD COUNT)' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
```

---

## ✅ Verification Steps

After fixing the bugs, verify:

1. ✅ Run: `mvn clean test -DsuiteXmlFile=testng.xml`
2. ✅ Verify all 35 tests pass
3. ✅ Check that price fields are NOT 0 for items in cart
4. ✅ Verify membershipPrice is calculated correctly (90% of original)
5. ✅ Ensure totalPrice calculation is correct

---

**Last Updated:** 2025-12-13 16:15:26  
**Status:** ❌ **8 BUGS DETECTED - REQUIRES FIX**  
**Priority:** 🔴 **HIGH**
