# Payment Validation - Quick Start Guide

## ⚡ Quick Start (3 Steps)

### Step 1: Navigate to Project Directory
```bash
cd C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI
```

### Step 2: Run Tests
**Option A - Use Batch Script (Easiest)**
```bash
run-payment-tests.bat
```

**Option B - Use Maven Command**
```bash
# Run complete flow including payment validation
mvn test -DsuiteXmlFile=testng.xml

# OR run only payment tests
mvn test -Dgroups=payment
```

### Step 3: View Results
Check console output or open:
```
target\surefire-reports\index.html
```

---

## 📋 What Gets Validated

✅ Payment amount limit (₹5,00,000 max)  
✅ Razorpay order configuration  
✅ Payment parameters (amount, currency, order_id)  
✅ Security validations  
✅ Error handling scenarios  

---

## 🎯 Expected Results

### ✅ Success Scenario (Amount ≤ ₹5,00,000)
```
✅ Amount is within payment limit
✅ Payment can proceed with Razorpay
✅ All validations passed
```

### ❌ Failure Scenario (Amount > ₹5,00,000)
```
❌ PAYMENT LIMIT EXCEEDED!
⚠️  Amount exceeds limit by ₹XXX
ℹ️  User should reduce cart or use alternate payment
Test FAILED (Expected behavior)
```

---

## 📚 Documentation

**For detailed information, see:**

1. **PAYMENT_IMPLEMENTATION_SUMMARY.md** - Complete implementation details
2. **PAYMENT_VALIDATION_GUIDE.md** - Comprehensive usage guide
3. **PaymentValidationAPITest.java** - Test implementation with JavaDoc

---

## 🆘 Troubleshooting

### Problem: "Order ID not found"
**Solution**: Run complete test suite, not just payment tests
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Problem: "Total amount not available"
**Solution**: Ensure GetCartByIdAPITest runs before payment validation

### Problem: Tests not running
**Solution**: Check Maven installation
```bash
mvn --version
```

---

## 💡 Key Features

- ✅ Validates same business rules as frontend
- ✅ Works with all user types (MEMBER, NON_MEMBER, NEW_USER)
- ✅ Comprehensive error messages
- ✅ Easy to run and debug
- ✅ Production-ready

---

## 🚀 Next Steps

1. ✅ Run the tests using batch script
2. ✅ Review test output
3. ✅ Integrate with CI/CD pipeline
4. ✅ Add to regular test suite

---

**Need Help?** Check the comprehensive guides:
- PAYMENT_IMPLEMENTATION_SUMMARY.md
- PAYMENT_VALIDATION_GUIDE.md
