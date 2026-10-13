# Day 6: Module 1 -- Collections Implementation, Workflow Queue & Console Application

## 1. Today's Objective
Implement `AuthServiceImpl` with in-memory Java Collections (`Map`, `Set`, `List`, `Queue`), build the organiser approval workflow queue, and create the menu-driven console application (`AuthConsoleApp`).

## 2. Concepts Learned & Collections Used
- **`Map<String, AuthUser>` (`usersByEmail`):** $O(1)$ fast lookup by email for user login and retrieval.
- **`Set<String>` (`registeredEmails`):** $O(1)$ email uniqueness enforcement to reject duplicate registrations.
- **`List<AuthUser>` (`allUsersList`):** Preserves chronological registration order.
- **`Queue<AuthUser>` (`pendingOrganiserApprovals`):** FIFO queue managing pending organiser registrations waiting for admin approval (`offer()` and `poll()`).
- **Menu-Driven Console Architecture:** Interactive CLI supporting Registration, Login, Profile View, and Logout with role-specific views.

## 3. Files Created / Updated
1. `AuthServiceImpl.java`: Service implementation encapsulating Map, Set, List, and Queue operations.
2. `AuthConsoleApp.java`: Interactive CLI entry point with role-based access control and demo seed accounts.

## 4. Work Completed
- Built user registration flow with input validation and duplicate detection.
- Built login flow checking credentials against stored hash.
- Implemented role-based profile display and action permissions.
- Pre-seeded test accounts for Admin, Organiser, Attendee, and Volunteer.

## 5. Execution Commands
```bash
javac -d bin -sourcepath "Project\src" ^
  Project\src\com\eventplatform\auth\UserRole.java ^
  Project\src\com\eventplatform\auth\exception\*.java ^
  Project\src\com\eventplatform\auth\model\*.java ^
  Project\src\com\eventplatform\auth\service\*.java ^
  Project\src\com\eventplatform\auth\app\AuthConsoleApp.java

java -cp bin com.eventplatform.auth.app.AuthConsoleApp
```
