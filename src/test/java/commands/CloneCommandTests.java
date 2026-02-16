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

public class CloneCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>(List.of(new ToDo("task")));

    @Nested
    @DisplayName("MarkTaskCommand()")
    class CloneCommand_Test {
        @Test
        @DisplayName("Success")
        public void CloneCommand_Success() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.CLONE, new ArrayList<>());

            commandLine.getValue().add("1");

            assertDoesNotThrow(() -> new CloneCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws TaskNotFoundException")
        public void CloneCommand_TaskNotInRange_TaskNotFoundException() {
            //task number more than tasklist size
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.CLONE, new ArrayList<>());

            commandLine.getValue().add("2");

            TaskNumberException taskNumberException =
                    assertThrows(TaskNumberException.TaskNotFoundException.class,
                            () -> new CloneCommand(commandLine, tasklist));

            assertEquals("Task not found in task list.", taskNumberException.getMessage());

            //task number less than 0
            commandLine.getValue().set(0, "0");

            taskNumberException = assertThrows(TaskNumberException.TaskNotFoundException.class,
                    () -> new CloneCommand(commandLine, tasklist));

            assertEquals("Task not found in task list.", taskNumberException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    public class Execute_Test {
        @Test
        @DisplayName("Success")
        public void execute_Success() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.CLONE, new ArrayList<>());

            commandLine.getValue().add("1");

            CloneCommand cloneCommand = assertDoesNotThrow(() -> new CloneCommand(commandLine, tasklist));
            cloneCommand.execute();

            assertEquals(2,tasklist.size());
            assertEquals(tasklist.get(1).getDescription(), tasklist.get(0).getDescription());
            assertInstanceOf(ToDo.class, tasklist.get(0));
        }
    }
}
