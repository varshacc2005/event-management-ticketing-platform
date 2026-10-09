package com.eventplatform.model;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Abstract base class for the Event hierarchy (Event -> ConcertEvent, ConferenceEvent).
 * Demonstrates Map<String, TicketTier> and Map<String, Queue<WaitlistEntry>> collections.
 */
public abstract class Event {
    protected String eventId;
    protected String title;
    protected String date;
    protected String venue;
    protected String organizerId;
    protected Map<String, TicketTier> tiers;
    protected Map<String, Queue<WaitlistEntry>> waitlistQueues;

    public Event(String eventId, String title, String date, String venue, String organizerId) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.organizerId = organizerId;
        this.tiers = new HashMap<>();
        this.waitlistQueues = new HashMap<>();
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public Map<String, TicketTier> getTiers() {
        return tiers;
    }

    public Map<String, Queue<WaitlistEntry>> getWaitlistQueues() {
        return waitlistQueues;
    }

    public void addTicketTier(TicketTier tier) {
        tiers.put(tier.getTierName().toUpperCase(), tier);
        waitlistQueues.put(tier.getTierName().toUpperCase(), new ArrayDeque<>());
    }

    public TicketTier getTier(String tierName) {
        return tiers.get(tierName.toUpperCase());
    }

    public Queue<WaitlistEntry> getWaitlistQueue(String tierName) {
        return waitlistQueues.get(tierName.toUpperCase());
    }

    public void addToWaitlist(String tierName, WaitlistEntry entry) {
        Queue<WaitlistEntry> queue = waitlistQueues.get(tierName.toUpperCase());
        if (queue != null) {
            queue.add(entry);
        }
    }

    public WaitlistEntry pollWaitlist(String tierName) {
        Queue<WaitlistEntry> queue = waitlistQueues.get(tierName.toUpperCase());
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    /**
     * Abstract method to get event category type.
     */
    public abstract String getCategory();

    /**
     * Abstract method to print special details specific to event subtype.
     */
    public abstract void displayCategoryDetails();

    public void displayEventSummary() {
        System.out.println("==================================================");
        System.out.println("Event ID: " + eventId + " | Category: " + getCategory());
        System.out.println("Title   : " + title);
        System.out.println("Date    : " + date + " | Venue: " + venue);
        System.out.println("Organizer ID: " + organizerId);
        displayCategoryDetails();
        System.out.println("---------------- Ticket Tiers ------------------");
        for (TicketTier tier : tiers.values()) {
            Queue<WaitlistEntry> wq = waitlistQueues.get(tier.getTierName().toUpperCase());
            int waitlistCount = (wq != null) ? wq.size() : 0;
            System.out.println(" -> " + tier + " | Waitlist Queue Size: " + waitlistCount);
        }
        System.out.println("==================================================");
    }
}
