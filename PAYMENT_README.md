# Payment Validation for Mr. Yoda Diagnostics API

## 📖 Overview

This module implements **Payment Validation** after order creation in the Mr. Yoda Diagnostics API automation framework. It validates business rules and Razorpay payment configuration based on the frontend payment flow.

---

## 🎯 Purpose

The payment validation ensures that:
1. **Business Rules** are enforced (e.g., ₹5,00,000 payment limit)
2. **Razorpay Configuration** is correct and complete
3. **Payment Flow** follows security best practices
4. **Error Handling** covers all scenarios

---

## 📦 What's Included

### Test Class
- **PaymentValidationAPITest.java** - Main test implementation

### Documentation
- **PAYMENT_QUICK_START.md** - Get started in 3 steps
- **PAYMENT_IMPLEMENTATION_SUMMARY.md** - Complete implementation details
- **PAYMENT_VALIDATION_GUIDE.md** - Comprehensive usage guide
- **PAYMENT_README.md** - This file

### Utilities
- **run-payment-tests.bat** - Interactive test execution script

---

## 🚀 Quick Start

```bash
# Navigate to project
cd C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI

# Run payment tests
run-payment-tests.bat
```

Or using Maven:
```bash
mvn test -Dgroups=payment
```

---

## 🔍 What Gets Tested

### 1. Payment Amount Limit
```
✅ Amount ≤ ₹5,00,000 → Payment proceeds
❌ Amount > ₹5,00,000 → Payment blocked
```

### 2. Razorpay Order Validation
```
✅ Order ID format (starts with "order_")
✅ Order status ("created")
✅ Amount in paise (rupees × 100)
✅ Currency (INR)
```

### 3. Payment Configuration
```
✅ Razorpay API key present
✅ Timeout configured (600 seconds)
✅ Redirect enabled
✅ Payment handler configured
```

### 4. Security Validations
```
✅ Server-side order creation
✅ Payment signature verification
✅ Order verification post-payment
✅ Timeout protection
```

---

## 📊 Test Flow

```
LoginAPITest
    ↓
BrandAPITest
    ↓
LocationAPITest
    ↓
GlobalSearchAPITest
    ↓
AddToCartAPITest
    ↓
AddressAPITest
    ↓
SlotAndCartUpdateAPITest
    ↓
GetCartByIdAPITest ⭐ (stores totalAmount)
    ↓
CreateOrderAPITest ⭐ (creates Razorpay order)
    ↓
PaymentValidationAPITest ⭐ (validates payment rules)
    ↓
✅ Complete
```

---

## 📝 Sample Output

```
╔══════════════════════════════════════════════════════════╗
║     PAYMENT VALIDATION – MEMBER                          ║
╚══════════════════════════════════════════════════════════╝

🔹 STEP 1: Validating Payment Amount Limit
   💰 Total Amount: ₹12450
   💰 Maximum Allowed: ₹500000
   ✅ Amount is within payment limit
   ✅ Payment can proceed with Razorpay

🔹 STEP 2: Validating Razorpay Order Configuration
   ✅ Razorpay Order ID: order_MXabc123xyz
   ✅ Order ID format is valid
   📋 Required Razorpay Payment Parameters:
      ✅ key: Razorpay API key
      ✅ amount: 1245000 paise
      ✅ currency: INR
      ✅ order_id: order_MXabc123xyz
      ✅ timeout: 600 seconds

╔════════════════════════════════════════════════════════════╗
║     ✅ PAYMENT VALIDATION COMPLETED - MEMBER
╠════════════════════════════════════════════════════════════╣
║  ✅ PAYMENT CAN PROCEED
║  Amount: ₹12450 (within limit)
║  Order ID: order_MXabc123xyz
╚════════════════════════════════════════════════════════════╝
```

---

## 🛠️ Configuration

### TestNG Integration

The payment validation test is included in `testng.xml`:

```xml
<class name="com.mryoda.diagnostics.api.tests.PaymentValidationAPITest" />
```

