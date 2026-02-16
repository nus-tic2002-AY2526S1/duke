package parser.userInputParser;

import enums.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventParserTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> event =
            new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

    @Nested
    @DisplayName("parseEvent()")
    class ParseEvent_Test {
        @Test
        @DisplayName("Success")
        public void parseEvent_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> eventCheck =
                    new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            event.getValue().add("task /from 13/10/25 0000 /to 14/10/25 0000");
            eventCheck.getValue().add("task");
            eventCheck.getValue().add("13/10/25 0000");
            eventCheck.getValue().add("14/10/25 0000");

            assertDoesNotThrow(() -> EventParser.parseEvent(event));
            assertEquals(event, eventCheck);
        }
    }
}
