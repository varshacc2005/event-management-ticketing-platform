# Day 5: Module 1 -- Authentication & User Management

## 1. Today's Objective
Design and implement a complete Authentication & User Management module for the Event Management & Ticketing Platform using Core Java. The goal was to add secure user registration, login, role-based access control, and profile viewing -- all using in-memory collections with no external frameworks or databases.

## 2. Concepts Learned

- **Abstract Classes & Inheritance:** Created `AuthUser` as an abstract parent class. Four concrete subclasses (`Admin`, `Organiser`, `AuthAttendee`, `Volunteer`) extend it, each adding role-specific fields and overriding `displayProfile()`.
- **Constructor Chaining:** Every subclass calls `super(userId, name, email, password, role)` to initialize shared fields defined in `AuthUser` without duplicating code.
- **Method Overriding & Polymorphism:** `currentUser.displayProfile()` calls the correct subclass version at runtime depending on the actual object type -- the JVM resolves this automatically.
- **Interface Contract:** `AuthService` interface defines what the auth module must do (`register`, `login`, `findByEmail`). `AuthServiceImpl` provides the implementation. The main app is coded against the interface, not the class.
- **Enums:** `UserRole` enum restricts role values to exactly four constants (`ADMIN`, `ORGANISER`, `ATTENDEE`, `VOLUNTEER`), preventing invalid string values.
- **Custom Checked Exception:** `DuplicateEmailException` extends `Exception`. The compiler forces every caller of `register()` to handle it explicitly.
- **Custom Unchecked Exception:** `InvalidRegistrationException` extends `RuntimeException`. Thrown immediately when input validation fails (empty name, invalid email, short password).
- **Password Hashing (Basic):** Passwords are stored as `hashCode()` integers, never as plain text. `verifyPassword()` re-hashes the input and compares -- the stored hash is never exposed.

## 3. Collections Used

- **`HashMap<String, AuthUser>` (`usersByEmail`):** Primary user store. Enables O(1) login lookup by email.
- **`HashSet<String>` (`registeredEmails`):** Tracks unique emails. `contains()` is O(1) and guarantees no duplicate registrations.
- **`ArrayList<AuthUser>` (`allUsersList`):** Stores all users in registration order (insertion order guaranteed, unlike HashMap).
- **`LinkedList<AuthUser>` as `Queue` (`pendingOrganiserApprovals`):** FIFO approval workflow. Organisers enter the queue on registration; an Admin processes them in first-come-first-served order using `poll()`.

## 4. Files Created

**Package: `com.eventplatform.auth`**
1. `UserRole.java` -- Enum defining ADMIN, ORGANISER, ATTENDEE, VOLUNTEER

**Package: `com.eventplatform.auth.exception`**
2. `DuplicateEmailException.java` -- Custom checked exception for duplicate email
3. `InvalidRegistrationException.java` -- Custom unchecked exception for bad input

**Package: `com.eventplatform.auth.model`**
4. `AuthUser.java` -- Abstract parent class with password hashing and profile display
5. `Admin.java` -- Subclass with `department` field
6. `Organiser.java` -- Subclass with `organizationName` field
7. `AuthAttendee.java` -- Subclass with `city` field
8. `Volunteer.java` -- Subclass with `assignedArea` field

**Package: `com.eventplatform.auth.service`**
9. `AuthService.java` -- Interface defining the authentication contract
10. `AuthServiceImpl.java` -- Full implementation with all four collections

**Package: `com.eventplatform.auth.app`**
11. `AuthConsoleApp.java` -- Menu-driven console app (Register, Login, View Profile, Logout, Exit)

**Documentation**
12. `Daily task/Module1_Auth_Documentation.md` -- Full review documentation

> All pre-existing files from Day 2, 3, and 4 are untouched.

## 5. Console Menu Implemented

```
1. Register
2. Login
3. View Profile  (login required)
4. Logout
5. Exit
```

- Profile access is blocked before login with `[ACCESS DENIED]` message.
- After login, a role-specific welcome message is shown.
- Invalid menu selections are handled with a clear error message.
- Four demo users are pre-loaded at startup for immediate testing.

## 6. Test Scenarios Verified

| Scenario | Result |
|----------|--------|
| Successful registration | `[SUCCESS] Registration Successful!` |
| Duplicate email registration | `[DUPLICATE EMAIL - CHECKED EXCEPTION]` |
| Empty name input | `[INVALID INPUT - UNCHECKED EXCEPTION] Name cannot be empty` |
| Invalid email format | `[INVALID INPUT - UNCHECKED EXCEPTION] Email is not valid` |
| Login with unregistered email | `[LOGIN FAILED] No account found` |
| Login with wrong password | `[LOGIN FAILED] Incorrect password` |
| Successful login | `[SUCCESS] Login Successful!` |
| Profile access before login | `[ACCESS DENIED] You must be logged in` |
| Role-specific behaviour (ADMIN) | Shows pending organiser approvals queue count |
| Logout and re-login | Session resets cleanly |

## 7. Execution Commands

Compile Module 1:
```bash
javac -d bin -sourcepath "Project\src" ^
  Project\src\com\eventplatform\auth\UserRole.java ^
  Project\src\com\eventplatform\auth\exception\DuplicateEmailException.java ^
  Project\src\com\eventplatform\auth\exception\InvalidRegistrationException.java ^
  Project\src\com\eventplatform\auth\model\AuthUser.java ^
  Project\src\com\eventplatform\auth\model\Admin.java ^
  Project\src\com\eventplatform\auth\model\Organiser.java ^
  Project\src\com\eventplatform\auth\model\AuthAttendee.java ^
  Project\src\com\eventplatform\auth\model\Volunteer.java ^
  Project\src\com\eventplatform\auth\service\AuthService.java ^
  Project\src\com\eventplatform\auth\service\AuthServiceImpl.java ^
  Project\src\com\eventplatform\auth\app\AuthConsoleApp.java
```

Run Module 1:
```bash
java -cp bin com.eventplatform.auth.app.AuthConsoleApp
```

## 8. Key Decisions & Notes

- `AuthAttendee` was named differently from the existing `Attendee` class (in the event module) to avoid a package naming clash. They represent the same real-world entity but serve different modules. A future integration step will link them by email.
- The `Organiser` (with S) in the auth module is separate from `Organizer` (with Z) in the event model -- this follows the module boundary principle.
- Pre-seeded demo users allow immediate testing without manual registration during the review.
