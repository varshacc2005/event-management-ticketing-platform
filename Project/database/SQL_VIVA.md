# SQL Viva & Architecture Reference Guide

**Project:** Event Management & Ticketing Platform  
**Target:** Assessment 2 — SQL Viva Examination (October 24, 2026)  
**Student Level:** 4th Year B.Tech Information Technology  
**RDBMS:** MySQL 8.0 (InnoDB Storage Engine)  

---

## 1. Multi-Table Transactions: COMMIT and ROLLBACK

In an Event Ticketing Platform, registering an attendee involves multiple table mutations that **must succeed or fail as a single atomic unit**.

### Scenario A: Successful Registration and Ticket Allocation (COMMIT)

```sql
USE event_platform_db;

START TRANSACTION;

-- Step 1: Check capacity with pessimistic locking (prevents race conditions)
SELECT capacity, booked_count 
FROM ticket_tiers 
WHERE tier_id = 'TTR-308' 
FOR UPDATE;

-- Step 2: Record the order in registrations table
INSERT INTO registrations (registration_id, event_id, attendee_id, tier_id, quantity, total_amount, status, registered_at)
VALUES ('REG-TXN-01', 'EVT-203', 'USR-1010', 'TTR-308', 1, 1299.00, 'CONFIRMED', NOW());

-- Step 3: Issue the digital QR ticket
INSERT INTO tickets (ticket_id, registration_id, event_id, attendee_id, tier_id, ticket_code, price_paid, status, issued_at)
VALUES ('TKT-TXN-01', 'REG-TXN-01', 'EVT-203', 'USR-1010', 'TTR-308', 'QR-TXN-01-CODE', 1299.00, 'ISSUED', NOW());

-- Step 4: Increment booked count on the tier
UPDATE ticket_tiers 
SET booked_count = booked_count + 1 
WHERE tier_id = 'TTR-308';

-- Step 5: Persist all changes permanently
COMMIT;
```

---

### Scenario B: Payment Failure / Sold Out Abort (ROLLBACK)

```sql
USE event_platform_db;

START TRANSACTION;

-- Step 1: Attempt registration insert
INSERT INTO registrations (registration_id, event_id, attendee_id, tier_id, quantity, total_amount, status, registered_at)
VALUES ('REG-TXN-FAIL', 'EVT-203', 'USR-1011', 'TTR-308', 1, 1299.00, 'CONFIRMED', NOW());

-- Step 2: Attempt ticket generation
INSERT INTO tickets (ticket_id, registration_id, event_id, attendee_id, tier_id, ticket_code, price_paid, status, issued_at)
VALUES ('TKT-TXN-FAIL', 'REG-TXN-FAIL', 'EVT-203', 'USR-1011', 'TTR-308', 'QR-ROLLBACK-CODE', 1299.00, 'ISSUED', NOW());

-- Step 3: Gate check triggers failure (e.g., payment gateway timeout or card decline)
-- ROLLBACK cleanly discards all uncommitted modifications
ROLLBACK;

-- Verification check: Both tables remain 100% clean
SELECT COUNT(*) FROM registrations WHERE registration_id = 'REG-TXN-FAIL'; -- Returns 0
SELECT COUNT(*) FROM tickets WHERE ticket_id = 'TKT-TXN-FAIL';             -- Returns 0
```

---

## 2. ACID Properties Explained with Platform Examples

| Property | Meaning | Event Ticketing Example |
|---|---|---|
| **A — Atomicity** | "All or nothing." Either every operation in the transaction succeeds, or the entire set is undone. | When booking a ticket, if money is deducted and registration is recorded, but the ticket generator crashes before saving the ticket, the whole transaction rolls back so the attendee is not charged for an empty booking. |
| **C — Consistency** | The database transitions from one valid state to another, strictly satisfying all constraints (FK, CHECK, UNIQUE). | If `capacity` is 50, a CHECK constraint `(booked_count <= capacity)` guarantees that the transaction cannot commit a 51st ticket. |
| **I — Isolation** | Concurrent transactions execute without interfering with one another or seeing uncommitted dirty data. | If two attendees attempt to buy the last available VIP ticket simultaneously, their transactions run in isolation; only one successfully commits, while the other receives a sold-out error. |
| **D — Durability** | Once a transaction commits, its data survives crashes, power loss, or server restarts (written to redo logs). | Once the confirmation message appears with your ticket code, even if the database server power cord is pulled out 1 second later, your ticket is safely recorded when the server boots back up. |

