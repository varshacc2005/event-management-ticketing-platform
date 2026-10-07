public class TicketTier {
    // 1. Fields (Attributes / Data)
    public String tierName;
    public double price;
    public int totalCapacity;

    // 2. Constructor (Runs when we write: new TicketTier(...))
    public TicketTier(String tierName, double price, int totalCapacity) {
        this.tierName = tierName;
        this.price = price;
        this.totalCapacity = totalCapacity;
    }

    // 3. Method (Action to display details)
    public void displayTierDetails() {
        System.out.println("  - Tier: " + this.tierName);
        System.out.println("    Price: ₹" + this.price);
        System.out.println("    Available Tickets: " + this.totalCapacity);
    }
}