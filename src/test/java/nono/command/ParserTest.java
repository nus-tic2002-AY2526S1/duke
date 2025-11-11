package nono.command;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import nono.exception.UserInputException;

public class ParserTest {

    @Test
    public void parse_todo_success() throws UserInputException {
        Command c = Parser.parse("todo sleep well");
        assertEquals(Command.Type.TODO, c.getType());
        assertEquals("sleep well", c.getDescription());
    }

    @Test
    public void parse_deadline_success() throws UserInputException {
        Command c = Parser.parse("deadline return book /by 2025-10-30 23:59");
        assertEquals(Command.Type.DEADLINE, c.getType());
        assertArrayEquals(new String[]{"return book", "2025-10-30 23:59"}, c.getDetails());
    }

    @Test
    public void parse_event_success() throws UserInputException {
        Command c = Parser.parse("event exam /from 2025-11-25 00:00 /to 2025-12-06 00:00");
        assertEquals(Command.Type.EVENT, c.getType());
    }

    @Test
    public void parse_invalidCommand_throwsException() {
        assertThrows(UserInputException.class, () -> Parser.parse("invalid"));
    }
}
