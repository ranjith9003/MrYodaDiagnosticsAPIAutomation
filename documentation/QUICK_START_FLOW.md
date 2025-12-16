# Quick Start Guide - Complete Flow Testing

## 🚀 Running the Complete Flow Tests

### Prerequisites
- Java 11 or higher installed
- Maven installed
- Eclipse IDE (optional, for running via IDE)

### Method 1: Run with Automatic Retry (Recommended)

Simply double-click or run:
```cmd
run-complete-flow.bat
```

This will:
- ✅ Run all three flows (Member, Existing Member, New User)
- ✅ Retry up to 3 times on failure
- ✅ Show detailed progress
- ✅ Display summary at the end

### Method 2: Run via Maven Command

```cmd
mvn clean test -DsuiteXmlFile=testng-complete-flow.xml
```

### Method 3: Run Individual Flow

**Member Flow Only:**
```cmd
mvn test -Dtest=CompleteFlowTest#testCompleteMemberFlow
```

**Existing Member Flow Only:**
```cmd
mvn test -Dtest=CompleteFlowTest#testCompleteExistingMemberFlow
```

**New User Flow Only:**
```cmd
mvn test -Dtest=CompleteFlowTest#testCompleteNewUserFlow
```

### Method 4: Run via Eclipse

1. Open Eclipse
2. Navigate to project: `MrYodaDiagnosticsAPI`
3. Right-click on `testng-complete-flow.xml`
4. Select: **Run As → TestNG Suite**

## 📊 What Gets Executed?

### Flow 1: Member Flow (5 steps)
```
Login → Locations → Brands → Search → Add to Cart
```

### Flow 2: Existing Member Flow (5 steps)
```
Login → Locations → Brands → Search → Add to Cart
```

### Flow 3: New User Flow (6 steps)
```
Register → Login → Locations → Brands → Search → Add to Cart
```

## ✅ What Gets Validated?

At each step, the framework validates:
- ✅ API response success
- ✅ Required data is present
- ✅ Data is stored in RequestContext
- ✅ Token is valid
- ✅ IDs are not null

## 📝 Sample Output

```
╔════════════════════════════════════════════════════════════════╗
║           🎯 MEMBER FLOW - COMPLETE EXECUTION 🎯               ║
╚════════════════════════════════════════════════════════════════╝

📍 Step 1/5: Member Login
✅ Member login data validated successfully

📍 Step 2/5: Fetch Locations
✅ Location data validated successfully - 15 locations stored

... (continues for all steps)

╔════════════════════════════════════════════════════════════════╗
║           ✅ MEMBER FLOW COMPLETED SUCCESSFULLY ✅             ║
╚════════════════════════════════════════════════════════════════╝

📊 =============== MEMBER FLOW SUMMARY ===============
👤 User ID       : abc-123-def-456
👤 First Name    : John
👤 Last Name     : Doe
🔑 Token         : Generated ✅
📍 Locations     : 15 stored
🏷️  Brands        : 3 stored
🛒 Cart ID       : cart-guid-789
💰 Total Amount  : ₹1500
======================================================
```

## 🔍 Viewing Reports

### TestNG HTML Report
```
target\surefire-reports\index.html
```

### Allure Report
```cmd
allure serve allure-results
```

## ⚙️ Configuration

Edit the mobile numbers in:
```
src/test/resources/config.properties
```

```properties
# Member credentials
member.mobile=9876543210

# Existing Member (Non-member) credentials
non.member.mobile=9876543211

# Note: New User mobile is auto-generated
```

## 🔧 Troubleshooting

### Issue: Tests fail immediately
**Check:**
- Is the API server running?
- Are credentials in config.properties correct?
- Is internet connection stable?

### Issue: Cart ID is null
**Check:**
- Did previous steps complete successfully?
- Is the test data available in Global Search?

### Issue: Location data not found
**Check:**
- Did Location API return data?
- Check console logs for API response

## 📁 Key Files

| File | Purpose |
|------|---------|
| `CompleteFlowTest.java` | Main flow orchestrator |
| `testng-complete-flow.xml` | TestNG configuration |
| `run-complete-flow.bat` | Batch runner with retry |
| `RequestContext.java` | Data storage utility |
| `config.properties` | Configuration file |

## 🎯 Expected Results

✅ **All flows should complete successfully** with:
- Valid tokens generated
- User data stored
- Locations fetched (15+)
- Brands fetched (3+)
- Tests found in search
- Items added to cart
- Cart IDs generated

## 📞 Need Help?

Check these files:
1. `COMPLETE_FLOW_IMPLEMENTATION.md` - Detailed documentation
2. Console output - Shows step-by-step progress
3. Test logs - `target/surefire-reports/`

---

**Ready to Run?**

Just execute:
```cmd
run-complete-flow.bat
```

That's it! 🚀
