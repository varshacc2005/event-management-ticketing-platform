# Day 6: Module 1 Review -- Testing, Debugging & Documentation

## 1. Today's Objective
Conduct a thorough review of Module 1 (Authentication & User Management). This included fixing a compilation error caused by Windows character encoding, running all test scenarios end-to-end, verifying correct output for every case, and writing complete review documentation.

## 2. Issue Found & Fixed -- Windows Encoding Error

During compilation, `javac` threw the following error:

```
error: unmappable character (0x90) for encoding windows-1252
```

**Root Cause:** Several source files contained Unicode box-drawing characters (e.g., `╔`, `║`, `╠`, `╚`, `✔`, `✘`, `──`) written as UTF-8 but compiled under Windows-1252 (the default encoding on Windows).

**Fix Applied:** All Unicode special characters were replaced with plain ASCII equivalents:
- `╔══╗` replaced with `+====+`
- `║` replaced with `|`
- `✔` replaced with `[SUCCESS]`
- `✘` replaced with `[FAILED]`
- `──` replaced with `----`

After the fix, all 11 source files compiled successfully with **exit code 0 and zero errors**.

**Lesson Learned:** When writing Java source files on Windows, either use only ASCII characters in string literals, or always pass `-encoding UTF-8` to `javac`. Unicode characters in `.java` files can cause silent encoding mismatches.

## 3. Test Scenarios Run & Results

All 10 test scenarios were executed and verified by piping input into the running program:

| # | Test Scenario | Input Used | Expected Output | Pass/Fail |
|---|---------------|-----------|-----------------|-----------|
| T1 | Successful registration (new user) | `john@test.com`, `Secure99`, ATTENDEE | `[SUCCESS] Registration Successful!` | Pass |
| T2 | Duplicate email registration | Same email as T1 | `[DUPLICATE EMAIL - CHECKED EXCEPTION]` | Pass |
| T3 | Empty name validation | Blank name field | `[INVALID INPUT - UNCHECKED EXCEPTION] Name cannot be empty` | Pass |
| T4 | Invalid email format | `bob@noat` (no dot) | `[INVALID INPUT] Email is not valid` | Pass |
| T5 | Login with unregistered email | `nobody@nowhere.com` | `[LOGIN FAILED] No account found` | Pass |
| T6 | Login with wrong password | `alice@mail.com` + wrong pass | `[LOGIN FAILED] Incorrect password` | Pass |
| T7 | Successful login | `alice@mail.com` / `Alice@123` | `[SUCCESS] Login Successful!` | Pass |
| T8 | Profile access before login | Option 3 (no session) | `[ACCESS DENIED] You must be logged in` | Pass |
| T9 | Role-specific behaviour -- ADMIN | Login as `admin@platform.com` | Shows pending approvals queue count = 1 | Pass |
| T10 | Logout and re-login as different role | Logout Alice, login as Priya | Role switches to ORGANISER cleanly | Pass |

## 4. OOP Concepts Verified in Review

| Concept | Evidence in Code |
|---------|-----------------|
| Abstract Class | `AuthUser` -- cannot be instantiated directly |
| Inheritance | `Admin`, `Organiser`, `AuthAttendee`, `Volunteer` all extend `AuthUser` |
| Constructor Chaining | Each subclass calls `super(userId, name, email, password, role)` |
| Method Overriding | `displayProfile()` overridden in all four subclasses |
| Polymorphism | `currentUser.displayProfile()` resolves to the correct subclass at runtime |
| Encapsulation | `passwordHash` is private with no getter; only `verifyPassword()` is exposed |
| Interface | `AuthService` defines contract; `AuthServiceImpl` implements it |
| Checked Exception | `DuplicateEmailException` extends `Exception`; compiler forces handling |
| Unchecked Exception | `InvalidRegistrationException` extends `RuntimeException`; fails fast |
| Enum | `UserRole` restricts valid role values to exactly four constants |

## 5. Collections Verified

| Collection | Variable | Verification Proof |
|------------|----------|--------------------|
| `HashMap<String, AuthUser>` | `usersByEmail` | Login O(1) lookup confirmed working |
| `HashSet<String>` | `registeredEmails` | Duplicate email correctly rejected |
| `ArrayList<AuthUser>` | `allUsersList` | `getTotalUserCount()` returns correct count after each registration |
| `Queue<AuthUser>` (LinkedList) | `pendingOrganiserApprovals` | Admin profile shows queue size = 1 with `peek()` displaying Priya Sharma |

## 6. Documentation Written

- `Daily task/Module1_Auth_Documentation.md` -- Full module documentation covering:
  - Module objective
  - Functional and non-functional requirements
  - Package structure
  - Class responsibilities table
  - OOP concepts with code examples
  - Collections used and why
  - Exception handling with code snippets
  - Sample input and output for all major scenarios
  - Compile and run commands
  - Current limitations
  - Future improvements

## 7. Final Project Structure After Day 6

```
Project/src/com/eventplatform/
+-- app/
|   +-- MainConsoleApp.java          (Day 4 -- unchanged)
+-- exception/
|   +-- BookingNotFoundException.java
|   +-- EventSoldOutException.java
|   +-- InvalidBookingException.java
+-- model/
|   +-- User.java, Attendee.java, Organizer.java
|   +-- Event.java, ConcertEvent.java, ConferenceEvent.java
|   +-- Ticket.java, TicketTier.java, WaitlistEntry.java
+-- service/
|   +-- Bookable.java, EventManagementService.java
+-- auth/                            (Day 5 -- NEW MODULE 1)
    +-- UserRole.java
    +-- exception/
    |   +-- DuplicateEmailException.java
    |   +-- InvalidRegistrationException.java
    +-- model/
    |   +-- AuthUser.java
    |   +-- Admin.java
    |   +-- Organiser.java
    |   +-- AuthAttendee.java
    |   +-- Volunteer.java
    +-- service/
    |   +-- AuthService.java
    |   +-- AuthServiceImpl.java
    +-- app/
        +-- AuthConsoleApp.java
```

## 8. Execution Commands

Run Module 1 (Authentication):
```bash
java -cp bin com.eventplatform.auth.app.AuthConsoleApp
```

Run Original Event Module (Day 4):
```bash
java -cp bin com.eventplatform.app.MainConsoleApp
java -cp bin com.eventplatform.app.MainConsoleApp --demo
```

## 9. Status

- Module 1 complete and fully tested.
- All existing modules from Day 2, 3, and 4 remain functional and untouched.
- Ready for faculty review.
