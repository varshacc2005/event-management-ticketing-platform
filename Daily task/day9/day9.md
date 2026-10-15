# Day 9: Business Queries, Advanced Joins, Aggregation, Views & Index Tuning
**Date:** October 15, 2026  
**Module:** Assessment 2 — SQL Database Checkpoint  
**Status:** Completed and Verified on MySQL 8.0  

---

## 1. Today's Objective
Author, format, and execute SQL solutions for 10 project business questions covering CRUD operations, filtering (`WHERE`, `IN`, `BETWEEN`, `LIKE`, `LIMIT`), table joins (`INNER`, `LEFT`, `RIGHT`, `FULL JOIN` emulation via `UNION`), grouping and aggregations (`COUNT`, `SUM`, `AVG`, `HAVING`), view queries, and performance indexing verified via `EXPLAIN`.

---

## 2. Work Completed

1. **CRUD Operations:**
   - Implemented standard `INSERT`, `SELECT`, `UPDATE`, and `DELETE` on user registration and test waitlist entries to confirm full DML compliance.

2. **10 Project Business Queries:**
   - **Q1:** Chronological upcoming published events (`WHERE`, `ORDER BY`, `LIMIT`).
   - **Q2:** Filter events by category and title keyword search (`IN`, `LIKE`).
   - **Q3:** Ticket tiers filtered by price range (`BETWEEN 500 AND 2000`).
   - **Q4:** Active events joined with host organizer details (3-table `INNER JOIN`).
   - **Q5:** All events and registrations audit including zero-booking events (`LEFT JOIN` and MySQL `FULL OUTER JOIN` emulation using `UNION`).
   - **Q6:** Attendee manifest for flagship event `EVT-201` (4-table `INNER JOIN` displaying ticket tokens and payment details).
   - **Q7:** Registration order counts and status breakdown per event (`GROUP BY`, `COUNT`, conditional `SUM(CASE WHEN...)`).
   - **Q8:** Financial revenue audit per event (`SUM(price_paid)`, `AVG`, `MIN`, `MAX`).
   - **Q9:** High-demand events meeting threshold criteria (`GROUP BY` with `HAVING COUNT >= 2 AND SUM >= 2000`).
   - **Q10:** Real-time gate attendance and turnout percentage query using the `vw_event_performance_summary` view.

3. **Performance Indexing & EXPLAIN Plans:**
   - Evaluated `idx_events_status_date` on `events(status, event_date)`: `EXPLAIN` verified index range scan (`type: range`, `key: idx_events_status_date`, `Extra: Using where; Using index`).
   - Evaluated `idx_registrations_event_status` on `registrations(event_id, status)`: `EXPLAIN` verified index lookup (`type: ref`, `key: idx_registrations_event_status`, `key_len: 83`).

---

## 3. Files Created / Modified
- `Project/database/queries.sql`: Complete query script with 10 business queries, CRUD section, FULL JOIN emulation, and EXPLAIN statements.

---

## 4. Verification & Execution Results
All queries were executed directly against MySQL 8.0. Every query executed cleanly with 0 errors:
- Correct row outputs matched business expectations.
- Emulated FULL OUTER JOIN using `UNION` combined unmatched rows without duplicate counting.
- Index execution plans confirmed `type: range` and `type: ref` usage.

---

## 5. Next Steps for Day 10
Implement multi-table ACID transactions demonstrating `COMMIT` and `ROLLBACK`, draft comprehensive viva documentation on ACID properties and isolation levels (`SQL_VIVA.md`), create the AI SQL validation document (`AI_SQL_VALIDATION.md`), and prepare the master database `README.md`.
