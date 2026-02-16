package parser.userInputParser;

import enums.CommandType;
import exceptions.FindException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FindParserTests {
    @Nested
    @DisplayName("parseFind()")
    public class parseFind_Test {
        @Test
        @DisplayName("Success")
        public void parseFind_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine =
                    new AbstractMap.SimpleEntry<>(CommandType.FIND, new ArrayList<>());

            commandLine.getValue().add("task");

            assertDoesNotThrow(()->FindParser.parseFind(commandLine));
        }

        @Test
        @DisplayName("Throws MissingKeywordException")
        public void parseFind_MissingKeyword_MissingKeywordException() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine =
                    new AbstractMap.SimpleEntry<>(CommandType.FIND, new ArrayList<>());

            commandLine.getValue().add("");

            FindException findException = assertThrows(FindException.MissingKeywordException.class,
                    ()->FindParser.parseFind(commandLine));

            assertEquals("Keyword to find is missing. Usage: find <keyword>", findException.getMessage());
        }
    }
}
