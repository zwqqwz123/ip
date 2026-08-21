/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a new deadline task.
     *
     * @param description text describing the task
     * @param by date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    protected String getDateDetails() {
        return " (by: " + by + ")";
    }
}
