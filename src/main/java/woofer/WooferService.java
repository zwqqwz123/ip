package woofer;

import java.io.IOException;
import java.util.List;

import woofer.exception.WooferException;
import woofer.parser.Parser;
import woofer.storage.Storage;
import woofer.task.Task;
import woofer.task.TaskList;

/**
 * Provides the application logic shared by Woofer's text and graphical user interfaces.
 */
public class WooferService {
    private final Storage storage;
    private final Parser parser;
    private TaskList taskList;
    private boolean loadingError;

    /**
     * Creates a service and loads the saved tasks from disk.
     */
    public WooferService() {
        storage = new Storage();
        parser = new Parser();
        taskList = loadTaskList();
    }

    /**
     * Returns whether the saved task file could not be loaded.
     *
     * @return true when Woofer started with an empty list because loading failed.
     */
    public boolean hasLoadingError() {
        return loadingError;
    }

    /**
     * Processes one command and returns the text that should be shown to the user.
     *
     * @param command command entered by the user.
     * @return the command response and whether the application should exit.
     * @throws WooferException when the command is invalid.
     */
    public Response execute(String command) throws WooferException {
        Parser.CommandType commandType = parser.parseCommandType(command);
        return switch (commandType) {
            case EXIT -> new Response("Bye. Hope to see you again soon!", true);
            case LIST -> new Response(listTasks(), false);
            case FIND -> new Response(findTasks(command), false);
            case DELETE -> new Response(deleteTask(command), false);
            case MARK -> new Response(markTask(command, true), false);
            case UNMARK -> new Response(markTask(command, false), false);
            case ADD -> new Response(addTask(command), false);
            default -> throw new IllegalStateException("Unsupported command type: " + commandType);
        };
    }

    /**
     * Loads saved tasks, falling back to an empty list when storage cannot be read.
     *
     * @return the loaded tasks, or an empty list when loading fails.
     */
    private TaskList loadTaskList() {
        try {
            return storage.load();
        } catch (IOException exception) {
            loadingError = true;
            return new TaskList();
        }
    }

    /**
     * Formats all tasks for display.
     *
     * @return a formatted list response.
     */
    private String listTasks() {
        return formatTasks(
                "Here are the tasks in your list (" + taskList.size() + "):",
                taskList.getTasks(),
                "There are no tasks in your list.");
    }

    /**
     * Finds tasks matching a keyword and formats the result.
     *
     * @param command find command containing the keyword.
     * @return a formatted search response.
     * @throws WooferException when no keyword is provided.
     */
    private String findTasks(String command) throws WooferException {
        String keyword = parser.parseFindKeyword(command);
        return formatTasks(
                "Here are the matching tasks in your list:",
                taskList.findTasks(keyword),
                "No matching tasks found.");
    }

    /**
     * Adds a task parsed from a command.
     *
     * @param command add command describing the task.
     * @return a confirmation response.
     * @throws WooferException when the task is malformed or the list is full.
     */
    private String addTask(String command) throws WooferException {
        Task task = parser.parseTask(command);
        if (!taskList.addTask(task)) {
            throw new WooferException("The task list is full.");
        }

        String response = "Got it. I've added this task:\n"
                + "  " + task.getDisplayText() + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.";
        return withSavingWarning(response);
    }

    /**
     * Marks or unmarks a task parsed from a command.
     *
     * @param command mark or unmark command containing a task number.
     * @param isDone whether the task should be marked as done.
     * @return a confirmation response.
     * @throws WooferException when the task number is invalid or out of range.
     */
    private String markTask(String command, boolean isDone) throws WooferException {
        Parser.CommandType commandType = parser.parseCommandType(command);
        int taskNumber = parser.parseTaskNumber(command, commandType);
        Task task = taskList.getTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        String response = isDone
                ? "Nice! I've marked this task as done:\n  [X] " + task.getDescription()
                : "OK, I've marked this task as not done yet:\n  [ ] " + task.getDescription();
        return withSavingWarning(response);
    }

    /**
     * Deletes a task parsed from a command.
     *
     * @param command delete command containing a task number.
     * @return a confirmation response.
     * @throws WooferException when the task number is invalid or out of range.
     */
    private String deleteTask(String command) throws WooferException {
        int taskNumber = parser.parseTaskNumber(command, Parser.CommandType.DELETE);
        Task task = taskList.deleteTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        String response = "Noted. I've removed this task:\n"
                + "  " + task.getDisplayText() + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.";
        return withSavingWarning(response);
    }

    /**
     * Saves the current task list and appends a warning if saving fails.
     *
     * @param response response to return when saving succeeds.
     * @return the response, possibly with a saving warning.
     */
    private String withSavingWarning(String response) {
        if (saveTaskList()) {
            return response;
        }
        return response + "\nWarning: Could not save your tasks.";
    }

    /**
     * Saves the current task list.
     *
     * @return true when saving succeeds.
     */
    private boolean saveTaskList() {
        try {
            storage.save(taskList);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    /**
     * Formats a heading followed by numbered tasks.
     *
     * @param heading heading shown before the tasks.
     * @param tasks tasks to format.
     * @param emptyMessage message shown when there are no tasks.
     * @return the formatted task output.
     */
    private String formatTasks(String heading, List<Task> tasks, String emptyMessage) {
        StringBuilder response = new StringBuilder(heading);
        if (tasks.isEmpty()) {
            return response.append('\n').append(emptyMessage).toString();
        }

        for (int index = 0; index < tasks.size(); index++) {
            response.append('\n')
                    .append(index + 1)
                    .append('.')
                    .append(tasks.get(index).getDisplayText());
        }
        return response.toString();
    }

    /**
     * Represents the result of processing one command.
     *
     * @param message text to show in the conversation.
     * @param exits whether the command requests application exit.
     */
    public record Response(String message, boolean exits) {
    }
}
