# Day 3: Object-Oriented Architecture, Interface Contracts & Custom Exceptions

## 1. Today's Objective
Expand the Event Management & Ticketing Platform by introducing clean object-oriented architecture, abstract class hierarchies, interface contracts, and custom exception handling for both checked and unchecked domain errors.

## 2. Concepts Learned
- **Abstract Classes & Inheritance:** Defining base abstractions (`User`, `Event`) that cannot be instantiated directly and enforcing specialized subclass implementations.
- **Polymorphism:** Handling different event categories (`ConcertEvent`, `ConferenceEvent`) and user roles (`Attendee`, `Organizer`) via common base references.
- **Interfaces:** Defining contract decouplings (`Bookable`) so service logic depends on behaviors rather than concrete classes.
- **Custom Checked Exceptions:** Creating checked exceptions (`EventSoldOutException`, `InvalidBookingException`) extending `Exception` to force explicit caller handling for domain rule violations.
- **Custom Unchecked Exceptions:** Creating runtime exceptions (`BookingNotFoundException`) extending `RuntimeException` for invalid resource lookups.

## 3. Files Created / Updated
1. **Abstract Base & Subclasses (`com.eventplatform.model`)**:
   - `User.java` (Abstract Base Class for users)
   - `Attendee.java` (Extends `User`, manages booked tickets list & ticket booking limits)
   - `Organizer.java` (Extends `User`, manages organization details)
   - `Event.java` (Abstract Base Class for events)
   - `ConcertEvent.java` (Extends `Event`, adds performer & backstage VIP attributes)
   - `ConferenceEvent.java` (Extends `Event`, adds keynote speaker & session track attributes)
2. **Interface (`com.eventplatform.service`)**:
   - `Bookable.java` (Interface contract for event & ticket management)
3. **Custom Exception Classes (`com.eventplatform.exception`)**:
   - `EventSoldOutException.java` (Custom Checked Exception)
   - `InvalidBookingException.java` (Custom Checked Exception)
   - `BookingNotFoundException.java` (Custom Unchecked Exception)

## 4. Key Implementation Highlights
- Refactored single-class models into clean multi-level inheritance hierarchies.
- Enforced strict booking policies: Maximum 4 tickets per attendee per event.
- Created custom exception types with contextual metadata (e.g., `eventId`, `tierName`).

## 5. Verification
Compiled all package files into `bin/`:
```bash
javac -d bin src/com/eventplatform/model/*.java src/com/eventplatform/exception/*.java src/com/eventplatform/service/*.java
```
