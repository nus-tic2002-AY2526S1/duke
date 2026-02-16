package parser.userInputParser;

import enums.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateParserTests {
    @Nested
    @DisplayName("parseUpdate()")
    class parseUpdate_Test {
        @Test
        @DisplayName("Success")
        public void parseUpdate_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> update =
                    new AbstractMap.SimpleEntry<>(CommandType.UPDATE, new ArrayList<>());

            update.getValue().add("1, task name: task2, start: 13/10/25 0000"); //task number

            assertDoesNotThrow(() -> UpdateParser.parseUpdate(update));
            assertEquals("1", update.getValue().get(0));
            assertEquals("task name: task2", update.getValue().get(1));
            assertEquals("start: 13/10/25 0000", update.getValue().get(2));
        }
    }
}
