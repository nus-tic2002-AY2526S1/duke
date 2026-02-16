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

public class DeleteCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>(List.of(new ToDo("task")));

    @Nested
    @DisplayName("DeleteCommand()")
    class DeleteCommand_Test {
        @Test
        @DisplayName("Success")
        public void DeleteCommand_Success() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DELETE, new ArrayList<>());

            commandLine.getValue().add("1");
            
            assertDoesNotThrow(() -> new DeleteCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws TaskNotFoundException")
        public void DeleteCommandTest_TaskNumberNotInRange_TaskNotFoundException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DELETE, new ArrayList<>());

            commandLine.getValue().add("2");

            TaskNumberException markUnmarkDeleteException = assertThrows(TaskNumberException.TaskNotFoundException.class,
                    () -> new DeleteCommand(commandLine, tasklist));

            assertEquals("Task not found in task list.", markUnmarkDeleteException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    class ExecuteTest {
        @Test
        @DisplayName("Success")
        public void execute_Success() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            commandLine.getValue().add("1");

            DeleteCommand deleteCommand = assertDoesNotThrow(() -> new DeleteCommand(commandLine, tasklist));
            deleteCommand.execute();

            assertEquals(0, tasklist.size());
        }
    }

}
