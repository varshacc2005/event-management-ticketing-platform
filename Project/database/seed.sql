-- ============================================================================
-- EVENT MANAGEMENT & TICKETING PLATFORM
-- Database Seed Data Script (seed.sql)
-- Requirement: At least 20 rows per main table
-- Target RDBMS: MySQL 8.0+
-- Assessment 2 SQL Checkpoint | Days 8, 9, 10
-- ============================================================================

USE event_platform_db;

-- Disable foreign key checks during batch reload to prevent insertion order conflicts
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE feedback;
TRUNCATE TABLE checkins;
TRUNCATE TABLE waitlist;
TRUNCATE TABLE tickets;
TRUNCATE TABLE registrations;
TRUNCATE TABLE ticket_tiers;
TRUNCATE TABLE events;
TRUNCATE TABLE venues;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 1. USERS (28 rows: 3 ADMIN, 6 ORGANISER, 15 ATTENDEE, 4 VOLUNTEER)
-- Passwords stored as SHA2 hashes matching authentication security principles
-- ============================================================================
INSERT INTO users (user_id, name, email, password_hash, role, organization_name, city, assigned_area, department, created_at) VALUES
('USR-1001', 'Alexander Wright', 'alex.wright@admin.eventflow.com', SHA2('AdminPass123!', 256), 'ADMIN', NULL, NULL, NULL, 'System Operations', '2026-01-10 09:00:00'),
('USR-1002', 'Beatrice Vance', 'beatrice.vance@admin.eventflow.com', SHA2('AdminPass456!', 256), 'ADMIN', NULL, NULL, NULL, 'Quality & Compliance', '2026-01-11 09:30:00'),
('USR-1003', 'Charles Xavier', 'charles.xavier@admin.eventflow.com', SHA2('AdminPass789!', 256), 'ADMIN', NULL, NULL, NULL, 'Security Audit', '2026-01-12 10:00:00'),

('USR-1004', 'David Miller', 'david.miller@soundwave.io', SHA2('OrgPass123!', 256), 'ORGANISER', 'SoundWave Live Events', NULL, NULL, NULL, '2026-02-01 11:00:00'),
('USR-1005', 'Elena Rostova', 'elena.rostova@techsummit.org', SHA2('OrgPass234!', 256), 'ORGANISER', 'Global Tech Summit Inc', NULL, NULL, NULL, '2026-02-02 11:30:00'),
('USR-1006', 'Farhan Akhtar', 'farhan.akhtar@indiebeats.com', SHA2('OrgPass345!', 256), 'ORGANISER', 'IndieBeats Music Productions', NULL, NULL, NULL, '2026-02-03 12:00:00'),
('USR-1007', 'Grace Hopper', 'grace.hopper@coderscon.edu', SHA2('OrgPass456!', 256), 'ORGANISER', 'DevInnovate Foundation', NULL, NULL, NULL, '2026-02-04 14:00:00'),
('USR-1008', 'Harish Chandra', 'harish.c@craftandai.in', SHA2('OrgPass567!', 256), 'ORGANISER', 'Craft & AI Guild', NULL, NULL, NULL, '2026-02-05 15:30:00'),
('USR-1009', 'Isabella Garcia', 'isabella.g@latinexpo.org', SHA2('OrgPass678!', 256), 'ORGANISER', 'Ibero Cultural Expos', NULL, NULL, NULL, '2026-02-06 16:00:00'),

('USR-1010', 'Aarav Sharma', 'aarav.sharma@gmail.com', SHA2('AttPass101!', 256), 'ATTENDEE', NULL, 'Bengaluru', NULL, NULL, '2026-03-01 10:15:00'),
('USR-1011', 'Bhavna Menon', 'bhavna.menon@outlook.com', SHA2('AttPass102!', 256), 'ATTENDEE', NULL, 'Chennai', NULL, NULL, '2026-03-02 11:20:00'),
('USR-1012', 'Chirag Patil', 'chirag.patil@yahoo.com', SHA2('AttPass103!', 256), 'ATTENDEE', NULL, 'Mumbai', NULL, NULL, '2026-03-03 12:45:00'),
('USR-1013', 'Divya Nair', 'divya.nair@gmail.com', SHA2('AttPass104!', 256), 'ATTENDEE', NULL, 'Kochi', NULL, NULL, '2026-03-04 14:10:00'),
('USR-1014', 'Eshan Verma', 'eshan.verma@gmail.com', SHA2('AttPass105!', 256), 'ATTENDEE', NULL, 'Delhi', NULL, NULL, '2026-03-05 15:00:00'),
('USR-1015', 'Fatima Sheikh', 'fatima.sheikh@hotmail.com', SHA2('AttPass106!', 256), 'ATTENDEE', NULL, 'Hyderabad', NULL, NULL, '2026-03-06 16:30:00'),
('USR-1016', 'Gautam Rao', 'gautam.rao@gmail.com', SHA2('AttPass107!', 256), 'ATTENDEE', NULL, 'Bengaluru', NULL, NULL, '2026-03-07 17:15:00'),
('USR-1017', 'Hina Kausar', 'hina.kausar@yahoo.com', SHA2('AttPass108!', 256), 'ATTENDEE', NULL, 'Pune', NULL, NULL, '2026-03-08 18:00:00'),
('USR-1018', 'Ishaan Gupta', 'ishaan.gupta@gmail.com', SHA2('AttPass109!', 256), 'ATTENDEE', NULL, 'Kolkata', NULL, NULL, '2026-03-09 19:20:00'),
('USR-1019', 'Juhi Chawla', 'juhi.chawla@gmail.com', SHA2('AttPass110!', 256), 'ATTENDEE', NULL, 'Chandigarh', NULL, NULL, '2026-03-10 20:00:00'),
('USR-1020', 'Karan Mehra', 'karan.mehra@outlook.com', SHA2('AttPass111!', 256), 'ATTENDEE', NULL, 'Bengaluru', NULL, NULL, '2026-03-11 10:00:00'),
('USR-1021', 'Lavanya Sundaram', 'lavanya.s@gmail.com', SHA2('AttPass112!', 256), 'ATTENDEE', NULL, 'Coimbatore', NULL, NULL, '2026-03-12 11:30:00'),
('USR-1022', 'Manish Pandey', 'manish.p@gmail.com', SHA2('AttPass113!', 256), 'ATTENDEE', NULL, 'Lucknow', NULL, NULL, '2026-03-13 14:00:00'),
('USR-1023', 'Neha Singhania', 'neha.s@gmail.com', SHA2('AttPass114!', 256), 'ATTENDEE', NULL, 'Jaipur', NULL, NULL, '2026-03-14 15:45:00'),
('USR-1024', 'Omkar Kulkarni', 'omkar.k@gmail.com', SHA2('AttPass115!', 256), 'ATTENDEE', NULL, 'Pune', NULL, NULL, '2026-03-15 16:30:00'),

