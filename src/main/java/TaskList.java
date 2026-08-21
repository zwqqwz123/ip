/**
 * Stores Woofer's tasks in memory.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount;

    /**
     * Adds a new task to the list.
     *
     * @param description text describing the task
     * @return true when the task was added, or false when the list is full
     */
    public boolean addTask(String description) {
        if (taskCount >= MAX_TASKS) {
            return false;
        }

        tasks[taskCount] = new Task(description);
        taskCount++;
        return true;
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of tasks
     */
    public int size() {
        return taskCount;
    }

    /**
     * Looks up a task using its one-based number from the user interface.
     *
     * @param taskNumber one-based task number
     * @return the task, or null when the number is outside the list
     */
    public Task getTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            return null;
        }
        return tasks[taskNumber - 1];
    }
}
