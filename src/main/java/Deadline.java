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
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the deadline date or time.
     *
     * @return the deadline date or time.
     */
    public String getBy() {
        return by;
    }

    @Override
    protected String getDateDetails() {
        return " (by: " + by + ")";
    }
}
