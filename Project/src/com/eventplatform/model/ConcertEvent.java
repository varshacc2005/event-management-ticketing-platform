package com.eventplatform.model;

/**
 * Subclass of Event representing a musical concert.
 */
public class ConcertEvent extends Event {
    private String performerName;
    private boolean backstageAccessAllowed;

    public ConcertEvent(String eventId, String title, String date, String venue, String organizerId,
                        String performerName, boolean backstageAccessAllowed) {
        super(eventId, title, date, venue, organizerId);
        this.performerName = performerName;
        this.backstageAccessAllowed = backstageAccessAllowed;
    }

    public String getPerformerName() {
        return performerName;
    }

    public boolean isBackstageAccessAllowed() {
        return backstageAccessAllowed;
    }

    @Override
    public String getCategory() {
        return "CONCERT";
    }

    @Override
    public void displayCategoryDetails() {
        System.out.println("Headliner Artist : " + performerName);
        System.out.println("Backstage Pass   : " + (backstageAccessAllowed ? "Available for VIP" : "Not Available"));
    }
}
