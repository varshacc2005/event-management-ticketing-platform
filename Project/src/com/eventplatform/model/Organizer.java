package com.eventplatform.model;

/**
 * Subclass of User representing an Event Organizer.
 */
public class Organizer extends User {
    private String organizationName;
    private String contactPhone;

    public Organizer(String userId, String name, String email, String organizationName, String contactPhone) {
        super(userId, name, email);
        this.organizationName = organizationName;
        this.contactPhone = contactPhone;
    }

    @Override
    public String getRole() {
        return "ORGANIZER";
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}
