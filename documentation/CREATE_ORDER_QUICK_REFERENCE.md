# CREATE ORDER API - QUICK REFERENCE

## 🚀 Endpoint
```
POST {{baseUrl}}/gateway/v2/CreateOrder
```

## 📋 Request Body
```json
{
    "user_id": "74518065-cc4b-4d9e-a24b-32e331e1963d"
}
```

## 🔐 Headers
```
Authorization: Bearer <token>
Content-Type: application/json
```

## ✅ Cross-API Validations Performed

| Validation | API Source | Field Validated |
|------------|-----------|-----------------|
| 1. User ID | LoginAPI | `user_id` |
| 2. Cart GUID | AddToCartAPI | `cart_guid` |
| 3. Address ID | GetAddressAPI | `address_id` |
| 4. Location ID | LocationAPI | `lab_location_id` |
| 5. Slot GUID | SlotAPI | `slot_guid` |
| 6. Brand ID | BrandAPI | `product_details[].brand_id` |
| 7. Product IDs | GlobalSearchAPI | `product_details[].product_id` |

## 📊 Expected Response
```json
{
    "status": 200,
    "success": true,
    "msg": "Order created successfully",
    "data": {
        "guid": "order-guid",
        "id": 123,
        "user_id": "user-id-from-login",
        "cart_guid": "cart-guid-from-addtocart",
        "address_id": "address-id-from-getaddress",
        "lab_location_id": "location-id-from-locationapi",
        "slot_guid": "slot-guid-from-slotapi",
        "final_cart_status": "open",
        "order_type": "home",
        "product_details": [...]
    }
}
```

## 🎯 Test Coverage

### User Types Tested:
1. **EXISTING_MEMBER** (8220220227)
2. **MEMBER** (9003730394)
3. **NEW_USER** (Auto-generated)

### Validation Steps (10 Steps):
1. HTTP Status = 200
2. Extract Order Details
3. Cross-validate with LoginAPI
4. Cross-validate with AddToCartAPI
5. Cross-validate with GetAddressAPI
6. Cross-validate with LocationAPI
7. Cross-validate with SlotAPI
8. Validate Order Status/Type
9. Validate Product Details
10. Store Order Data

## 💾 Data Storage

After successful validation, order data is stored:

```java
// For EXISTING_MEMBER
RequestContext.setExistingMemberOrderGuid(orderGuid);
RequestContext.setExistingMemberOrderId(orderId);

// For MEMBER
RequestContext.setMemberOrderGuid(orderGuid);
RequestContext.setMemberOrderId(orderId);

// For NEW_USER
RequestContext.setNewUserOrderGuid(orderGuid);
RequestContext.setNewUserOrderId(orderId);
```

## 🔄 Complete Flow Summary

```
1. Login → Get Token & User ID
2. Register User (NEW_USER only)
3. Get Locations → Select Location
4. Get Brands → Select Brand
5. Global Search → Find Tests
6. Add to Cart → Create Cart
7. Get Cart → Verify Cart
8. Add Address → Save Address
9. Get Address → Retrieve Address ID
10. Validate Location → Confirm Service Area
11. Get Slots → Find Available Time
12. Update Cart with Slot
13. ✅ CREATE ORDER ← (Current Step)
```

## ⚠️ Important Notes

- **Address ID Matching**: May not always match if address wasn't explicitly set in cart (warning only, not failure)
- **Product Quantity**: Products with quantity=0 are excluded from validation
- **Location Consistency**: All products must have same location_id
- **Brand Consistency**: All products must have same brand_id

## 🎉 Success Criteria

Test passes when:
- ✅ Order GUID returned
- ✅ Order ID returned
- ✅ All cross-API validations pass
- ✅ Order data stored successfully

---
**Created**: December 13, 2025
**Status**: Ready for Execution
