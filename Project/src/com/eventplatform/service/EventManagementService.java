package com.eventplatform.service;

import com.eventplatform.exception.BookingNotFoundException;
import com.eventplatform.exception.EventSoldOutException;
import com.eventplatform.exception.InvalidBookingException;
import com.eventplatform.model.*;

import java.util.*;

/**
 * Service implementation managing in-memory event data structures.
 * Deliberately utilizes:
 * - Map: eventsMap (eventId -> Event), attendeesMap (attendeeId -> Attendee)
 * - Set: registeredEmails (guarantees unique user emails)
 * - List: masterTicketList (stores all issued tickets), getAllEvents()
 * - Queue: Event's waitlistQueues (FIFO waitlist processing upon cancellation)
 */
public class EventManagementService implements Bookable {
    private final Map<String, Event> eventsMap;
    private final Map<String, Attendee> attendeesMap;
    private final Map<String, Organizer> organizersMap;
    private final Set<String> registeredEmails;
    private final List<Ticket> masterTicketList;
    private int ticketCounter = 1000;

    public EventManagementService() {
        this.eventsMap = new HashMap<>();
        this.attendeesMap = new HashMap<>();
        this.organizersMap = new HashMap<>();
        this.registeredEmails = new HashSet<>();
        this.masterTicketList = new ArrayList<>();
    }

    @Override
    public void registerAttendee(Attendee attendee) throws InvalidBookingException {
        if (registeredEmails.contains(attendee.getEmail().toLowerCase())) {
            throw new InvalidBookingException("Registration failed: Email '" + attendee.getEmail() + "' is already registered!");
        }
        registeredEmails.add(attendee.getEmail().toLowerCase());
        attendeesMap.put(attendee.getUserId(), attendee);
    }

    @Override
    public void registerOrganizer(Organizer organizer) {
        organizersMap.put(organizer.getUserId(), organizer);
    }

    @Override
    public void createEvent(Event event) {
        eventsMap.put(event.getEventId(), event);
    }

    @Override
    public Ticket bookTicket(String eventId, String attendeeId, String tierName)
            throws EventSoldOutException, InvalidBookingException {

        Event event = eventsMap.get(eventId);
        if (event == null) {
            throw new InvalidBookingException("Booking error: Event with ID '" + eventId + "' does not exist.");
        }

        Attendee attendee = attendeesMap.get(attendeeId);
        if (attendee == null) {
            throw new InvalidBookingException("Booking error: Attendee with ID '" + attendeeId + "' is not registered.");
        }

        // Rule Check: Max 4 tickets per attendee per event
        if (attendee.getTicketCountForEvent(eventId) >= Attendee.MAX_TICKETS_PER_EVENT) {
            throw new InvalidBookingException("Booking error: Attendee '" + attendee.getName() +
                    "' has reached the maximum booking limit of " + Attendee.MAX_TICKETS_PER_EVENT +
                    " tickets for event '" + event.getTitle() + "'.");
        }

        TicketTier tier = event.getTier(tierName);
        if (tier == null) {
            throw new InvalidBookingException("Booking error: Invalid ticket tier '" + tierName + "' for event '" + event.getTitle() + "'.");
        }

        // Check Capacity
        if (tier.isSoldOut()) {
            WaitlistEntry waitlistEntry = new WaitlistEntry(attendeeId, tier.getTierName());
            event.addToWaitlist(tier.getTierName(), waitlistEntry);
            throw new EventSoldOutException(
                    "Tier '" + tier.getTierName() + "' for event '" + event.getTitle() +
                            "' is SOLD OUT! Added attendee '" + attendee.getName() + "' to the Waitlist Queue.",
                    eventId, tier.getTierName());
        }

        // Capacity available: issue ticket
        tier.incrementBookedCount();
        String ticketId = "TKT-" + (++ticketCounter);
        Ticket ticket = new Ticket(ticketId, eventId, attendeeId, tier.getTierName(), tier.getPrice());

        attendee.addTicket(ticket);
        masterTicketList.add(ticket);
        return ticket;
    }

    @Override
    public boolean cancelTicket(String ticketId) throws BookingNotFoundException {
        Ticket ticketToCancel = null;
        for (Ticket t : masterTicketList) {
            if (t.getTicketId().equalsIgnoreCase(ticketId)) {
                ticketToCancel = t;
                break;
            }
        }

        if (ticketToCancel == null) {
            throw new BookingNotFoundException("Cancellation failed: Ticket ID '" + ticketId + "' not found in system records.");
        }

        masterTicketList.remove(ticketToCancel);
        Attendee attendee = attendeesMap.get(ticketToCancel.getAttendeeId());
        if (attendee != null) {
            attendee.removeTicket(ticketId);
        }

        Event event = eventsMap.get(ticketToCancel.getEventId());
        if (event != null) {
            TicketTier tier = event.getTier(ticketToCancel.getTierName());
            if (tier != null) {
                tier.decrementBookedCount();
                System.out.println("\n[SYSTEM LOG] Ticket " + ticketId + " successfully cancelled. Releasing 1 seat for tier " + tier.getTierName() + ".");

                // Check Waitlist Queue for auto-promotion!
                WaitlistEntry nextInQueue = event.pollWaitlist(tier.getTierName());
                if (nextInQueue != null) {
                    System.out.println("[WAITLIST AUTO-PROMOTION QUEUE] Polling next entry from Queue: " + nextInQueue);
                    try {
                        Ticket promotedTicket = bookTicket(event.getEventId(), nextInQueue.getAttendeeId(), nextInQueue.getTierName());
                        System.out.println("[WAITLIST AUTO-PROMOTION SUCCESS] Auto-issued ticket " + promotedTicket.getTicketId() +
                                " for waitlisted attendee ID: " + nextInQueue.getAttendeeId());
                    } catch (Exception e) {
                        System.out.println("[WAITLIST AUTO-PROMOTION ERROR] Could not process auto-booking: " + e.getMessage());
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(eventsMap.values());
    }

    @Override
    public Event getEventById(String eventId) {
        return eventsMap.get(eventId);
    }

    @Override
    public Attendee getAttendeeById(String attendeeId) {
        return attendeesMap.get(attendeeId);
    }

    @Override
    public List<Ticket> getAttendeeTickets(String attendeeId) {
        Attendee a = attendeesMap.get(attendeeId);
        if (a != null) {
            return a.getBookedTickets();
        }
        return Collections.emptyList();
    }
}