---

## 3. Transaction Isolation Levels & Ticketing Scenarios

MySQL InnoDB supports four isolation levels defined by the SQL-92 standard:

### 1. READ UNCOMMITTED (Lowest Isolation)
- **Mechanism:** Transactions can read rows that are being modified by other uncommitted transactions (**Dirty Read**).
- **Ticketing Impact:** Attendee A starts booking the last seat, and Attendee B sees "0 seats remaining". Attendee A's payment then fails and rolls back. Attendee B saw a "phantom" lack of seats based on uncommitted work.

### 2. READ COMMITTED
- **Mechanism:** A transaction only sees data committed before the query began. Prevents dirty reads.
- **Ticketing Impact:** If Attendee A queries available seats at 10:00 AM (sees 5), and Attendee B buys 2 tickets and commits at 10:01 AM, Attendee A's second query at 10:02 AM will see 3 seats (**Non-repeatable Read**).

### 3. REPEATABLE READ (MySQL Default)
- **Mechanism:** Consistent snapshot isolation. Any row read during a transaction will return the exact same values throughout that transaction. Prevents dirty reads and non-repeatable reads. InnoDB also prevents phantom reads in range scans using **Next-Key Locks**.
- **Ticketing Impact:** When generating an event sales report, even if tickets are being sold concurrently by thousands of fans, the report reflects a perfectly consistent snapshot of the database at transaction start time.

### 4. SERIALIZABLE (Highest Isolation)
- **Mechanism:** Implements strict two-phase locking. Every plain `SELECT` is implicitly converted to `SELECT ... FOR SHARE`. Completely prevents dirty reads, non-repeatable reads, and phantom reads, at the cost of concurrency throughput.
- **Ticketing Impact:** Ideal for high-stakes flash ticket releases (e.g., Coldplay stadium concert) where exact ticket allocation sequence must be serialized to prevent overbooking.

---

## 4. Top 15 SQL Viva Questions & Crisp Answers

### Q1: What is Third Normal Form (3NF) and how does your project achieve it?
> **Answer:** A schema is in 3NF if it is in 2NF (no partial key dependencies) and has no transitive dependencies (non-key columns depend only on the primary key). In our project:
> - `venues` is separated from `events` so venue address doesn't transitively depend on `event_id`.
> - `users` is separated from `registrations` so attendee profile data doesn't repeat per booking.
> - `ticket_tiers` is separated from `events` so tier capacity is independent of event metadata.

### Q2: Why did you use DECIMAL instead of FLOAT or DOUBLE for ticket prices?
> **Answer:** `FLOAT` and `DOUBLE` use binary floating-point representation, which introduces rounding inaccuracies (e.g. `0.1 + 0.2 = 0.30000000000000004`). In financial transactions like ticket bookings, exact precision is mandatory. `DECIMAL(10, 2)` stores exact fixed-point numerical values.

### Q3: What is the difference between INNER JOIN, LEFT JOIN, and RIGHT JOIN?
> **Answer:**
> - `INNER JOIN`: Returns only matching rows from both tables.
> - `LEFT JOIN`: Returns all rows from the left table and matched rows from the right table (NULL if no match).
> - `RIGHT JOIN`: Returns all rows from the right table and matched rows from the left table.

### Q4: How do you emulate a FULL OUTER JOIN in MySQL?
> **Answer:** MySQL does not have a native `FULL OUTER JOIN` keyword. We emulate it by combining a `LEFT JOIN` and the unmatched rows from a `RIGHT JOIN` (using `WHERE left_table.id IS NULL`) combined via `UNION`. `UNION` eliminates duplicate matching rows.

