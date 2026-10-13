package com.eventplatform.auth.exception;

/**
 * =============================================================
 * DuplicateEmailException — Custom CHECKED Exception
 * =============================================================
 *
 * WHAT IS A CHECKED EXCEPTION?
 * A checked exception extends java.lang.Exception (not RuntimeException).
 * The compiler forces you to either:
 *   (a) declare it in the method signature using "throws", OR
 *   (b) catch it with a try-catch block.
 *
 * WHY USE IT HERE?
 * Duplicate email is a PREDICTABLE, RECOVERABLE error. The user
 * simply typed an email that already exists. We want the calling
 * code to HANDLE this gracefully (show an error message) rather
 * than crash silently.
 */
public class DuplicateEmailException extends Exception {

    // We store the duplicate email so the caller can include it
    // in a helpful error message without re-parsing the message string.
    private final String email;

    public DuplicateEmailException(String email) {
        super("Registration failed: Email '" + email + "' is already registered. Please use a different email.");
        this.email = email;
    }

    // Getter so the caller can retrieve just the email value.
    public String getEmail() {
        return email;
    }
}
