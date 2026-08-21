import java.util.Scanner;

/**
 * A simple command-line chatbot that manages tasks until the user says bye.
 */
public class Woofer {
    /**
     * Starts Woofer, manages stored tasks, and exits on "bye".
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String separator = "_".repeat(100);
        String banner = " __        __   ____   ____   _____   _____   ____  \n"
                + " \\ \\      / /  / __ \\ / __ \\ |  ___| | ____| |  _ \\ \n"
                + "  \\ \\ /\\ / /  | |  | | |  | || |_    |  _|   | |_) |\n"
                + "   \\ V  V /   | |__| | |__| ||  _|   | |___  |  _ < \n"
                + "    \\_/\\_/     \\____/ \\____/ |_|     |_____| |_| \\_\\";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Woofer.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if ("bye".equals(command)) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            try {
                if ("list".equals(command)) {
                    printTaskList(taskList);
                } else if (command.startsWith("delete ")) {
                    deleteTask(taskList, command);
                } else if (command.startsWith("mark ")) {
                    markTask(taskList, command, 5);
                } else if (command.startsWith("unmark ")) {
                    markTask(taskList, command, 7);
                } else {
                    printAddedTask(taskList, parseTask(command));
                }
            } catch (WooferException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }

            System.out.println(separator);
        }
    }

    /**
     * Creates a typed task from a user command.
     *
     * @param command command entered by the user
     * @return a typed task
     * @throws WooferException when the command is unknown or malformed
     */
    private static Task parseTask(String command) throws WooferException {
        if ("todo".equals(command) || command.startsWith("todo ")) {
            String description = command.length() > 5 ? command.substring(5).trim() : "";
            if (description.isBlank()) {
                throw new WooferException("The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if ("deadline".equals(command) || command.startsWith("deadline ")) {
            String details = command.length() > 9 ? command.substring(9).trim() : "";
            int byMarker = details.indexOf(" /by ");
            if (byMarker < 0) {
                throw new WooferException("A deadline must include /by followed by a date or time.");
            }
            String description = details.substring(0, byMarker).trim();
            String by = details.substring(byMarker + 5).trim();
            if (description.isBlank()) {
                throw new WooferException("The description of a deadline cannot be empty.");
            }
            if (by.isBlank()) {
                throw new WooferException("A deadline must include a date or time after /by.");
            }
            return new Deadline(description, by);
        }

        if ("event".equals(command) || command.startsWith("event ")) {
            String details = command.length() > 6 ? command.substring(6).trim() : "";
            int fromMarker = details.indexOf(" /from ");
            int toMarker = details.indexOf(" /to ", fromMarker + 7);
            if (fromMarker < 0 || toMarker < 0) {
                throw new WooferException(
                        "An event must include /from and /to date or time details.");
            }
            String description = details.substring(0, fromMarker).trim();
            String from = details.substring(fromMarker + 7, toMarker).trim();
            String to = details.substring(toMarker + 5).trim();
            if (description.isBlank()) {
                throw new WooferException("The description of an event cannot be empty.");
            }
            if (from.isBlank() || to.isBlank()) {
                throw new WooferException("An event must include both start and end details.");
            }
            return new Event(description, from, to);
        }

        throw new WooferException(
                "I don't know what that means. Try todo, deadline, event, list, mark, "
                        + "unmark, or delete.");
    }

    /**
     * Prints all tasks in the task list.
     *
     * @param taskList list to display
     */
    private static void printTaskList(TaskList taskList) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= taskList.size(); i++) {
            Task task = taskList.getTask(i);
            System.out.println(i + "." + task.getDisplayText());
        }
    }

    /**
     * Marks or unmarks a task based on a command.
     *
     * @param taskList list containing the task
     * @param command command entered by the user
     * @param prefixLength length of the command prefix before the number
     * @throws WooferException when the task number is invalid or out of range
     */
    private static void markTask(TaskList taskList, String command, int prefixLength)
            throws WooferException {
        int taskNumber = getTaskNumber(command, prefixLength);
        Task task = taskList.getTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        if (command.startsWith("mark ")) {
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  [X] " + task.getDescription());
        } else {
            task.markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  [ ] " + task.getDescription());
        }
    }

    /**
     * Deletes a task based on a command.
     *
     * @param taskList list containing the task
     * @param command command entered by the user
     * @throws WooferException when the task number is invalid or out of range
     */
    private static void deleteTask(TaskList taskList, String command)
            throws WooferException {
        int taskNumber = getTaskNumber(command, 7);
        Task task = taskList.deleteTask(taskNumber);
        if (task == null) {
            throw new WooferException("That task does not exist.");
        }

        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task.getDisplayText());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    /**
     * Adds a task and prints the confirmation shown to the user.
     *
     * @param taskList list to update
     * @param task task to add
     * @throws WooferException when the task list is full
     */
    private static void printAddedTask(TaskList taskList, Task task) throws WooferException {
        if (!taskList.addTask(task)) {
            throw new WooferException("The task list is full.");
        }

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.getDisplayText());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    /**
     * Converts the task number in a command into a one-based task number.
     *
     * @param command command containing a task number
     * @param prefixLength length of the command prefix before the number
     * @return the one-based task number
     * @throws WooferException when the number is not valid
     */
    private static int getTaskNumber(String command, int prefixLength) throws WooferException {
        try {
            return Integer.parseInt(command.substring(prefixLength));
        } catch (NumberFormatException exception) {
            throw new WooferException("Please provide a valid task number.");
        }
    }
}
