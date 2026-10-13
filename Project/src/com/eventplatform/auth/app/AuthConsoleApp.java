package com.eventplatform.auth.app;

import com.eventplatform.auth.UserRole;
import com.eventplatform.auth.exception.DuplicateEmailException;
import com.eventplatform.auth.exception.InvalidRegistrationException;
import com.eventplatform.auth.model.AuthUser;
import com.eventplatform.auth.service.AuthService;
import com.eventplatform.auth.service.AuthServiceImpl;

import java.util.Queue;
import java.util.Scanner;

/**
 * =============================================================
 * AuthConsoleApp -- Module 1: Authentication & User Management
 * =============================================================
 *
 * *** LEARNING PROTOTYPE -- NOT FOR PRODUCTION USE ***
 * This is a console-based demonstration of authentication concepts
 * using in-memory Java collections. Passwords are stored as
 * hashCode() values (not cryptographically secure). In a real
 * system, use BCrypt or Argon2 with a proper database.
 *
 * MENU:
 *   1. Register
 *   2. Login
 *   3. View Profile
 *   4. Logout
 *   5. Exit
 */
public class AuthConsoleApp {

    // The service typed against the INTERFACE, not the implementation.
    // This is "programming to an interface" -- a key OOP design principle.
    private static AuthService authService = new AuthServiceImpl();

    // Tracks who is currently logged in (null = nobody is logged in)
    private static AuthUser currentUser = null;

    // One Scanner shared across the whole app to avoid resource leaks
    private static Scanner scanner = new Scanner(System.in);

