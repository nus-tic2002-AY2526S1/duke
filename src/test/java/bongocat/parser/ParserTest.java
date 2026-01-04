package bongocat.parser;

import bongocat.BongoException;
import bongocat.commands.Command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_invalidCommand_throwsException() {
        assertThrows(BongoException.class, () -> {
            Parser.parse("nonsense command");
        });
    }

    @Test
    public void parse_todoCommand_returnsCorrectType() {
        Command cmd = Parser.parse("todo eat");
        assertEquals("AddTodoCommand", cmd.getClass().getSimpleName());
    }
}
