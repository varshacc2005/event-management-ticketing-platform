# Day 7: Module 1 Review -- Testing, Debugging, Documentation & Verification

## 1. Today's Objective
Execute comprehensive testing across all validation, exception, and role-based flows, resolve Windows platform compilation issues, and prepare faculty review documentation.

## 2. Issues Found & Debugged -- Windows Character Encoding
- **Issue:** `javac` reported `unmappable character (0x90) for encoding windows-1252` due to Unicode box borders and symbols.
- **Resolution:** Replaced Unicode decorative symbols with standard ASCII equivalents (`+====+`, `|`, `[SUCCESS]`, `[FAILED]`).
- **Result:** Clean compilation across all 11 Java source files with exit code 0.

## 3. Test Scenarios Executed & Verified

| Test ID | Scenario | Input / Action | Result | Status |
|---------|----------|----------------|--------|--------|
| T1 | Successful Registration | Valid details, Role: ATTENDEE | Account created, User ID issued | PASS |
| T2 | Duplicate Email | Attempt existing email registration | `DuplicateEmailException` caught | PASS |
| T3 | Empty Name Validation | Blank name input | `InvalidRegistrationException` caught | PASS |
| T4 | Invalid Email Format | `bob@noat` | `InvalidRegistrationException` caught | PASS |
| T5 | Short Password | Password < 6 characters | Validation error displayed | PASS |
| T6 | Unregistered Email Login | `nobody@nowhere.com` | `[LOGIN FAILED] No account found` | PASS |
| T7 | Invalid Password Login | Incorrect password | `[LOGIN FAILED] Incorrect password` | PASS |
| T8 | Successful Login | Valid credentials | Role-specific welcome rendered | PASS |
| T9 | Unauthorized Profile Access | View profile before login | `[ACCESS DENIED]` | PASS |
| T10 | Role-Based Permissions | Login as ADMIN vs ATTENDEE | Admin sees queue size; Attendee sees booking options | PASS |

## 4. Documentation Prepared
Created `Daily task/Module1_Auth_Documentation.md` detailing:
- Functional and non-functional requirements.
- OOP concepts utilized (Inheritance, Polymorphism, Abstraction, Encapsulation).
- Collections architecture and role justifications.
- Complete execution instructions and anticipated faculty viva Q&A.

## 5. Final Status
- Module 1 fully functional and verified via terminal.
- Ready for deployment and GitHub submission.
