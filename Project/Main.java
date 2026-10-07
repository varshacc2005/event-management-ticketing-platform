public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   EVENT MANAGEMENT SYSTEM (DAY 2)      ");
        System.out.println("========================================");

        // 1. Create a TicketTier object
        TicketTier vipTier = new TicketTier("VIP", 2000.0, 50);

        // 2. Create an Event object (passing the TicketTier into it)
        Event techConference = new Event("Tech Conference 2026", "2026-11-15", vipTier);

        // 3. Create an Organizer object (passing the Event into it)
        Organizer organizer = new Organizer("Varsha", "varsha@techconf.org", techConference);

        // 4. Display the complete chain of information
        organizer.displayOrganizerInfo();

        System.out.println("========================================");
    }
}