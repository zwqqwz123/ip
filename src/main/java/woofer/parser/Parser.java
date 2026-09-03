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
    private static final int FIND_PREFIX_LENGTH = 5;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final String TODO_FORMAT = "todo <description>";
    private static final String DEADLINE_FORMAT = "deadline <description> /by <yyyy-MM-dd>";
    private static final String EVENT_FORMAT = "event <description> /from <yyyy-MM-dd>"
            + " /to <yyyy-MM-dd>";
    private static final String FIND_FORMAT = "find <keyword>";
    private static final String DELETE_FORMAT = "delete <number>";
    private static final String MARK_FORMAT = "mark <number>";
    private static final String UNMARK_FORMAT = "unmark <number>";

    /**
     * Creates a parser for Woofer commands.
     */
    public Parser() {
    }

    /**
     * The command categories understood by Woofer.
     */
    public enum CommandType {
        /** Indicates that the user wants to exit Woofer. */
        EXIT,
        /** Indicates that the user wants to view all tasks. */
        LIST,
        /** Indicates that the user wants to find tasks by keyword. */
        FIND,
        /** Indicates that the user wants to delete a task. */
        DELETE,
        /** Indicates that the user wants to mark a task as done. */
        MARK,
        /** Indicates that the user wants to mark a task as not done. */
        UNMARK,
        /** Indicates that the user wants to add a task. */
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
        if ("find".equals(command) || command.startsWith("find ")) {
            return CommandType.FIND;
        }
        if ("delete".equals(command) || command.startsWith("delete ")) {
            return CommandType.DELETE;
        }
        if ("unmark".equals(command) || command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        }
        if ("mark".equals(command) || command.startsWith("mark ")) {
            return CommandType.MARK;
        }
        return CommandType.ADD;
    }

    /**
     * Extracts the keyword from a find command.
     *
     * @param command command containing a search keyword.
     * @return the search keyword.
     * @throws WooferException when the command does not contain a keyword.
     */
    public String parseFindKeyword(String command) throws WooferException {
        String keyword = command.length() > FIND_PREFIX_LENGTH
                ? command.substring(FIND_PREFIX_LENGTH).trim()
                : "";
        if (keyword.isBlank()) {
            throw invalidFormat(FIND_FORMAT);
        }
        return keyword;
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
                throw invalidFormat(TODO_FORMAT);
            }
            return new Todo(description);
        }
        if (command.startsWith("todo")) {
            throw invalidFormat(TODO_FORMAT);
        }

        if ("deadline".equals(command) || command.startsWith("deadline ")) {
            String details = command.length() > 9 ? command.substring(9).trim() : "";
            int byMarker = details.indexOf(" /by ");
            if (byMarker < 0) {
                throw invalidFormat(DEADLINE_FORMAT);
            }
            String description = details.substring(0, byMarker).trim();
            String by = details.substring(byMarker + 5).trim();
            if (description.isBlank()) {
                throw invalidFormat(DEADLINE_FORMAT);
            }
            if (by.isBlank()) {
                throw invalidFormat(DEADLINE_FORMAT);
            }
            return new Deadline(description, parseDate(by, DEADLINE_FORMAT));
        }
        if (command.startsWith("deadline")) {
            throw invalidFormat(DEADLINE_FORMAT);
        }

        if ("event".equals(command) || command.startsWith("event ")) {
            String details = command.length() > 6 ? command.substring(6).trim() : "";
            int fromMarker = details.indexOf(" /from ");
            int toMarker = details.indexOf(" /to ", fromMarker + 7);
            if (fromMarker < 0 || toMarker < 0 || toMarker <= fromMarker) {
                throw invalidFormat(EVENT_FORMAT);
            }
            String description = details.substring(0, fromMarker).trim();
            String from = details.substring(fromMarker + 7, toMarker).trim();
            String to = details.substring(toMarker + 5).trim();
            if (description.isBlank()) {
                throw invalidFormat(EVENT_FORMAT);
            }
            if (from.isBlank() || to.isBlank()) {
                throw invalidFormat(EVENT_FORMAT);
            }
            return new Event(description, parseDate(from, EVENT_FORMAT),
                    parseDate(to, EVENT_FORMAT));
        }
        if (command.startsWith("event")) {
            throw invalidFormat(EVENT_FORMAT);
        }

        throw new WooferException("Unknown command. Try one of: " + TODO_FORMAT + ", "
                + DEADLINE_FORMAT + ", " + EVENT_FORMAT + ", list, " + FIND_FORMAT + ", "
                + MARK_FORMAT + ", " + UNMARK_FORMAT + ", or " + DELETE_FORMAT + ".");
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

        String commandFormat = switch (commandType) {
            case DELETE -> DELETE_FORMAT;
            case MARK -> MARK_FORMAT;
            case UNMARK -> UNMARK_FORMAT;
            default -> "";
        };
        if (command.length() <= prefixLength) {
            throw invalidFormat(commandFormat);
        }

        try {
            return Integer.parseInt(command.substring(prefixLength).trim());
        } catch (NumberFormatException exception) {
            throw invalidFormat(commandFormat);
        }
    }

    /**
     * Parses a date entered in ISO local-date format.
     *
     * @param date date entered by the user.
     * @param commandFormat complete command format to show when parsing fails.
     * @return the parsed date.
     * @throws WooferException when the date does not use the yyyy-MM-dd format.
     */
    private LocalDate parseDate(String date, String commandFormat) throws WooferException {
        try {
            return LocalDate.parse(date, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw invalidFormat(commandFormat);
        }
    }

    /**
     * Creates a parser error that states the complete required command format.
     *
     * @param commandFormat complete command format.
     * @return parser error.
     */
    private WooferException invalidFormat(String commandFormat) {
        return new WooferException("Required format: " + commandFormat + ".");
    }
}
