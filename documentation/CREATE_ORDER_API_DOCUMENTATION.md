# CREATE ORDER API - COMPLETE FLOW DOCUMENTATION

## Overview
The **CreateOrder API** is the final step in the complete diagnostic test booking flow. It creates an order using the `user_id` and validates the response against ALL previous API responses.

---

## 🎯 API Details

### **Endpoint**
```
POST {{baseUrl}}/gateway/v2/CreateOrder
```

### **Request Payload**
```json
{
    "user_id": "74518065-cc4b-4d9e-a24b-32e331e1963d"
}
```

### **Headers**
```
Authorization: Bearer <token>
Content-Type: application/json
```

---

## 🔄 Complete Flow Integration

The CreateOrder API is the **10th step** in the complete flow:

1. **LoginAPI** → Get user token and user_id
2. **UserCreateAPI** → Register new user (for NEW_USER only)
3. **LocationAPI** → Get and store location_id
4. **BrandAPI** → Get and store brand_id
5. **GlobalSearchAPI** → Search tests and store product_id
6. **AddToCartAPI** → Add tests to cart, get cart_guid
7. **GetCartByIdAPI** → Validate cart details
8. **AddressAPI** → Add delivery address
9. **GetAddressByUserIdAPI** → Get and store address_id
10. **GetCentersByAddAPI** → Validate location with address
11. **SlotAPI** → Get available slots and store slot_guid
12. **UpdateCartWithSlotAPI** → Update cart with selected slot
13. **✅ CreateOrderAPI** → Create final order (CURRENT)

---

## ✅ Comprehensive Cross-API Validation

The CreateOrder API test validates the response against **ALL previous APIs**:

### **1. LoginAPI Validation**
```java
✅ User ID matches LoginAPI
   - EXISTING_MEMBER: RequestContext.getExistingMemberUserId()
   - MEMBER: RequestContext.getMemberUserId()
   - NEW_USER: RequestContext.getNewUserUserId()
```

### **2. AddToCartAPI Validation**
```java
✅ Cart GUID matches AddToCartAPI
   - EXISTING_MEMBER: RequestContext.getExistingMemberCartGuid()
   - MEMBER: RequestContext.getMemberCartGuid()
   - NEW_USER: RequestContext.getNewUserCartGuid()
```

### **3. GetAddressAPI Validation**
```java
✅ Address ID matches GetAddressAPI
   - EXISTING_MEMBER: RequestContext.getExistingMemberAddressId()
   - MEMBER: RequestContext.getMemberAddressId()
   - NEW_USER: RequestContext.getNewUserAddressId()
```

### **4. LocationAPI Validation**
```java
✅ Lab Location ID matches LocationAPI
   - RequestContext.getSelectedLocationId()
   - Ensures order is for correct location
```

### **5. SlotAPI Validation**
```java
✅ Slot GUID matches SlotAPI
   - RequestContext.getSelectedSlotGuid()
   - Ensures order has correct appointment time
```

### **6. BrandAPI Validation**
```java
✅ Brand ID matches BrandAPI (for each product)
   - RequestContext.getSelectedBrandId()
   - Validates brand consistency across products
```

### **7. GlobalSearchAPI Validation**
```java
✅ Product IDs match GlobalSearchAPI
   - Validates each product in order
   - Ensures correct tests are included
```

---

## 📋 Test Scenarios

### **Scenario 1: EXISTING_MEMBER**
```java
@Test(priority = 1)
public void testCreateOrder_ForExistingMember()

Request:
{
    "user_id": "74518065-cc4b-4d9e-a24b-32e331e1963d"
}

Validations:
✅ User ID from LoginAPI
✅ Cart GUID from AddToCartAPI
✅ Address ID from GetAddressAPI
✅ Location ID from LocationAPI
✅ Slot GUID from SlotAPI
✅ Brand ID from BrandAPI
✅ Product IDs from GlobalSearchAPI
```

### **Scenario 2: MEMBER**
```java
@Test(priority = 2)
public void testCreateOrder_ForMember()

Request:
{
    "user_id": "2592eebe-cc3d-471a-99f9-56757ff76ea3"
}

Validations:
✅ Same as EXISTING_MEMBER
```

### **Scenario 3: NEW_USER**
```java
@Test(priority = 3)
public void testCreateOrder_ForNewUser()

Request:
{
    "user_id": "950f6528-a98a-4f65-a647-da069a29fedb"
}

Validations:
✅ Same as EXISTING_MEMBER
```

