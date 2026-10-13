package com.eventplatform.auth.model;

import com.eventplatform.auth.UserRole;

/**
 * AuthAttendee -- Subclass of AuthUser for the Authentication Module
 *
 * Represents a registered attendee who can log in and book tickets.
 * Named "AuthAttendee" to avoid a naming clash with the existing
 * com.eventplatform.model.Attendee class (which handles ticket booking).
 * A future integration step would link them by email.
 */
public class AuthAttendee extends AuthUser {

    // Attendee-specific field: city they are attending from
    private String city;

    public AuthAttendee(String userId, String name, String email, String password, String city) {
        super(userId, name, email, password, UserRole.ATTENDEE);
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String getRoleDescription() {
        return "Event Attendee | City: " + city;
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("  [ATTENDEE] City : " + city);
        System.out.println("  [ATTENDEE] Can browse events and book tickets.");
        System.out.println("+==========================================+");
    }
}