('USR-1025', 'Pooja Hegde', 'pooja.hegde@volunteers.org', SHA2('VolPass101!', 256), 'VOLUNTEER', NULL, NULL, 'Gate A - VIP Desk', NULL, '2026-03-16 08:00:00'),
('USR-1026', 'Rahul Dravid', 'rahul.dravid@volunteers.org', SHA2('VolPass102!', 256), 'VOLUNTEER', NULL, NULL, 'Gate B - General Entry', NULL, '2026-03-16 08:30:00'),
('USR-1027', 'Sneha Reddy', 'sneha.reddy@volunteers.org', SHA2('VolPass103!', 256), 'VOLUNTEER', NULL, NULL, 'Auditorium Entry North', NULL, '2026-03-16 09:00:00'),
('USR-1028', 'Tariq Anwar', 'tariq.anwar@volunteers.org', SHA2('VolPass104!', 256), 'VOLUNTEER', NULL, NULL, 'Hall C Workshop Desk', NULL, '2026-03-16 09:30:00');

-- ============================================================================
-- 2. VENUES (20 rows)
-- ============================================================================
INSERT INTO venues (venue_id, name, city, address, capacity, created_at) VALUES
('VEN-101', 'Bangalore International Exhibition Centre', 'Bengaluru', '10th Mile, Tumkur Road', 5000, '2026-01-15 10:00:00'),
('VEN-102', 'Jio World Convention Centre', 'Mumbai', 'G Block, Bandra Kurla Complex', 4500, '2026-01-16 10:30:00'),
('VEN-103', 'India Habitat Centre', 'Delhi', 'Lodhi Road, Near Airforce Bal Bharati School', 1200, '2026-01-17 11:00:00'),
('VEN-104', 'Chennai Trade Centre', 'Chennai', 'Nandambakkam, Mount Poonamallee Road', 2500, '2026-01-18 11:30:00'),
('VEN-105', 'Hyderabad International Convention Centre', 'Hyderabad', 'Novotel & HICC Complex, Madhapur', 4000, '2026-01-19 12:00:00'),
('VEN-106', 'Koramangala Indoor Stadium', 'Bengaluru', '80 Feet Road, 8th Block, Koramangala', 3000, '2026-01-20 12:30:00'),
('VEN-107', 'Science City Auditorium', 'Kolkata', 'J.B.S. Haldane Avenue, Mirania Gardens', 2200, '2026-01-21 13:00:00'),
('VEN-108', 'Pune Trade Center', 'Pune', 'Moshi, Alandi Road', 1800, '2026-01-22 13:30:00'),
('VEN-109', 'NIMHANS Convention Centre', 'Bengaluru', 'Hosur Road, Lakkasandra', 1000, '2026-01-23 14:00:00'),
('VEN-110', 'Birla Auditorium', 'Jaipur', 'Statue Circle, Ashok Nagar', 1300, '2026-01-24 14:30:00'),
('VEN-111', 'Lalit Ashok Expo Hall', 'Bengaluru', 'Kumara Park High Grounds', 900, '2026-01-25 15:00:00'),
('VEN-112', 'Taj Krishna Grand Ballroom', 'Hyderabad', 'Road No 1, Banjara Hills', 800, '2026-01-26 15:30:00'),
('VEN-113', 'Leela Palace Grand Hall', 'Chennai', 'Adyar Seaface, MRC Nagar', 750, '2026-01-27 16:00:00'),
('VEN-114', 'Manpho Convention Centre', 'Bengaluru', 'Nagavara, Outer Ring Road', 2800, '2026-01-28 16:30:00'),
('VEN-115', 'YMCA Grounds Nandanam', 'Chennai', 'No 497, Anna Salai', 6000, '2026-01-29 17:00:00'),
('VEN-116', 'Indira Gandhi Indoor Stadium', 'Delhi', 'Indraprastha Estate, Near Raj Ghat', 12000, '2026-01-30 17:30:00'),
('VEN-117', 'Ravindra Bharathi Auditorium', 'Hyderabad', 'Saifabad, Lakdikapul', 1100, '2026-01-31 18:00:00'),
('VEN-118', 'Chowdiah Memorial Hall', 'Bengaluru', '16th Cross, Malleswaram', 1050, '2026-02-01 09:00:00'),
('VEN-119', 'Shanmukhananda Hall', 'Mumbai', 'Flank Road, Sion East', 2700, '2026-02-02 09:30:00'),
('VEN-120', 'Cochin International Marina Hall', 'Kochi', 'Bolgatty Island', 1500, '2026-02-03 10:00:00');