---

## 📊 Response Validation

### **Expected Response Structure**
```json
{
    "status": 200,
    "success": true,
    "msg": "Order created successfully",
    "data": {
        "guid": "order-guid-here",
        "id": 123,
        "user_id": "74518065-cc4b-4d9e-a24b-32e331e1963d",
        "cart_guid": "cart-guid-from-addtocart",
        "address_id": "address-guid-from-getaddress",
        "lab_location_id": "location-id-from-locationapi",
        "slot_guid": "slot-guid-from-slotapi",
        "final_cart_status": "open",
        "order_type": "home",
        "product_details": [
            {
                "product_id": "675921110856fe1e1e992ea8",
                "quantity": 1,
                "brand_id": "967a5f02-2e38-47c8-b850-c4aeee8898ed",
                "location_id": "676a5fa720093d2807af03a5"
            }
        ]
    }
}
```

### **Validation Steps**

#### **STEP 1: Basic Response Validation**
```
✅ HTTP Status Code = 200
✅ success flag = true
✅ Message present
```

#### **STEP 2: Extract Order Details**
```
✅ Order GUID extracted
✅ Order ID extracted
✅ User ID extracted
```

#### **STEP 3: Cross-validate with LoginAPI**
```java
Assert.assertEquals(orderUserId, expectedUserId);
// Ensures order is created for correct user
```

#### **STEP 4: Cross-validate with AddToCart API**
```java
Assert.assertEquals(orderCartGuid, expectedCartGuid);
// Ensures order links to correct cart
```

#### **STEP 5: Cross-validate with GetAddressAPI**
```java
if (orderAddressId != null && expectedAddressId != null) {
    Assert.assertEquals(orderAddressId, expectedAddressId);
}
// Ensures order has correct delivery address
```

#### **STEP 6: Cross-validate with LocationAPI**
```java
Assert.assertEquals(orderLocationId, expectedLocationId);
// Ensures order is for correct lab location
```

#### **STEP 7: Cross-validate with SlotAPI**
```java
Assert.assertEquals(orderSlotGuid, expectedSlotGuid);
// Ensures order has correct appointment slot
```

#### **STEP 8: Validate Order Status and Type**
```
✅ Order Status validated
✅ Order Type validated (home/lab)
```

#### **STEP 9: Validate Product Details**
```java
For each product:
    ✅ Product ID present
    ✅ Quantity validated
    ✅ Brand ID matches BrandAPI
    ✅ Location ID matches LocationAPI
```

#### **STEP 10: Store Order Data**
```java
// Store for future use
RequestContext.setExistingMemberOrderGuid(orderGuid);
RequestContext.setExistingMemberOrderId(orderId);
```

---

## 🎯 Key Features

### **1. Complete Cross-API Validation**
- Validates against **7 different APIs**
- Ensures data consistency across entire flow
- Detects any mismatch immediately

### **2. User Type Specific**
- Separate test for each user type
- Uses correct token for authentication
- Validates user-specific data

### **3. Comprehensive Logging**
- Console output shows validation steps
- Clear indication of what's being validated
- Easy debugging with detailed logs

### **4. Data Storage for Future Use**
- Stores Order GUID and Order ID
- Can be used in future APIs (Payment, Tracking, etc.)
- Maintains context throughout test suite

---

## 📝 Console Output Example

