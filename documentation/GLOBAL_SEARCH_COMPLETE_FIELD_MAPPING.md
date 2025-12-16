# Global Search API - Complete Field Mapping & Validation

## Summary
All fields from the Global Search API response are now being stored and validated comprehensively.

## API Response Structure
Based on the actual API response, the following fields are extracted and stored:

### Basic Fields
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `_id` | String | `getProductId(testName)` | MongoDB Product ID |
| `test_id` | String | `getTestId(testName)` | Test ID (e.g., GEN110) |
| `test_name` | String | Stored as key | Test Name |
| `slug` | String | `getSlug(testName)` | URL-friendly test name |
| `status` | String | `getTestStatus(testName)` | ACTIVE/INACTIVE |
| `Type` | String | `getTestType(testName)` | diagnostics, etc. |

### Pricing Fields
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `price` | Number | `getTestPrice(testName)` | Current price |
| `original_price` | Number | `getOriginalPrice(testName)` | Original price before discount |
| `b2b_price` | Number | `getB2BPrice(testName)` | B2B price (can be null) |
| `discount_percentage` | Number | `getDiscountPercentage(testName)` | Discount percentage |
| `discount_rate` | String | `getDiscountRate(testName)` | Discount rate in rupees |
| `rewards_percentage` | String | `getRewardsPercentage(testName)` | Rewards percentage |
| `membership_discount` | Number | `getMembershipDiscount(testName)` | Membership discount % |
| `courier_charges` | Number | `getCourierCharges(testName)` | Courier charges |
| `cpt_price` | Number | `getCPTPrice(testName)` | CPT price |
| `actual_cprt_price` | Number | `getActualCPRTPrice(testName)` | Actual CPRT price |
| `cpt_comment` | String | `getCPTComment(testName)` | CPT comment |

### Detail Fields
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `specimen` | String | `getSpecimen(testName)` | Sample specimen type |
| `turn_around_time` | Object | `getTurnAroundTime(testName)` | TAT for results |
| `home_collection` | String | `getHomeCollection(testName)` | Home collection availability |
| `pre_test_information` | String | `getPreTestInformation(testName)` | Pre-test instructions |
| `description` | String | `getDescription(testName)` | Test description |
| `comment` | String | `getComment(testName)` | Additional comments |
| `usage` | String | `getUsage(testName)` | Test usage information |
| `result_interpretation` | String | `getResultInterpretation(testName)` | How to interpret results |

### Boolean Flags
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `popular` | Boolean | `isPopular(testName)` | Is this a popular test? |
| `speciality_tests` | Boolean | `isSpecialityTest(testName)` | Is this a speciality test? |
| `frequently_booked` | Boolean | `isFrequentlyBooked(testName)` | Is frequently booked? |

### Array/List Fields
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `components` | List | `getComponents(testName)` | Test components |
| `locations` | List | `getLocations(testName)` | Available location IDs |
| `genders` | List | `getGenders(testName)` | Applicable genders (MALE, FEMALE) |
| `business_type` | List | `getBusinessType(testName)` | B2B, B2C |
| `stability` | List | `getStability(testName)` | Stability information |
| `method` | List | `getMethod(testName)` | Test methods |
| `organ` | List | `getOrgan(testName)` | Related organs |
| `diseases` | List | `getDiseases(testName)` | Related diseases |
| `search_keywords` | List | `getSearchKeywords(testName)` | Search keywords |
| `other_names` | List | `getOtherNames(testName)` | Alternative test names |
| `frequently_asked_questions` | List | `getFrequentlyAskedQuestions(testName)` | FAQs |
| `department` | List | `getDepartment(testName)` | Department info with _id, name, createdAt, updatedAt, active |
| `doctor_speciality` | List | `getDoctorSpeciality(testName)` | Doctor speciality |
| `doctorsSpeciality` | List | `getDoctorsSpeciality(testName)` | Doctors speciality (different field) |

### Timestamp Fields
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `createdAt` | String | `getCreatedAt(testName)` | Creation timestamp |
| `updatedAt` | String | `getUpdatedAt(testName)` | Last update timestamp |
| `index` | Integer | `getIndex(testName)` | Index number |

