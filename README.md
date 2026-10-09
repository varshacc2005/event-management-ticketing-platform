# Event Management & Ticketing Platform

A plain Java in-memory console application for an **Event Management & Ticketing Platform**, implementing core organizer and attendee flows with class hierarchies, interface contracts, custom exceptions (checked & unchecked), and deliberate usage of Java Collections (`List`, `Set`, `Map`, `Queue`).

---

## 1. Project Folder Structure

```text
event-management-ticketing-platform/
├── bin/                             # Compiled Java bytecodes (.class files)
│   └── com/eventplatform/
├── src/                             # [PRIMARY SOURCE DIRECTORY] Modular Java Package Architecture
│   └── com/eventplatform/
│       ├── app/
│       │   └── MainConsoleApp.java  # CLI Entry Point & Automated Showcase Driver
│       ├── exception/
│       │   ├── EventSoldOutException.java      # Custom Checked Exception
│       │   ├── InvalidBookingException.java   # Custom Checked Exception
│       │   └── BookingNotFoundException.java  # Custom Unchecked Exception
│       ├── model/
│       │   ├── User.java                       # Abstract Base Class
│       │   ├── Attendee.java                   # Subclass of User
│       │   ├── Organizer.java                  # Subclass of User
│       │   ├── Event.java                      # Abstract Base Class
│       │   ├── ConcertEvent.java               # Subclass of Event
│       │   ├── ConferenceEvent.java            # Subclass of Event
│       │   ├── TicketTier.java                 # Tier Metadata
│       │   ├── Ticket.java                     # Issued Ticket Record
│       │   └── WaitlistEntry.java              # Queue Payload
│       └── service/
│           ├── Bookable.java                   # Core Interface Contract
│           └── EventManagementService.java     # In-Memory Service (List, Set, Map, Queue)
├── Daily task/                      # Daily progress documentation
│   ├── day1/day1.md                 # Day 1: Setup & GitHub repo initialization
│   ├── day2/day2.md                 # Day 2: Initial single-class domain modeling
│   ├── day3/day3.md                 # Day 3: Class hierarchy, interfaces & custom exceptions
│   └── day4/day4.md                 # Day 4: Collections, waitlist engine & console application
└── Project/                         # Initial Day 1 & Day 2 basic scratch files
```

> [!NOTE]
> `src/` is your **actual primary project directory** containing the production-grade modular package structure (`com.eventplatform.*`). The `Project/` folder contains the early Day 1 & Day 2 scratch files.

---

## 2. Confirmation on Application Type

**YES, this is strictly a TERMINAL / CONSOLE Application (CLI).**
No GUI, no Web UI, no Spring, and no external database are required or used. The application runs entirely within the terminal using standard I/O (`System.out.println` and `Scanner` keyboard input).

---

## 3. Core Flows Implemented in Memory

1. **Flow 1: Event & Ticket Tier Management (Organizer Flow)**:
   - Creating Concert & Conference events with specialized attributes (headliner artist, backstage access, keynote speakers, track sessions).
   - Setting up ticket tiers (VIP, Standard, Early Bird) with prices and capacities.
2. **Flow 2: Ticket Booking, Cancellation & Waitlist Queue Engine (Attendee Flow)**:
   - Booking tickets with tier selection & ticket limit rules (max 4 tickets per attendee per event).
   - Enqueuing attendees into a FIFO `Queue<WaitlistEntry>` when a tier is sold out (`EventSoldOutException`).
   - Automatically polling the waitlist `Queue` and issuing tickets to waitlisted attendees upon ticket cancellation.

---

## 4. Execution Commands

### Step 1: Compile the Java Application
Run the following command from the root directory:
```bash
javac -d bin src/com/eventplatform/model/*.java src/com/eventplatform/exception/*.java src/com/eventplatform/service/*.java src/com/eventplatform/app/*.java
```

### Step 2: Run Execution Modes

#### Option A: Automated Technical Feature Showcase (Recommended for Testing)
Demonstrates both flows, exceptions, collections, and waitlist queue auto-promotion automatically:
```bash
java -cp bin com.eventplatform.app.MainConsoleApp --demo
```

#### Option B: Interactive Terminal Menu System
Launches the interactive command-line menu:
```bash
java -cp bin com.eventplatform.app.MainConsoleApp
```