### Q5: What is the difference between WHERE and HAVING?
> **Answer:** `WHERE` filters individual rows *before* aggregation occurs. `HAVING` filters aggregated groups *after* `GROUP BY` has executed (e.g., `HAVING COUNT(r.registration_id) >= 2`).

### Q6: What is a Database View and why did you create one?
> **Answer:** A View is a stored virtual query that behaves like a table without storing separate data. We created `vw_event_performance_summary` to encapsulate complex multi-table joins across events, venues, organizers, tickets, checkins, and feedback into a clean, reusable interface for reporting dashboards.

### Q7: What indexes did you create and how did you verify them?
> **Answer:**
> 1. `idx_events_status_date` on `events(status, event_date)` to optimize filtering upcoming published events.
> 2. `idx_registrations_event_status` on `registrations(event_id, status)` to speed up booking aggregations.
> We verified them using `EXPLAIN`, which demonstrated `type: ref` and `type: range` lookups.

### Q8: What does EXPLAIN output indicate?
> **Answer:** `EXPLAIN` displays the MySQL query optimizer's execution plan:
> - `type`: Access type (`ALL` = full table scan, `ref` = indexed equality lookup, `range` = index range scan).
> - `possible_keys`: Indexes MySQL considered.
> - `key`: The actual index chosen.
> - `rows`: Estimated number of rows MySQL must examine.
> - `Extra`: Additional details like `Using index` (covering index) or `Using filesort` (disk/memory sorting).

### Q9: Why is `ON DELETE CASCADE` used on tickets but `ON DELETE RESTRICT` on events?
> **Answer:** If a registration order is deleted, its child tickets are invalid and should cascade delete (`CASCADE`). However, if an event has registered attendees, deleting the event must be forbidden (`RESTRICT`) to preserve historical financial and legal records.

### Q10: How do you prevent ticket overbooking during concurrent purchases?
> **Answer:** Inside a database transaction, we execute `SELECT ... FOR UPDATE` on the target `ticket_tier` row. This acquires an exclusive row-level lock in InnoDB, forcing other simultaneous buyers to wait until the current transaction commits or rolls back, verifying `booked_count < capacity`.

### Q11: How does your database match your Java application design?
> **Answer:**
> - Java `AuthUser` roles (`ADMIN`, `ORGANISER`, `ATTENDEE`, `VOLUNTEER`) match the MySQL `users.role` ENUM.
> - Java `Event` class hierarchy maps to `events` table with `category` ENUM.
> - Java `TicketTier` maps to `ticket_tiers` with `capacity` and `booked_count`.
> - Java FIFO `Queue<WaitlistEntry>` maps to the `waitlist` table sorted by `requested_at`.

### Q12: What is the difference between TRUNCATE and DELETE?
> **Answer:** `DELETE` is a DML statement that removes rows one by one, logs each row removal, and fires triggers. `TRUNCATE` is a DDL statement that deallocates the table's data pages, resets auto-increment counters, runs much faster, and cannot be rolled back in some configurations.

### Q13: What is the difference between UNIQUE and PRIMARY KEY?
> **Answer:** A table can have only one `PRIMARY KEY`, which cannot contain `NULL` values. A table can have multiple `UNIQUE` constraints, which ensure all non-null values in the column or composite columns are distinct.

### Q14: What is a composite / compound key?
> **Answer:** A key made of two or more columns combined together. In our `ticket_tiers` table, `(event_id, tier_name)` has a composite UNIQUE constraint so an event cannot have two tiers with the exact same name.

### Q15: Why is password stored as a SHA2 hash in users table?
> **Answer:** Storing passwords in plain text violates data security standards (OWASP, GDPR). Storing a cryptographic hash (`SHA2('password', 256)`) ensures that even if database records are exposed, raw passwords cannot be reverse-engineered.