### Test Dependencies

Uses `dependsOnGroups` to ensure proper execution order:

```java
@Test(groups = {"payment"}, dependsOnGroups = {"createOrder"})
```

---

## 🎓 Key Concepts

### Why No Actual Payment?

As mentioned in requirements, there's no dedicated Razorpay payment API endpoint. Therefore, this test:
- ✅ Validates business rules
- ✅ Validates order creation
- ✅ Validates configuration
- ❌ Does NOT process actual payment

### Frontend Integration

The test validates the same logic as the frontend TypeScript code:

**Frontend**:
```typescript
if (Number(totalAmount) > 500000) {
    // Show warning and block payment
}
```

**API Test**:
```java
if (totalAmount > MAX_PAYMENT_AMOUNT) {
    // Fail test with warning message
}
```

---

## 📈 Test Execution Options

### Option 1: Interactive Script
```bash
run-payment-tests.bat
```
- User-friendly menu
- Multiple execution modes
- Built-in error checking

### Option 2: Maven Groups
```bash
mvn test -Dgroups=payment
```
- Runs only payment tests
- Requires CreateOrder to run first

### Option 3: Complete Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```
- Runs all tests in order
- Includes payment validation

### Option 4: Specific User Type
```bash
mvn test -Dtest=PaymentValidationAPITest#testPaymentValidation_ForMember
```
- Tests single user type
- Requires dependencies to run first

---

## ⚠️ Important Notes

### Prerequisites
1. CreateOrderAPITest must run first (creates order)
2. GetCartByIdAPITest must run first (gets total amount)
3. Maven must be installed and configured
4. Java 8+ required

### Data Dependencies
- **totalAmount** - From GetCartByIdAPITest
- **orderId** - From CreateOrderAPITest
- **userId** - From LoginAPITest

### Test Groups
- `payment` - Payment validation tests
- `createOrder` - Order creation tests (dependency)
- `regression` - All regression tests

---

## 🐛 Troubleshooting

### Error: "Order ID not found"
**Cause**: CreateOrderAPITest didn't run  
**Solution**: Run complete test suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Error: "Total amount not available"
**Cause**: GetCartByIdAPITest didn't run  
**Solution**: Run complete test suite

### Error: Maven not found
**Cause**: Maven not installed or not in PATH  
**Solution**: Install Maven and add to PATH

### Tests Pass but No Validation
**Cause**: Amount not stored in RequestContext  
**Solution**: Check GetCartByIdAPITest logs

---

## 📚 Additional Resources

### Documentation Files
1. **PAYMENT_QUICK_START.md** - 3-step quick start
2. **PAYMENT_IMPLEMENTATION_SUMMARY.md** - Complete details
3. **PAYMENT_VALIDATION_GUIDE.md** - Comprehensive guide

### Code Files
- `PaymentValidationAPITest.java` - Main test class
- `RequestContext.java` - Data storage
- `CreateOrderAPITest.java` - Order creation
- `GetCartByIdAPITest.java` - Cart total

### Configuration Files
- `testng.xml` - Test suite configuration
- `pom.xml` - Maven dependencies

---

## ✅ Validation Checklist

After implementation, verify:

- [ ] PaymentValidationAPITest.java created
- [ ] testng.xml updated with payment test
- [ ] run-payment-tests.bat created
- [ ] Documentation files created
- [ ] No compilation errors
- [ ] Tests can be executed
- [ ] Test output is clear and detailed
- [ ] Business rules validated correctly
- [ ] Integration with existing tests works

---

## 🎉 Summary

✅ **Payment validation implemented**  
✅ **Business rules enforced (₹5,00,000 limit)**  
✅ **Razorpay configuration validated**  
✅ **Security checks in place**  
✅ **Error handling comprehensive**  
✅ **Well documented**  
✅ **Easy to run and maintain**  

---

**Implementation Status**: ✅ Complete  
**Last Updated**: December 15, 2025  
**Version**: 1.0.0
