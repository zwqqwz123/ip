package woofer.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import woofer.exception.WooferException;
import woofer.task.Deadline;
import woofer.task.Task;
import woofer.task.Todo;

/**
 * Tests the parsing of Woofer commands.
 */
public class ParserTest {
    private final Parser parser = new Parser();

    /**
     * Verifies that a todo command creates a todo with the expected description.
     *
     * @throws WooferException when the valid command cannot be parsed.
     */
    @Test
    public void parseTodoCommandCreatesTodo() throws WooferException {
        Task task = parser.parseTask("todo read book");

        assertInstanceOf(Todo.class, task);
        assertEquals("read book", task.getDescription());
        assertEquals("[T][ ] read book", task.getDisplayText());
    }

    /**
     * Verifies that a deadline command parses its date as a local date.
     *
     * @throws WooferException when the valid command cannot be parsed.
     */
    @Test
    public void parseDeadlineCommandParsesDate() throws WooferException {
        Task task = parser.parseTask("deadline return book /by 2019-12-02");

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals(LocalDate.of(2019, 12, 2), deadline.getBy());
        assertEquals("[D][ ] return book (by: Dec 02 2019)", deadline.getDisplayText());
    }

    /**
     * Verifies that malformed dates are rejected with a Woofer exception.
     */
    @Test
    public void parseDeadlineCommandRejectsInvalidDate() {
        assertThrows(
                WooferException.class,
                () -> parser.parseTask("deadline return book /by 02/12/2019"));
    }

    /**
     * Verifies that a task number is extracted from a mark command.
     *
     * @throws WooferException when the valid command cannot be parsed.
     */
    @Test
    public void parseTaskNumberExtractsMarkNumber() throws WooferException {
        assertEquals(3, parser.parseTaskNumber("mark 3", Parser.CommandType.MARK));
    }
}
