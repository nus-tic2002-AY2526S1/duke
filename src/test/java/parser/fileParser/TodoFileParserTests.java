package parser.fileParser;

import exceptions.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodoFileParserTests {
    @Nested
    @DisplayName("parseTodoFile()")
    class ParseTodoFileTest {
        @Test
        @DisplayName("Success")
        void parseTodoFile_Success() {
            ArrayList<String> input = new ArrayList<>(List.of("task1"));

            assertDoesNotThrow(() -> TodoFileParser.parseTodoFile(input));
        }

        @Test
        @DisplayName("Empty Task Name Throws FileCorruptedException")
        void parseTodoFile_EmptyTaskName_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>(List.of(" "));

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> TodoFileParser.parseTodoFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }

        @Test
        @DisplayName("Missing TaskName Throws FileCorruptedException")
        void parseTodoFile_MissingTaskName_FileCorruptedException() {
            ArrayList<String> input = new ArrayList<>();

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> TodoFileParser.parseTodoFile(input));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }
    }
}
