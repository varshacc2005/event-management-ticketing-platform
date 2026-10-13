# Day 5: Module 1 -- Authentication Architecture, Models & Custom Exceptions

## 1. Today's Objective
Design and implement the core domain models, custom exception hierarchy, and authentication service contracts for **Module 1: Authentication & User Management** using Core Java.

## 2. Concepts Learned
- **Abstract Classes & Model Hierarchy:** Created `AuthUser` as an abstract base class. Derived four concrete roles: `Admin`, `Organiser`, `AuthAttendee`, and `Volunteer`.
- **Constructor Chaining (`super`):** Subclasses chain constructors to `AuthUser` to reuse state initialization (`userId`, `name`, `email`, `passwordHash`, `role`).
- **Encapsulation & Basic Hashing:** Password input is stored as a hash (`hashCode()`), preventing plain-text storage in memory.
- **Interface Segregation & Contracts:** Defined `AuthService` interface with `register()`, `login()`, `findByEmail()`, and `getTotalUserCount()`.
- **Custom Exceptions:**
  - `DuplicateEmailException`: Checked exception (extends `Exception`) to force handling of duplicate emails.
  - `InvalidRegistrationException`: Unchecked exception (extends `RuntimeException`) for input validation failures.

## 3. Files Created
1. `UserRole.java`: Enum containing `ADMIN`, `ORGANISER`, `ATTENDEE`, `VOLUNTEER`.
2. `DuplicateEmailException.java`: Custom checked exception.
3. `InvalidRegistrationException.java`: Custom unchecked exception.
4. `AuthUser.java`: Abstract parent model.
5. `Admin.java`: Subclass with `department`.
6. `Organiser.java`: Subclass with `organizationName`.
7. `AuthAttendee.java`: Subclass with `city`.
8. `Volunteer.java`: Subclass with `assignedArea`.
9. `AuthService.java`: Service interface definition.

## 4. Verification
Compiled all newly created models, exceptions, and service interface to ensure clean syntax and structural integrity.
