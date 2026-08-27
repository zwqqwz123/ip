package woofer;

import java.io.IOException;

import woofer.exception.WooferException;
import woofer.parser.Parser;
import woofer.storage.Storage;
import woofer.task.Task;
import woofer.task.TaskList;
import woofer.ui.Ui;

/**
 * Coordinates Woofer's user interface, command parser, storage, and task list.
 */
public class Woofer {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList taskList;

    /**
     * Creates a Woofer application with its supporting components.
     */
    public Woofer() {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
        taskList = new TaskList();
    }

    /**
     * Starts Woofer.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Woofer().run();
    }

    /**
     * Runs Woofer until the user enters the exit command.
     */
    public void run() {
        ui.showWelcome();
        taskList = loadTaskList();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showLine();

            try {
                Parser.CommandType commandType = parser.parseCommandType(command);
                if (commandType == Parser.CommandType.EXIT) {
                    ui.showBye();
                    break;
                }
                handleCommand(command, commandType);
            } catch (WooferException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
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
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /**
     * Executes a parsed command using Woofer's components.
     *
     * @param command command entered by the user.
     * @param commandType category of the command.
     * @throws WooferException when the command is invalid.
     */
    private void handleCommand(String command, Parser.CommandType commandType)
            throws WooferException {
        switch (commandType) {
        case LIST -> ui.showTaskList(taskList);
        case DELETE -> deleteTask(parser.parseTaskNumber(command, commandType));
        case MARK -> markTask(parser.parseTaskNumber(command, commandType), true);
        case UNMARK -> markTask(parser.parseTaskNumber(command, commandType), false);
        case ADD -> addTask(parser.parseTask(command));
        case EXIT -> {
            // The exit command is handled before this method is called.
        }
        }
    }

    /**
     * Saves the current tasks and reports a warning when storage cannot be written.
     *
     * @param taskList tasks to save.
     */
    private void saveTaskList(TaskList taskList) {
        try {
            storage.save(taskList);
        } catch (IOException exception) {
            ui.showSavingError();
        }
    }

    /**
     * Marks or unmarks a task.
     *
     * @param taskNumber one-based task number.
     * @param isDone whether the task should be marked as done.
     * @throws WooferException when the task number is invalid or out of range.
     */
    private void markTask(int taskNumber, boolean isDone) throws WooferException {
        Task task = taskList.getTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        ui.showMarkedTask(task, isDone);
        saveTaskList(taskList);
    }

    /**
     * Deletes a task.
     *
     * @param taskNumber one-based task number.
     * @throws WooferException when the task number is invalid or out of range.
     */
    private void deleteTask(int taskNumber) throws WooferException {
        Task task = taskList.deleteTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        ui.showDeletedTask(task, taskList.size());
        saveTaskList(taskList);
    }

    /**
     * Adds a task to Woofer's task list.
     *
     * @param task task to add.
     * @throws WooferException when the task list is full.
     */
    private void addTask(Task task) throws WooferException {
        if (!taskList.addTask(task)) {
            throw new WooferException("The task list is full.");
        }

        ui.showAddedTask(task, taskList.size());
        saveTaskList(taskList);
    }
}
