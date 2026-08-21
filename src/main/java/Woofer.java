import java.util.Scanner;

/**
 * A simple command-line chatbot that stores and tracks tasks until the user
 * says bye.
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

        String[] tasks = new String[100];
        boolean[] completed = new boolean[100];
        int taskCount = 0;
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
                for (int i = 0; i < taskCount; i++) {
                    String status = completed[i] ? "X" : " ";
                    System.out.println((i + 1) + ".[" + status + "] " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = getTaskIndex(command, 5);
                if (taskIndex < 0 || taskIndex >= taskCount) {
                    System.out.println("Task does not exist!");
                } else {
                    completed[taskIndex] = true;
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + tasks[taskIndex]);
                }
            } else if (command.startsWith("unmark ")) {
                int taskIndex = getTaskIndex(command, 7);
                if (taskIndex < 0 || taskIndex >= taskCount) {
                    System.out.println("Task does not exist!");
                } else {
                    completed[taskIndex] = false;
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + tasks[taskIndex]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
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
    private static int getTaskIndex(String command, int prefixLength) {
        try {
            return Integer.parseInt(command.substring(prefixLength)) - 1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
