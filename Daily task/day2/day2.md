# Day 2: Basic Java Class Design & Object Modeling

## 1. Today's Objective
Implement the fundamental Java classes required for an organizer to create an event with ticket tiers:
`Organizer -> Event -> TicketTier`

## 2. Concepts Learned
- **Class:** A blueprint that defines attributes and behaviors.
- **Object:** A concrete instance created in memory using the `new` keyword.
- **Fields (Attributes):** Variables inside a class that hold the object's data/state.
- **Constructor:** A special method matching the class name that initializes an object when created.
- **Methods:** Functions inside a class that define the actions or behaviors an object can perform.
- **Composition (Has-a Relationship):** Connecting classes together (e.g., an Event *has a* TicketTier; an Organizer *has an* Event).
- **Separation of Concerns:** Keeping `Organizer`, `Event`, and `TicketTier` in separate classes so each has a single, well-defined responsibility.

## 3. Classes Created
1. `TicketTier.java`:
   - Fields: `tierName`, `price`, `totalCapacity`
   - Method: `displayTierDetails()`
2. `Event.java`:
   - Fields: `eventName`, `eventDate`, `tier`
   - Method: `displayEventDetails()`
3. `Organizer.java`:
   - Fields: `organizerName`, `contactEmail`, `event`
   - Method: `displayOrganizerInfo()`
4. `Main.java`:
   - Entry point containing the `main()` method to instantiate objects and run the program.

## 4. What Was Implemented
- Modeled the core domain entities for the ticketing platform.
- Created parameterized constructors for clean object initialization.
- Linked objects together:
  `Organizer ("Varsha")` $\rightarrow$ `Event ("Tech Conference 2026")` $\rightarrow$ `TicketTier ("VIP", ₹2000, 50 tickets)`.

## 5. Testing & Output
Compiled and executed using:
```bash
javac *.java
java Main