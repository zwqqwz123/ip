import java.util.Scanner;

/**
 * A simple command-line chatbot that echoes commands until the user says bye.
 */
public class Woofer {
    /**
     * Starts Woofer, reads commands from standard input, and exits on "bye".
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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if ("bye".equals(command)) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(command);
            System.out.println(separator);
        }
    }
}
