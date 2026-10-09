package com.eventplatform.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an issued ticket for an attendee.
 */
public class Ticket {
    private String ticketId;
    private String eventId;
    private String attendeeId;
    private String tierName;
    private double pricePaid;
    private String bookingTime;

    public Ticket(String ticketId, String eventId, String attendeeId, String tierName, double pricePaid) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.attendeeId = attendeeId;
        this.tierName = tierName;
        this.pricePaid = pricePaid;
        this.bookingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public String getTierName() {
        return tierName;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return String.format("Ticket #%s | Event: %s | Attendee: %s | Tier: %s | Paid: $%.2f | Time: %s",
                ticketId, eventId, attendeeId, tierName, pricePaid, bookingTime);
    }
}
