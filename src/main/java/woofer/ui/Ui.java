package woofer.ui;

import java.util.Scanner;

import woofer.task.Task;
import woofer.task.TaskList;

/**
 * Handles user input and output for Woofer.
 */
public class Ui {
    private static final String SEPARATOR = "_".repeat(100);
    private static final String BANNER = " __        __   ____   ____   _____   _____   ____  \n"
            + " \\ \\      / /  / __ \\ / __ \\ |  ___| | ____| |  _ \\ \n"
            + "  \\ \\ /\\ / /  | |  | | |  | || |_    |  _|   | |_) |\n"
            + "   \\ V  V /   | |__| | |__| ||  _|   | |___  |  _ < \n"
            + "    \\_/\\_/     \\____/ \\____/ |_|     |_____| |_| \\_\\";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays Woofer's welcome message.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Woofer.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return {@code true} when another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from the user.
     *
     * @return the command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Woofer's separator line.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays Woofer's exit message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param taskList list of tasks to display.
     */
    public void showTaskList(TaskList taskList) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= taskList.size(); i++) {
            Task task = taskList.getTask(i);
            System.out.println(i + "." + task.getDisplayText());
        }
    }

    /**
     * Displays the confirmation for adding a task.
     *
     * @param task added task.
     * @param taskCount number of tasks after adding.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.getDisplayText());
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays the confirmation for changing a task's completion status.
     *
     * @param task task whose status changed.
     * @param isDone whether the task is now done.
     */
    public void showMarkedTask(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  [X] " + task.getDescription());
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  [ ] " + task.getDescription());
        }
    }

    /**
     * Displays the confirmation for deleting a task.
     *
     * @param task deleted task.
     * @param remainingTaskCount number of tasks after deletion.
     */
    public void showDeletedTask(Task task, int remainingTaskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task.getDisplayText());
        System.out.println("Now you have " + remainingTaskCount + " tasks in the list.");
    }

    /**
     * Displays an input error.
     *
     * @param message error message to display.
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /**
     * Displays a warning that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("Warning: Could not load saved tasks. Starting with an empty list.");
    }

    /**
     * Displays a warning that the current tasks could not be saved.
     */
    public void showSavingError() {
        System.out.println("Warning: Could not save your tasks.");
    }
}
