package com.eventplatform.exception;

/**
 * Custom Unchecked Exception thrown when a ticket ID or booking lookup fails.
 */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
