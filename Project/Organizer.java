public class Organizer {
    // 1. Fields
    public String organizerName;
    public String contactEmail;
    public Event event; // Organizer holds an Event object!

    // 2. Constructor
    public Organizer(String organizerName, String contactEmail, Event event) {
        this.organizerName = organizerName;
        this.contactEmail = contactEmail;
        this.event = event;
    }

    // 3. Method to display organizer and event info
    public void displayOrganizerInfo() {
        System.out.println("Organizer: " + this.organizerName);
        System.out.println("Contact: " + this.contactEmail);
        System.out.println("----------------------------------------");
        this.event.displayEventDetails(); // Calls Event's display method!
    }
}