-- ============================================================================
-- 3. EVENTS (20 rows: CONCERT, CONFERENCE, WORKSHOP, SEMINAR)
-- Includes historical completed, upcoming published, draft, and cancelled events
-- ============================================================================
INSERT INTO events (event_id, title, description, category, event_date, venue_id, organizer_id, status, created_at) VALUES
('EVT-201', 'Cloud Native & DevOps Summit 2026', 'Premier enterprise conference on Kubernetes, microservices and CI/CD automation.', 'CONFERENCE', '2026-11-15 09:00:00', 'VEN-101', 'USR-1005', 'PUBLISHED', '2026-03-01 10:00:00'),
('EVT-202', 'Electric Rhythms Music Festival', 'Open air electronic music experience with international synth artists.', 'CONCERT', '2026-11-20 18:00:00', 'VEN-115', 'USR-1004', 'PUBLISHED', '2026-03-02 11:00:00'),
('EVT-203', 'Practical Microservices in Java Workshop', 'Deep dive code lab on Spring Boot, gRPC and distributed tracing.', 'WORKSHOP', '2026-11-25 10:00:00', 'VEN-109', 'USR-1007', 'PUBLISHED', '2026-03-03 12:00:00'),
('EVT-204', 'Generative AI & LLM Systems Seminar', 'Executive keynote and panel discussions on enterprise LLM deployment.', 'SEMINAR', '2026-11-28 14:00:00', 'VEN-103', 'USR-1008', 'PUBLISHED', '2026-03-04 13:00:00'),
('EVT-205', 'Acoustic Unplugged Night Bangalore', 'Intimate live concert featuring celebrated indie-folk performers.', 'CONCERT', '2026-12-05 19:00:00', 'VEN-118', 'USR-1006', 'PUBLISHED', '2026-03-05 14:00:00'),
('EVT-206', 'Global Full-Stack Web Con 2026', 'Two-day technical conference on TypeScript, React 19, and Next.js.', 'CONFERENCE', '2026-12-10 09:30:00', 'VEN-102', 'USR-1005', 'PUBLISHED', '2026-03-06 15:00:00'),
('EVT-207', 'Database Performance Tuning Masterclass', 'Hands-on indexing, query execution optimization, and MySQL 8 internals.', 'WORKSHOP', '2026-12-15 10:00:00', 'VEN-104', 'USR-1008', 'PUBLISHED', '2026-03-07 16:00:00'),
('EVT-208', 'Cybersecurity Defense Strategies 2026', 'Zero trust architecture, threat intelligence, and compliance audit seminar.', 'SEMINAR', '2026-12-20 11:00:00', 'VEN-105', 'USR-1007', 'PUBLISHED', '2026-03-08 17:00:00'),
('EVT-209', 'Fusion Jazz Night Live in Mumbai', 'Electrifying instrumental jazz fusion with saxophone maestros.', 'CONCERT', '2026-12-28 20:00:00', 'VEN-119', 'USR-1004', 'PUBLISHED', '2026-03-09 18:00:00'),
('EVT-210', 'Fintech Disrupt Summit Hyderabad', 'Banking API modernization, UPI protocols, and decentralized ledgers.', 'CONFERENCE', '2027-01-10 09:00:00', 'VEN-112', 'USR-1005', 'PUBLISHED', '2026-03-10 19:00:00'),
('EVT-211', 'Docker & Kubernetes Deep Dive', 'Full day container orchestration bootcamp with hands-on cluster setups.', 'WORKSHOP', '2027-01-18 10:00:00', 'VEN-108', 'USR-1007', 'PUBLISHED', '2026-03-11 09:00:00'),
('EVT-212', 'Future of Clean Mobility Seminar', 'Electric battery management and autonomous vehicle systems seminar.', 'SEMINAR', '2027-01-22 14:30:00', 'VEN-107', 'USR-1008', 'PUBLISHED', '2026-03-12 10:00:00'),
('EVT-213', 'Symphonic Rock Odyssey Delhi', 'Classical orchestral ensemble performing legendary rock anthems.', 'CONCERT', '2027-02-05 18:30:00', 'VEN-116', 'USR-1004', 'PUBLISHED', '2026-03-13 11:00:00'),
('EVT-214', 'Healthcare AI Innovations Expo', 'Clinical decision support systems and medical computer vision.', 'CONFERENCE', '2027-02-12 09:30:00', 'VEN-110', 'USR-1009', 'PUBLISHED', '2026-03-14 12:00:00'),
('EVT-215', 'API Security & OAuth2 Boot Camp', 'Designing robust token auth, rate limiting, and mTLS connections.', 'WORKSHOP', '2027-02-18 10:00:00', 'VEN-111', 'USR-1007', 'PUBLISHED', '2026-03-15 13:00:00'),
('EVT-216', 'Quantum Computing Horizon 2027', 'Introductory seminar on qubit manipulation and quantum error correction.', 'SEMINAR', '2027-02-25 15:00:00', 'VEN-117', 'USR-1008', 'DRAFT', '2026-03-16 14:00:00'),
('EVT-217', 'Kochi Coastal Indie Fest', 'Beachside indie rock and regional music showcase.', 'CONCERT', '2027-03-02 17:00:00', 'VEN-120', 'USR-1006', 'DRAFT', '2026-03-17 15:00:00'),
('EVT-218', 'AI in Retail & E-Commerce', 'Personalization algorithms, demand forecasting, and inventory AI.', 'CONFERENCE', '2027-03-10 10:00:00', 'VEN-114', 'USR-1009', 'CANCELLED', '2026-03-18 16:00:00'),
('EVT-219', 'National Tech Symposium 2026 (Winter)', 'Annual student and industry showcase held earlier this year.', 'CONFERENCE', '2026-02-15 09:00:00', 'VEN-101', 'USR-1005', 'COMPLETED', '2026-01-05 10:00:00'),
('EVT-220', 'Monsoon Classical Ragas Evening', 'Hindustani and Carnatic jugalbandi concert held earlier in the season.', 'CONCERT', '2026-02-28 18:00:00', 'VEN-118', 'USR-1006', 'COMPLETED', '2026-01-10 11:00:00');