    // ====================================================================
    // main() -- Entry point
    // ====================================================================
    public static void main(String[] args) {
        printBanner();
        seedDemoUsers(); // Pre-loads one user of each role for immediate testing

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    handleViewProfile();
                    break;
                case "4":
                    handleLogout();
                    break;
                case "5":
                    System.out.println("\n  Goodbye! Thank you for using the Event Platform.");
                    running = false;
                    break;
                default:
                    System.out.println("\n  [ERROR] Invalid option. Please enter a number between 1 and 5.");
            }
        }
        scanner.close();
    }

    // ====================================================================
    // MENU DISPLAY
    // ====================================================================
    private static void printBanner() {
        System.out.println("=============================================================");
        System.out.println("   EVENT MANAGEMENT & TICKETING PLATFORM");
        System.out.println("   Module 1: Authentication & User Management");
        System.out.println("   [ LEARNING PROTOTYPE -- Java Console Application ]");
        System.out.println("=============================================================");
    }

    private static void printMenu() {
        System.out.println("\n---------------------------------------------");
        if (currentUser != null) {
            System.out.println("  Logged in as: " + currentUser.getName()
                    + " [" + currentUser.getRole() + "]");
        } else {
            System.out.println("  Status: Not logged in");
        }
        System.out.println("---------------------------------------------");
        System.out.println("  1. Register");
        System.out.println("  2. Login");
        System.out.println("  3. View Profile  " + (currentUser == null ? "(login required)" : ""));
        System.out.println("  4. Logout");
        System.out.println("  5. Exit");
        System.out.println("---------------------------------------------");
    }

    // ====================================================================
    // HANDLER 1: Register
    // ====================================================================
    private static void handleRegister() {
        System.out.println("\n-------- REGISTER NEW USER --------");

        System.out.print("  Full Name      : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Email Address  : ");
        String email = scanner.nextLine().trim();

        System.out.print("  Password (min " + AuthServiceImpl.MIN_PASSWORD_LENGTH + " chars): ");
        String password = scanner.nextLine();

        System.out.println("  Select Role:");
        System.out.println("    1 - ADMIN");
        System.out.println("    2 - ORGANISER");
        System.out.println("    3 - ATTENDEE");
        System.out.println("    4 - VOLUNTEER");
        System.out.print("  Your choice: ");
        String roleChoice = scanner.nextLine().trim();

        UserRole role;
        String extraLabel;
        switch (roleChoice) {
            case "1": role = UserRole.ADMIN;     extraLabel = "Department (e.g. IT, Operations)"; break;
            case "2": role = UserRole.ORGANISER; extraLabel = "Organization Name"; break;
            case "3": role = UserRole.ATTENDEE;  extraLabel = "Your City"; break;
            case "4": role = UserRole.VOLUNTEER; extraLabel = "Assigned Area (e.g. Entry Gate, Stage)"; break;
            default:
                System.out.println("  [ERROR] Invalid role selection. Registration cancelled.");
                return;
        }

        System.out.print("  " + extraLabel + ": ");
        String extra = scanner.nextLine().trim();

        // ── Call the service -- wrap in try-catch for both exception types ──
        try {
            // authService.register() can throw:
            //   (1) DuplicateEmailException (CHECKED)  -- duplicate email found
            //   (2) InvalidRegistrationException (UNCHECKED) -- bad input values
            AuthUser newUser = authService.register(name, email, password, role, extra);

            System.out.println("\n  [SUCCESS] Registration Successful!");
            System.out.println("  Welcome, " + newUser.getName() + "!");
            System.out.println("  Your User ID : " + newUser.getUserId());
            System.out.println("  Role         : " + newUser.getRole());
            System.out.println("  Total users registered so far: " + authService.getTotalUserCount());

        } catch (DuplicateEmailException e) {
            // CHECKED exception -- predictable, recoverable error
            System.out.println("\n  [DUPLICATE EMAIL - CHECKED EXCEPTION] " + e.getMessage());

        } catch (InvalidRegistrationException e) {
            // UNCHECKED exception -- caught here to show a friendly message
            System.out.println("\n  [INVALID INPUT - UNCHECKED EXCEPTION] " + e.getMessage());
        }
    }

    // ====================================================================
    // HANDLER 2: Login
    // ====================================================================
    private static void handleLogin() {
        if (currentUser != null) {
            System.out.println("\n  [INFO] You are already logged in as: " + currentUser.getName());
            System.out.println("  Please logout first (option 4) before logging in as another user.");
            return;
        }

        System.out.println("\n-------- LOGIN --------");
        System.out.print("  Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("  Password: ");
        String password = scanner.nextLine();

        // login() returns null on failure -- no exception for wrong credentials.
        // Wrong password is a normal, expected scenario, not an "exceptional" event.
        AuthUser user = authService.login(email, password);

        if (user == null) {
            // Give specific feedback (in a real system, use one generic message for security)
            if (authService.findByEmail(email) == null) {
                System.out.println("\n  [LOGIN FAILED] No account found with email: " + email);
            } else {
                System.out.println("\n  [LOGIN FAILED] Incorrect password. Please try again.");
            }
            return;
        }

        currentUser = user;
        System.out.println("\n  [SUCCESS] Login Successful! Welcome back, " + currentUser.getName() + "!");
        showRoleSpecificWelcome(currentUser);
    }

    // ====================================================================
    // HANDLER 3: View Profile
    // ====================================================================
    private static void handleViewProfile() {
        // Guard: only logged-in users can view a profile
        if (currentUser == null) {
            System.out.println("\n  [ACCESS DENIED] You must be logged in to view your profile.");
            System.out.println("  Please select option 2 to Login first.");
            return;
        }
        System.out.println();
        currentUser.displayProfile(); // Polymorphism: calls the right subclass version
        showRoleSpecificOptions(currentUser);
    }

    // ====================================================================
    // HANDLER 4: Logout
    // ====================================================================
    private static void handleLogout() {
        if (currentUser == null) {
            System.out.println("\n  [INFO] You are not currently logged in.");
            return;
        }
        System.out.println("\n  [SUCCESS] Logged out. Goodbye, " + currentUser.getName() + "!");
        currentUser = null; // Simply clear the reference -- session ends
    }

    // ====================================================================
    // ROLE-SPECIFIC MESSAGES (demonstrates role-based behaviour)
    // ====================================================================

    /**
     * Shows a welcome message based on the user's role after login.
     * Different roles see different messages and have different capabilities.
     */
    private static void showRoleSpecificWelcome(AuthUser user) {
        System.out.println("\n  -- Role-Based Access ----------------------------------");
        switch (user.getRole()) {
            case ADMIN:
                System.out.println("  [ADMIN] You have full system access.");
                // Cast to impl to access the approval queue (admin-only feature)
                int pending = ((AuthServiceImpl) authService).getPendingOrganiserApprovals().size();
                System.out.println("  [ADMIN] Pending organiser approvals in queue: " + pending);
                System.out.println("  [ADMIN] Total registered users: " + authService.getTotalUserCount());
                break;
            case ORGANISER:
                System.out.println("  [ORGANISER] You can create and manage events.");
                System.out.println("  [ORGANISER] Note: Your account may be pending admin approval.");
                break;
            case ATTENDEE:
                System.out.println("  [ATTENDEE] You can browse events and book tickets.");
                break;
            case VOLUNTEER:
                System.out.println("  [VOLUNTEER] You can view your assigned event schedule.");
                break;
        }
        System.out.println("  -------------------------------------------------------");
    }

    /**
     * Shows role-specific available actions after viewing the profile.
     * This is where future module integrations would be hooked in.
     */
    private static void showRoleSpecificOptions(AuthUser user) {
        System.out.println("\n  -- What You Can Do [" + user.getRole() + "] ----------------------");
        switch (user.getRole()) {
            case ADMIN:
                System.out.println("  * View all registered users");
                System.out.println("  * Approve pending organiser requests");
                System.out.println("  * Manage platform settings (future modules)");
                Queue<AuthUser> queue = ((AuthServiceImpl) authService).getPendingOrganiserApprovals();
                System.out.println("  * Pending Organiser Approvals in Queue: " + queue.size());
                if (!queue.isEmpty()) {
                    // peek() looks at the head without removing it
                    System.out.println("    Next in queue: " + queue.peek().getName()
                            + " <" + queue.peek().getEmail() + ">");
                }
                break;
            case ORGANISER:
                System.out.println("  * Create new events (Module 2)");
                System.out.println("  * Manage ticket tiers (Module 2)");
                System.out.println("  * View your event analytics (future)");
                break;
            case ATTENDEE:
                System.out.println("  * Browse all events (Module 2)");
                System.out.println("  * Book tickets (Module 2)");
                System.out.println("  * View your booking history (Module 2)");
                break;
            case VOLUNTEER:
                System.out.println("  * View assigned events (future)");
                System.out.println("  * Check in attendees (future)");
                System.out.println("  * View your volunteer schedule (future)");
                break;
        }
        System.out.println("  -------------------------------------------------------");
    }

    // ====================================================================
    // SEED DATA -- Pre-loads one of each role for immediate testing
    // ====================================================================

    /**
     * Registers one user of each role automatically at startup.
     * You can test login immediately without registering manually.
     *
     * Demo credentials:
     *   admin@platform.com  / Admin@123  (ADMIN)
     *   priya@events.com    / Pass@123   (ORGANISER)
     *   alice@mail.com      / Alice@123  (ATTENDEE)
     *   ravi@volunteer.com  / Ravi@123   (VOLUNTEER)
     */
    private static void seedDemoUsers() {
        System.out.println("\n  [SYSTEM] Loading demo users for testing...");
        try {
            authService.register("Platform Admin",  "admin@platform.com", "Admin@123", UserRole.ADMIN,     "IT");
            authService.register("Priya Sharma",    "priya@events.com",   "Pass@123",  UserRole.ORGANISER, "Priya Events Pvt Ltd");
            authService.register("Alice Fernandes", "alice@mail.com",     "Alice@123", UserRole.ATTENDEE,  "Mumbai");
            authService.register("Ravi Kumar",      "ravi@volunteer.com", "Ravi@123",  UserRole.VOLUNTEER, "Entry Gate");
        } catch (DuplicateEmailException e) {
            System.err.println("  [SEED ERROR] " + e.getMessage());
        }

        System.out.println("  [SYSTEM] Demo users loaded. Login with these credentials:");
        System.out.println("  +-------------------------------------------------------------------+");
        System.out.println("  | Role       | Email                 | Password   | Extra Detail    |");
        System.out.println("  +-------------------------------------------------------------------+");
        System.out.println("  | ADMIN      | admin@platform.com    | Admin@123  | IT              |");
        System.out.println("  | ORGANISER  | priya@events.com      | Pass@123   | Priya Events    |");
        System.out.println("  | ATTENDEE   | alice@mail.com        | Alice@123  | Mumbai          |");
        System.out.println("  | VOLUNTEER  | ravi@volunteer.com    | Ravi@123   | Entry Gate      |");
        System.out.println("  +-------------------------------------------------------------------+");
    }
}
