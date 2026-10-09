package com.eventplatform.model;

/**
 * Subclass of Event representing a professional conference.
 */
public class ConferenceEvent extends Event {
    private String keynoteSpeaker;
    private int numberOfTracks;

    public ConferenceEvent(String eventId, String title, String date, String venue, String organizerId,
                           String keynoteSpeaker, int numberOfTracks) {
        super(eventId, title, date, venue, organizerId);
        this.keynoteSpeaker = keynoteSpeaker;
        this.numberOfTracks = numberOfTracks;
    }

    public String getKeynoteSpeaker() {
        return keynoteSpeaker;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    @Override
    public String getCategory() {
        return "CONFERENCE";
    }

    @Override
    public void displayCategoryDetails() {
        System.out.println("Keynote Speaker : " + keynoteSpeaker);
        System.out.println("Track Count     : " + numberOfTracks + " Parallel Tracks");
    }
}
