package com.eventplatform.auth.service;

import com.eventplatform.auth.UserRole;
import com.eventplatform.auth.exception.DuplicateEmailException;
import com.eventplatform.auth.exception.InvalidRegistrationException;
import com.eventplatform.auth.model.AuthUser;

/**
 * =============================================================
 * AuthService — Interface (Contract) for Authentication
 * =============================================================
 *
 * WHY AN INTERFACE?
 * An interface defines WHAT the service must do, but NOT HOW.
 * This separation of "contract" from "implementation" is a key OOP principle
 * called "programming to an interface". It means:
 *   - In future, we could swap AuthServiceImpl with a DatabaseAuthService
 *     without changing any code that USES this interface.
 *   - It makes unit testing easier (you can mock the interface).
 *
 * The implementation class (AuthServiceImpl) provides the actual code.
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     *
     * @param name     Full name (must not be empty)
     * @param email    Email address (must be unique and not empty)
     * @param password Password (must meet minimum length requirement)
     * @param role     One of: ADMIN, ORGANISER, ATTENDEE, VOLUNTEER
     * @param extra    An optional role-specific detail
     *                  (department for Admin, org name for Organiser,
     *                   city for Attendee, area for Volunteer)
     * @return The newly created AuthUser object
     *
     * @throws DuplicateEmailException       if the email is already registered (CHECKED)
     * @throws InvalidRegistrationException  if name/email is empty or password too short (UNCHECKED)
     */
    AuthUser register(String name, String email, String password, UserRole role, String extra)
            throws DuplicateEmailException;

    /**
     * Logs in a user with their email and password.
     *
     * @param email    The registered email address
     * @param password The password to verify
     * @return The AuthUser object if login is successful, null if credentials don't match
     */
    AuthUser login(String email, String password);

    /**
     * Finds a user by their email address.
     *
     * @param email The email to search
     * @return The AuthUser if found, null otherwise
     */
    AuthUser findByEmail(String email);

    /**
     * Returns the total number of registered users.
     */
    int getTotalUserCount();
}
