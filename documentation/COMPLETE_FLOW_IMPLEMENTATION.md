# Complete Flow Implementation - MrYoda Diagnostics API

## 📋 Overview

This implementation provides a comprehensive end-to-end flow testing framework for the MrYoda Diagnostics API. It supports three user types with complete data storage and validation at each step.

## 🎯 Features Implemented

### 1. **Separate Data Storage for Each User Type**
   - **Member**: Existing user with full profile
   - **Existing Member**: Non-member user (alternative existing user)
   - **New User**: Newly registered user

### 2. **Complete Response Data Storage**
   - ✅ Login tokens and user details
   - ✅ Location IDs and names
   - ✅ Brand IDs and names
   - ✅ Test/Product details from Global Search
   - ✅ Cart ID, numeric ID, and total amount (separate for each user type)

### 3. **Automatic Retry Mechanism**
   - Retries failed tests up to 3 times
   - 5-second delay between retries
   - Detailed logging of each attempt

### 4. **Complete Flow Orchestration**
   - Validates data at each step
   - Fails fast if critical data is missing
   - Provides detailed summary at the end

## 🚀 How to Run

### Option 1: Run Complete Flow with Retry (Recommended)
```cmd
run-complete-flow.bat
```

### Option 2: Run via Maven
```cmd
mvn clean test -DsuiteXmlFile=testng-complete-flow.xml
```

### Option 3: Run Individual Flows
```cmd
# Member Flow Only
mvn test -Dtest=CompleteFlowTest#testCompleteMemberFlow

# Existing Member Flow Only
mvn test -Dtest=CompleteFlowTest#testCompleteExistingMemberFlow

# New User Flow Only
mvn test -Dtest=CompleteFlowTest#testCompleteNewUserFlow
```

### Option 4: Run via Eclipse
1. Right-click on `testng-complete-flow.xml`
2. Select **Run As → TestNG Suite**

## 📊 Flow Details

### 🔵 Member Flow
```
1. Login (Member) ✅
   ↓ Stores: Token, User ID, First Name, Last Name
2. Get Locations ✅
   ↓ Stores: Location IDs mapped to names
3. Get Brands ✅
   ↓ Stores: Brand IDs mapped to names
4. Global Search ✅
   ↓ Stores: Test details (ID, price, type, etc.)
5. Add to Cart ✅
   ↓ Stores: Cart GUID, Cart ID, Total Amount
```

**Data Stored:**
- `RequestContext.getMemberToken()`
- `RequestContext.getMemberUserId()`
- `RequestContext.getMemberFirstName()`
- `RequestContext.getMemberLastName()`
- `RequestContext.getMemberCartId()`
- `RequestContext.getMemberCartNumericId()`
- `RequestContext.getMemberTotalAmount()`

### 🟢 Existing Member Flow
```
1. Login (Existing Member) ✅
   ↓ Stores: Token, User ID, First Name, Last Name
2. Get Locations ✅
   ↓ Stores: Location IDs (shared)
3. Get Brands ✅
   ↓ Stores: Brand IDs (shared)
4. Global Search ✅
   ↓ Reuses stored test data
5. Add to Cart ✅
   ↓ Stores: Cart GUID, Cart ID, Total Amount
```

**Data Stored:**
- `RequestContext.getExistingMemberToken()`
- `RequestContext.getExistingMemberUserId()`
- `RequestContext.getExistingMemberFirstName()`
- `RequestContext.getExistingMemberLastName()`
- `RequestContext.getExistingMemberCartId()`
- `RequestContext.getExistingMemberCartNumericId()`
- `RequestContext.getExistingMemberTotalAmount()`

### 🟡 New User Flow
```
1. User Registration ✅
   ↓ Stores: User ID, Mobile, First Name, Last Name
2. Login (New User) ✅
   ↓ Stores: Token, User ID, First Name, Last Name
3. Get Locations ✅
   ↓ Stores: Location IDs (shared)
4. Get Brands ✅
   ↓ Stores: Brand IDs (shared)
5. Global Search ✅
   ↓ Reuses stored test data
6. Add to Cart ✅
   ↓ Stores: Cart GUID, Cart ID, Total Amount
```

**Data Stored:**
- `RequestContext.getNewUserToken()`
- `RequestContext.getNewUserUserId()`
- `RequestContext.getNewUserFirstName()`
- `RequestContext.getNewUserLastName()`
- `RequestContext.getMobile()`
- `RequestContext.getNewUserCartId()`
- `RequestContext.getNewUserCartNumericId()`
- `RequestContext.getNewUserTotalAmount()`

