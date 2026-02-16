package parser.fileParser;

import exceptions.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineFileParserTests {
    @Nested
    @DisplayName("parseDeadlineFile()")
    class ParseDeadlineFileTest {
        @Test
        @DisplayName("Success")
        void parseDeadlineFile_Success() {
            ArrayList<String> input = new ArrayList<>(List.of("task1 | by: 13/10/25 0000"));
            ArrayList<String> expectedOutput = new ArrayList<>(List.of("task1", "13/10/25 0000"));

            assertDoesNotThrow(() -> DeadlineFileParser.parseDeadlineFile(input));
            assertEquals(expectedOutput, input);
        }

        @Test
        @DisplayName("Missing Task Name Throws FileCorruptedException")
        void parseTodoFile_MissingTaskName_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("| by: 13/10/25 0000 "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing By Throws FileCorruptedException")
        void parseTodoFile_MissingByKeyword_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 13/10/25 0000 "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing By-DateTime Throws FileCorruptedException")
        void parseTodoFile_MissingByDateTime_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of("task 1 | by:"));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> DeadlineFileParser.parseDeadlineFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }
    }
}
