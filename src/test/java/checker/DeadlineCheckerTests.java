package checker;

import exceptions.DeadlineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineCheckerTests {

    @Nested
    @DisplayName("checkDeadlineByKeyword()")
    class CheckDeadlineByKeyword_Test {
        private String keywordTest;

        @Test
        @DisplayName("Success")
        public void checkDeadlineByKeyword_Success() {
            keywordTest = "deadline task /by 02/10/2025 00:00";

            assertDoesNotThrow(() -> DeadlineChecker.checkDeadlineByKeyword(keywordTest));
        }

        @Test
        @DisplayName("Throws MissingByKeywordException")
        public void checkDeadlineByKeyword_MissingBy_MissingByKeywordException() {
            keywordTest = "deadline task 02/10/2025 00:00";

            DeadlineException deadlineException = assertThrows(DeadlineException.MissingDeadlineByKeywordException.class
                    , () -> DeadlineChecker.checkDeadlineByKeyword(keywordTest));

            assertEquals("Missing /by keyword in deadline command. Usage: deadline <task name> " +
                            "/by <dd/MM/yyyy> <HH:mm (24-hour)>", deadlineException.getMessage());
        }
    }

    @Nested
    @DisplayName("checkDeadlineFormat()")
    class CheckDeadlineFormat_Test {
        private final ArrayList<String> formatTest = new ArrayList<>();

        @Test
        @DisplayName("Success")
        public void checkDeadlineFormat_Success() {
            formatTest.add("task");
            formatTest.add("22/10/22 00:00");

            assertDoesNotThrow(() -> DeadlineChecker.checkDeadlineFormat(formatTest));
        }

        @Test
        @DisplayName("Throws MissingTaskNameException")
        public void checkDeadlineFormat_MissingTaskName_MissingTaskNameException() {
            formatTest.add("");
            formatTest.add("22/10/22 00:00");

            DeadlineException deadlineException = assertThrows(DeadlineException.MissingDeadlineTaskNameException.class
                    , () -> DeadlineChecker.checkDeadlineFormat(formatTest));

            assertEquals("Missing task name in deadline command. Usage: deadline <task name> " +
                            "/by <dd/MM/yyyy> <HH:mm (24-hour)>", deadlineException.getMessage());
        }

        @Test
        @DisplayName("Throws MissingDeadlineByException")
        public void checkDeadlineFormat_MissingByDate_MissingDeadlineByException() {
            formatTest.add("task");
            formatTest.add("");

            DeadlineException deadlineException = assertThrows(DeadlineException.MissingDeadlineByException.class
                    , () -> DeadlineChecker.checkDeadlineFormat(formatTest));

            assertEquals("Missing by-date and time in deadline command. Usage: deadline <task name> " +
                            "/by <dd/MM/yyyy> <HH:mm (24-hour)>", deadlineException.getMessage());
        }
    }
}