-- ============================================================================
-- 4. TICKET TIERS (25 rows)
-- Multi-tier setups across events: VIP, Standard, Early Bird, Student
-- ============================================================================
INSERT INTO ticket_tiers (tier_id, event_id, tier_name, price, capacity, booked_count, early_bird_deadline, created_at) VALUES
('TTR-301', 'EVT-201', 'Early Bird Pass', 1499.00, 100, 100, '2026-09-30 23:59:59', '2026-03-01 10:05:00'),
('TTR-302', 'EVT-201', 'Standard Delegate', 2499.00, 250, 180, NULL, '2026-03-01 10:05:00'),
('TTR-303', 'EVT-201', 'VIP Executive Pass', 4999.00, 50, 50, NULL, '2026-03-01 10:05:00'),

('TTR-304', 'EVT-202', 'General Admission', 799.00, 1000, 650, NULL, '2026-03-02 11:05:00'),
('TTR-305', 'EVT-202', 'VIP Pit Pass', 1999.00, 200, 200, NULL, '2026-03-02 11:05:00'),
('TTR-306', 'EVT-202', 'Early Bird GA', 599.00, 300, 300, '2026-10-15 23:59:59', '2026-03-02 11:05:00'),

('TTR-307', 'EVT-203', 'Student Pass', 499.00, 40, 40, NULL, '2026-03-03 12:05:00'),
('TTR-308', 'EVT-203', 'Professional Lab Pass', 1299.00, 60, 45, NULL, '2026-03-03 12:05:00'),

('TTR-309', 'EVT-204', 'Early Bird Entry', 899.00, 80, 80, '2026-10-31 23:59:59', '2026-03-04 13:05:00'),
('TTR-310', 'EVT-204', 'Standard Admission', 1499.00, 120, 70, NULL, '2026-03-04 13:05:00'),

('TTR-311', 'EVT-205', 'Balcony Seat', 650.00, 150, 90, NULL, '2026-03-05 14:05:00'),
('TTR-312', 'EVT-205', 'Front Row Premium', 1500.00, 50, 50, NULL, '2026-03-05 14:05:00'),

('TTR-313', 'EVT-206', 'Standard Conference', 1999.00, 400, 220, NULL, '2026-03-06 15:05:00'),
('TTR-314', 'EVT-206', 'VIP All Access', 3999.00, 100, 85, NULL, '2026-03-06 15:05:00'),

('TTR-315', 'EVT-207', 'Hands-on Attendee', 1199.00, 50, 50, NULL, '2026-03-07 16:05:00'),

('TTR-316', 'EVT-208', 'Corporate Delegate', 2199.00, 120, 60, NULL, '2026-03-08 17:05:00'),

('TTR-317', 'EVT-209', 'General Seating', 850.00, 300, 140, NULL, '2026-03-09 18:05:00'),
('TTR-318', 'EVT-209', 'VIP Lounge Table', 2200.00, 40, 40, NULL, '2026-03-09 18:05:00'),

('TTR-319', 'EVT-210', 'Standard Delegate', 1799.00, 200, 110, NULL, '2026-03-10 19:05:00'),

('TTR-320', 'EVT-211', 'Developer Ticket', 999.00, 80, 55, NULL, '2026-03-11 09:05:00'),

('TTR-321', 'EVT-212', 'Open Seminar Entry', 0.00, 250, 190, NULL, '2026-03-12 10:05:00'),

