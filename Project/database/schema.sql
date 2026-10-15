-- ============================================================================
-- EVENT MANAGEMENT & TICKETING PLATFORM
-- Database Schema Definition (schema.sql)
-- Target RDBMS: MySQL 8.0+
-- Assessment 2 SQL Checkpoint | Days 8, 9, 10
-- ============================================================================

-- Drop existing database to ensure a clean, reproducible setup
DROP DATABASE IF EXISTS event_platform_db;
CREATE DATABASE event_platform_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE event_platform_db;

-- ============================================================================
-- TABLE 1: users
-- Represents platform users across 4 roles: ADMIN, ORGANISER, ATTENDEE, VOLUNTEER.
-- 3NF: Single primary key (user_id), no repeating groups, role-specific attributes
-- are optional columns dependent only on user_id.
-- ============================================================================
CREATE TABLE users (
    user_id VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ORGANISER', 'ATTENDEE', 'VOLUNTEER') NOT NULL,
    organization_name VARCHAR(150) NULL, -- Relevant for ORGANISER
    city VARCHAR(100) NULL,              -- Relevant for ATTENDEE
    assigned_area VARCHAR(100) NULL,     -- Relevant for VOLUNTEER
    department VARCHAR(100) NULL,        -- Relevant for ADMIN
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_name_not_empty CHECK (CHAR_LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_users_email_format CHECK (email LIKE '%_@__%.__%')
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 2: venues
-- Physical or virtual hosting facilities for events.
-- 3NF: Separated venue entity eliminates transitive dependency between
-- event and venue location attributes.
-- ============================================================================
CREATE TABLE venues (
    venue_id VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_venues PRIMARY KEY (venue_id),
    CONSTRAINT chk_venues_capacity CHECK (capacity > 0)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 3: events
-- Central entity for concerts, conferences, seminars, and workshops.
-- Foreign keys link to the creator (users.user_id) and location (venues.venue_id).
-- ============================================================================
CREATE TABLE events (
    event_id VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NULL,
    category ENUM('CONCERT', 'CONFERENCE', 'WORKSHOP', 'SEMINAR') NOT NULL,
    event_date DATETIME NOT NULL,
    venue_id VARCHAR(20) NOT NULL,
    organizer_id VARCHAR(20) NOT NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_events PRIMARY KEY (event_id),
    CONSTRAINT fk_events_venue FOREIGN KEY (venue_id)
        REFERENCES venues (venue_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_events_organizer FOREIGN KEY (organizer_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_events_title_not_empty CHECK (CHAR_LENGTH(TRIM(title)) > 0)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 4: ticket_tiers
-- Pricing tiers (e.g., VIP, Standard, Early Bird) with seat quotas.
-- 3NF: Tier details depend entirely on (event_id, tier_name) compound uniqueness.
-- ============================================================================
CREATE TABLE ticket_tiers (
    tier_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    tier_name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    booked_count INT NOT NULL DEFAULT 0,
    early_bird_deadline DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_ticket_tiers PRIMARY KEY (tier_id),
    CONSTRAINT fk_ticket_tiers_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_ticket_tiers_event_tier UNIQUE (event_id, tier_name),
    CONSTRAINT chk_tier_price CHECK (price >= 0.00),
    CONSTRAINT chk_tier_capacity CHECK (capacity > 0),
    CONSTRAINT chk_tier_booked_count CHECK (booked_count >= 0 AND booked_count <= capacity)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 5: registrations
-- Attendee booking orders grouping one or more tickets for an event tier.
-- Business rule: Maximum 4 tickets per registration order.
-- ============================================================================
CREATE TABLE registrations (
    registration_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    attendee_id VARCHAR(20) NOT NULL,
    tier_id VARCHAR(20) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') NOT NULL DEFAULT 'CONFIRMED',
    registered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_registrations PRIMARY KEY (registration_id),
    CONSTRAINT fk_registrations_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_registrations_attendee FOREIGN KEY (attendee_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_registrations_tier FOREIGN KEY (tier_id)
        REFERENCES ticket_tiers (tier_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_registration_quantity CHECK (quantity >= 1 AND quantity <= 4),
    CONSTRAINT chk_registration_total_amount CHECK (total_amount >= 0.00)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 6: tickets
-- Individual issued digital tickets bearing unique QR/barcode tokens.
-- Directly derived from confirmed registrations.
-- ============================================================================
CREATE TABLE tickets (
    ticket_id VARCHAR(20) NOT NULL,
    registration_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    attendee_id VARCHAR(20) NOT NULL,
    tier_id VARCHAR(20) NOT NULL,
    ticket_code VARCHAR(64) NOT NULL,
    price_paid DECIMAL(10, 2) NOT NULL,
    status ENUM('ISSUED', 'USED', 'CANCELLED') NOT NULL DEFAULT 'ISSUED',
    issued_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_tickets PRIMARY KEY (ticket_id),
    CONSTRAINT fk_tickets_registration FOREIGN KEY (registration_id)
        REFERENCES registrations (registration_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_tickets_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_tickets_attendee FOREIGN KEY (attendee_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_tickets_tier FOREIGN KEY (tier_id)
        REFERENCES ticket_tiers (tier_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_tickets_ticket_code UNIQUE (ticket_code),
    CONSTRAINT chk_ticket_price_paid CHECK (price_paid >= 0.00)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 7: checkins
-- Volunteer scanning log verifying attendance at venue gates.
-- 1-to-1 relationship with valid tickets (a ticket can only be checked in once).
-- ============================================================================
CREATE TABLE checkins (
    checkin_id VARCHAR(20) NOT NULL,
    ticket_id VARCHAR(20) NOT NULL,
    volunteer_id VARCHAR(20) NOT NULL,
    checkin_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SUCCESS', 'FAILED') NOT NULL DEFAULT 'SUCCESS',

    CONSTRAINT pk_checkins PRIMARY KEY (checkin_id),
    CONSTRAINT fk_checkins_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets (ticket_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_checkins_volunteer FOREIGN KEY (volunteer_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_checkins_ticket UNIQUE (ticket_id)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 8: waitlist
-- FIFO queue storing waitlist requests when ticket tiers are sold out.
-- Matches Java in-memory Queue<WaitlistEntry> design.
-- ============================================================================
CREATE TABLE waitlist (
    waitlist_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    attendee_id VARCHAR(20) NOT NULL,
    tier_id VARCHAR(20) NOT NULL,
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('WAITING', 'PROMOTED', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'WAITING',

    CONSTRAINT pk_waitlist PRIMARY KEY (waitlist_id),
    CONSTRAINT fk_waitlist_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_waitlist_attendee FOREIGN KEY (attendee_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_waitlist_tier FOREIGN KEY (tier_id)
        REFERENCES ticket_tiers (tier_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- TABLE 9: feedback
-- Post-event survey ratings and reviews submitted by attendees.
-- 3NF: Composite uniqueness (event_id, attendee_id) ensures 1 review per attendee per event.
-- ============================================================================
CREATE TABLE feedback (
    feedback_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    attendee_id VARCHAR(20) NOT NULL,
    rating INT NOT NULL,
    comments TEXT NULL,
    submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_feedback PRIMARY KEY (feedback_id),
    CONSTRAINT fk_feedback_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_feedback_attendee FOREIGN KEY (attendee_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_feedback_event_attendee UNIQUE (event_id, attendee_id),
    CONSTRAINT chk_feedback_rating CHECK (rating >= 1 AND rating <= 5)
) ENGINE=InnoDB;

-- ============================================================================
-- PERFORMANCE INDEXES (Requirement: At least two useful indexes)
-- Index 1: Optimize filtering upcoming events by publication status and date
-- Index 2: Optimize aggregation of registrations by event and booking status
-- ============================================================================
CREATE INDEX idx_events_status_date ON events (status, event_date);
CREATE INDEX idx_registrations_event_status ON registrations (event_id, status);

-- ============================================================================
-- REPORTING VIEW (Requirement: At least one useful reporting view)
-- vw_event_performance_summary:
-- Consolidates event capacity, confirmed ticket sales, gross revenue,
-- actual check-in attendance, and attendee feedback ratings into one view.
-- ============================================================================
CREATE OR REPLACE VIEW vw_event_performance_summary AS
SELECT 
    e.event_id,
    e.title AS event_title,
    e.category,
    e.event_date,
    e.status AS event_status,
    u.name AS organizer_name,
    v.name AS venue_name,
    v.city AS venue_city,
    COALESCE(SUM(DISTINCT tt.capacity), 0) AS total_capacity,
    COUNT(DISTINCT CASE WHEN r.status = 'CONFIRMED' THEN t.ticket_id END) AS confirmed_tickets_sold,
    COALESCE(SUM(DISTINCT CASE WHEN r.status = 'CONFIRMED' THEN r.total_amount ELSE 0 END), 0.00) AS total_revenue,
    COUNT(DISTINCT c.checkin_id) AS checked_in_count,
    ROUND(
        CASE 
            WHEN COUNT(DISTINCT CASE WHEN r.status = 'CONFIRMED' THEN t.ticket_id END) > 0 
            THEN (COUNT(DISTINCT c.checkin_id) * 100.0) / COUNT(DISTINCT CASE WHEN r.status = 'CONFIRMED' THEN t.ticket_id END)
            ELSE 0.00 
        END, 2
    ) AS attendance_percentage,
    ROUND(AVG(f.rating), 2) AS average_rating
FROM events e
JOIN users u ON e.organizer_id = u.user_id
JOIN venues v ON e.venue_id = v.venue_id
LEFT JOIN ticket_tiers tt ON e.event_id = tt.event_id
LEFT JOIN registrations r ON e.event_id = r.event_id
LEFT JOIN tickets t ON r.registration_id = t.registration_id AND t.status = 'ISSUED'
LEFT JOIN checkins c ON t.ticket_id = c.ticket_id AND c.status = 'SUCCESS'
LEFT JOIN feedback f ON e.event_id = f.event_id
GROUP BY 
    e.event_id, 
    e.title, 
    e.category, 
    e.event_date, 
    e.status, 
    u.name, 
    v.name, 
    v.city;
