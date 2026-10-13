package com.eventplatform.auth.model;

import com.eventplatform.auth.UserRole;

/**
 * Volunteer -- Subclass of AuthUser
 *
 * Represents a volunteer who assists at events.
 * Demonstrates: Inheritance, Constructor chaining, Method Overriding.
 */
public class Volunteer extends AuthUser {

    // Volunteer-specific field: the area they are assigned to
    private String assignedArea;

    public Volunteer(String userId, String name, String email, String password, String assignedArea) {
        super(userId, name, email, password, UserRole.VOLUNTEER);
        this.assignedArea = assignedArea;
    }

    public String getAssignedArea() {
        return assignedArea;
    }

    @Override
    public String getRoleDescription() {
        return "Event Volunteer | Assigned Area: " + assignedArea;
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("  [VOLUNTEER] Assigned Area : " + assignedArea);
        System.out.println("  [VOLUNTEER] Can assist with event operations.");
        System.out.println("+==========================================+");
    }
}
