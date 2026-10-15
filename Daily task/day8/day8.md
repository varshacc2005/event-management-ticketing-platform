# Day 8: Database Design, Schema Definition & Seed Data Implementation
**Date:** October 14, 2026  
**Module:** Assessment 2 — SQL Database Checkpoint  
**Status:** Completed and Verified on MySQL 8.0  

---

## 1. Today's Objective
Transition the in-memory domain models and business rules from Days 1–7 into a production-grade relational database schema normalized to 3NF, apply constraints and foreign keys, create a comprehensive reporting view, and populate every main table with at least 20 realistic rows.

---

## 2. Work Completed

1. **Entity Identification & Relational Modeling (3NF):**
   - Mapped Java entities to 9 relational tables: `users`, `venues`, `events`, `ticket_tiers`, `registrations`, `tickets`, `checkins`, `waitlist`, and `feedback`.
   - Guaranteed Third Normal Form (3NF) by isolating venue location details from events, extracting tier metadata from events, and separating registrations from issued individual QR tickets.
   - Enforced constraints: Primary Keys, Foreign Keys with appropriate `ON UPDATE CASCADE` / `ON DELETE RESTRICT` / `CASCADE` actions, `UNIQUE` constraints (e.g. unique email, composite `(event_id, tier_name)`, unique ticket codes, and unique check-ins per ticket), and `CHECK` constraints on price, capacity, and ratings.

2. **Reporting View:**
   - Designed and created `vw_event_performance_summary` in `Project/database/schema.sql` consolidating event metadata, capacity, ticket sales, revenue, volunteer check-ins, attendance percentage, and average feedback rating.

3. **Database Seed Data Generation (`seed.sql`):**
   - Populated every main table with realistic, foreign-key-consistent data.
   - Inserted at least 20 rows per table (28 users across 4 roles, 20 venues, 20 events across 4 categories and statuses, 25 ticket tiers, 26 registrations, 27 tickets, 21 checkins, 21 waitlist entries, and 22 feedback reviews).

---

## 3. Files Created / Modified
- `Project/database/schema.sql`: Complete DDL schema script with 9 tables, constraints, indexes, and reporting view.
- `Project/database/seed.sql`: Data insertion script with 20+ rows per main table.

---

## 4. Verification & Test Execution Results

Executed against local MySQL 8.0 Server (`MySQL80` service on port 3306):

| Table Name | Target Minimum Rows | Actual Rows Populated | Foreign Key Integrity | Status |
|---|---|---|---|---|
| `users` | 20 | 28 | N/A (Root table) | PASS |
| `venues` | 20 | 20 | N/A (Root table) | PASS |
| `events` | 20 | 20 | Verified (`venue_id`, `organizer_id`) | PASS |
| `ticket_tiers` | 20 | 25 | Verified (`event_id`) | PASS |
| `registrations` | 20 | 26 | Verified (`event_id`, `attendee_id`, `tier_id`) | PASS |
| `tickets` | 20 | 27 | Verified (`registration_id`, `event_id`, `tier_id`) | PASS |
| `checkins` | 20 | 21 | Verified (`ticket_id`, `volunteer_id`) | PASS |
| `waitlist` | 20 | 21 | Verified (`event_id`, `attendee_id`, `tier_id`) | PASS |
| `feedback` | 20 | 22 | Verified (`event_id`, `attendee_id`) | PASS |

**Reporting View Test:**
Queried `vw_event_performance_summary` successfully returning aggregated ticket counts, revenue, and attendance percentages.

---

## 5. Next Steps for Day 9
Implement the 10 business SQL queries, demonstrate all required join types (including FULL OUTER JOIN emulation using UNION), write aggregation queries with `HAVING`, and analyze execution plans using `EXPLAIN`.
