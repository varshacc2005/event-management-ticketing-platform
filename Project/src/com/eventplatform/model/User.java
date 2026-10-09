package com.eventplatform.model;

/**
 * Abstract base class representing a User in the system.
 * Part of the User class hierarchy (User -> Attendee, Organizer).
 */
public abstract class User {
    protected String userId;
    protected String name;
    protected String email;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Abstract method to get user role description.
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return "[" + getRole() + "] ID: " + userId + " | Name: " + name + " | Email: " + email;
    }
}
