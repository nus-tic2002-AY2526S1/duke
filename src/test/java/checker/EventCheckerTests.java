package checker;

import exceptions.EventException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class EventCheckerTests {
    @Nested
    @DisplayName("checkEventKeywords()")
    class CheckEventKeywords_Test {
        private String keywordTest;

        @Test
        @DisplayName("Success")
        public void checkEventKeywords_Success() {
            keywordTest = "event task /from 13/10/25 1100 /to 13/10/25 1700";

            assertDoesNotThrow(() -> EventChecker.checkEventKeywords(keywordTest));
        }

        @Test
        @DisplayName("Throws MissingEventKeywords")
        public void checkEventKeywords_MissingFromAndTo_MissingEventKeywordsException() {
            keywordTest = "event task";

            EventException eventException = assertThrows(EventException.MissingEventKeywordsException.class,
                    () -> EventChecker.checkEventKeywords(keywordTest));

            assertEquals("Missing /from and /to keywords in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>",
                    eventException.getMessage());
        }

        @Test
        @DisplayName("Throws MissingEventFromKeyword")
        public void checkEventKeywords_MissingFrom_MissingEventFromKeywordException() {
            keywordTest = "event task /to";

            EventException eventException = assertThrows(EventException.MissingEventFromKeywordException.class,
                    () -> EventChecker.checkEventKeywords(keywordTest));

            assertEquals("Missing /from keyword in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>",
                    eventException.getMessage());
        }

        @Test
        @DisplayName("Throws MissingEventToKeyword")
        public void checkEventKeywords_MissingTo_MissingEventToKeywordException() {
            keywordTest = "event task /from";

            EventException eventException = assertThrows(EventException.MissingEventToKeywordException.class,
                    () -> EventChecker.checkEventKeywords(keywordTest));

            assertEquals("Missing /to keyword in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>",
                    eventException.getMessage());
        }
    }

    @Nested
    @DisplayName("checkEventFormat()")
    class CheckEventFormat_Test {
        private final ArrayList<String> formatTest = new ArrayList<>();

        @Test
        @DisplayName("Success")
        public void checkEventFormat_Success() {
            formatTest.add("task");
            formatTest.add("22/10/22 00:00");
            formatTest.add("22/10/22 1200");

            assertDoesNotThrow(() -> EventChecker.checkEventFormat(formatTest));
        }

        @Test
        @DisplayName("Throws MissingTaskNameException")
        public void checkEventFormat_MissingTaskName_MissingTaskNameException() {
            formatTest.add("");
            formatTest.add("22/10/22 00:00");
            formatTest.add("22/10/22 1200");

            EventException eventException = assertThrows(EventException.MissingEventTaskNameException.class
                    , () -> EventChecker.checkEventFormat(formatTest));

            assertEquals("Missing task name in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>"
                    , eventException.getMessage());
        }

        @Test
        @DisplayName("Throws MissingEventFromException")
        public void checkEventFormat_MissingFromDate_MissingEventFromException() {
            formatTest.add("task");
            formatTest.add("");
            formatTest.add("22/10/22 1200");

            EventException eventException = assertThrows(EventException.MissingEventFromException.class
                    , () -> EventChecker.checkEventFormat(formatTest));

            assertEquals("Missing from-date and time in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>"
                    , eventException.getMessage());
        }

        @Test
        @DisplayName("Throws MissingEventToException")
        public void checkEventFormat_MissingToDate_MissingEventToException() {
            formatTest.add("task");
            formatTest.add("22/10/22 00:00");
            formatTest.add("");

            EventException eventException = assertThrows(EventException.MissingEventToException.class
                    , () -> EventChecker.checkEventFormat(formatTest));

            assertEquals("Missing to-date and time in event command. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>"
                    , eventException.getMessage());
        }

        @Test
        @DisplayName("Throws EventException")
        public void checkEventFormat_MissingInfo_EventException() {

            EventException eventException = assertThrows(EventException.class
                    , () -> EventChecker.checkEventFormat(formatTest));

            assertEquals("Invalid event format. Usage: event <task name> " +
                            "/from <dd/MM/yyyy> <HH:mm (24-hour)> /to <dd/MM/yyyy> <HH:mm (24-hour)>"
                    , eventException.getMessage());
        }
    }
}
