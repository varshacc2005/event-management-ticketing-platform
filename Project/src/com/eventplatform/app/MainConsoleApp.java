package com.eventplatform.app;

import com.eventplatform.exception.BookingNotFoundException;
import com.eventplatform.exception.EventSoldOutException;
import com.eventplatform.exception.InvalidBookingException;
import com.eventplatform.model.*;
import com.eventplatform.service.EventManagementService;

import java.util.List;
import java.util.Scanner;

/**
 * Main Console Application for Event Management & Ticketing Platform.
 * Demonstrates Class Hierarchy, Interfaces, Custom Exceptions (Checked & Unchecked),
 * and Collections (List, Set, Map, Queue).
 */
public class MainConsoleApp {

    public static void main(String[] args) {
        EventManagementService service = new EventManagementService();
        seedInitialData(service);

        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            runAutomatedShowcase(service);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================================================");
        System.out.println("   WELCOME TO THE EVENT MANAGEMENT & TICKETING PLATFORM (JAVA CONSOLE)   ");
        System.out.println("=========================================================================");

        while (running) {
            printMainMenu();
            System.out.print("Select an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    runAutomatedShowcase(service);
                    break;
                case "2":
                    displayAllEvents(service);
                    break;
                case "3":
                    handleRegisterAttendee(service, scanner);
                    break;
                case "4":
                    handleCreateEvent(service, scanner);
                    break;
                case "5":
                    handleBookTicket(service, scanner);
                    break;
                case "6":
                    handleCancelTicket(service, scanner);
                    break;
                case "7":
                    handleViewAttendeeTickets(service, scanner);
                    break;
                case "8":
                    System.out.println("\nThank you for using the Event Management Platform. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[INVALID SELECTION] Please enter a valid number between 1 and 8.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n----------------------------- MAIN MENU -----------------------------");
        System.out.println("1. Run Automated Technical Feature Showcase (Flows, Exceptions & Collections)");
        System.out.println("2. View All Events & Ticket Tiers (Map & List)");
        System.out.println("3. Register New Attendee (Set Email Uniqueness)");
        System.out.println("4. Create New Event (Organizer Flow - Concert/Conference Hierarchy)");
        System.out.println("5. Book Ticket for Attendee (Attendee Flow - Tier Capacity & Limits)");
        System.out.println("6. Cancel Ticket & Trigger Waitlist Queue (Queue FIFO Processing)");
        System.out.println("7. View Attendee Bookings History (List)");
        System.out.println("8. Exit Application");
        System.out.println("---------------------------------------------------------------------");
    }

    public static void seedInitialData(EventManagementService service) {
        try {
            // Register Organizers
            Organizer org1 = new Organizer("ORG-01", "Varsha Tech Events", "contact@varshaevents.com", "Varsha Corp", "+91-9876543210");
            Organizer org2 = new Organizer("ORG-02", "Grand Music Prod", "info@grandmusic.org", "Grand Music", "+91-9123456789");
            service.registerOrganizer(org1);
            service.registerOrganizer(org2);

            // Register Attendees
            Attendee att1 = new Attendee("ATT-101", "Alice Smith", "alice@example.com");
            Attendee att2 = new Attendee("ATT-102", "Bob Johnson", "bob@example.com");
            Attendee att3 = new Attendee("ATT-103", "Charlie Brown", "charlie@example.com");
            Attendee att4 = new Attendee("ATT-104", "Diana Prince", "diana@example.com");

            service.registerAttendee(att1);
            service.registerAttendee(att2);
            service.registerAttendee(att3);
            service.registerAttendee(att4);

            // Create Concert Event (Limited capacity tier to showcase waitlist & sold-out flow)
            ConcertEvent concert = new ConcertEvent("EVT-101", "Rock Legends World Tour", "2026-11-20", "Starlight Arena", "ORG-02", "The Beatles Reboot", true);
            concert.addTicketTier(new TicketTier("VIP", 250.0, 2)); // Small capacity = 2
            concert.addTicketTier(new TicketTier("STANDARD", 100.0, 10));
            service.createEvent(concert);

            // Create Conference Event
            ConferenceEvent conf = new ConferenceEvent("EVT-102", "Global AI & Java Tech Summit", "2026-12-05", "Convention Center", "ORG-01", "Dr. James Gosling", 4);
            conf.addTicketTier(new TicketTier("EARLY_BIRD", 150.0, 5));
            conf.addTicketTier(new TicketTier("STANDARD", 300.0, 20));
            service.createEvent(conf);

        } catch (Exception e) {
            System.err.println("Error seeding data: " + e.getMessage());
        }
    }

    public static void runAutomatedShowcase(EventManagementService service) {
        System.out.println("\n=========================================================================");
        System.out.println("        AUTOMATED TECHNICAL FEATURE SHOWCASE (FLOWS & OBJECTIVES)        ");
        System.out.println("=========================================================================");

        System.out.println("\n--- [FLOW 1: EVENT & TIER MANAGEMENT (ORGANIZER FLOW)] ---");
        System.out.println("Displaying initialized events from Map<String, Event> ...");
        displayAllEvents(service);

        System.out.println("\n--- [FLOW 2: TICKET BOOKING & CAPACITIES (ATTENDEE FLOW)] ---");
        try {
            System.out.println("1. Alice (ATT-101) books VIP ticket for Rock Legends (EVT-101)...");
            Ticket t1 = service.bookTicket("EVT-101", "ATT-101", "VIP");
            System.out.println("   [SUCCESS] Issued: " + t1);

            System.out.println("2. Bob (ATT-102) books VIP ticket for Rock Legends (EVT-101)...");
            Ticket t2 = service.bookTicket("EVT-101", "ATT-102", "VIP");
            System.out.println("   [SUCCESS] Issued: " + t2);

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\n--- [TESTING CUSTOM CHECKED EXCEPTION 1: EventSoldOutException & Queue] ---");
        System.out.println("Attempting 3rd VIP booking for Charlie (ATT-103) when capacity is 2 ...");
        try {
            service.bookTicket("EVT-101", "ATT-103", "VIP");
        } catch (EventSoldOutException e) {
            System.out.println("   [CAUGHT CHECKED EXCEPTION] EventSoldOutException: " + e.getMessage());
            System.out.println("   Event ID: " + e.getEventId() + " | Tier: " + e.getTierName());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        System.out.println("Attempting 4th VIP booking for Diana (ATT-104) when tier is sold out ...");
        try {
            service.bookTicket("EVT-101", "ATT-104", "VIP");
        } catch (EventSoldOutException e) {
            System.out.println("   [CAUGHT CHECKED EXCEPTION] EventSoldOutException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nChecking Event EVT-101 summary (Notice Waitlist Queue size = 2):");
        service.getEventById("EVT-101").displayEventSummary();

        System.out.println("\n--- [TESTING CUSTOM CHECKED EXCEPTION 2: InvalidBookingException (Ticket Limit)] ---");
        System.out.println("Attempting to book > 4 tickets for Alice (ATT-101) on EVT-102 ...");
        try {
            System.out.println("   Booking ticket 1..."); service.bookTicket("EVT-102", "ATT-101", "STANDARD");
            System.out.println("   Booking ticket 2..."); service.bookTicket("EVT-102", "ATT-101", "STANDARD");
            System.out.println("   Booking ticket 3..."); service.bookTicket("EVT-102", "ATT-101", "STANDARD");
            System.out.println("   Booking ticket 4..."); service.bookTicket("EVT-102", "ATT-101", "STANDARD");
            System.out.println("   Booking ticket 5 (Exceeds limit of 4)...");
            service.bookTicket("EVT-102", "ATT-101", "STANDARD");
        } catch (InvalidBookingException e) {
            System.out.println("   [CAUGHT CHECKED EXCEPTION] InvalidBookingException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\n--- [TESTING SET UNIQUNESS: Duplicate Email Registration] ---");
        try {
            System.out.println("Attempting to register new attendee with existing email 'alice@example.com'...");
            service.registerAttendee(new Attendee("ATT-999", "Fake Alice", "alice@example.com"));
        } catch (InvalidBookingException e) {
            System.out.println("   [CAUGHT CHECKED EXCEPTION] InvalidBookingException: " + e.getMessage());
        }

        System.out.println("\n--- [TESTING CUSTOM UNCHECKED EXCEPTION: BookingNotFoundException] ---");
        try {
            System.out.println("Attempting to cancel non-existent ticket 'TKT-99999'...");
            service.cancelTicket("TKT-99999");
        } catch (BookingNotFoundException e) {
            System.out.println("   [CAUGHT UNCHECKED EXCEPTION] BookingNotFoundException: " + e.getMessage());
        }

        System.out.println("\n--- [TESTING WAITLIST QUEUE AUTO-PROMOTION ON CANCELLATION] ---");
        List<Ticket> aliceTickets = service.getAttendeeTickets("ATT-101");
        Ticket ticketToCancel = null;
        for (Ticket t : aliceTickets) {
            if (t.getEventId().equals("EVT-101") && t.getTierName().equals("VIP")) {
                ticketToCancel = t;
                break;
            }
        }

        if (ticketToCancel != null) {
            System.out.println("Cancelling Alice's VIP ticket (" + ticketToCancel.getTicketId() + ") for EVT-101...");
            service.cancelTicket(ticketToCancel.getTicketId());
        }

        System.out.println("\nUpdated Event EVT-101 summary after ticket cancellation & waitlist queue auto-promotion:");
        service.getEventById("EVT-101").displayEventSummary();

        System.out.println("=========================================================================");
        System.out.println("                 AUTOMATED FEATURE SHOWCASE COMPLETED                    ");
        System.out.println("=========================================================================\n");
    }

    private static void displayAllEvents(EventManagementService service) {
        List<Event> events = service.getAllEvents();
        System.out.println("\n--- CATALOG OF EVENTS (Total: " + events.size() + ") ---");
        for (Event event : events) {
            event.displayEventSummary();
        }
    }

    private static void handleRegisterAttendee(EventManagementService service, Scanner scanner) {
        System.out.println("\n--- REGISTER NEW ATTENDEE ---");
        System.out.print("Enter Attendee ID (e.g. ATT-201): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();

        try {
            Attendee attendee = new Attendee(id, name, email);
            service.registerAttendee(attendee);
            System.out.println("[SUCCESS] Registered Attendee: " + attendee);
        } catch (InvalidBookingException e) {
            System.out.println("[REGISTRATION FAILED] " + e.getMessage());
        }
    }

    private static void handleCreateEvent(EventManagementService service, Scanner scanner) {
        System.out.println("\n--- CREATE NEW EVENT (ORGANIZER FLOW) ---");
        System.out.print("Select Event Type (1 for Concert, 2 for Conference): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter Event ID (e.g. EVT-301): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Event Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        System.out.print("Enter Venue Name: ");
        String venue = scanner.nextLine().trim();
        System.out.print("Enter Organizer ID (e.g. ORG-01): ");
        String orgId = scanner.nextLine().trim();

        Event event;
        if ("1".equals(type)) {
            System.out.print("Enter Headliner Performer Name: ");
            String performer = scanner.nextLine().trim();
            System.out.print("Allow Backstage VIP Pass? (true/false): ");
            boolean backstage = Boolean.parseBoolean(scanner.nextLine().trim());
            event = new ConcertEvent(id, title, date, venue, orgId, performer, backstage);
        } else {
            System.out.print("Enter Keynote Speaker: ");
            String speaker = scanner.nextLine().trim();
            System.out.print("Enter Number of Track Sessions: ");
            int tracks = Integer.parseInt(scanner.nextLine().trim());
            event = new ConferenceEvent(id, title, date, venue, orgId, speaker, tracks);
        }

        // Add default ticket tiers
        System.out.print("Enter Standard Ticket Price: $");
        double stdPrice = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Standard Ticket Capacity: ");
        int stdCap = Integer.parseInt(scanner.nextLine().trim());
        event.addTicketTier(new TicketTier("STANDARD", stdPrice, stdCap));

        System.out.print("Enter VIP Ticket Price: $");
        double vipPrice = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter VIP Ticket Capacity: ");
        int vipCap = Integer.parseInt(scanner.nextLine().trim());
        event.addTicketTier(new TicketTier("VIP", vipPrice, vipCap));

        service.createEvent(event);
        System.out.println("[SUCCESS] Created Event successfully: " + event.getTitle());
    }

    private static void handleBookTicket(EventManagementService service, Scanner scanner) {
        System.out.println("\n--- BOOK TICKET (ATTENDEE FLOW) ---");
        System.out.print("Enter Event ID (e.g. EVT-101): ");
        String eventId = scanner.nextLine().trim();
        System.out.print("Enter Attendee ID (e.g. ATT-101): ");
        String attendeeId = scanner.nextLine().trim();
        System.out.print("Enter Ticket Tier (STANDARD / VIP / EARLY_BIRD): ");
        String tierName = scanner.nextLine().trim();

        try {
            Ticket ticket = service.bookTicket(eventId, attendeeId, tierName);
            System.out.println("\n[BOOKING SUCCESSFUL]");
            System.out.println("Issued Ticket: " + ticket);
        } catch (EventSoldOutException e) {
            System.out.println("\n[EVENT SOLD OUT CHECKED EXCEPTION]");
            System.out.println(e.getMessage());
        } catch (InvalidBookingException e) {
            System.out.println("\n[INVALID BOOKING CHECKED EXCEPTION]");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private static void handleCancelTicket(EventManagementService service, Scanner scanner) {
        System.out.println("\n--- CANCEL TICKET & PROCESS WAITLIST QUEUE ---");
        System.out.print("Enter Ticket ID to cancel (e.g. TKT-1001): ");
        String ticketId = scanner.nextLine().trim();

        try {
            service.cancelTicket(ticketId);
            System.out.println("[SUCCESS] Cancellation processing completed.");
        } catch (BookingNotFoundException e) {
            System.out.println("\n[UNCHECKED EXCEPTION CAUGHT] " + e.getMessage());
        }
    }

    private static void handleViewAttendeeTickets(EventManagementService service, Scanner scanner) {
        System.out.println("\n--- VIEW ATTENDEE BOOKING HISTORY ---");
        System.out.print("Enter Attendee ID (e.g. ATT-101): ");
        String attendeeId = scanner.nextLine().trim();

        Attendee attendee = service.getAttendeeById(attendeeId);
        if (attendee == null) {
            System.out.println("[ERROR] Attendee ID '" + attendeeId + "' not found.");
            return;
        }

        System.out.println("\nAttendee Info: " + attendee);
        List<Ticket> tickets = service.getAttendeeTickets(attendeeId);
        System.out.println("Booked Tickets Count: " + tickets.size());
        if (tickets.isEmpty()) {
            System.out.println("  (No tickets booked yet)");
        } else {
            for (Ticket t : tickets) {
                System.out.println("  -> " + t);
            }
        }
    }
}
