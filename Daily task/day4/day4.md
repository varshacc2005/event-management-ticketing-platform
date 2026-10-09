# Day 4: Java Collections (List, Set, Map, Queue), Waitlist Engine & Full Console Application

## 1. Today's Objective
Implement deliberate in-memory Java Collections (`List`, `Set`, `Map`, `Queue`), build an event-driven FIFO waitlist auto-promotion engine, and construct a complete interactive console menu and automated technical showcase runner.

## 2. Concepts Learned & Collections Used
- **`Map` (`HashMap`)**: Used for $O(1)$ fast lookups:
  - `Map<String, Event>` for event catalog lookup by `eventId`.
  - `Map<String, TicketTier>` for ticket tier metadata lookup by `tierName`.
- **`Set` (`HashSet`)**: Used for uniqueness validation:
  - `Set<String>` for storing registered user email addresses to prevent duplicate registrations.
- **`List` (`ArrayList`)**: Used for ordered collection storage:
  - `List<Ticket>` inside `Attendee` for personal booking history.
  - `List<Ticket>` in service master records.
- **`Queue` (`ArrayDeque`)**: Used for FIFO waitlist queue management:
  - `Queue<WaitlistEntry>` per ticket tier. When a tier is sold out, booking requests are enqueued and `EventSoldOutException` is thrown.
  - Upon ticket cancellation, the system automatically polls the next entry from the `Queue` and auto-issues a ticket to the waitlisted attendee!

## 3. Files Created / Updated
1. **Core Service & Payload (`com.eventplatform.service` & `com.eventplatform.model`)**:
   - `WaitlistEntry.java` (Payload stored inside the Queue)
   - `EventManagementService.java` (Implements `Bookable`, encapsulates List, Set, Map, and Queue operations)
2. **Console UI & Application Driver (`com.eventplatform.app`)**:
   - `MainConsoleApp.java` (Contains both interactive CLI menu system and automated technical demonstration showcase runner)

## 4. Work Completed
- Implemented **Core Flow 1 (Organizer Flow)**: Event creation, category display (`ConcertEvent` vs `ConferenceEvent`), ticket tier setup, venue capacity tracking.
- Implemented **Core Flow 2 (Attendee Flow)**: Ticket booking with tier selection, capacity validation, waitlisting, ticket cancellation, and waitlist queue auto-promotion.
- Added 2 execution modes in `MainConsoleApp`:
  1. `--demo` flag / option 1: Automated technical showcase proving all flows, exceptions, and collections empirically.
  2. Options 2–8: Full interactive scanner menu system for user manual interaction.

## 5. Execution & Commands
Compile all source files:
```bash
javac -d bin src/com/eventplatform/model/*.java src/com/eventplatform/exception/*.java src/com/eventplatform/service/*.java src/com/eventplatform/app/*.java
```

Run Automated Technical Demonstration:
```bash
java -cp bin com.eventplatform.app.MainConsoleApp --demo
```

Run Interactive Console Menu:
```bash
java -cp bin com.eventplatform.app.MainConsoleApp
```
