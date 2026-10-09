package com.eventplatform.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of User representing an Attendee who books tickets.
 * Demonstrates deliberate use of List<Ticket> collection.
 */
public class Attendee extends User {
    public static final int MAX_TICKETS_PER_EVENT = 4;
    private final List<Ticket> bookedTickets;

    public Attendee(String userId, String name, String email) {
        super(userId, name, email);
        this.bookedTickets = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "ATTENDEE";
    }

    public List<Ticket> getBookedTickets() {
        return bookedTickets;
    }

    public void addTicket(Ticket ticket) {
        bookedTickets.add(ticket);
    }

    public boolean removeTicket(String ticketId) {
        return bookedTickets.removeIf(t -> t.getTicketId().equals(ticketId));
    }

    public int getTicketCountForEvent(String eventId) {
        int count = 0;
        for (Ticket t : bookedTickets) {
            if (t.getEventId().equals(eventId)) {
                count++;
            }
        }
        return count;
    }
}
