package com.eventplatform.auth.exception;

/**
 * =============================================================
 * InvalidRegistrationException — Custom UNCHECKED Exception
 * =============================================================
 *
 * WHAT IS AN UNCHECKED EXCEPTION?
 * An unchecked exception extends java.lang.RuntimeException.
 * The compiler does NOT force you to catch or declare it.
 * It represents a programming/input error that usually cannot
 * be recovered from in a meaningful way at that point.
 *
 * WHY USE IT HERE?
 * Invalid input (empty name, short password, null values) is a
 * PROGRAMMING / INPUT LOGIC ERROR that should fail fast before
 * any processing happens. It acts like a guard / validation wall.
 * We still catch it in Main to print a friendly message to the user.
 */
public class InvalidRegistrationException extends RuntimeException {

    public InvalidRegistrationException(String message) {
        super(message);
    }
}
