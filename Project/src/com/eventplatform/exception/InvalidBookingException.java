package com.eventplatform.exception;

/**
 * Custom Checked Exception thrown when ticket booking rules are violated (e.g. ticket limit per attendee exceeded).
 */
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
