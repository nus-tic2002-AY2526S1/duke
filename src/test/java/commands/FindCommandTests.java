package commands;

import enums.CommandType;
import exceptions.FindException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FindCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine =
            new AbstractMap.SimpleEntry<>(CommandType.FIND, new ArrayList<>());
    ArrayList<Task> tasklist = new ArrayList<>();

    public FindCommandTests() {
        tasklist.add(new ToDo("read book"));
        tasklist.add(new Deadline("return book", LocalDateTime.parse("2025-10-22T22:00:00")));
        tasklist.add(new Event("work", LocalDateTime.parse("2025-10-23T22:00:00"),
                LocalDateTime.parse("2025-10-22T23:00:00")));
    }

    @Nested
    @DisplayName("FindCommand()")
    public class FindCommand_Test {
        @Test
        @DisplayName("Success")
        public void findCommand_Success() {
            commandLine.getValue().add("book");

            assertDoesNotThrow(() -> new FindCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws TaskNotFoundException")
        public void findCommand_EmptyList_TaskNotFoundException() {
            commandLine.getValue().add("task");

            FindException findException = assertThrows(FindException.TaskNotFoundException.class,
                    () -> new FindCommand(commandLine, tasklist));

            assertEquals("No tasks found.", findException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    public class Execute_Test {
        @Test
        @DisplayName("Success")
        public void execute_Success() {
            commandLine.getValue().add("book");

            Command command = assertDoesNotThrow(() -> new FindCommand(commandLine, tasklist));

            assertDoesNotThrow(command::execute);
        }
    }
}
