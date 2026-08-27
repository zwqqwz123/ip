import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    /**
     * Creates a new deadline task.
     *
     * @param description text describing the task.
     * @param by date by which the task should be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the deadline date or time.
     *
     * @return the deadline date.
     */
    public LocalDate getBy() {
        return by;
    }

    @Override
    protected String getDateDetails() {
        return " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
