-- ============================================================================
-- EVENT MANAGEMENT & TICKETING PLATFORM
-- Business Queries, Joins, Aggregation & Analysis (queries.sql)
-- Target RDBMS: MySQL 8.0+
-- Assessment 2 SQL Checkpoint | Day 9 & Day 10
-- ============================================================================

USE event_platform_db;

-- ============================================================================
-- SECTION 1: CRUD OPERATIONS (INSERT, UPDATE, DELETE, SELECT)
-- Demonstrating basic Data Manipulation Language (DML) capabilities
-- ============================================================================

-- 1.1 INSERT: Register a new attendee user into the system
INSERT INTO users (user_id, name, email, password_hash, role, city, created_at)
VALUES ('USR-1029', 'Rohit Sharma', 'rohit.sharma@crickettech.in', SHA2('RohitPass999!', 256), 'ATTENDEE', 'Mumbai', NOW());

-- 1.2 SELECT: Retrieve the newly registered user profile
SELECT user_id, name, email, role, city, created_at
FROM users
WHERE user_id = 'USR-1029';

-- 1.3 UPDATE: Update attendee's preferred location
UPDATE users
SET city = 'Navi Mumbai'
WHERE user_id = 'USR-1029';

-- 1.4 DELETE: Remove a test waitlist record safely
INSERT INTO waitlist (waitlist_id, event_id, attendee_id, tier_id, status)
VALUES ('WTL-TEMP-99', 'EVT-201', 'USR-1029', 'TTR-301', 'CANCELLED');

DELETE FROM waitlist
WHERE waitlist_id = 'WTL-TEMP-99';

-- Clean up test user to preserve reference data cleanliness
DELETE FROM users
WHERE user_id = 'USR-1029';


-- ============================================================================
-- SECTION 2: 10 PROJECT BUSINESS QUERIES
-- Answering core operational and managerial questions for the platform
-- ============================================================================

-- ----------------------------------------------------------------------------
-- QUESTION 1: Upcoming Published Events
-- Business Purpose: Organizers and platform admins need to list upcoming active
-- events sorted chronologically so attendees see immediate schedules.
-- Concepts Used: SELECT, WHERE, ORDER BY, LIMIT
-- ----------------------------------------------------------------------------
SELECT 
    event_id,
    title AS event_title,
    category,
    event_date,
    status
FROM events
WHERE status = 'PUBLISHED'
  AND event_date >= '2026-10-01 00:00:00'
ORDER BY event_date ASC
LIMIT 10;


-- ----------------------------------------------------------------------------
-- QUESTION 2: Filter Events by Category and Title Keyword
-- Business Purpose: Event discovery search allowing users to find technical
-- conferences or developer workshops matching search keywords.
-- Concepts Used: WHERE, IN, LIKE, ORDER BY
-- ----------------------------------------------------------------------------
SELECT 
    event_id,
    title,
    category,
    event_date,
    status
FROM events
WHERE category IN ('CONFERENCE', 'WORKSHOP')
  AND (title LIKE '%DevOps%' OR title LIKE '%Java%' OR title LIKE '%Web%' OR title LIKE '%Kubernetes%')
ORDER BY category, event_date ASC;


-- ----------------------------------------------------------------------------
-- QUESTION 3: Ticket Tiers Within Specified Price Range
-- Business Purpose: Help budget-conscious attendees browse available tickets
-- priced between $500 and $2,000 across active events.
-- Concepts Used: WHERE, BETWEEN, ORDER BY
-- ----------------------------------------------------------------------------
SELECT 
    tt.tier_id,
    e.title AS event_title,
    tt.tier_name,
    tt.price,
    tt.capacity,
    tt.booked_count,
    (tt.capacity - tt.booked_count) AS available_seats
FROM ticket_tiers tt
JOIN events e ON tt.event_id = e.event_id
WHERE tt.price BETWEEN 500.00 AND 2000.00
  AND e.status = 'PUBLISHED'
ORDER BY tt.price ASC;