## 🔍 Key Changes Made

### 1. **RequestContext.java** - Enhanced Storage
```java
// Separate cart data for each user type
- setMemberCartId() / getMemberCartId()
- setExistingMemberCartId() / getExistingMemberCartId()
- setNewUserCartId() / getNewUserCartId()

// Separate total amounts
- setMemberTotalAmount() / getMemberTotalAmount()
- setExistingMemberTotalAmount() / getExistingMemberTotalAmount()
- setNewUserTotalAmount() / getNewUserTotalAmount()
```

### 2. **AddToCartAPITest.java** - User-Specific Storage
```java
// Enhanced validation method
validateAddToCartResponse(Response response, String userType)
// Now stores data separately based on userType: "MEMBER", "EXISTING_MEMBER", "NEW_USER"
```

### 3. **CompleteFlowTest.java** - Flow Orchestration
- **testCompleteMemberFlow()**: Complete member journey
- **testCompleteExistingMemberFlow()**: Complete existing member journey
- **testCompleteNewUserFlow()**: Complete new user journey
- Validation after each step
- Detailed summary at the end

## 📁 Files Created/Modified

### New Files
1. `CompleteFlowTest.java` - Flow orchestrator
2. `testng-complete-flow.xml` - TestNG suite for flows
3. `run-complete-flow.bat` - Batch file with retry logic
4. `COMPLETE_FLOW_IMPLEMENTATION.md` - This documentation

### Modified Files
1. `RequestContext.java` - Enhanced cart storage
2. `AddToCartAPITest.java` - User-specific validation

## ✅ Validations Performed

### At Each Step:
- ✅ Token is not null or empty
- ✅ User ID is not null or empty
- ✅ Response data is stored successfully
- ✅ Required fields are populated

### Final Validations:
- ✅ All locations stored
- ✅ All brands stored
- ✅ Test data from Global Search available
- ✅ Cart created successfully with valid ID

## 📝 Example Output

```
╔════════════════════════════════════════════════════════════════╗
║                                                                ║
║           🎯 MEMBER FLOW - COMPLETE EXECUTION 🎯               ║
║                                                                ║
╚════════════════════════════════════════════════════════════════╝

📍 Step 1/5: Member Login
✅ Member login data validated successfully

📍 Step 2/5: Fetch Locations
✅ Location data validated successfully - 15 locations stored

📍 Step 3/5: Fetch Brands
✅ Brand data validated successfully - 3 brands stored

📍 Step 4/5: Global Search
✅ Global search data validated successfully

📍 Step 5/5: Add to Cart
✅ Member cart data validated successfully

╔════════════════════════════════════════════════════════════════╗
║                                                                ║
║           ✅ MEMBER FLOW COMPLETED SUCCESSFULLY ✅             ║
║                                                                ║
╚════════════════════════════════════════════════════════════════╝

📊 =============== MEMBER FLOW SUMMARY ===============
👤 User ID       : 67f92c7e-abc1-4f5e-9d3f-123456789abc
👤 First Name    : John
👤 Last Name     : Doe
🔑 Token         : Generated ✅
📍 Locations     : 15 stored
🏷️  Brands        : 3 stored
🛒 Cart ID       : cart-guid-123
💰 Total Amount  : ₹1500
======================================================
```

## 🔧 Troubleshooting

### Issue: Tests fail on first run
**Solution**: Run `run-complete-flow.bat` which has automatic retry

### Issue: Cart ID is null
**Solution**: Check if Add to Cart API returned success response

### Issue: Location/Brand data not stored
**Solution**: Ensure Location and Brand APIs are called before Global Search

## 🎯 Next Steps

To extend this framework:

1. **Add More APIs**: Follow the same pattern
   - Store response data in `RequestContext`
   - Create separate storage for each user type
   - Validate data after each step

2. **Add More Validations**:
   - Price validations
   - Discount calculations
   - Cart item verification

3. **Add More Flows**:
   - Payment flow
   - Order confirmation flow
   - Report generation flow

## 📞 Support

For issues or questions:
1. Check test logs in `target/surefire-reports/`
2. Review console output for detailed error messages
3. Check Allure reports: `allure serve allure-results`

---

**✅ Implementation Complete!**

All flows are now configured to:
- Store response values at every step
- Validate data before proceeding
- Retry on failure
- Provide detailed summaries
- Support all three user types (Member, Existing Member, New User)