('TTR-322', 'EVT-213', 'Grand Arena Pass', 1100.00, 800, 450, NULL, '2026-03-13 11:05:00'),

('TTR-323', 'EVT-214', 'Healthcare Professional', 1899.00, 150, 75, NULL, '2026-03-14 12:05:00'),

('TTR-324', 'EVT-219', 'Completed Standard', 1500.00, 200, 200, NULL, '2026-01-05 10:10:00'),
('TTR-325', 'EVT-220', 'Completed Classical', 750.00, 120, 120, NULL, '2026-01-10 11:10:00');

-- ============================================================================
-- 5. REGISTRATIONS (26 rows)
-- Booking orders with quantities between 1 and 4 tickets per business rule
-- ============================================================================
INSERT INTO registrations (registration_id, event_id, attendee_id, tier_id, quantity, total_amount, status, registered_at) VALUES
('REG-401', 'EVT-201', 'USR-1010', 'TTR-301', 2, 2998.00, 'CONFIRMED', '2026-03-15 10:00:00'),
('REG-402', 'EVT-201', 'USR-1011', 'TTR-302', 1, 2499.00, 'CONFIRMED', '2026-03-15 11:20:00'),
('REG-403', 'EVT-201', 'USR-1012', 'TTR-303', 1, 4999.00, 'CONFIRMED', '2026-03-15 12:40:00'),
('REG-404', 'EVT-202', 'USR-1013', 'TTR-304', 3, 2397.00, 'CONFIRMED', '2026-03-16 09:15:00'),
('REG-405', 'EVT-202', 'USR-1014', 'TTR-305', 2, 3998.00, 'CONFIRMED', '2026-03-16 10:30:00'),
('REG-406', 'EVT-202', 'USR-1015', 'TTR-306', 1, 599.00, 'CONFIRMED', '2026-03-16 11:45:00'),
('REG-407', 'EVT-203', 'USR-1016', 'TTR-307', 1, 499.00, 'CONFIRMED', '2026-03-17 14:00:00'),
('REG-408', 'EVT-203', 'USR-1017', 'TTR-308', 2, 2598.00, 'CONFIRMED', '2026-03-17 15:10:00'),
('REG-409', 'EVT-204', 'USR-1018', 'TTR-309', 1, 899.00, 'CONFIRMED', '2026-03-18 16:00:00'),
('REG-410', 'EVT-204', 'USR-1019', 'TTR-310', 1, 1499.00, 'CONFIRMED', '2026-03-18 16:30:00'),
('REG-411', 'EVT-205', 'USR-1020', 'TTR-311', 2, 1300.00, 'CONFIRMED', '2026-03-19 10:00:00'),
('REG-412', 'EVT-205', 'USR-1021', 'TTR-312', 1, 1500.00, 'CONFIRMED', '2026-03-19 11:00:00'),
('REG-413', 'EVT-206', 'USR-1022', 'TTR-313', 2, 3998.00, 'CONFIRMED', '2026-03-20 12:00:00'),
('REG-414', 'EVT-206', 'USR-1023', 'TTR-314', 1, 3999.00, 'CONFIRMED', '2026-03-20 13:00:00'),
('REG-415', 'EVT-207', 'USR-1024', 'TTR-315', 1, 1199.00, 'CONFIRMED', '2026-03-21 14:00:00'),
('REG-416', 'EVT-208', 'USR-1010', 'TTR-316', 1, 2199.00, 'CONFIRMED', '2026-03-22 15:00:00'),
('REG-417', 'EVT-209', 'USR-1011', 'TTR-317', 2, 1700.00, 'CONFIRMED', '2026-03-23 16:00:00'),
('REG-418', 'EVT-209', 'USR-1012', 'TTR-318', 1, 2200.00, 'CONFIRMED', '2026-03-23 17:00:00'),
('REG-419', 'EVT-210', 'USR-1013', 'TTR-319', 1, 1799.00, 'CONFIRMED', '2026-03-24 10:00:00'),
('REG-420', 'EVT-211', 'USR-1014', 'TTR-320', 1, 999.00, 'CONFIRMED', '2026-03-25 11:00:00'),
('REG-421', 'EVT-212', 'USR-1015', 'TTR-321', 2, 0.00, 'CONFIRMED', '2026-03-26 12:00:00'),
('REG-422', 'EVT-201', 'USR-1014', 'TTR-302', 1, 2499.00, 'CANCELLED', '2026-03-27 13:00:00'),
('REG-423', 'EVT-202', 'USR-1016', 'TTR-304', 2, 1598.00, 'CANCELLED', '2026-03-27 14:00:00'),
('REG-424', 'EVT-203', 'USR-1018', 'TTR-308', 1, 1299.00, 'PENDING', '2026-03-28 15:00:00'),
('REG-425', 'EVT-219', 'USR-1017', 'TTR-324', 2, 3000.00, 'CONFIRMED', '2026-01-20 10:00:00'),
('REG-426', 'EVT-220', 'USR-1018', 'TTR-325', 1, 750.00, 'CONFIRMED', '2026-01-25 11:00:00');