-- ----------------------------------------------------------------------------
-- QUESTION 4: Event Details with Host Organizer Information
-- Business Purpose: Platform administrative catalog listing every event
-- alongside its verified hosting organization and contact email.
-- Concepts Used: INNER JOIN across 3 tables (events, users, venues)
-- ----------------------------------------------------------------------------
SELECT 
    e.event_id,
    e.title AS event_title,
    e.category,
    e.event_date,
    u.name AS organizer_name,
    u.email AS organizer_email,
    u.organization_name,
    v.name AS venue_name,
    v.city AS venue_city
FROM events e
INNER JOIN users u ON e.organizer_id = u.user_id
INNER JOIN venues v ON e.venue_id = v.venue_id
WHERE e.status = 'PUBLISHED'
ORDER BY e.event_date ASC;


-- ----------------------------------------------------------------------------
-- QUESTION 5: Events and Bookings Audit (Including Zero-Booking Events)
-- Business Purpose: Identify events that have not received any registrations yet,
-- as well as all active registrations, demonstrating comprehensive join mechanics.
-- Concepts Used: LEFT JOIN, RIGHT JOIN, and MySQL FULL OUTER JOIN emulation using UNION
-- ----------------------------------------------------------------------------

-- Part A: Standard LEFT JOIN (Show all events even if registrations = 0)
SELECT 
    e.event_id,
    e.title AS event_title,
    e.status AS event_status,
    COALESCE(r.registration_id, 'NO REGISTRATION') AS registration_id,
    COALESCE(r.status, 'N/A') AS booking_status,
    COALESCE(r.total_amount, 0.00) AS booking_amount
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id
ORDER BY e.event_id ASC, r.registration_id ASC;

-- Part B: Emulate FULL OUTER JOIN using UNION in MySQL 8.x
-- Combines all events (with or without bookings) and any standalone bookings without duplicates
SELECT 
    e.event_id,
    e.title AS event_title,
    r.registration_id,
    r.attendee_id,
    r.status AS registration_status,
    r.total_amount
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id

UNION

SELECT 
    e.event_id,
    e.title AS event_title,
    r.registration_id,
    r.attendee_id,
    r.status AS registration_status,
    r.total_amount
FROM events e
RIGHT JOIN registrations r ON e.event_id = r.event_id
WHERE e.event_id IS NULL;


-- ----------------------------------------------------------------------------
-- QUESTION 6: Attendee Manifest for a Specific Event
-- Business Purpose: Generate an attendee manifest for 'EVT-201' (Cloud Native Summit)
-- showing user details, tier purchased, ticket code, and issue status.
-- Concepts Used: INNER JOIN across 4 tables (events, registrations, tickets, users)
-- ----------------------------------------------------------------------------
SELECT 
    e.event_id,
    e.title AS event_title,
    u.name AS attendee_name,
    u.email AS attendee_email,
    u.city AS attendee_city,
    tt.tier_name,
    t.ticket_code,
    t.price_paid,
    t.status AS ticket_status,
    t.issued_at
FROM tickets t
INNER JOIN registrations r ON t.registration_id = r.registration_id
INNER JOIN events e ON t.event_id = e.event_id
INNER JOIN users u ON t.attendee_id = u.user_id
INNER JOIN ticket_tiers tt ON t.tier_id = tt.tier_id
WHERE e.event_id = 'EVT-201'
  AND r.status = 'CONFIRMED'
ORDER BY t.issued_at ASC;


