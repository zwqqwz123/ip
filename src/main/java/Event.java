/**
 * A task that occurs between a specified start and end date or time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates a new event task.
     *
     * @param description text describing the event
     * @param from date or time when the event starts
     * @param to date or time when the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    protected String getDateDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
