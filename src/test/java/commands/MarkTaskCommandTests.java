package commands;

import enums.CommandType;
import exceptions.TaskNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.ToDo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarkTaskCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>(List.of(new ToDo("task")));

    @Nested
    @DisplayName("MarkTaskCommand()")
    class MarkTaskCommand_Test {
        @Test
        @DisplayName("Mark Success")
        public void MarkTaskCommand_Success() {
            //mark
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            commandLine.getValue().add("1");

            assertDoesNotThrow(() -> new MarkTaskCommand(commandLine, tasklist));

            //unmark
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.UNMARK, new ArrayList<>());

            tasklist.get(0).setIsDone(true);
            commandLine.getValue().add("1");

            assertDoesNotThrow(() -> new MarkTaskCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws TaskNotFoundException")
        public void MarkTaskCommand_TaskNotInRange_TaskNotFoundException() {
            //task number more than tasklist size
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            commandLine.getValue().add("2");

            TaskNumberException markUnmarkDeleteException =
                    assertThrows(TaskNumberException.TaskNotFoundException.class,
                            () -> new MarkTaskCommand(commandLine, tasklist));

            assertEquals("Task not found in task list.", markUnmarkDeleteException.getMessage());

            //task number less than 0
            commandLine.getValue().set(0, "0");

            markUnmarkDeleteException = assertThrows(TaskNumberException.TaskNotFoundException.class,
                    () -> new MarkTaskCommand(commandLine, tasklist));

            assertEquals("Task not found in task list.", markUnmarkDeleteException.getMessage());
        }

        @Test
        @DisplayName("Throws TaskAlreadyMarkedException")
        public void MarkTaskCommand_TaskAlreadyMarked_TaskAlreadyMarkedException() {
            //task already marked as done
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            commandLine.getValue().add("1");
            tasklist.get(0).setIsDone(true);

            TaskNumberException markUnmarkDeleteException =
                    assertThrows(TaskNumberException.TaskAlreadyMarkedException.class,
                            () -> new MarkTaskCommand(commandLine, tasklist));

            assertEquals("Task is already marked in task list as done.", markUnmarkDeleteException.getMessage());

            //task already marked as not done
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.UNMARK, new ArrayList<>());

            commandLine.getValue().add("1");
            tasklist.get(0).setIsDone(false);

            markUnmarkDeleteException = assertThrows(TaskNumberException.TaskAlreadyMarkedException.class,
                    () -> new MarkTaskCommand(commandLine, tasklist));

            assertEquals("Task is already marked in task list as not done.", markUnmarkDeleteException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    public class Execute_Test {
        @Test
        @DisplayName("Success")
        public void execute_Success() {
            //mark
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            commandLine.getValue().add("1");

            MarkTaskCommand markTaskCommand = assertDoesNotThrow(() -> new MarkTaskCommand(commandLine, tasklist));
            markTaskCommand.execute();

            assertTrue(tasklist.get(0).getIsDone());

            //unmark
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.UNMARK, new ArrayList<>());

            commandLine.getValue().add("1");

            markTaskCommand = assertDoesNotThrow(() -> new MarkTaskCommand(commandLine, tasklist));
            markTaskCommand.execute();

            assertFalse(tasklist.get(0).getIsDone());
        }
    }
}
