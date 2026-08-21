import java.util.Scanner;

/**
 * A simple command-line chatbot that stores tasks until the user says bye.
 */
public class Woofer {
    /**
     * Starts Woofer, stores commands, lists stored tasks, and exits on "bye".
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }

            System.out.println(separator);
        }
    }
}
