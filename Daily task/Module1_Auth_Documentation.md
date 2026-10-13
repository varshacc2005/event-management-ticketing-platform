# Module 1 -- Authentication & User Management

> **Project:** Event Management & Ticketing Platform
> **Student:** 4th Year B.Tech Information Technology
> **Status:** Complete and Verified (compiled and tested, exit code 0)

---

## 1. Module Objective

Build a complete, in-memory Authentication & User Management system as a console application using Core Java. The module must demonstrate OOP concepts, Java Collections, custom exceptions, and role-based access -- without using any database, Spring Boot, or external framework.

---

## 2. Functional Requirements

| # | Requirement | Status |
|---|-------------|--------|
| FR-1 | User can register with name, email, password, and role | Done |
| FR-2 | Four roles supported: ADMIN, ORGANISER, ATTENDEE, VOLUNTEER | Done |
| FR-3 | Name and email cannot be empty | Done |
| FR-4 | Duplicate email registration is rejected | Done |
| FR-5 | Password must be at least 6 characters | Done |
| FR-6 | Login using registered email and password | Done |
| FR-7 | Role-specific welcome message and menu options after login | Done |
| FR-8 | Users can view their profile (only when logged in) | Done |
| FR-9 | Passwords stored as hash, not plain text | Done |
| FR-10 | All errors handled with meaningful messages | Done |

---

## 3. Non-Functional Requirements

| # | Requirement |
|---|-------------|
| NFR-1 | Pure Java -- no frameworks, no database, no external libraries |
| NFR-2 | In-memory storage using Java Collections |
| NFR-3 | Simple, readable code suitable for a beginner |
| NFR-4 | All files compile with zero errors using javac |
| NFR-5 | Menu-driven console interface |
| NFR-6 | Consistent package structure under com.eventplatform.auth |

---

## 4. New Package Structure

```
Project/src/com/eventplatform/
+-- auth/                                       NEW MODULE 1 PACKAGE
    +-- UserRole.java                           Enum: ADMIN, ORGANISER, ATTENDEE, VOLUNTEER
    +-- exception/
    |   +-- DuplicateEmailException.java        Custom CHECKED exception
    |   +-- InvalidRegistrationException.java   Custom UNCHECKED exception
    +-- model/
    |   +-- AuthUser.java                       Abstract parent class
    |   +-- Admin.java                          Subclass of AuthUser
    |   +-- Organiser.java                      Subclass of AuthUser
    |   +-- AuthAttendee.java                   Subclass of AuthUser
    |   +-- Volunteer.java                      Subclass of AuthUser
    +-- service/
    |   +-- AuthService.java                    Interface (contract)
    |   +-- AuthServiceImpl.java                Implementation (logic + collections)
    +-- app/
        +-- AuthConsoleApp.java                 Main entry point (menu loop)
```

Note: All existing files in com.eventplatform.model, .service, .exception, and .app are UNTOUCHED.

---

## 5. Classes and Their Responsibilities

| Class | Type | Responsibility |
|-------|------|----------------|
| UserRole | Enum | Defines the four allowed roles as constants |
| AuthUser | Abstract Class | Stores userId, name, email, passwordHash, role. Defines getRoleDescription() (abstract) and displayProfile() |
| Admin | Concrete Class | Extends AuthUser. Adds department field. Overrides getRoleDescription() and displayProfile() |
| Organiser | Concrete Class | Extends AuthUser. Adds organizationName |
| AuthAttendee | Concrete Class | Extends AuthUser. Adds city |
| Volunteer | Concrete Class | Extends AuthUser. Adds assignedArea |
| AuthService | Interface | Defines the contract: register(), login(), findByEmail(), getTotalUserCount() |
| AuthServiceImpl | Implementation | Implements AuthService. Manages all four collections and business logic |
| DuplicateEmailException | Checked Exception | Thrown when a duplicate email is detected during registration |
| InvalidRegistrationException | Unchecked Exception | Thrown when input values are invalid |
| AuthConsoleApp | Main Class | Menu loop: Register, Login, View Profile, Logout, Exit |

---

## 6. OOP Concepts Demonstrated

### Abstraction
AuthUser is declared abstract. You cannot call "new AuthUser(...)" -- the compiler prevents it. Every user must be a concrete subclass.

### Inheritance
```
AuthUser (abstract)
+-- Admin
+-- Organiser
+-- AuthAttendee
+-- Volunteer
```
Each subclass uses super(userId, name, email, password, role) in its constructor -- this is constructor chaining.

### Polymorphism (Method Overriding)
```java
// In AuthConsoleApp:
currentUser.displayProfile();
// Calls Admin.displayProfile() if user is Admin,
// Organiser.displayProfile() if Organiser, etc.
// JVM decides at RUNTIME -- this is runtime polymorphism.
```