-- ============================================================================
-- 6. TICKETS (27 rows: QR token format matching Java model)
-- ============================================================================
INSERT INTO tickets (ticket_id, registration_id, event_id, attendee_id, tier_id, ticket_code, price_paid, status, issued_at) VALUES
('TKT-501', 'REG-401', 'EVT-201', 'USR-1010', 'TTR-301', 'QR-EVT201-TTR301-501-A', 1499.00, 'ISSUED', '2026-03-15 10:01:00'),
('TKT-502', 'REG-401', 'EVT-201', 'USR-1010', 'TTR-301', 'QR-EVT201-TTR301-502-B', 1499.00, 'ISSUED', '2026-03-15 10:01:00'),
('TKT-503', 'REG-402', 'EVT-201', 'USR-1011', 'TTR-302', 'QR-EVT201-TTR302-503-A', 2499.00, 'ISSUED', '2026-03-15 11:21:00'),
('TKT-504', 'REG-403', 'EVT-201', 'USR-1012', 'TTR-303', 'QR-EVT201-TTR303-504-A', 4999.00, 'ISSUED', '2026-03-15 12:41:00'),
('TKT-505', 'REG-404', 'EVT-202', 'USR-1013', 'TTR-304', 'QR-EVT202-TTR304-505-A', 799.00, 'ISSUED', '2026-03-16 09:16:00'),
('TKT-506', 'REG-404', 'EVT-202', 'USR-1013', 'TTR-304', 'QR-EVT202-TTR304-506-B', 799.00, 'ISSUED', '2026-03-16 09:16:00'),
('TKT-507', 'REG-404', 'EVT-202', 'USR-1013', 'TTR-304', 'QR-EVT202-TTR304-507-C', 799.00, 'ISSUED', '2026-03-16 09:16:00'),
('TKT-508', 'REG-405', 'EVT-202', 'USR-1014', 'TTR-305', 'QR-EVT202-TTR305-508-A', 1999.00, 'ISSUED', '2026-03-16 10:31:00'),
('TKT-509', 'REG-405', 'EVT-202', 'USR-1014', 'TTR-305', 'QR-EVT202-TTR305-509-B', 1999.00, 'ISSUED', '2026-03-16 10:31:00'),
('TKT-510', 'REG-406', 'EVT-202', 'USR-1015', 'TTR-306', 'QR-EVT202-TTR306-510-A', 599.00, 'ISSUED', '2026-03-16 11:46:00'),
('TKT-511', 'REG-407', 'EVT-203', 'USR-1016', 'TTR-307', 'QR-EVT203-TTR307-511-A', 499.00, 'ISSUED', '2026-03-17 14:01:00'),
('TKT-512', 'REG-408', 'EVT-203', 'USR-1017', 'TTR-308', 'QR-EVT203-TTR308-512-A', 1299.00, 'ISSUED', '2026-03-17 15:11:00'),
('TKT-513', 'REG-408', 'EVT-203', 'USR-1017', 'TTR-308', 'QR-EVT203-TTR308-513-B', 1299.00, 'ISSUED', '2026-03-17 15:11:00'),
('TKT-514', 'REG-409', 'EVT-204', 'USR-1018', 'TTR-309', 'QR-EVT204-TTR309-514-A', 899.00, 'ISSUED', '2026-03-18 16:01:00'),
('TKT-515', 'REG-410', 'EVT-204', 'USR-1019', 'TTR-310', 'QR-EVT204-TTR310-515-A', 1499.00, 'ISSUED', '2026-03-18 16:31:00'),
('TKT-516', 'REG-411', 'EVT-205', 'USR-1020', 'TTR-311', 'QR-EVT205-TTR311-516-A', 650.00, 'ISSUED', '2026-03-19 10:01:00'),
('TKT-517', 'REG-411', 'EVT-205', 'USR-1020', 'TTR-311', 'QR-EVT205-TTR311-517-B', 650.00, 'ISSUED', '2026-03-19 10:01:00'),
('TKT-518', 'REG-412', 'EVT-205', 'USR-1021', 'TTR-312', 'QR-EVT205-TTR312-518-A', 1500.00, 'ISSUED', '2026-03-19 11:01:00'),
('TKT-519', 'REG-413', 'EVT-206', 'USR-1022', 'TTR-313', 'QR-EVT206-TTR313-519-A', 1999.00, 'ISSUED', '2026-03-20 12:01:00'),
('TKT-520', 'REG-413', 'EVT-206', 'USR-1022', 'TTR-313', 'QR-EVT206-TTR313-520-B', 1999.00, 'ISSUED', '2026-03-20 12:01:00'),
('TKT-521', 'REG-414', 'EVT-206', 'USR-1023', 'TTR-314', 'QR-EVT206-TTR314-521-A', 3999.00, 'ISSUED', '2026-03-20 13:01:00'),
('TKT-522', 'REG-415', 'EVT-207', 'USR-1024', 'TTR-315', 'QR-EVT207-TTR315-522-A', 1199.00, 'ISSUED', '2026-03-21 14:01:00'),
('TKT-523', 'REG-416', 'EVT-208', 'USR-1010', 'TTR-316', 'QR-EVT208-TTR316-523-A', 2199.00, 'ISSUED', '2026-03-22 15:01:00'),
('TKT-524', 'REG-422', 'EVT-201', 'USR-1014', 'TTR-302', 'QR-EVT201-TTR302-524-X', 2499.00, 'CANCELLED', '2026-03-27 13:01:00'),
('TKT-525', 'REG-425', 'EVT-219', 'USR-1017', 'TTR-324', 'QR-EVT219-TTR324-525-A', 1500.00, 'USED', '2026-01-20 10:05:00'),
('TKT-526', 'REG-425', 'EVT-219', 'USR-1017', 'TTR-324', 'QR-EVT219-TTR324-526-B', 1500.00, 'USED', '2026-01-20 10:05:00'),
('TKT-527', 'REG-426', 'EVT-220', 'USR-1018', 'TTR-325', 'QR-EVT220-TTR325-527-A', 750.00, 'USED', '2026-01-25 11:05:00');

