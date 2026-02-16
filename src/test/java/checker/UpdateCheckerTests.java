package checker;

import exceptions.UpdateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCheckerTests {

    @Nested
    @DisplayName("checkTodoValid()")
    class CheckTodoValid_Test {
        ArrayList<String> todoUpdate = new ArrayList<>();

        @Test
        @DisplayName("Success")
        public void checkTodoValid_Success() {
            todoUpdate.add("taskName");
            todoUpdate.add("task1");

            assertDoesNotThrow(() -> UpdateChecker.checkTodoUpdateValid(todoUpdate));
        }

        @Test
        @DisplayName("Throws InvalidUpdateTodoFieldException")
        public void checkTodoValid_MissingFieldForUpdate_InvalidUpdateTodoFieldException() {
            todoUpdate.add("");
            todoUpdate.add("task1");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateTodoFieldException.class,
                    () -> UpdateChecker.checkTodoUpdateValid(todoUpdate));

            assertEquals("Invalid update field for todo task. Usage: update <task number>, taskName: <update-info>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateTodoInfoException")
        public void checkTodoValid_EmptyInfo_InvalidUpdateInfoFieldException() {
            todoUpdate.add("taskName");
            todoUpdate.add("");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateTodoInfoException.class,
                    () -> UpdateChecker.checkTodoUpdateValid(todoUpdate));

            assertEquals("Invalid update info for todo task. Usage: update <task number>, taskName: <update-info>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateFormatException")
        public void checkTodoValid_MissingInfo_InvalidUpdateFormatException() {
            todoUpdate.add("taskName");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateFormatException.class,
                    () -> UpdateChecker.checkTodoUpdateValid(todoUpdate));

            assertEquals("Invalid update format. Usage: update <task number>, <update-field>: <update-info>",
                    updateException.getMessage());
        }
    }

    @Nested
    @DisplayName("checkDeadlineValid()")
    class CheckDeadlineValid_Test {
        ArrayList<String> deadlineUpdate = new ArrayList<>();

        @Test
        @DisplayName("Success")
        public void checkDeadlineValid_Success() {
            deadlineUpdate.add("taskName");
            deadlineUpdate.add("task1");

            assertDoesNotThrow(() -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));

            deadlineUpdate.clear();

            deadlineUpdate.add("by");
            deadlineUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));

            deadlineUpdate.clear();

            deadlineUpdate.add("taskName");
            deadlineUpdate.add("task1");
            deadlineUpdate.add("by");
            deadlineUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));
        }

        @Test
        @DisplayName("Throws InvalidUpdateDeadlineFieldException")
        public void checkDeadlineValid_MissingFieldForUpdate_InvalidUpdateDeadlineFieldException() {
            deadlineUpdate.add("");
            deadlineUpdate.add("task1");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateDeadlineFieldException.class,
                    () -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));

            assertEquals("Invalid update field for deadline task. Usage: update <task number>, taskName: " +
                            "<update-info>, by: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateDeadlineInfoException")
        public void checkDeadlineValid_EmptyInfo_InvalidUpdateInfoFieldException() {
            deadlineUpdate.add("taskName");
            deadlineUpdate.add("");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateDeadlineInfoException.class,
                    () -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));

            assertEquals("Invalid update info for deadline task. Usage: update <task number>, taskName: " +
                            "<update-info>, by: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateFormatException")
        public void checkDeadlineValid_MissingInfo_InvalidUpdateFormatException() {
            deadlineUpdate.add("taskName");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateFormatException.class,
                    () -> UpdateChecker.checkDeadlineUpdateValid(deadlineUpdate));

            assertEquals("Invalid update format. Usage: update <task number>, <update-field>: <update-info>",
                    updateException.getMessage());
        }
    }

    @Nested
    @DisplayName("checkEventValid()")
    class CheckEventValid_Test {
        ArrayList<String> eventUpdate = new ArrayList<>();

        @Test
        @DisplayName("Task Name Success")
        public void checkEventValid_TaskName_Success() {
            //update task name only
            eventUpdate.add("taskName");
            eventUpdate.add("task1");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update start only
            eventUpdate.add("start");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update end only
            eventUpdate.add("end");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update task name and start only
            eventUpdate.add("taskName");
            eventUpdate.add("task1");
            eventUpdate.add("start");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update task name and end only
            eventUpdate.add("taskName");
            eventUpdate.add("task1");
            eventUpdate.add("end");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update start and end only
            eventUpdate.add("start");
            eventUpdate.add("13/10/25 0000");
            eventUpdate.add("end");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            eventUpdate.clear();

            //update all
            eventUpdate.add("taskName");
            eventUpdate.add("task1");
            eventUpdate.add("start");
            eventUpdate.add("13/10/25 0000");
            eventUpdate.add("end");
            eventUpdate.add("13/10/25 0000");

            assertDoesNotThrow(() -> UpdateChecker.checkEventUpdateValid(eventUpdate));
        }

        @Test
        @DisplayName("Throws InvalidUpdateEventFieldException")
        public void checkEventValid_MissingFieldForUpdate_InvalidUpdateEventFieldException() {
            eventUpdate.add("");
            eventUpdate.add("task1");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateEventFieldException.class,
                    () -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            assertEquals("Invalid update field for event task. Usage: update <task number>, taskName: " +
                            "<update-info>, start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateEventInfoException")
        public void checkEventValid_EmptyInfo_InvalidUpdateInfoFieldException() {
            eventUpdate.add("taskName");
            eventUpdate.add("");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateEventInfoException.class,
                    () -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            assertEquals("Invalid update info for event task. Usage: update <task number>, taskName: " +
                            "<update-info>, start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Throws InvalidUpdateFormatException")
        public void checkEventValid_MissingInfo_InvalidUpdateFormatException() {
            eventUpdate.add("taskName");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateFormatException.class,
                    () -> UpdateChecker.checkEventUpdateValid(eventUpdate));

            assertEquals("Invalid update format. Usage: update <task number>, <update-field>: <update-info>",
                    updateException.getMessage());
        }
    }
}