### Encapsulation
All fields in AuthUser are private. Access is only via getters. The passwordHash field has NO getter -- you can only call verifyPassword(input), so the hash is never exposed.

### Interface
AuthService defines the contract. AuthServiceImpl implements it. AuthConsoleApp declares "AuthService authService" (not AuthServiceImpl). The implementation can be swapped without changing the main app.

---

## 7. Collections Used and Why

| Collection | Variable Name | Why This Collection? |
|------------|---------------|----------------------|
| HashMap<String, AuthUser> | usersByEmail | O(1) email lookup during login. Key = lowercase email. Instantly finds any user. |
| HashSet<String> | registeredEmails | Uniqueness enforcement. contains() is O(1). Makes duplicate check explicit and fast. |
| ArrayList<AuthUser> | allUsersList | Preserves registration order. HashMap does not guarantee insertion order. |
| LinkedList<AuthUser> as Queue | pendingOrganiserApprovals | FIFO approval workflow. Organisers enter the queue on registration. Admin processes them in order using poll(). |

Key point: Each collection has a unique, justified reason. They are not added merely to satisfy the requirement.

---

## 8. Exception Handling

### DuplicateEmailException (CHECKED)

```java
// Thrown in AuthServiceImpl:
if (registeredEmails.contains(normalizedEmail)) {
    throw new DuplicateEmailException(email.trim());
}

// Caught in AuthConsoleApp:
try {
    authService.register(...);
} catch (DuplicateEmailException e) {     // Compiler FORCES you to handle this
    System.out.println(e.getMessage());
}
```
Why checked? Duplicate email is predictable and recoverable. The user can try a different email.

### InvalidRegistrationException (UNCHECKED)

```java
// Thrown in validateRegistrationInput():
if (name == null || name.trim().isEmpty()) {
    throw new InvalidRegistrationException("Name cannot be empty.");
}

// Still caught in AuthConsoleApp for user-friendly output:
} catch (InvalidRegistrationException e) {
    System.out.println(e.getMessage());
}
```
Why unchecked? Empty name or bad email format is an input/logic error that should fail fast.

---

## 9. Sample Input and Output

### Successful Registration
```
Full Name      : Arjun Mehta
Email Address  : arjun@mail.com
Password       : Arjun@99
Role           : 3 (ATTENDEE)
City           : Pune

[SUCCESS] Registration Successful!
Welcome, Arjun Mehta!
Your User ID : USR-1006
Role         : ATTENDEE
```

### Duplicate Email
```
Email Address  : arjun@mail.com

[DUPLICATE EMAIL - CHECKED EXCEPTION] Registration failed: Email 'arjun@mail.com' is already registered.
```

### Empty Name
```
Full Name      : (blank)

[INVALID INPUT - UNCHECKED EXCEPTION] Registration failed: Name cannot be empty.
```

### Wrong Password
```
Password: wrongpass

[LOGIN FAILED] Incorrect password. Please try again.
```

### Profile Access Before Login
```
3. View Profile

[ACCESS DENIED] You must be logged in to view your profile.
```

### Admin Login
```
Email: admin@platform.com | Password: Admin@123

[ADMIN] You have full system access.
[ADMIN] Pending organiser approvals in queue: 1
[ADMIN] Total registered users: 4
```

---

## 10. How to Compile and Run

Open Command Prompt in the project root:
C:\Users\Lenovo\Desktop\event-management-ticketing-platform\

### Compile Module 1
```
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

### Run Module 1
```
java -cp bin com.eventplatform.auth.app.AuthConsoleApp
```

### Run Original Event Module (unchanged)
```
javac -d bin Project\src\com\eventplatform\model\*.java ^
              Project\src\com\eventplatform\exception\*.java ^
              Project\src\com\eventplatform\service\*.java ^
              Project\src\com\eventplatform\app\*.java

java -cp bin com.eventplatform.app.MainConsoleApp
java -cp bin com.eventplatform.app.MainConsoleApp --demo
```

---

## 11. Current Limitations

| Limitation | Explanation |
|------------|-------------|
| Password hashing | hashCode() is NOT cryptographically secure. Real apps must use BCrypt or Argon2. |
| No session timeout | Session lasts until Logout or Exit. |
| In-memory only | All data is lost when the program exits. |
| Auth and booking not linked | AuthAttendee and the booking Attendee class are not yet integrated. |
| No password confirmation | Registration does not ask users to re-enter the password. |

---

## 12. Future Improvements

1. Replace hashCode() with BCrypt for secure password storage
2. Add SQLite or file-based persistence
3. Link AuthUser to the event booking module by email
4. Add Admin dashboard to list users and process approvals interactively
5. Add password reset feature
6. Add session timeout mechanism
