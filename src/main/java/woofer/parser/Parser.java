package woofer.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import woofer.exception.WooferException;
import woofer.task.Deadline;
import woofer.task.Event;
import woofer.task.Task;
import woofer.task.Todo;

/**
 * Interprets user commands and creates the corresponding task objects.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final int DELETE_PREFIX_LENGTH = 7;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;

    /**
     * The command categories understood by Woofer.
     */
    public enum CommandType {
        EXIT,
        LIST,
        DELETE,
        MARK,
        UNMARK,
        ADD
    }

    /**
     * Identifies the type of a user command.
     *
     * @param command command entered by the user.
     * @return the command category.
     */
    public CommandType parseCommandType(String command) {
        if ("bye".equals(command)) {
            return CommandType.EXIT;
        }
        if ("list".equals(command)) {
            return CommandType.LIST;
        }
        if (command.startsWith("delete ")) {
            return CommandType.DELETE;
        }
        if (command.startsWith("mark ")) {
            return CommandType.MARK;
        }
        if (command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        }
        return CommandType.ADD;
    }

    /**
     * Creates a typed task from a user command.
     *
     * @param command command entered by the user.
     * @return a typed task.
     * @throws WooferException when the command is unknown or malformed.
     */
    public Task parseTask(String command) throws WooferException {
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
            return new Deadline(description, parseDate(by));
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
            return new Event(description, parseDate(from), parseDate(to));
        }

        throw new WooferException(
                "I don't know what that means. Try todo, deadline, event, list, mark, "
                        + "unmark, or delete.");
    }

    /**
     * Extracts the task number from a command.
     *
     * @param command command containing a task number.
     * @param commandType category of the command.
     * @return the one-based task number.
     * @throws WooferException when the command does not contain a valid task number.
     */
    public int parseTaskNumber(String command, CommandType commandType) throws WooferException {
        int prefixLength = switch (commandType) {
        case DELETE -> DELETE_PREFIX_LENGTH;
        case MARK -> MARK_PREFIX_LENGTH;
        case UNMARK -> UNMARK_PREFIX_LENGTH;
        default -> throw new WooferException("That command does not contain a task number.");
        };

        try {
            return Integer.parseInt(command.substring(prefixLength));
        } catch (NumberFormatException exception) {
            throw new WooferException("Please provide a valid task number.");
        }
    }

    /**
     * Parses a date entered in ISO local-date format.
     *
     * @param date date entered by the user.
     * @return the parsed date.
     * @throws WooferException when the date does not use the yyyy-MM-dd format.
     */
    private LocalDate parseDate(String date) throws WooferException {
        try {
            return LocalDate.parse(date, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new WooferException("Please use dates in yyyy-MM-dd format.");
        }
    }
}
