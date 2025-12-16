# ERROR LOG DOCUMENTATION

## Overview
The `validation_errors.log` file contains **ONLY the most recent test execution** with detailed ACTUAL vs EXPECTED values for all validation errors.

## Key Features

### ✅ 1. **Fresh Log Each Execution**
- The log file is **automatically cleared** at the start of each test suite
- Only the **latest test run** results are displayed
- No historical clutter from previous executions

### ✅ 2. **ACTUAL vs EXPECTED Values**
- Every error shows **what was expected** vs **what was actually received**
- Clear comparison for easy debugging
- Specific value examples included

### ✅ 3. **Detailed Bug Tracking**
- Individual errors logged as they're found
- Summary log at the end showing all bugs for each item
- Timestamp for each error entry

## Error Log Format

### Header (Auto-Generated Each Run)
```
═══════════════════════════════════════════════════════════
VALIDATION ERROR LOG - TEST EXECUTION
EXECUTION DATE: 2025-12-13 HH:MM:SS
═══════════════════════════════════════════════════════════
```

### Individual Error Entry
```
═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 HH:MM:SS
TEST: testGetCartById_EXISTING_MEMBER
ERROR: ❌ BUG: price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
═══════════════════════════════════════════════════════════
```

### Summary Entry
```
═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 HH:MM:SS
TEST: testGetCartById_EXISTING_MEMBER
ERROR: SUMMARY: 3 bugs found in 'Blood Coagulation' - ❌ BUG: price is ZERO | ❌ BUG: original_price is ZERO | ❌ BUG: membershipPrice is ZERO
═══════════════════════════════════════════════════════════
```

## Error Message Formats

### 1. **NULL Value Errors**

#### Quantity NULL:
```
❌ BUG: quantity is NULL for item 'Test Name' | EXPECTED: positive integer | ACTUAL: NULL
```

#### Price NULL:
```
❌ BUG: price is NULL for item 'Test Name' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: NULL
```

#### Original Price NULL:
```
❌ BUG: original_price is NULL for item 'Test Name' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: NULL
```

#### Membership Price NULL (for members):
```
❌ BUG: membershipPrice is NULL for MEMBER user, item 'Test Name' | EXPECTED: 90% of original price | ACTUAL: NULL
```

### 2. **ZERO Value Errors**

#### Quantity ZERO:
```
❌ BUG: quantity is ZERO for item 'Test Name' | EXPECTED: positive integer | ACTUAL: 0
```

#### Price ZERO:
```
❌ BUG: price is ZERO for item 'Test Name' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
```

#### Original Price ZERO:
```
❌ BUG: original_price is ZERO for item 'Test Name' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
```

#### Membership Price ZERO (for members):
```
❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Test Name' | EXPECTED: ₹279 (90% discount) | ACTUAL: ₹0
```

## Example Log Output

### Complete Error Log Example:
```
═══════════════════════════════════════════════════════════
VALIDATION ERROR LOG - TEST EXECUTION
EXECUTION DATE: 2025-12-13 15:05:30
═══════════════════════════════════════════════════════════

═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 15:05:35
TEST: testGetCartById_EXISTING_MEMBER
ERROR: ❌ BUG: price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
═══════════════════════════════════════════════════════════

═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 15:05:35
TEST: testGetCartById_EXISTING_MEMBER
ERROR: ❌ BUG: original_price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0
═══════════════════════════════════════════════════════════

═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 15:05:35
TEST: testGetCartById_EXISTING_MEMBER
ERROR: ❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation' | EXPECTED: ₹22500 (90% discount) | ACTUAL: ₹0
═══════════════════════════════════════════════════════════

═══════════════════════════════════════════════════════════
TIMESTAMP: 2025-12-13 15:05:35
TEST: testGetCartById_EXISTING_MEMBER
ERROR: SUMMARY: 3 bugs found in 'Blood Coagulation' - ❌ BUG: price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0 | ❌ BUG: original_price is ZERO for item 'Blood Coagulation' | EXPECTED: positive amount (e.g., ₹310) | ACTUAL: ₹0 | ❌ BUG: membershipPrice is ZERO for MEMBER user, item 'Blood Coagulation' | EXPECTED: ₹22500 (90% discount) | ACTUAL: ₹0
═══════════════════════════════════════════════════════════
```

## Benefits

1. **✅ Clean History** - Only current execution visible
2. **✅ Clear Debugging** - ACTUAL vs EXPECTED always shown
3. **✅ Complete Picture** - All bugs logged before test fails
4. **✅ Easy Analysis** - Timestamps and test names included
5. **✅ Specific Values** - Exact amounts and expectations shown

## File Location

```
C:\Users\RANJITH\eclipse-workspace\MrYodaDiagnosticsAPI\validation_errors.log
```

## How It Works

1. **First Test Execution**: Log file is cleared and header is added
2. **During Validation**: Each bug is logged as it's discovered
3. **Before Failing**: Summary of all bugs is logged
4. **Next Test Run**: Old log is cleared, fresh log begins

---
**Last Updated**: December 13, 2025  
**Version**: 3.0 (Enhanced with ACTUAL vs EXPECTED)
