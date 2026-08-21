/**
 * The fixed set of task types supported by Woofer.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String icon;

    /**
     * Creates a task type with its display icon.
     *
     * @param icon one-letter icon shown in the task list
     */
    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the display icon for this task type.
     *
     * @return the task type icon
     */
    public String getIcon() {
        return icon;
    }
}
