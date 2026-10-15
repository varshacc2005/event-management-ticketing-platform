# Database Implementation & Assessment 2 Guide
## Event Management & Ticketing Platform

> **Module:** Assessment 2 — SQL Database Checkpoint  
> **Target Date:** October 14–16, 2026 (Completed)  
> **Final Assessment Date:** October 24, 2026  
> **Target RDBMS:** MySQL 8.0+ (InnoDB Storage Engine)  

---

## 1. Project & Database Overview

This database module models the full lifecycle of an **Event Management & Ticketing Platform**, transitioning the core business rules and OOP class structures developed in Java (Days 1–7) into a production-grade relational database schema normalized to **Third Normal Form (3NF)**.

### Core Domain Capabilities:
- **User Management:** Multi-role authentication schema supporting `ADMIN`, `ORGANISER`, `ATTENDEE`, and `VOLUNTEER`.
- **Venue & Event Catalog:** Physical venues with seating capacities hosting Conferences, Concerts, Workshops, and Seminars.
- **Tiered Ticketing:** Multi-tier pricing structures (Early Bird, Standard, VIP) with strict seat capacity bounds (`booked_count <= capacity`).
- **Registration Orders & QR Tickets:** Attendee booking orders (max 4 tickets per order) issuing unique scannable QR ticket tokens.
- **Gate Check-In:** Real-time volunteer ticket validation at venue entrances with duplicate scan protection.
- **Waitlist FIFO Engine:** Relational queuing mechanism for attendees when event tiers sell out.
- **Attendee Feedback:** Post-event rating system (1 to 5 stars) with comments.

---

## 2. Deliverables Summary

| File | Purpose | Lines / Rows |
|---|---|---|
| [`schema.sql`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/schema.sql) | DDL script: 9 normalized tables, foreign keys, CHECK/UNIQUE constraints, 2 performance indexes, 1 reporting view. | 9 tables, 1 view, 2 indexes |
| [`seed.sql`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/seed.sql) | DML sample data: Realistic test data satisfying all constraints with at least 20 rows per table. | 20+ rows per main table |
| [`queries.sql`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/queries.sql) | SQL solutions for 10 project business questions, CRUD operations, joins (INNER, LEFT, RIGHT, FULL via UNION), aggregations, and EXPLAIN plans. | 10 Queries + CRUD + EXPLAIN |
| [`SQL_VIVA.md`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/SQL_VIVA.md) | Multi-table transaction tests (COMMIT/ROLLBACK), ACID properties, 4 isolation levels, and 15 viva Q&As. | Comprehensive Guide |
| [`AI_SQL_VALIDATION.md`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/AI_SQL_VALIDATION.md) | Critical evaluation of AI-generated SQL with Cartesian aggregation errors, explanation, and corrected subquery approach. | Full Analysis + Verified Proof |

---

## 3. Database Prerequisites & Setup

### Prerequisites:
- **MySQL Server 8.0+** running locally (Port `3306`).
- Standard InnoDB storage engine enabled (default in MySQL 8.0).
- MySQL Client command-line tool (`mysql.exe`) or MySQL Workbench.

### Execution Order:
To initialize or rebuild the database from scratch, execute the scripts in the following order:

```bash
# Step 1: Create database schema, tables, constraints, indexes, and views
mysql -u root -p < Project/database/schema.sql

# Step 2: Populate tables with verified realistic seed data
mysql -u root -p < Project/database/seed.sql

# Step 3: Run the 10 business queries and EXPLAIN execution plans
mysql -u root -p < Project/database/queries.sql
```

*(On Windows PowerShell, use `cmd /c "mysql -u root -p < Project\database\schema.sql"`)*

---

## 4. Entity Relationships & Schema Design (3NF)

```text
[users] 1 ───────────< N [events] 1 ───────────< N [ticket_tiers]
   │                        │                           │
   │                        │                           │
   │ 1                      │ 1                         │ 1
   ├─────< N [registrations] ┤                           │
   │              │                                     │
   │              │ 1                                   │
   │              └─────────< N [tickets] >─────────────┘
   │                               │
   │                               │ 1
   ├─────< N [checkins] >──────────┘ (1-to-1 scan verification)
   │
   ├─────< N [waitlist] (FIFO Queue for sold out tiers)
   │
   └─────< N [feedback] (1 review per attendee per event)
```

### Table Overview & Primary Keys:

| Table | Primary Key | Foreign Keys | Key Constraints & Rules |
|---|---|---|---|
| `users` | `user_id` | *None* | `UNIQUE(email)`, Role ENUM, `password_hash` |
| `venues` | `venue_id` | *None* | `capacity > 0` |
| `events` | `event_id` | `venue_id` -> venues, `organizer_id` -> users | Category ENUM, Status ENUM |
| `ticket_tiers` | `tier_id` | `event_id` -> events | `UNIQUE(event_id, tier_name)`, `booked_count <= capacity`, `price >= 0` |
| `registrations` | `registration_id` | `event_id`, `attendee_id`, `tier_id` | `quantity BETWEEN 1 AND 4`, `total_amount >= 0` |
| `tickets` | `ticket_id` | `registration_id`, `event_id`, `attendee_id`, `tier_id` | `UNIQUE(ticket_code)` |
| `checkins` | `checkin_id` | `ticket_id`, `volunteer_id` | `UNIQUE(ticket_id)` (prevents double check-in) |
| `waitlist` | `waitlist_id` | `event_id`, `attendee_id`, `tier_id` | Status ENUM (WAITING, PROMOTED, etc.) |
| `feedback` | `feedback_id` | `event_id`, `attendee_id` | `UNIQUE(event_id, attendee_id)`, `rating BETWEEN 1 AND 5` |

