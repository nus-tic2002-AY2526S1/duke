package checker;

import exceptions.TodoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TodoCheckerTests {
    @Nested
    @DisplayName("checkTodoTaskName()")
    class CheckTodoTaskNameTest {
        String taskName;

        @Test
        @DisplayName("Success")
        public void checkTodoTaskName_Success() {
            taskName = "task1";

            assertDoesNotThrow(() -> TodoChecker.checkTodoTaskName(taskName));
        }

        @Test
        @DisplayName("Throws MissingTodoTaskNameException")
        public void checkTodoTaskName_MissingTaskName_MissingTodoTaskNameException() {
            taskName = "";

            TodoException todoException = assertThrows(TodoException.MissingTodoTaskNameException.class,
                    () -> TodoChecker.checkTodoTaskName(taskName));

            assertEquals("Missing task name in todo command. Usage: todo <task name>", todoException.getMessage());
        }
    }
}
