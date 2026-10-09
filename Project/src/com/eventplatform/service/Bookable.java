package com.eventplatform.service;

import com.eventplatform.exception.BookingNotFoundException;
import com.eventplatform.exception.EventSoldOutException;
import com.eventplatform.exception.InvalidBookingException;
import com.eventplatform.model.Attendee;
import com.eventplatform.model.Event;
import com.eventplatform.model.Organizer;
import com.eventplatform.model.Ticket;

import java.util.List;

/**
 * Interface defining contract for Event & Ticket operations.
 */
public interface Bookable {
    void registerAttendee(Attendee attendee) throws InvalidBookingException;
    void registerOrganizer(Organizer organizer);
    void createEvent(Event event);
    
    Ticket bookTicket(String eventId, String attendeeId, String tierName) 
            throws EventSoldOutException, InvalidBookingException;
    
    boolean cancelTicket(String ticketId) throws BookingNotFoundException;
    
    List<Event> getAllEvents();
    Event getEventById(String eventId);
    Attendee getAttendeeById(String attendeeId);
    List<Ticket> getAttendeeTickets(String attendeeId);
}