### Normalization Justification (1NF, 2NF, 3NF):
1. **1NF (First Normal Form):** Every table attribute holds atomic values. No repeating groups (e.g., ticket codes are not stored as comma-separated strings inside `registrations`; each has a dedicated row in `tickets`).
2. **2NF (Second Normal Form):** The schema is in 1NF, and all non-key columns depend entirely on their table's primary key (no partial dependencies on composite keys).
3. **3NF (Third Normal Form):** The schema is in 2NF, and there are no transitive dependencies. For example, venue details (`city`, `address`, `capacity`) are not stored inside the `events` table; they reside in `venues` referenced by `venue_id`. Attendee details reside in `users`, preventing redundancy across repeated registrations.

---

## 5. Seed Data Row Counts

Every main table contains at least 20 verified rows:

```text
+---------------+-----------+
| table_name    | row_count |
+---------------+-----------+
| users         |        28 |
| venues        |        20 |
| events        |        20 |
| ticket_tiers  |        25 |
| registrations |        26 |
| tickets       |        27 |
| checkins      |        21 |
| waitlist      |        21 |
| feedback      |        22 |
+---------------+-----------+
```

---

## 6. The 10 Business Queries (Quick Reference)

All queries are implemented with comments and execution tests in [`queries.sql`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/queries.sql):

1. **Q1: Upcoming Published Events:** Filtered by `status = 'PUBLISHED'` and `event_date >= NOW()`, sorted chronologically (`ORDER BY`, `LIMIT`).
2. **Q2: Filter Events by Category & Keyword:** Uses `category IN ('CONFERENCE', 'WORKSHOP')` and `title LIKE '%...'`.
3. **Q3: Ticket Tiers in Price Range:** Uses `BETWEEN 500.00 AND 2000.00` to list tickets within attendee budgets.
4. **Q4: Events with Organizer Name:** 3-table `INNER JOIN` across `events`, `users`, and `venues`.
5. **Q5: Events & Registrations Audit (Including 0-booking events):** Implements `LEFT JOIN` and MySQL `FULL OUTER JOIN` equivalent using `UNION`.
6. **Q6: Attendee Manifest for Event EVT-201:** 4-table `INNER JOIN` displaying attendee profiles, ticket codes, and pricing.
7. **Q7: Total Registrations Breakdown:** `GROUP BY`, `COUNT`, and conditional aggregation (`SUM(CASE WHEN status = 'CONFIRMED'...)`).
8. **Q8: Ticket Revenue & Price Analytics:** Aggregates `SUM(price_paid)`, `AVG`, `MIN`, and `MAX` per event.
9. **Q9: High-Demand Events via HAVING:** Filters events with `HAVING COUNT(r.registration_id) >= 2 AND SUM(r.total_amount) >= 2000.00`.
10. **Q10: Gate Attendance Turnout Report:** Queries `vw_event_performance_summary` comparing sold tickets against verified check-ins.

---

## 7. Performance Indexes & EXPLAIN Verification

Two strategic indexes are declared in `schema.sql`:

### Index 1: `idx_events_status_date` on `events(status, event_date)`
- **Query Supported:** Homepage upcoming events search (Question 1).
- **EXPLAIN Output:**
  ```text
  table: events | type: range | key: idx_events_status_date | Extra: Using where; Using index
  ```
- **Conclusion:** Facilitates index-range scans and eliminates temporary table filesorts.

### Index 2: `idx_registrations_event_status` on `registrations(event_id, status)`
- **Query Supported:** Event financial reporting and capacity tracking (Questions 7, 8, 9).
- **EXPLAIN Output:**
  ```text
  table: registrations | type: ref | key: idx_registrations_event_status | key_len: 83 | ref: const,const
  ```
- **Conclusion:** Replaces full table scans with constant-time indexed reference lookups (`type: ref`).

---

## 8. Multi-Table Transactions (COMMIT & ROLLBACK)

Tested and documented in [`SQL_VIVA.md`](file:///c:/Users/Lenovo/Desktop/event-management-ticketing-platform/Project/database/SQL_VIVA.md):
- **COMMIT Flow:** Acquires exclusive row lock on tier (`SELECT ... FOR UPDATE`), inserts `registrations`, inserts `tickets`, increments `booked_count`, and commits cleanly.
- **ROLLBACK Flow:** Inserts registration and ticket, encounters failure/cancellation, issues `ROLLBACK`, leaving the database in its exact pre-transaction state (`0` residual rows).

---

## 9. Known Limitations & Remaining Tasks (Days 11+)

1. **Current State:** The database layer (`Project/database/*.sql`) is fully implemented, verified on MySQL 8.0, and independent.
2. **Java Application Connection:** The Java application (Module 1 in `Project/src/com/eventplatform/`) currently uses an in-memory collection engine. Connecting Java to MySQL using JDBC (or JPA/Hibernate) is scheduled for Module 2/Assessment 3.
3. **Assessment 2 Preparation:** Review the viva cheat sheet in `SQL_VIVA.md` and the AI query debugging walkthrough in `AI_SQL_VALIDATION.md` prior to October 24, 2026.
