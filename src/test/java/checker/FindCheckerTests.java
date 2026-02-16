package checker;

import exceptions.FindException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FindCheckerTests {
    @Nested
    @DisplayName("checkKeyword()")
    class CheckKeyword_Test {
        String keyword;

        @Test
        @DisplayName("Success")
        public void checkKeyword_Success() {
            keyword = "task1";

            assertDoesNotThrow(() -> FindChecker.checkKeyword(keyword));
        }

        @Test
        @DisplayName("Throws MissingKeywordException")
        public void checkKeyword_MissingKeyword_MissingKeywordException() {
            keyword = "";

            FindException findException = assertThrows(FindException.MissingKeywordException.class,
                    () -> FindChecker.checkKeyword(keyword));

            assertEquals("Keyword to find is missing. Usage: find <keyword>", findException.getMessage());
        }
    }
}
