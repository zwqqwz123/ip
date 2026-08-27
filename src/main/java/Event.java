import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that occurs between a specified start and end date or time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates a new event task.
     *
     * @param description text describing the event.
     * @param from date when the event starts.
     * @param to date when the event ends.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return the event's starting date.
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return the event's ending date.
     */
    public LocalDate getTo() {
        return to;
    }

    @Override
    protected String getDateDetails() {
        return " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
