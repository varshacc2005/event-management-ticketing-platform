package com.eventplatform.auth.model;

import com.eventplatform.auth.UserRole;

/**
 * =============================================================
 * AuthUser -- Abstract Base Class for Authentication Module
 * =============================================================
 *
 * This class is the parent of Admin, Organiser, AuthAttendee, Volunteer.
 * It stores the user's core info and a hashed password.
 *
 * WHY ABSTRACT?
 * We never create a plain "AuthUser" directly. Every user must
 * have a specific role. Marking this abstract enforces that rule
 * at compile time -- the compiler will not allow "new AuthUser(...)".
 *
 * SECURITY NOTE (LEARNING PROTOTYPE):
 * We store a simple hashCode() of the password here. This is NOT
 * cryptographically secure. In a real production system, always use
 * a proper password hashing library like BCrypt, Argon2, or PBKDF2.
 * NEVER store plain-text passwords in any real application.
 */
public abstract class AuthUser {

    // Unique auto-generated ID for each user (e.g. "USR-1001")
    private String userId;

    // User's full name
    private String name;

    // Email used for login -- must be unique across all users
    private String email;

    // We store a simple hash (int) of the password, NOT the plain text.
    // This is a simplified approach for this learning prototype.
    private int passwordHash;

    // The role assigned to this user (ADMIN, ORGANISER, ATTENDEE, VOLUNTEER)
    private UserRole role;

    /**
     * Constructor for AuthUser.
     *
     * @param userId   Unique identifier (e.g. "USR-1001")
     * @param name     Full name of the user
     * @param email    Email address (used for login)
     * @param password Plain-text password -- converted to hash and discarded
     * @param role     Role assigned to this user
     */
    public AuthUser(String userId, String name, String email, String password, UserRole role) {
        this.userId       = userId;
        this.name         = name;
        this.email        = email;
        this.passwordHash = password.hashCode(); // Store hash, not plain text
        this.role         = role;
    }

    // ===================== Getters =====================

    public String getUserId() { return userId; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public UserRole getRole() { return role; }

    /**
     * Verifies a given password against the stored hash.
     * We hash the input and compare -- we never "unhash" the stored value.
     */
    public boolean verifyPassword(String inputPassword) {
        return this.passwordHash == inputPassword.hashCode();
    }

    /**
     * Abstract method -- each subclass must describe its role-specific details.
     * This enforces method overriding (a key OOP concept).
     */
    public abstract String getRoleDescription();

    /**
     * Displays this user's profile on the console.
     * Each subclass can call super.displayProfile() and then add its own fields.
     */
    public void displayProfile() {
        System.out.println("+==========================================+");
        System.out.println("|              YOUR PROFILE                |");
        System.out.println("+==========================================+");
        System.out.println("  User ID  : " + userId);
        System.out.println("  Name     : " + name);
        System.out.println("  Email    : " + email);
        System.out.println("  Role     : " + role);
        System.out.println("  Details  : " + getRoleDescription());
    }

    @Override
    public String toString() {
        return "[" + role + "] " + name + " <" + email + "> (ID: " + userId + ")";
    }
}
