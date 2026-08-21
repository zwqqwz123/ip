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

            if ("list".equals(command)) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 1; i <= taskList.size(); i++) {
                    Task task = taskList.getTask(i);
                    System.out.println(i + ".[" + task.getStatusIcon() + "] "
                            + task.getDescription());
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = getTaskNumber(command, 5);
                Task task = taskList.getTask(taskNumber);
                if (task == null) {
                    System.out.println("Task does not exist!");
                } else {
                    task.markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + task.getDescription());
                }
            } else if (command.startsWith("unmark ")) {
                int taskNumber = getTaskNumber(command, 7);
                Task task = taskList.getTask(taskNumber);
                if (task == null) {
                    System.out.println("Task does not exist!");
                } else {
                    task.markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + task.getDescription());
                }
            } else {
                if (taskList.addTask(command)) {
                    System.out.println("added: " + command);
                } else {
                    System.out.println("Task list is full!");
                }
            }

            System.out.println(separator);
        }
    }

    /**
     * Converts the one-based task number in a command into a zero-based index.
     *
     * @param command command containing a task number
     * @param prefixLength length of the command prefix before the number
     * @return the zero-based task index, or -1 when the number is invalid
     */
    private static int getTaskNumber(String command, int prefixLength) {
        try {
            return Integer.parseInt(command.substring(prefixLength));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
