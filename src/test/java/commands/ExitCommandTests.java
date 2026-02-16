package commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExitCommandTests {
    ArrayList<Task> tasklist = new ArrayList<>();

    @Nested
    @DisplayName("ExitCommand()")
    public class ExitCommand_Test {
        @Test
        @DisplayName("Success")
        public void ExitCommand_Success() {
            assertDoesNotThrow(() -> new ExitCommand(tasklist));
        }
    }
}
