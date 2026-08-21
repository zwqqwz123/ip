/**
 * Represents one task in Woofer's in-memory task list.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new task that is initially not done.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task's status icon.
     *
     * @return {@code X} when done, or a blank space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the one-letter icon for this task type.
     *
     * @return the task type icon
     */
    public abstract String getTypeIcon();

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns additional information displayed after the description.
     *
     * @return task-specific date/time information
     */
    protected String getDateDetails() {
        return "";
    }

    /**
     * Formats this task for the task list.
     *
     * @return the task type, completion status, description, and date details
     */
    public String getDisplayText() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getDateDetails();
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }
}
