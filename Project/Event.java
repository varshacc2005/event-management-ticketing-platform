public class Event {
    // 1. Fields
    public String eventName;
    public String eventDate;
    public TicketTier tier; // Notice: TicketTier is used as a data type!

    // 2. Constructor
    public Event(String eventName, String eventDate, TicketTier tier) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.tier = tier;
    }

    // 3. Method to display event and its ticket details
    public void displayEventDetails() {
        System.out.println("Event: " + this.eventName);
        System.out.println("Date: " + this.eventDate);
        System.out.println("Ticket Tier Details:");
        this.tier.displayTierDetails(); // Calls the method we created in TicketTier!
    }
}