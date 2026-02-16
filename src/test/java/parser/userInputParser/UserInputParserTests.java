package parser.userInputParser;

import enums.CommandType;
import exceptions.GrootException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserInputParserTests {
    @Nested
    @DisplayName("parseUserInput()")
    class ParseUserInput_Test {
        @Test
        @DisplayName("todo Success")
        public void parseUserInput_Success() {
            //todo command
            String todoInput = "todo task1";

            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> result =
                    assertDoesNotThrow(() -> UserInputParser.parseUserInput(todoInput));

            assertEquals(CommandType.TODO, result.getKey());
            assertEquals("task1", result.getValue().get(0));

            //deadline command
            String deadlineInput = "deadline task1 /by 13/10/25 0000";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(deadlineInput));

            assertEquals(CommandType.DEADLINE, result.getKey());
            assertEquals("task1", result.getValue().get(0));
            assertEquals("13/10/25 0000", result.getValue().get(1));

            //event command
            String eventInput = "event task1 /from 13/10/25 0000 /to 14/10/25 0000";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(eventInput));

            assertEquals(CommandType.EVENT, result.getKey());
            assertEquals("task1", result.getValue().get(0));
            assertEquals("13/10/25 0000", result.getValue().get(1));
            assertEquals("14/10/25 0000", result.getValue().get(2));

            //list command
            String listInput = "list";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(listInput));

            assertEquals(CommandType.LIST, result.getKey());

            //view command
            String viewInput = "view 13/10/25";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(viewInput));

            assertEquals(CommandType.VIEW, result.getKey());
            assertEquals("13/10/25", result.getValue().get(0));

            //no command
            String noInput = "";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(noInput));

            assertEquals(CommandType.NONE, result.getKey());

            //mark command
            String markInput = "mark 1";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(markInput));

            assertEquals(CommandType.MARK, result.getKey());
            assertEquals("1", result.getValue().get(0));

            //unmark command
            String unmarkInput = "unmark 1";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(unmarkInput));

            assertEquals(CommandType.UNMARK, result.getKey());
            assertEquals("1", result.getValue().get(0));

            //delete command
            String deleteInput = "delete 1";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(deleteInput));

            assertEquals(CommandType.DELETE, result.getKey());
            assertEquals("1", result.getValue().get(0));

            //update command
            String updateInput = "update 1, taskName: task1, start: 13/10/25 0000, to: 14/10/25 0000";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(updateInput));

            assertEquals(CommandType.UPDATE, result.getKey());
            assertEquals("1", result.getValue().get(0));
            assertEquals("taskName: task1", result.getValue().get(1));
            assertEquals("start: 13/10/25 0000", result.getValue().get(2));
            assertEquals("to: 14/10/25 0000", result.getValue().get(3));

            //find command
            String findInput = "find task";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(findInput));

            assertEquals(CommandType.FIND, result.getKey());
            assertEquals("task", result.getValue().get(0));

            //clone command
            String cloneInput = "clone 1";

            result = assertDoesNotThrow(() -> UserInputParser.parseUserInput(cloneInput));

            assertEquals(CommandType.CLONE, result.getKey());
            assertEquals("1", result.getValue().get(0));
        }

        @Test
        @DisplayName("Throws InvalidCommandException")
        public void parseUserInput_InvalidCommand() {
            String input = "invalid";

            GrootException grootException = assertThrows(GrootException.InvalidCommandException.class,
                    () -> UserInputParser.parseUserInput(input));

            assertEquals("Invalid command: invalid", grootException.getMessage());
        }
    }
}
