package com.eventplatform.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payload class placed into a Queue when an event tier is sold out.
 */
public class WaitlistEntry {
    private String attendeeId;
    private String tierName;
    private String requestTime;

    public WaitlistEntry(String attendeeId, String tierName) {
        this.attendeeId = attendeeId;
        this.tierName = tierName;
        this.requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public String getTierName() {
        return tierName;
    }

    public String getRequestTime() {
        return requestTime;
    }

    @Override
    public String toString() {
        return "WaitlistEntry [Attendee: " + attendeeId + " | Tier: " + tierName + " | Requested: " + requestTime + "]";
    }
}
