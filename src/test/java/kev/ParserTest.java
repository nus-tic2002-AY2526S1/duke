package kev;

import kev.command.*;
import kev.exception.KevException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_addTodo_returnsAddCommand() throws KevException {
        assertTrue(Parser.parse("todo read") instanceof AddCommand);
    }

    @Test
    public void parse_mark_returnsMarkCommand() throws KevException {
        assertTrue(Parser.parse("mark 1") instanceof MarkCommand);
    }

    @Test
    public void parse_invalid_throwsException() {
        assertThrows(KevException.class, () -> Parser.parse("nonsensecommand"));
    }
}
