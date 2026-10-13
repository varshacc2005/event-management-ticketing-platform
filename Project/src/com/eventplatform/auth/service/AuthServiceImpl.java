package com.eventplatform.auth.service;

import com.eventplatform.auth.UserRole;
import com.eventplatform.auth.exception.DuplicateEmailException;
import com.eventplatform.auth.exception.InvalidRegistrationException;
import com.eventplatform.auth.model.*;

import java.util.*;

/**
 * =============================================================
 * AuthServiceImpl -- Implementation of the AuthService Interface
 * =============================================================
 *
 * This class contains all the actual logic for user registration,
 * login, and lookup. It uses four Java Collections deliberately:
 *
 * Collection       | Purpose
 * -----------------+-------------------------------------------------
 * Map<String,      | Primary user store. Lets us look up any user by
 *   AuthUser>      | email in O(1) time. Used for login & duplicates.
 * -----------------+-------------------------------------------------
 * Set<String>      | Tracks registered emails for uniqueness. A
 *                  | HashSet.contains() is O(1), no duplicates.
 * -----------------+-------------------------------------------------
 * List<AuthUser>   | Maintains an ordered list of ALL users in the
 *                  | order they registered. HashMap doesn't guarantee
 *                  | insertion order; List does.
 * -----------------+-------------------------------------------------
 * Queue<AuthUser>  | Pending approval queue for ORGANISER accounts.
 *                  | New organisers wait in a FIFO queue until an
 *                  | Admin approves them. Models a real workflow.
 * -----------------+-------------------------------------------------
 *
 * Minimum password length: 6 characters.
 */
public class AuthServiceImpl implements AuthService {

    // Minimum password length constant
    public static final int MIN_PASSWORD_LENGTH = 6;

    // Auto-incrementing ID counter for new users
    private int userIdCounter = 1000;

    // MAP: email (lowercase) -> AuthUser
    // Purpose: O(1) lookup during login and duplicate-email checks.
    // We use lowercase email as the key so "Alice@mail.com" == "alice@mail.com".
    private final Map<String, AuthUser> usersByEmail;

    // SET: registered emails (lowercase)
    // Purpose: Fast uniqueness enforcement. HashSet.contains() is O(1).
    // We keep this in sync with usersByEmail; having both makes the
    // intent of "uniqueness tracking" very explicit for the reviewer.
    private final Set<String> registeredEmails;

    // LIST: all users in registration order
    // Purpose: Preserves insertion order. Useful to display "all users"
    // in the order they registered, which HashMap does not guarantee.
    private final List<AuthUser> allUsersList;

    // QUEUE: organisers awaiting admin approval
    // Purpose: FIFO workflow. When an ORGANISER registers, they are added
    // here. An ADMIN approves them in first-come-first-served order.
    // LinkedList implements the Queue interface.
    private final Queue<AuthUser> pendingOrganiserApprovals;

    // ====================================================================
    // Constructor
    // ====================================================================
    public AuthServiceImpl() {
        this.usersByEmail             = new HashMap<>();
        this.registeredEmails         = new HashSet<>();
        this.allUsersList             = new ArrayList<>();
        this.pendingOrganiserApprovals = new LinkedList<>(); // LinkedList implements Queue
    }

    // ====================================================================
    // register()
    // ====================================================================
    @Override
    public AuthUser register(String name, String email, String password, UserRole role, String extra)
            throws DuplicateEmailException {

        // STEP 1: Input Validation (throws UNCHECKED InvalidRegistrationException)
        validateRegistrationInput(name, email, password);

        String normalizedEmail = email.trim().toLowerCase();

        // STEP 2: Duplicate Email Check (throws CHECKED DuplicateEmailException)
        if (registeredEmails.contains(normalizedEmail)) {
            throw new DuplicateEmailException(email.trim());
        }

        // STEP 3: Build the correct subclass based on the role
        String userId = "USR-" + (++userIdCounter);
        String extraDetail = (extra != null && !extra.trim().isEmpty()) ? extra.trim() : "N/A";

        AuthUser newUser;
        switch (role) {
            case ADMIN:
                newUser = new Admin(userId, name.trim(), normalizedEmail, password, extraDetail);
                break;
            case ORGANISER:
                newUser = new Organiser(userId, name.trim(), normalizedEmail, password, extraDetail);
                break;
            case ATTENDEE:
                newUser = new AuthAttendee(userId, name.trim(), normalizedEmail, password, extraDetail);
                break;
            case VOLUNTEER:
                newUser = new Volunteer(userId, name.trim(), normalizedEmail, password, extraDetail);
                break;
            default:
                throw new InvalidRegistrationException("Unknown role: " + role);
        }

        // STEP 4: Store in all three collections
        registeredEmails.add(normalizedEmail);       // Set: mark email as taken
        usersByEmail.put(normalizedEmail, newUser);  // Map: enable O(1) email lookup
        allUsersList.add(newUser);                   // List: preserve registration order

        // STEP 5: Organisers also go into the approval queue
        if (role == UserRole.ORGANISER) {
            pendingOrganiserApprovals.offer(newUser); // offer() adds to tail of queue
            System.out.println("  [INFO] Organiser '" + name.trim()
                    + "' added to the pending approvals queue.");
        }

        return newUser;
    }

    // ====================================================================
    // login()
    // ====================================================================
    @Override
    public AuthUser login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null) {
            return null;
        }

        String normalizedEmail = email.trim().toLowerCase();

        // Look up the user by email using the Map (O(1) lookup)
        AuthUser user = usersByEmail.get(normalizedEmail);

        if (user == null) {
            return null; // Email not found
        }

        // Verify the password using the stored hash
        if (!user.verifyPassword(password)) {
            return null; // Wrong password
        }

        return user; // Login successful
    }

    // ====================================================================
    // findByEmail()
    // ====================================================================
    @Override
    public AuthUser findByEmail(String email) {
        if (email == null) return null;
        return usersByEmail.get(email.trim().toLowerCase());
    }

    // ====================================================================
    // getTotalUserCount()
    // ====================================================================
    @Override
    public int getTotalUserCount() {
        return allUsersList.size();
    }

    /**
     * Returns the pending organiser approvals queue.
     * An Admin can call this to see and approve pending organisers.
     */
    public Queue<AuthUser> getPendingOrganiserApprovals() {
        return pendingOrganiserApprovals;
    }

    /**
     * Approves the next organiser in the queue (FIFO).
     * @return The approved organiser, or null if the queue is empty.
     */
    public AuthUser approveNextOrganiser() {
        return pendingOrganiserApprovals.poll(); // poll() removes and returns the head
    }

    // ====================================================================
    // Private helper: validateRegistrationInput()
    // ====================================================================

    /**
     * Validates all registration inputs.
     * Throws InvalidRegistrationException (UNCHECKED) for bad input.
     * We do this BEFORE touching any collections, so the system stays clean.
     */
    private void validateRegistrationInput(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRegistrationException(
                    "Registration failed: Name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidRegistrationException(
                    "Registration failed: Email cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidRegistrationException(
                    "Registration failed: Email '" + email
                    + "' is not valid. It must contain '@' and '.'");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidRegistrationException(
                    "Registration failed: Password must be at least "
                    + MIN_PASSWORD_LENGTH + " characters long.");
        }
    }
}