-- ============================================================================
-- 7. CHECKINS (21 rows: Volunteer scans at venue entrance)
-- ============================================================================
INSERT INTO checkins (checkin_id, ticket_id, volunteer_id, checkin_time, status) VALUES
('CHK-601', 'TKT-501', 'USR-1025', '2026-11-15 08:30:00', 'SUCCESS'),
('CHK-602', 'TKT-502', 'USR-1025', '2026-11-15 08:35:00', 'SUCCESS'),
('CHK-603', 'TKT-503', 'USR-1026', '2026-11-15 08:45:00', 'SUCCESS'),
('CHK-604', 'TKT-504', 'USR-1025', '2026-11-15 08:50:00', 'SUCCESS'),
('CHK-605', 'TKT-505', 'USR-1026', '2026-11-20 17:30:00', 'SUCCESS'),
('CHK-606', 'TKT-506', 'USR-1026', '2026-11-20 17:35:00', 'SUCCESS'),
('CHK-607', 'TKT-507', 'USR-1026', '2026-11-20 17:40:00', 'SUCCESS'),
('CHK-608', 'TKT-508', 'USR-1025', '2026-11-20 17:45:00', 'SUCCESS'),
('CHK-609', 'TKT-509', 'USR-1025', '2026-11-20 17:50:00', 'SUCCESS'),
('CHK-610', 'TKT-510', 'USR-1026', '2026-11-20 18:00:00', 'SUCCESS'),
('CHK-611', 'TKT-511', 'USR-1028', '2026-11-25 09:30:00', 'SUCCESS'),
('CHK-612', 'TKT-512', 'USR-1028', '2026-11-25 09:40:00', 'SUCCESS'),
('CHK-613', 'TKT-513', 'USR-1028', '2026-11-25 09:45:00', 'SUCCESS'),
('CHK-614', 'TKT-514', 'USR-1027', '2026-11-28 13:30:00', 'SUCCESS'),
('CHK-615', 'TKT-515', 'USR-1027', '2026-11-28 13:45:00', 'SUCCESS'),
('CHK-616', 'TKT-516', 'USR-1025', '2026-12-05 18:30:00', 'SUCCESS'),
('CHK-617', 'TKT-517', 'USR-1025', '2026-12-05 18:35:00', 'SUCCESS'),
('CHK-618', 'TKT-518', 'USR-1025', '2026-12-05 18:40:00', 'SUCCESS'),
('CHK-619', 'TKT-525', 'USR-1027', '2026-02-15 08:30:00', 'SUCCESS'),
('CHK-620', 'TKT-526', 'USR-1027', '2026-02-15 08:35:00', 'SUCCESS'),
('CHK-621', 'TKT-527', 'USR-1026', '2026-02-28 17:45:00', 'SUCCESS');

-- ============================================================================
-- 8. WAITLIST (21 rows: FIFO queue entries when ticket tiers sell out)
-- ============================================================================
INSERT INTO waitlist (waitlist_id, event_id, attendee_id, tier_id, requested_at, status) VALUES
('WTL-701', 'EVT-201', 'USR-1013', 'TTR-301', '2026-03-20 09:00:00', 'PROMOTED'),
('WTL-702', 'EVT-201', 'USR-1015', 'TTR-301', '2026-03-20 09:15:00', 'WAITING'),
('WTL-703', 'EVT-201', 'USR-1016', 'TTR-301', '2026-03-20 09:30:00', 'WAITING'),
('WTL-704', 'EVT-201', 'USR-1017', 'TTR-303', '2026-03-21 10:00:00', 'WAITING'),
('WTL-705', 'EVT-201', 'USR-1018', 'TTR-303', '2026-03-21 10:15:00', 'WAITING'),
('WTL-706', 'EVT-202', 'USR-1010', 'TTR-305', '2026-03-22 11:00:00', 'WAITING'),
('WTL-707', 'EVT-202', 'USR-1011', 'TTR-305', '2026-03-22 11:20:00', 'WAITING'),
('WTL-708', 'EVT-202', 'USR-1012', 'TTR-306', '2026-03-22 11:45:00', 'PROMOTED'),
('WTL-709', 'EVT-202', 'USR-1019', 'TTR-306', '2026-03-22 12:00:00', 'WAITING'),
('WTL-710', 'EVT-203', 'USR-1013', 'TTR-307', '2026-03-23 13:00:00', 'WAITING'),
('WTL-711', 'EVT-203', 'USR-1014', 'TTR-307', '2026-03-23 13:15:00', 'WAITING'),
('WTL-712', 'EVT-204', 'USR-1015', 'TTR-309', '2026-03-24 14:00:00', 'CANCELLED'),
('WTL-713', 'EVT-204', 'USR-1016', 'TTR-309', '2026-03-24 14:30:00', 'WAITING'),
('WTL-714', 'EVT-205', 'USR-1017', 'TTR-312', '2026-03-25 15:00:00', 'WAITING'),
('WTL-715', 'EVT-205', 'USR-1022', 'TTR-312', '2026-03-25 15:30:00', 'WAITING'),
('WTL-716', 'EVT-207', 'USR-1018', 'TTR-315', '2026-03-26 16:00:00', 'WAITING'),
('WTL-717', 'EVT-207', 'USR-1019', 'TTR-315', '2026-03-26 16:20:00', 'WAITING'),
('WTL-718', 'EVT-209', 'USR-1020', 'TTR-318', '2026-03-27 17:00:00', 'WAITING'),
('WTL-719', 'EVT-209', 'USR-1021', 'TTR-318', '2026-03-27 17:30:00', 'EXPIRED'),
('WTL-720', 'EVT-201', 'USR-1023', 'TTR-301', '2026-03-28 18:00:00', 'WAITING'),
('WTL-721', 'EVT-202', 'USR-1024', 'TTR-305', '2026-03-28 18:30:00', 'WAITING');

