# 🔍 Membership Price Debugging - Console Output Guide

## Error from Log
```
TIMESTAMP: 2025-12-13 18:06:46
TEST: testGetCartById_EXISTING_MEMBER
ERROR: membershipPrice MUST be ₹279 (90% of ₹310) but got ₹310
```

## What I've Added to Show in Console

I've added comprehensive console logging to show **exactly where** the `membershipPrice` value is coming from. When you run the tests now, you will see:

---

### 1️⃣ **COMPLETE API RESPONSE** (at the start of validation)
```
🔍 ====== COMPLETE API RESPONSE (Pretty Print) ======
{
  "success": true,
  "msg": "Cart fetched successfully",
  "data": {
    "guid": "...",
    "id": 123,
    "user_id": "...",
    "items": [
      {
        "test_name": "CBC(COMPLETE BLOOD COUNT)",
        "price": 310,
        "original_price": 310,
        "membershipPrice": 310,  // <-- THIS IS WHERE THE BUG IS!
        "quantity": 1,
        ...
      }
    ],
    ...
  }
}
=====================================================
```

**This shows the RAW JSON response from the API**, so you can see exactly what value the API is returning for `membershipPrice`.

---

### 2️⃣ **RAW VALUES FROM API** (for each item in cart)
```
🔍 ====== ITEM 0 - RAW API RESPONSE VALUES ======
   📍 JSON Path: data.items[0].membershipPrice
   📦 membershipPriceObj (raw from API): 310
   📦 priceObj (raw from API): 310
   📦 originalPriceObj (raw from API): 310
   📦 testNameObj: CBC(COMPLETE BLOOD COUNT)
   📦 itemName: CBC(COMPLETE BLOOD COUNT)
======================================
```

**This shows:**
- The exact JSON path used to extract the value: `data.items[0].membershipPrice`
- The raw object value retrieved: `310`
- All related price fields for comparison

---

### 3️⃣ **MEMBERSHIP PRICE EXTRACTION PROCESS** (when validating for members)
```
🔍 ====== MEMBERSHIP PRICE EXTRACTION ======
   📍 Checking membershipPriceObj: 310
   📍 membershipPriceObj is null? false
   ✅ Extracted apiMembershipPrice: ₹310
   📍 Type of membershipPriceObj: java.lang.Integer
   📍 Raw value: 310
   📍 Original Price from API: ₹310
   📍 Calculated Membership Price (90%): ₹279
   📍 API Membership Price: ₹310
==========================================
```

**This shows:**
- Whether the membershipPriceObj is null
- The extracted integer value
- The data type of the object
- **The EXPECTED value (₹279)** calculated as 90% of original price
- **The ACTUAL value (₹310)** from the API

---

### 4️⃣ **VALIDATION ERROR WITH DETAILED EXPLANATION**
```
      ✅ CBC(COMPLETE BLOOD COUNT) (MEMBER PRICE)
         Original Price: ₹310
         API membershipPrice: ₹310
         Calculated (90% of original): ₹279
         ❌ MEMBERSHIP PRICE MISMATCH!
            Expected: ₹279
            Actual: ₹310
            🔍 This means API returned membershipPrice = 310 instead of the expected discounted price
```

**This clearly shows the mismatch and explains that the API is returning the wrong value.**

---

## Root Cause

Based on this error, the issue is:

**The API is returning `membershipPrice: 310` when it should be returning `membershipPrice: 279`**

### Why this is a bug:
1. For `EXISTING_MEMBER` users, they have an active membership
2. Members should get a **10% discount** on test prices
3. Original price = ₹310
4. Expected membership price = ₹310 × 0.90 = ₹279
5. **ACTUAL API response = ₹310** (no discount applied!)

### Where the value comes from:
- **JSON Path**: `data.items[0].membershipPrice` (or `data.cartItems[0].membershipPrice`)
- **API Response**: The GetCartById API returns this field
- **Expected Calculation**: Backend should calculate `membershipPrice = original_price * 0.90`
- **Actual Behavior**: Backend is returning `membershipPrice = original_price` (no discount!)

---

## How to Run Tests to See Console Output

### Option 1: Run Complete Suite (Recommended)
```bash
cd C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
mvn clean test
```

### Option 2: Run via TestNG XML
```bash
cd C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
mvn clean test -DsuiteXmlFile=testng.xml
```

### Option 3: View Latest Test Output
Check the Surefire reports:
```
C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI\target\surefire-reports\
```

---

## Files Modified

I've added detailed console logging in:
- `GetCartByIdAPITest.java` - Line ~534 (Raw API values extraction)
- `GetCartByIdAPITest.java` - Line ~93 (Complete API response print)
- `GetCartByIdAPITest.java` - Line ~763 (Membership price extraction and validation)

---

## Next Steps

1. **Run the tests** to see the detailed console output
2. **Identify the exact API response** showing membershipPrice = 310
3. **Report to backend team** that the GetCartById API is not applying membership discount
4. **Expected fix**: Backend should return `membershipPrice: 279` for members

---

## Backend API Issue

The bug is in the **GetCartById API** endpoint:
- **Endpoint**: `GET /api/cart/getcartbyid`
- **Issue**: Not calculating membership discount properly
- **Field**: `membershipPrice` in response
- **Expected**: Should be 90% of original_price for members
- **Actual**: Returning 100% of original_price (no discount)
