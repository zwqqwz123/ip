package woofer.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stores Woofer's tasks in memory.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Creates an empty task list.
     */
    public TaskList() {
    }

    /**
     * Adds a new task to the list.
     *
     * @param description text describing the task.
     * @return true when the task was added, or false when the list is full.
     */
    public boolean addTask(String description) {
        return addTask(new Todo(description));
    }

    /**
     * Adds a task of any supported type to the list.
     *
     * @param task task to add.
     * @return true when the task was added, or false when the list is full.
     */
    public boolean addTask(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            return false;
        }

        tasks.add(task);
        return true;
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks in insertion order.
     *
     * @return an unmodifiable snapshot of the stored tasks.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Finds tasks whose descriptions contain the specified keyword.
     *
     * <p>The search is case-insensitive and preserves the tasks' insertion order.</p>
     *
     * @param keyword text to search for.
     * @return an unmodifiable list of matching tasks, or an empty list for a blank keyword.
     */
    public List<Task> findTasks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            String description = task.getDescription().toLowerCase(Locale.ROOT);
            if (description.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return List.copyOf(matchingTasks);
    }

    /**
     * Looks up a task using its one-based number from the user interface.
     *
     * @param taskNumber one-based task number.
     * @return the task, or null when the number is outside the list.
     */
    public Task getTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns a task using its one-based number.
     *
     * @param taskNumber one-based task number.
     * @return the removed task, or null when the number is outside the list.
     */
    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.remove(taskNumber - 1);
    }
}
