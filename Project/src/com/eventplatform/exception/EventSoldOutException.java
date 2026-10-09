package com.eventplatform.exception;

/**
 * Custom Checked Exception thrown when a ticket tier or event is at full capacity.
 */
public class EventSoldOutException extends Exception {
    private final String eventId;
    private final String tierName;

    public EventSoldOutException(String message, String eventId, String tierName) {
        super(message);
        this.eventId = eventId;
        this.tierName = tierName;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTierName() {
        return tierName;
    }
}
