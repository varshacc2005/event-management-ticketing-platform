package com.eventplatform.auth;

/**
 * Enum representing the four supported user roles in the system.
 *
 * An enum is the cleanest way to restrict role values to exactly
 * four known constants. It prevents typos (e.g. "ADNIM") and lets
 * us use switch/if statements safely.
 */
public enum UserRole {
    ADMIN,
    ORGANISER,
    ATTENDEE,
    VOLUNTEER
}
