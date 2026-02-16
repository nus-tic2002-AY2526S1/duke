package parser.userInputParser;

import enums.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TodoParserTests {
    @Nested
    @DisplayName("parseTodo()")
    class ParseTodoTest {
        @Test
        @DisplayName("Success")
        public void parseTodoTest_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> todo =
                    new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>(List.of("task1")));

            assertDoesNotThrow(() -> TodoParser.parseTodo(todo));
        }
    }
}
