package parser;

import exceptions.DateTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeParserTests {
    String[] acceptedDateTimeFormats = {
            // '-' separator
            // single digit days and months
            "1-1-25 00:00",
            "1-1-25 0000",
            "1-1-2025 00:00",
            "1-1-2025 0000",
            // single digit days
            "1-10-25 00:00",
            "1-10-25 0000",
            "1-10-2025 00:00",
            "1-10-2025 0000",
            // single digit months
            "10-1-25 00:00",
            "10-1-25 0000",
            "10-1-2025 00:00",
            "10-1-2025 0000",
            //standard
            "10-10-25 00:00",
            "10-10-25 0000",
            "10-10-2025 00:00",
            "10-10-2025 0000",

            // '/' separator
            // single digit days and months
            "1/1/25 00:00",
            "1/1/25 0000",
            "1/1/2025 00:00",
            "1/1/2025 0000",
            // single digit days
            "1/10/25 00:00",
            "1/10/25 0000",
            "1/10/2025 00:00",
            "1/10/2025 0000",
            // single digit months
            "10/1/25 00:00",
            "10/1/25 0000",
            "10/1/2025 00:00",
            "10/1/2025 0000",
            //standard
            "10/10/25 00:00",
            "10/10/25 0000",
            "10/10/2025 00:00",
            "10/10/2025 0000",
            // month in 3 letters
            "10 Oct 2025 0000", //used in file
    };
    String[] acceptedDateFormats = {
            // '-' separator
            "1-1-25",
            "1-1-2025",
            "1-10-25",
            "1-10-2025",
            "10-1-25",
            "10-1-2025",
            "10-10-25",
            "10-10-2025",

            // '/' separator
            "1/1/25",
            "1/1/2025",
            "1/10/25",
            "1/10/2025",
            "10/1/25",
            "10/1/2025",
            "10/10/25",
            "10/10/2025",
    };

    @Nested
    @DisplayName("parseDateTime()")
    class ParseDateTime_Test {
        @Test
        @DisplayName("Success")
        void parseDateTime_Success() {
            for (String date : acceptedDateTimeFormats) {
                assertDoesNotThrow(() -> DateTimeParser.parseDateTime(date));
            }
        }

        @Test
        @DisplayName("InvalidDate Throws DateTimeException")
        void parseDateTime_InvalidDate_DateTimeException() {
            String date = "131025 0000";

            assertThrows(DateTimeException.class, () -> DateTimeParser.parseDateTime(date));
        }

        @Test
        @DisplayName("InvalidTime Throws DateTimeException")
        void parseDateTime_InvalidTime_DateTimeException() {
            String date = "13-10-25 2500";

            assertThrows(DateTimeException.class, () -> DateTimeParser.parseDateTime(date));
        }
    }

    @Nested
    @DisplayName("parseDate()")
    class ParseDate_Test {
        @Test
        @DisplayName("Success")
        void parseDate_Success() {
            for (String date : acceptedDateFormats) {
                assertDoesNotThrow(() -> DateTimeParser.parseDate(date));
            }
        }

        @Test
        @DisplayName("InvalidDate Throws DateTimeException")
        void parseDate_InvalidDate_DateTimeException() {
            String date = "13-13-25";

            assertThrows(DateTimeException.class, () -> DateTimeParser.parseDate(date));
        }
    }

    @Nested
    @DisplayName("formatDateTime()")
    class FormatDateTimeTests {
        @Test
        @DisplayName("Success")
        void formatDateTime_Success() {
            LocalDateTime date = LocalDateTime.parse("2025-05-25T00:00:00");

            String dateTime = assertDoesNotThrow(() -> DateTimeParser.formatDateTime(date));

            assertEquals("25 May 2025 0000", dateTime);
        }
    }
}