-- ----------------------------------------------------------------------------
-- QUESTION 7: Registration Counts and Status Breakdown per Event
-- Business Purpose: Executive summary of orders per event, distinguishing
-- confirmed, cancelled, and pending orders.
-- Concepts Used: GROUP BY, COUNT, SUM with conditional CASE WHEN
-- ----------------------------------------------------------------------------
SELECT 
    e.event_id,
    e.title AS event_title,
    COUNT(r.registration_id) AS total_orders_placed,
    SUM(CASE WHEN r.status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_orders,
    SUM(CASE WHEN r.status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_orders,
    SUM(CASE WHEN r.status = 'PENDING' THEN 1 ELSE 0 END) AS pending_orders,
    COALESCE(SUM(CASE WHEN r.status = 'CONFIRMED' THEN r.quantity ELSE 0 END), 0) AS total_confirmed_tickets
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id
GROUP BY e.event_id, e.title
ORDER BY total_confirmed_tickets DESC, e.event_id ASC;


-- ----------------------------------------------------------------------------
-- QUESTION 8: Revenue and Pricing Analytics per Event
-- Business Purpose: Financial revenue audit showing gross ticket sales,
-- highest tier price, lowest tier price, and average price paid.
-- Concepts Used: GROUP BY, SUM, AVG, MIN, MAX, DECIMAL rounding
-- ----------------------------------------------------------------------------
SELECT 
    e.event_id,
    e.title AS event_title,
    COUNT(t.ticket_id) AS tickets_sold,
    COALESCE(SUM(t.price_paid), 0.00) AS gross_revenue,
    COALESCE(ROUND(AVG(t.price_paid), 2), 0.00) AS average_ticket_price,
    COALESCE(MIN(t.price_paid), 0.00) AS min_ticket_price,
    COALESCE(MAX(t.price_paid), 0.00) AS max_ticket_price
FROM events e
LEFT JOIN tickets t ON e.event_id = t.event_id AND t.status IN ('ISSUED', 'USED')
GROUP BY e.event_id, e.title
ORDER BY gross_revenue DESC;


-- ----------------------------------------------------------------------------
-- QUESTION 9: High-Demand Events Filtered by HAVING Clause
-- Business Purpose: Identify popular events with 2 or more confirmed registrations
-- and gross revenue of at least $2,000 for VIP sponsor allocation.
-- Concepts Used: GROUP BY, COUNT, SUM, HAVING
-- ----------------------------------------------------------------------------
SELECT 
    e.event_id,
    e.title AS event_title,
    e.category,
    COUNT(r.registration_id) AS confirmed_order_count,
    SUM(r.total_amount) AS total_revenue
FROM events e
JOIN registrations r ON e.event_id = r.event_id
WHERE r.status = 'CONFIRMED'
GROUP BY e.event_id, e.title, e.category
HAVING COUNT(r.registration_id) >= 2
   AND SUM(r.total_amount) >= 2000.00
ORDER BY total_revenue DESC;


-- ----------------------------------------------------------------------------
-- QUESTION 10: Event Gate Attendance & Turnout Report
-- Business Purpose: Compare tickets issued against gate check-in scans to measure
-- actual physical turnout percentage for venue safety and crowd management.
-- Concepts Used: Reporting view query, filtering, sorting
-- ----------------------------------------------------------------------------
SELECT 
    event_id,
    event_title,
    category,
    venue_name,
    venue_city,
    confirmed_tickets_sold,
    checked_in_count,
    attendance_percentage,
    average_rating
FROM vw_event_performance_summary
WHERE confirmed_tickets_sold > 0
ORDER BY attendance_percentage DESC, confirmed_tickets_sold DESC;


-- ============================================================================
-- SECTION 3: INDEX JUSTIFICATION AND EXPLAIN EXECUTION PLANS
-- Requirement: At least two useful indexes justified using EXPLAIN
-- ============================================================================

-- ----------------------------------------------------------------------------
-- INDEX 1: idx_events_status_date on events(status, event_date)
-- Justification: The homepage and mobile apps constantly query for upcoming
-- published events sorted by date (Question 1). Without an index, MySQL performs
-- a full table scan and sorts rows in temporary memory (filesort).
-- The compound index (status, event_date) enables an index range scan and provides
-- pre-sorted order, avoiding the filesort entirely.
-- ----------------------------------------------------------------------------
EXPLAIN
SELECT 
    event_id,
    title,
    category,
    event_date
FROM events
WHERE status = 'PUBLISHED'
  AND event_date >= '2026-10-01 00:00:00'
ORDER BY event_date ASC;

-- ----------------------------------------------------------------------------
-- INDEX 2: idx_registrations_event_status on registrations(event_id, status)
-- Justification: Questions 7, 8, 9 and the reporting view repeatedly aggregate
-- and filter bookings by event_id and confirmed status.
-- This composite index provides direct index ref lookups for foreign key joins
-- filtered by status, eliminating full table scans across millions of booking rows.
-- ----------------------------------------------------------------------------
EXPLAIN
SELECT 
    event_id,
    COUNT(*) AS confirmed_bookings,
    SUM(total_amount) AS revenue
FROM registrations
WHERE event_id = 'EVT-201'
  AND status = 'CONFIRMED'
GROUP BY event_id;
