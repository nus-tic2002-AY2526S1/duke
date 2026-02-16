package parser.userInputParser;

import enums.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskNumberParserTests {

    @Nested
    @DisplayName("parseTaskNumber")
    class parseTaskNumber_Test {
        @Test
        @DisplayName("Success")
        public void parseTaskNumber_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> command =
                    new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            command.getValue().add("1");

            assertDoesNotThrow(() -> TaskNumberParser.parseTaskNumber(command));
        }
    }

    @Nested
    @DisplayName("getTaskNumber()")
    class GetTaskNumber_Test {
        @Test
        @DisplayName("Success")
        public void getTaskNumber_Success() {
            int taskNumber = assertDoesNotThrow(() -> TaskNumberParser.getTaskNumber("1", 2));

            assertEquals(1, taskNumber);
        }
    }
}
