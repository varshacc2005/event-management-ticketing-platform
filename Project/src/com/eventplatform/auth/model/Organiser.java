package com.eventplatform.auth.model;

import com.eventplatform.auth.UserRole;

/**
 * Organiser -- Subclass of AuthUser
 *
 * Represents an event organiser who creates and manages events.
 * Note: This is the AUTH module's Organiser (with S, as specified).
 * It is separate from com.eventplatform.model.Organizer (with Z)
 * which handles event management logic. They can be linked by email
 * in a future integration module.
 */
public class Organiser extends AuthUser {

    // Organiser-specific field: the company or organization name
    private String organizationName;

    public Organiser(String userId, String name, String email, String password, String organizationName) {
        super(userId, name, email, password, UserRole.ORGANISER);
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String getRoleDescription() {
        return "Event Organiser | Organization: " + organizationName;
    }

    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("  [ORGANISER] Organization : " + organizationName);
        System.out.println("  [ORGANISER] Can create and manage events.");
        System.out.println("+==========================================+");
    }
}
