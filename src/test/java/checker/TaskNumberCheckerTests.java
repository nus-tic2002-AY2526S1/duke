package checker;

import enums.CommandType;
import exceptions.TaskNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskNumberCheckerTests {
    @Nested
    @DisplayName("checkTaskNumberFormat()")
    class CheckTaskNumberFormat_Test {
        String taskNumber;

        @Test
        @DisplayName("Success")
        public void checkTaskNumberFormat_Success() {
            taskNumber = "1";

            assertDoesNotThrow(() -> TaskNumberChecker.checkTaskNumberFormat(taskNumber, CommandType.MARK));
        }

        @Test
        @DisplayName("Throws MissingTaskNumberException")
        public void checkTaskNumberFormat_MissingTaskNumber_MissingTaskNumberException() {
            taskNumber = "";
            TaskNumberException taskNumberException =
                    assertThrows(TaskNumberException.MissingTaskNumberException.class,
                    () -> TaskNumberChecker.checkTaskNumberFormat(taskNumber, CommandType.MARK));

            assertEquals("Missing task number. Usage: mark <task number>", taskNumberException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidTaskNumberException")
        public void checkTaskNumberFormat_NotDigit_InvalidTaskNumberException() {
            taskNumber = "a";

            TaskNumberException taskNumberException =
                    assertThrows(TaskNumberException.InvalidTaskNumberException.class,
                    () -> TaskNumberChecker.checkTaskNumberFormat(taskNumber, CommandType.MARK));

            assertEquals("Invalid task number. Usage: mark <task number>", taskNumberException.getMessage());
        }
    }

    @Nested
    @DisplayName("checkTaskNumberValid()")
    class CheckTaskNumberValid_Test {
        int taskNumber;
        final int taskListSize = 2;

        @Test
        @DisplayName("Success ->")
        public void checkTaskNumberValid_Success() {
            taskNumber = 1;

            assertDoesNotThrow(() -> TaskNumberChecker.checkTaskNumberValid(taskNumber, taskListSize));
        }

        @Test
        @DisplayName("Throws TaskNotFoundException")
        public void checkTaskNumberFormat_TaskNumberNotInRange_TaskNotFoundException() {
            taskNumber = 3; //more than task list size

            TaskNumberException markUnmarkDeleteException =
                    assertThrows(TaskNumberException.TaskNotFoundException.class,
                    () -> TaskNumberChecker.checkTaskNumberValid(taskNumber, taskListSize));

            assertEquals("Task not found in task list.", markUnmarkDeleteException.getMessage());

            taskNumber = 0; //task number cannot be less than 1

            markUnmarkDeleteException = assertThrows(TaskNumberException.TaskNotFoundException.class,
                    () -> TaskNumberChecker.checkTaskNumberValid(taskNumber, taskListSize));

            assertEquals("Task not found in task list.", markUnmarkDeleteException.getMessage());
        }
    }

    @Nested
    class CheckTaskStatusValid_Test {
        @Test
        @DisplayName("Success")
        public void checkTaskStatusValid_Success() {
            assertDoesNotThrow(() -> TaskNumberChecker.checkTaskStatus(true, false));
            assertDoesNotThrow(() -> TaskNumberChecker.checkTaskStatus(false, true));
        }

        @Test
        @DisplayName("Throws TaskAlreadyMarkedException")
        public void checkTaskStatusValid_TaskAlreadyMarked_TaskAlreadyMarkedException() {
            TaskNumberException markUnmarkDeleteException =
                    assertThrows(TaskNumberException.TaskAlreadyMarkedException.class,
                    () -> TaskNumberChecker.checkTaskStatus(true, true));

            assertEquals("Task is already marked in task list as done.", markUnmarkDeleteException.getMessage());

            markUnmarkDeleteException = assertThrows(TaskNumberException.TaskAlreadyMarkedException.class,
                    () -> TaskNumberChecker.checkTaskStatus(false, false));

            assertEquals("Task is already marked in task list as not done.", markUnmarkDeleteException.getMessage());
        }
    }
}
