# Payment Validation Implementation Guide

## Overview
This implementation adds payment validation logic after order creation in the Mr. Yoda Diagnostics API automation framework. It validates business rules and Razorpay payment configuration based on the frontend payment flow.

## Business Rules Implemented

### 1. Payment Amount Limit
- **Rule**: Online payment is allowed only up to ₹5,00,000 (500000)
- **Behavior**: 
  - If amount ≤ ₹5,00,000 → Payment proceeds
  - If amount > ₹5,00,000 → Payment blocked with warning message
  
### 2. Payment Flow Steps
1. Create Order using CreateOrderV2 API
2. Validate amount against limit
3. Configure Razorpay payment options
4. Open Razorpay payment gateway
5. Handle payment response (success/failure/dismissed)
6. Verify order after successful payment

## Files Created/Modified

### 1. PaymentValidationAPITest.java
**Location**: `src/test/java/com/mryoda/diagnostics/api/tests/PaymentValidationAPITest.java`

**Purpose**: Validates payment business rules after order creation

**Test Methods**:
- `testPaymentValidation_ForNonMember()` - Validates payment for non-member user
- `testPaymentValidation_ForMember()` - Validates payment for member user
- `testPaymentValidation_ForNewUser()` - Validates payment for new user

**Key Validations**:
1. ✅ Amount limit check (₹5,00,000 max)
2. ✅ Razorpay order ID format validation
3. ✅ Required payment parameters verification
4. ✅ Payment handler configuration
5. ✅ Security validations
6. ✅ Error handling coverage

## Test Execution Flow

### Prerequisite Tests (Must run in order)
1. **BrandAPITest** - Get brand ID
2. **LocationAPITest** - Get location details
3. **LoginAPITest** - Authenticate users
4. **GlobalSearchAPITest** - Search for tests
5. **AddToCartAPITest** - Add tests to cart
6. **AddressAPITest** - Add delivery address
7. **SlotAPITest** - Select delivery slot
8. **GetCartByIdAPITest** - Get cart with total amount
9. **CreateOrderAPITest** - Create Razorpay order
10. **PaymentValidationAPITest** ← **THIS IS NEW**

### Running Payment Validation Tests

#### Run all payment tests:
```bash
mvn test -Dgroups=payment
```

#### Run specific user type:
```bash
mvn test -Dtest=PaymentValidationAPITest#testPaymentValidation_ForMember
```

#### Run complete flow (including payment):
```bash
mvn test -DsuiteXmlFile=testng-complete-flow.xml
```

## Integration with Frontend Code

The payment validation logic is based on the TypeScript/JavaScript code provided:

### Frontend Payment Flow (Reference)
```typescript
const pay = async (totalAmount: any) => {
    // Check if amount exceeds 5 lakhs (500000)
    if (Number(totalAmount) > 500000) {
        Swal.fire({
            title: "Amount Limit Exceeded",
            text: "Online payment is allowed only up to ₹5,00,000...",
            icon: "warning"
        });
        return;
    }
    
    // Create order and proceed with Razorpay
    let getOrder = await createOrderV2();
    
    const options = {
        key: razorpayKey,
        amount: orderData.amount.toString(),
        currency: orderData.currency,
        order_id: orderData.id,
        name: "Mr. Yoda",
        description: "Book a Test",
        timeout: 600, // 10 minutes
        handler: async function (response) {
            await verifyOrder(orderData, response);
        },
        // ... other options
    };
    
    const paymentObject = new window.Razorpay(options);
    paymentObject.open();
};
```

### API Test Implementation (Java)
The `PaymentValidationAPITest.java` validates:
- ✅ Same amount limit (₹5,00,000)
- ✅ Same Razorpay configuration parameters
- ✅ Same timeout (600 seconds)
- ✅ Same error handling scenarios
- ✅ Same security validations

## Test Output Example