### Raw Data Access
| Field Name | Type | Helper Method | Description |
|------------|------|---------------|-------------|
| `raw` | Map | `getRawTestData(testName)` | Complete raw JSON object |

## Implementation Details

### Storage Method
All fields are extracted in `GlobalSearchHelper.extractAndStoreTests()` method and stored in RequestContext using a Map structure.

### Example API Response
```json
{
    "status": 200,
    "success": true,
    "msg": "Tests fetched successfully",
    "total": 213,
    "page": 1,
    "limit": 50,
    "total_pages": 5,
    "total_amount": 0,
    "data": [
        {
            "_id": "675921110856fe1e1e992ec9",
            "test_id": "GEN110",
            "test_name": "Blood Coagulation",
            "components": [],
            "turn_around_time": null,
            "specimen": "",
            "slug": "blood-coagulation",
            "stability": [],
            "comment": "",
            "usage": "",
            "locations": ["64870066842708a0d5ae6c77", ...],
            "department": [
                {
                    "_id": "675697b0262d1460f8d55d36",
                    "name": "HEMATOLOGICAL GENETICS",
                    "createdAt": "2024-12-09T07:09:35.963Z",
                    "updatedAt": "2024-12-09T07:09:35.963Z",
                    "active": true
                }
            ],
            "doctor_speciality": [],
            "method": [],
            "organ": [],
            "doctorsSpeciality": [],
            "diseases": [],
            "price": 25000,
            "original_price": 25000,
            "cpt_comment": "",
            "cpt_price": 0,
            "actual_cprt_price": 0,
            "genders": ["MALE", "FEMALE"],
            "b2b_price": null,
            "home_collection": "NOT AVAILABLE",
            "popular": false,
            "speciality_tests": false,
            "frequently_booked": false,
            "pre_test_information": "",
            "business_type": ["B2B", "B2C"],
            "status": "ACTIVE",
            "createdAt": "2024-12-11T05:20:17.850Z",
            "updatedAt": "2025-10-21T13:41:54.304Z",
            "description": "",
            "courier_charges": 0,
            "frequently_asked_questions": [],
            "other_names": [],
            "result_interpretation": "",
            "index": 12,
            "search_keywords": [],
            "Type": "diagnostics",
            "discount_percentage": 0,
            "discount_rate": "22500",
            "rewards_percentage": "1125",
            "membership_discount": 10
        }
    ]
}
```

## Test Validations
The test now validates **40+ parameters** including:
- ✅ All basic fields (test_id, product_id, slug, status, type)
- ✅ All pricing fields (11 fields including discounts, rewards, CPT)
- ✅ All detail fields (8 fields including specimen, TAT, descriptions)
- ✅ All boolean flags (3 fields)
- ✅ All array/list fields (14 fields including components, locations, departments)
- ✅ All timestamp fields (3 fields)
- ✅ Raw data access for any custom fields

## Usage Example
```java
// Retrieve any field using helper methods
String testId = GlobalSearchHelper.getTestId("Blood Coagulation");
double price = GlobalSearchHelper.getTestPrice("Blood Coagulation");
List<String> genders = GlobalSearchHelper.getGenders("Blood Coagulation");
List<Map<String, Object>> department = GlobalSearchHelper.getDepartment("Blood Coagulation");

// Print complete test details
GlobalSearchHelper.printTestDetails("Blood Coagulation");

// Access raw data if needed
Map<String, Object> rawData = GlobalSearchHelper.getRawTestData("Blood Coagulation");
```

## Files Modified
1. **GlobalSearchHelper.java** - Added 40+ helper methods for comprehensive field access
2. **GlobalSearchAPITest.java** - Updated test to validate all fields with proper assertions

## Benefits
✅ Complete field coverage - no data loss  
✅ Type-safe helper methods for all fields  
✅ Comprehensive validation ensuring data integrity  
✅ Easy access to any field in downstream tests  
✅ Future-proof design with raw data access  
✅ Clear documentation of all available fields
