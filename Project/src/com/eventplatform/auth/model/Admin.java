package com.eventplatform.auth.model;

import com.eventplatform.auth.UserRole;

/**
 * Admin -- Subclass of AuthUser
 *
 * Represents a platform administrator with full system access.
 * Demonstrates: Inheritance, Constructor chaining (super()), Method Overriding.
 */
public class Admin extends AuthUser {

    // Admin-specific field: the department they belong to
    private String department;

    /**
     * Constructor calls the parent AuthUser constructor using super().
     * This is "constructor chaining" -- avoids duplicating common field logic.
     */
    public Admin(String userId, String name, String email, String password, String department) {
        super(userId, name, email, password, UserRole.ADMIN);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    /**
     * @Override -- Overrides the abstract method from AuthUser.
     * This is runtime polymorphism: the JVM picks this version when
     * the actual object is an Admin, even if the reference type is AuthUser.
     */
    @Override
    public String getRoleDescription() {
        return "Platform Administrator | Department: " + department;
    }

    /**
     * Override displayProfile to also show Admin-specific info.
     * We call super.displayProfile() first to reuse the common display code.
     */
    @Override
    public void displayProfile() {
        super.displayProfile();
        System.out.println("  [ADMIN]   Department   : " + department);
        System.out.println("  [ADMIN]   Access Level : FULL SYSTEM ACCESS");
        System.out.println("+==========================================+");
    }
}
