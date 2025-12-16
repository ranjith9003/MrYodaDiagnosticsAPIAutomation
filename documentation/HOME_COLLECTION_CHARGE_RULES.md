# 🚚 HOME COLLECTION CHARGE - BUSINESS RULES

## Updated: December 13, 2025

---

## 📋 **BUSINESS RULES (PRIORITY ORDER)**

### **RULE 1: MEMBERS GET FREE HOME COLLECTION (HIGHEST PRIORITY)** ⭐
- **Applies to**: MEMBER & EXISTING_MEMBER user types
- **Condition**: User has membership
- **Charge**: ₹0 (FREE)
- **Logic**: `if (isMember) → homeCollectionCharge = ₹0`
- **Reason**: Membership benefit - free home collection regardless of order value
- **Note**: This rule overrides all other rules

**Example**:
```
User: EXISTING_MEMBER
Items Subtotal: ₹310
Payment Mode: CASH
Expected Home Collection: ₹0 (FREE - membership benefit)
```

---

### **RULE 2: ORDERS >= ₹999 GET FREE DELIVERY**
- **Applies to**: Non-members only
- **Condition**: Items subtotal >= ₹999
- **Charge**: ₹0 (FREE)
- **Logic**: `if (!isMember && itemsSubtotal >= 999) → homeCollectionCharge = ₹0`
- **Reason**: Free delivery for high-value orders

**Example**:
```
User: NEW_USER (non-member)
Items Subtotal: ₹1200
Payment Mode: CASH
Expected Home Collection: ₹0 (FREE - order >= ₹999)
```

---

### **RULE 3: ONLINE PAYMENT WAIVES DELIVERY FEE**
- **Applies to**: Non-members with orders < ₹999
- **Condition**: Payment mode = ONLINE or PREPAID
- **Charge**: ₹0 (FREE)
- **Logic**: `if (!isMember && itemsSubtotal < 999 && paymentMode = 'online') → homeCollectionCharge = ₹0`
- **Reason**: Incentive for online payments

**Example**:
```
User: NEW_USER (non-member)
Items Subtotal: ₹500
Payment Mode: ONLINE
Expected Home Collection: ₹0 (FREE - online payment benefit)
```

---

### **RULE 4: CASH PAYMENT < ₹999 GETS CHARGED**
- **Applies to**: Non-members with orders < ₹999 and cash payment
- **Condition**: Payment mode = CASH or COD
- **Charge**: ₹250
- **Logic**: `if (!isMember && itemsSubtotal < 999 && paymentMode = 'cash') → homeCollectionCharge = ₹250`
- **Reason**: Standard delivery charge for small cash orders

**Example**:
```
User: NEW_USER (non-member)
Items Subtotal: ₹310
Payment Mode: CASH
Expected Home Collection: ₹250 (charged for cash payment)
```

---

## 🎯 **VALIDATION FLOW**

```
START
  ↓
Is User a MEMBER? ───YES───→ homeCollectionCharge = ₹0 (RULE 1) → END
  ↓ NO
Is itemsSubtotal >= ₹999? ───YES───→ homeCollectionCharge = ₹0 (RULE 2) → END
  ↓ NO
Is paymentMode = 'online'? ───YES───→ homeCollectionCharge = ₹0 (RULE 3) → END
  ↓ NO
Is paymentMode = 'cash'? ───YES───→ homeCollectionCharge = ₹250 (RULE 4) → END
  ↓ NO
DEFAULT → homeCollectionCharge = ₹0 → END
```

---

## 📊 **TEST SCENARIOS**

### ✅ **Scenario 1: EXISTING_MEMBER with ₹310 order (CASH)**
- **User Type**: EXISTING_MEMBER
- **Items Subtotal**: ₹310
- **Payment Mode**: CASH
- **Is Member**: YES
- **Expected Charge**: ₹0 (FREE - membership benefit)
- **Rule Applied**: RULE 1

### ✅ **Scenario 2: MEMBER with ₹310 order (CASH)**
- **User Type**: MEMBER
- **Items Subtotal**: ₹310
- **Payment Mode**: CASH
- **Is Member**: YES
- **Expected Charge**: ₹0 (FREE - membership benefit)
- **Rule Applied**: RULE 1

### ✅ **Scenario 3: NEW_USER with ₹310 order (CASH)**
- **User Type**: NEW_USER
- **Items Subtotal**: ₹310
- **Payment Mode**: CASH
- **Is Member**: NO
- **Expected Charge**: ₹250 (charged for cash payment)
- **Rule Applied**: RULE 4

### ✅ **Scenario 4: NEW_USER with ₹1200 order (CASH)**
- **User Type**: NEW_USER
- **Items Subtotal**: ₹1200
- **Payment Mode**: CASH
- **Is Member**: NO
- **Expected Charge**: ₹0 (FREE - order >= ₹999)
- **Rule Applied**: RULE 2

### ✅ **Scenario 5: NEW_USER with ₹500 order (ONLINE)**
- **User Type**: NEW_USER
- **Items Subtotal**: ₹500
- **Payment Mode**: ONLINE
- **Is Member**: NO
- **Expected Charge**: ₹0 (FREE - online payment)
- **Rule Applied**: RULE 3

---

## 🔧 **IMPLEMENTATION**

### Code Logic:
```java
if (isMember) {
    // MEMBERS ALWAYS GET FREE HOME COLLECTION (RULE 1)
    homeCollectionCharge = 0;
} else if (itemsSubtotal >= 999) {
    // NON-MEMBERS: FREE if order >= ₹999 (RULE 2)
    homeCollectionCharge = 0;
} else if ("online".equalsIgnoreCase(paymentMode)) {
    // NON-MEMBERS < ₹999: FREE for online payment (RULE 3)
    homeCollectionCharge = 0;
} else if ("cash".equalsIgnoreCase(paymentMode)) {
    // NON-MEMBERS < ₹999: ₹250 for cash payment (RULE 4)
    homeCollectionCharge = 250;
} else {
    // DEFAULT
    homeCollectionCharge = 0;
}
```

---

## ✅ **STRICT VALIDATION**

All validations are **STRICT** - no tolerance allowed:
- **Expected** = **Actual** → ✅ PASS
- **Expected** ≠ **Actual** → ❌ FAIL (logged to `validation_errors.log`)

---

## 🎁 **MEMBERSHIP BENEFITS**

Members enjoy:
1. **FREE Home Collection** (regardless of order value)
2. **10% Discount** on items subtotal
3. Priority service

---

## 📝 **NOTES**

1. **Member detection**:
   - Check `data.membership_id` in API response
   - OR user type = "EXISTING_MEMBER" or "MEMBER"

2. **Payment modes**:
   - CASH/COD → Cash payment
   - ONLINE/PREPAID → Online payment

3. **Order value**:
   - Calculated from items subtotal (before discounts)
   - Only includes items with home collection available

---

**Last Updated**: December 13, 2025
**Validation Type**: STRICT (Zero Tolerance)
**Error Logging**: Enabled (`validation_errors.log`)