-- ============================================================================
-- 9. FEEDBACK (22 rows: Attendee post-event ratings and reviews)
-- ============================================================================
INSERT INTO feedback (feedback_id, event_id, attendee_id, rating, comments, submitted_at) VALUES
('FDB-801', 'EVT-219', 'USR-1010', 5, 'Exceptional technical discussions and well organized keynote sessions.', '2026-02-16 10:00:00'),
('FDB-802', 'EVT-219', 'USR-1011', 4, 'Great speaker lineup. High bandwidth WiFi inside the expo hall was a plus.', '2026-02-16 10:30:00'),
('FDB-803', 'EVT-219', 'USR-1012', 4, 'Very insightful track sessions on cloud architectures and security.', '2026-02-16 11:00:00'),
('FDB-804', 'EVT-219', 'USR-1017', 5, 'Seamless registration badge collection and excellent catering arrangements.', '2026-02-16 11:30:00'),
('FDB-805', 'EVT-220', 'USR-1013', 5, 'Mesmerizing acoustic performance! The hall acoustics were truly pristine.', '2026-03-01 09:30:00'),
('FDB-806', 'EVT-220', 'USR-1014', 5, 'Soulful evening with top-tier musicians. Looking forward to the next season.', '2026-03-01 10:00:00'),
('FDB-807', 'EVT-220', 'USR-1018', 4, 'Beautiful atmosphere and well-managed audience entry.', '2026-03-01 10:30:00'),
('FDB-808', 'EVT-201', 'USR-1010', 5, 'Early access materials and workshop schedule are thoroughly well-planned.', '2026-03-25 14:00:00'),
('FDB-809', 'EVT-201', 'USR-1011', 4, 'Excited for the upcoming summit, agenda topics are highly relevant.', '2026-03-25 15:00:00'),
('FDB-810', 'EVT-201', 'USR-1012', 5, 'VIP executive networking lounge details look fantastic.', '2026-03-25 16:00:00'),
('FDB-811', 'EVT-202', 'USR-1013', 4, 'Ticket delivery was instantaneous upon payment. Great UX.', '2026-03-26 10:00:00'),
('FDB-812', 'EVT-202', 'USR-1014', 4, 'Festival lineup is world-class. VIP perks clearly communicated.', '2026-03-26 11:00:00'),
('FDB-813', 'EVT-202', 'USR-1015', 5, 'Super fast checkout process for early bird tickets.', '2026-03-26 12:00:00'),
('FDB-814', 'EVT-203', 'USR-1016', 5, 'Hands-on lab prerequisites were sent well in advance. Very helpful.', '2026-03-27 13:00:00'),
('FDB-815', 'EVT-203', 'USR-1017', 4, 'Comprehensive agenda for intermediate to advanced Java engineers.', '2026-03-27 14:00:00'),
('FDB-816', 'EVT-204', 'USR-1018', 5, 'Impressive panel of AI researchers from industry and academia.', '2026-03-28 15:00:00'),
('FDB-817', 'EVT-204', 'USR-1019', 3, 'Pricing was slightly high, but content quality justified the cost.', '2026-03-28 16:00:00'),
('FDB-818', 'EVT-205', 'USR-1020', 4, 'Great seating chart visualization during booking.', '2026-03-29 11:00:00'),
('FDB-819', 'EVT-205', 'USR-1021', 5, 'Front row seat confirmation was immediate and smooth.', '2026-03-29 11:30:00'),
('FDB-820', 'EVT-206', 'USR-1022', 4, 'Good selection of front-end and web engineering tracks.', '2026-03-30 10:00:00'),
('FDB-821', 'EVT-206', 'USR-1023', 5, 'All access pass includes speaker dinner, great networking value.', '2026-03-30 11:00:00'),
('FDB-822', 'EVT-207', 'USR-1024', 5, 'Focused masterclass on real-world indexing and query plans.', '2026-03-31 14:00:00');
