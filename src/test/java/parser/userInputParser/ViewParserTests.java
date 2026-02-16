package parser.userInputParser;

import enums.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ViewParserTests {
    @Nested
    @DisplayName("parseView()")
    class ParseViewTest {
        @Test
        @DisplayName("Success")
        public void parseView_Success() {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> view =
                    new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>(List.of("13/10/25")));

            assertDoesNotThrow(() -> ViewParser.parseView(view));
        }
    }
}
