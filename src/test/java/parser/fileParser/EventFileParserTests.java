package parser.fileParser;

import exceptions.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventFileParserTests {
    @Nested
    @DisplayName("parseEventFile()")
    class ParseEventFileTest {
        @Test
        @DisplayName("Success")
        void parseEventFile_Success() {
            ArrayList<String> input = new ArrayList<>(List.of("task1 | from: 13/10/25 0000, to: 14/10/25 0000"));
            ArrayList<String> expectedOutput = new ArrayList<>(List.of("task1", "13/10/25 0000", "14/10/25 0000"));

            assertDoesNotThrow(() -> EventFileParser.parseEventFile(input));
            assertEquals(expectedOutput, input);
        }

        @Test
        @DisplayName("Missing Task Name Throws FileCorruptedException")
        void parseTodoFile_MissingTaskName_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("| from: 13/10/25 0000, to: 14/10/25 0000"));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing From Throws FileCorruptedException")
        void parseTodoFile_MissingFromKeyword_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 13/10/25 0000, to: 14/10/25 0000 "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing From-DateTime Throws FileCorruptedException")
        void parseTodoFile_MissingFromDateTime_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 | from: , to: 14/10/25 0000"));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing To Throws FileCorruptedException")
        void parseTodoFile_MissingToKeyword_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 | from: 13/10/25 0000, 14/10/25 0000 "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing To-DateTime Throws FileCorruptedException")
        void parseTodoFile_MissingToDateTime_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 | from: 13/10/25 0000, to: "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }
    }
}
