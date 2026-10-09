package com.eventplatform.model;

/**
 * Represents a ticket tier within an event (e.g., VIP, Standard, Early Bird).
 */
public class TicketTier {
    private String tierName;
    private double price;
    private int capacity;
    private int bookedCount;

    public TicketTier(String tierName, double price, int capacity) {
        this.tierName = tierName;
        this.price = price;
        this.capacity = capacity;
        this.bookedCount = 0;
    }

    public String getTierName() {
        return tierName;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public int getAvailableSeats() {
        return capacity - bookedCount;
    }

    public boolean isSoldOut() {
        return bookedCount >= capacity;
    }

    public void incrementBookedCount() {
        if (bookedCount < capacity) {
            bookedCount++;
        }
    }

    public void decrementBookedCount() {
        if (bookedCount > 0) {
            bookedCount--;
        }
    }

    @Override
    public String toString() {
        return String.format("%s [Price: $%.2f | Capacity: %d | Available: %d]",
                tierName, price, capacity, getAvailableSeats());
    }
}