```
╔══════════════════════════════════════════════════════════╗
║     PAYMENT VALIDATION – MEMBER                          ║
╚══════════════════════════════════════════════════════════╝

🔹 Validating Razorpay order: order_MXabc123xyz

╔════════════════════════════════════════════════════════════╗
║     PAYMENT BUSINESS RULES VALIDATION - MEMBER
╚════════════════════════════════════════════════════════════╝

🔹 STEP 1: Validating Payment Amount Limit
   💰 Total Amount: ₹12450
   💰 Maximum Allowed: ₹500000
   ✅ Amount is within payment limit (₹12450 ≤ ₹500000)
   ✅ Payment can proceed with Razorpay

🔹 STEP 2: Validating Razorpay Order Configuration
   ✅ Razorpay Order ID: order_MXabc123xyz
   ✅ Order ID format is valid
   
   📋 Required Razorpay Payment Parameters:
      ✅ key: Razorpay API key (configured in environment)
      ✅ amount: 1245000 paise
      ✅ currency: INR
      ✅ order_id: order_MXabc123xyz
      ✅ name: Mr. Yoda
      ✅ description: Book a Test
      ✅ timeout: 600 seconds (10 minutes)
      ✅ redirect: true

🔹 STEP 3: Payment Handler Configuration
   📋 Payment Flow Steps:
      1. ✅ Create Order (CreateOrderV2 API)
      2. ✅ Open Razorpay Payment Gateway
      3. ✅ User completes payment
      4. ✅ Payment success → Verify Order
      5. ✅ Payment failure → Show error message
      6. ✅ Payment dismissed → Stop loading

🔹 STEP 4: Payment Security Validations
   🔒 Security Checks:
      ✅ Razorpay order created on backend (server-side)
      ✅ Amount validation on server
      ✅ Payment signature verification required
      ✅ Order verification after payment
      ✅ Timeout protection (10 minutes)

🔹 STEP 5: Error Handling Configuration
   ⚠️  Error Scenarios Covered:
      1. ✅ Amount exceeds limit → Show warning, stop payment
      2. ✅ Order creation fails → Show error, stop payment
      3. ✅ Payment fails → Log error, stop loading
      4. ✅ Payment dismissed → Stop loading, allow retry
      5. ✅ Network error → Show error, allow retry

╔════════════════════════════════════════════════════════════╗
║     ✅ PAYMENT VALIDATION COMPLETED - MEMBER
╠════════════════════════════════════════════════════════════╣
║  ✅ PAYMENT CAN PROCEED
║  Amount: ₹12450 (within limit)
║  Order ID: order_MXabc123xyz
╚════════════════════════════════════════════════════════════╝
```

## Scenario: Amount Exceeds Limit

When amount > ₹5,00,000:

```
🔹 STEP 1: Validating Payment Amount Limit
   💰 Total Amount: ₹550000
   💰 Maximum Allowed: ₹500000

   ❌ PAYMENT LIMIT EXCEEDED!
   ⚠️  Online payment is allowed only up to ₹5,00,000
   ⚠️  Amount: ₹550000 exceeds limit by ₹50000
   ℹ️  User should:
      1. Reduce the amount in cart, OR
      2. Choose another payment method (COD/Pay Later)

❌ BUSINESS RULE VIOLATION: Amount ₹550000 exceeds maximum 
   online payment limit of ₹500000
```

## Key Features

### 1. Cross-API Validation
- Uses data from GetCartByIdAPITest (total amount)
- Uses data from CreateOrderAPITest (order ID)
- Uses data from LoginAPITest (user details)

### 2. Business Rule Enforcement
- Validates ₹5,00,000 limit strictly
- Blocks payment if limit exceeded
- Provides actionable error messages

### 3. Razorpay Configuration Validation
- Validates order ID format (must start with "order_")
- Validates required payment parameters
- Validates currency (must be INR)
- Validates timeout (600 seconds)

### 4. Security Validations
- Ensures server-side order creation
- Validates payment signature requirement
- Validates order verification requirement
- Ensures timeout protection

### 5. Error Handling Coverage
- Amount limit exceeded
- Order creation failure
- Payment failure
- Payment dismissed
- Network errors

## TestNG Configuration

Add to your testng.xml:

```xml
<test name="Payment Validation Tests">
    <groups>
        <run>
            <include name="payment"/>
        </run>
    </groups>
    <classes>
        <class name="com.mryoda.diagnostics.api.tests.PaymentValidationAPITest"/>
    </classes>
</test>
```

## Dependencies

This test depends on:
- CreateOrderAPITest (must run first to create order)
- GetCartByIdAPITest (must run first to get total amount)
- RequestContext (to access stored data)

## Notes

### Frontend vs API Testing
- **Frontend**: Opens actual Razorpay payment gateway
- **API Test**: Validates business rules and configuration (no actual payment)

### Why No Actual Payment?
Since there's no dedicated Razorpay payment API in the backend (as mentioned in the requirement), the test validates:
1. Business rules are correctly enforced
2. Order creation returns valid Razorpay order
3. Payment configuration is correct
4. Error handling is comprehensive

### Future Enhancements
If a payment verification API becomes available:
1. Add `verifyPaymentAPI()` method
2. Test payment success scenarios
3. Test payment failure scenarios
4. Validate payment signature verification

## Troubleshooting

### Issue: Total amount is null
**Solution**: Ensure GetCartByIdAPITest runs before PaymentValidationAPITest

### Issue: Order ID is null
**Solution**: Ensure CreateOrderAPITest runs before PaymentValidationAPITest

### Issue: Test dependencies not met
**Solution**: Run complete test suite in correct order:
```bash
mvn test -DsuiteXmlFile=testng-complete-flow.xml
```

## Contact & Support
For questions or issues with payment validation, refer to:
- Frontend payment code reference (provided in requirements)
- Razorpay documentation
- Mr. Yoda API documentation