```
╔══════════════════════════════════════════════════════════╗
║     CREATE ORDER API – EXISTING MEMBER                   ║
╚══════════════════════════════════════════════════════════╝

🔹 CROSS-API VALIDATION: Using data from all previous APIs
   ✅ User ID from LoginAPI: 74518065-cc4b-4d9e-a24b-32e331e1963d

📤 CREATE ORDER REQUEST:
   User ID: 74518065-cc4b-4d9e-a24b-32e331e1963d

📥 CREATE ORDER RESPONSE:
   Status Code: 200
   Response Body: {"status":200,"success":true,...}

╔════════════════════════════════════════════════════════════╗
║     COMPREHENSIVE CREATE ORDER VALIDATION - EXISTING_MEMBER
╚════════════════════════════════════════════════════════════╝

🔹 STEP 1: Validating API Response
   ✅ HTTP Status: 200
   ✅ Success flag: true
   ✅ Response message: Order created successfully

🔹 STEP 2: Extracting Order Details
   ✅ Order GUID: fd0d7d71-3903-4445-b0bc-a81dd37a08a8
   ✅ Order ID: 506
   ✅ User ID: 74518065-cc4b-4d9e-a24b-32e331e1963d

🔹 STEP 3: Cross-validating with LoginAPI
   ✅ User ID matches LoginAPI: 74518065-cc4b-4d9e-a24b-32e331e1963d

🔹 STEP 4: Cross-validating with AddToCart API
   ✅ Cart GUID matches AddToCart: fd0d7d71-3903-4445-b0bc-a81dd37a08a8

🔹 STEP 5: Cross-validating with GetAddressAPI
   ✅ Address ID matches GetAddressAPI: b511d64d-419a-43fc-9a89-c142b29dbf0b

🔹 STEP 6: Cross-validating with LocationAPI
   ✅ Lab location ID matches LocationAPI: 676a5fa720093d2807af03a5

🔹 STEP 7: Cross-validating with SlotAPI
   ✅ Slot GUID matches SlotAPI: 855333b0-3441-43df-b09b-fd651de250c5

🔹 STEP 8: Validating Order Status and Type
   ✅ Order Status: open
   ✅ Order Type: home

🔹 STEP 9: Validating Product Details
   ✅ Total products in order: 1

   📦 Product 1:
      Product ID: 675921110856fe1e1e992ea8
      Quantity: 1
      Brand ID: 967a5f02-2e38-47c8-b850-c4aeee8898ed
      Location ID: 676a5fa720093d2807af03a5
      ✅ Brand ID matches BrandAPI
      ✅ Location ID matches LocationAPI

🔹 STEP 10: Storing Order Data for EXISTING_MEMBER
   ✅ EXISTING_MEMBER order data stored
      Order GUID: fd0d7d71-3903-4445-b0bc-a81dd37a08a8
      Order ID: 506

╔════════════════════════════════════════════════════════════╗
║     ✅ ALL VALIDATIONS PASSED FOR EXISTING_MEMBER
╠════════════════════════════════════════════════════════════╣
║  Order GUID: fd0d7d71-3903-4445-b0bc-a81dd37a08a8
║  Order ID: 506
║  User ID: 74518065-cc4b-4d9e-a24b-32e331e1963d
║  Status: open
║  Type: home
╚════════════════════════════════════════════════════════════╝
```

---

## 🔧 Technical Implementation

### **File Structure**
```
src/test/java/com/mryoda/diagnostics/api/tests/
└── CreateOrderAPITest.java

src/main/java/com/mryoda/diagnostics/api/
├── endpoints/
│   └── APIEndpoints.java (CREATE_ORDER endpoint)
└── utils/
    └── RequestContext.java (Order storage methods)
```

### **RequestContext Methods**
```java
// Order setters
setExistingMemberOrderGuid(String guid)
setExistingMemberOrderId(Integer id)
setMemberOrderGuid(String guid)
setMemberOrderId(Integer id)
setNewUserOrderGuid(String guid)
setNewUserOrderId(Integer id)

// Order getters
getExistingMemberOrderGuid()
getExistingMemberOrderId()
getMemberOrderGuid()
getMemberOrderId()
getNewUserOrderGuid()
getNewUserOrderId()
```

---

## ✅ Success Criteria

The test passes when:
1. ✅ HTTP Status = 200
2. ✅ success flag = true
3. ✅ Order GUID and ID are returned
4. ✅ User ID matches LoginAPI
5. ✅ Cart GUID matches AddToCartAPI
6. ✅ Address ID matches GetAddressAPI (if present)
7. ✅ Location ID matches LocationAPI
8. ✅ Slot GUID matches SlotAPI
9. ✅ All product Brand IDs match BrandAPI
10. ✅ All product Location IDs match LocationAPI
11. ✅ Order data successfully stored in RequestContext

---

## 🎉 Benefits

### **1. Complete Integration Testing**
- Tests entire booking flow end-to-end
- Validates data consistency across all APIs
- Ensures no data loss or corruption

### **2. Early Bug Detection**
- Catches mismatches immediately
- Identifies integration issues
- Validates business logic

### **3. Maintainable**
- Clear separation of concerns
- Reusable validation methods
- Easy to extend for new validations

### **4. Production-Ready**
- Comprehensive error handling
- Detailed logging
- Professional test structure

---

**Last Updated**: December 13, 2025  
**Version**: 1.0  
**Status**: ✅ Ready for Execution
