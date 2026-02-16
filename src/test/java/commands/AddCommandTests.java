package commands;

import enums.CommandType;
import exceptions.DateTimeException;
import exceptions.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AddCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>();

    @Nested
    @DisplayName("AddCommand()")
    class AddCommand_Test {
        @Test
        @DisplayName("Success")
        public void AddCommand_Success() {
            //todo
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            commandLine.getValue().add("task");

            assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));

            //deadline
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DEADLINE, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 0000");

            assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));

            //event
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 00:00");
            commandLine.getValue().add("13/10/25 1200");

            assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws DateTimeException")
        public void AddCommandTest_InvalidDateTimeFormat_DateTimeException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25");
            commandLine.getValue().add("13/10/25 1200");

            DateTimeException dateTimeException = assertThrows(DateTimeException.class,
                    () -> new AddCommand(commandLine, tasklist));

            assertEquals("", dateTimeException.getMessage());
        }
    }

    @Nested
    @DisplayName("createTask()")
    class CreateTask_Test {
        @Test
        @DisplayName("Success")
        public void createTask_Success() {
            //create todo task
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            commandLine.getValue().add("task");

            assertDoesNotThrow(() -> AddCommand.createTask(commandLine));

            //create deadline task
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DEADLINE, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 0000");

            assertDoesNotThrow(() -> AddCommand.createTask(commandLine));

            //create event task
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 0000");
            commandLine.getValue().add("13/10/25 1200");

            assertDoesNotThrow(() -> AddCommand.createTask(commandLine));
        }

        @Test
        @DisplayName("Throws FileCorruptedException")
        public void createTask_IncorrectFormat_FileCorruptedException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            FileException fileException = assertThrows(FileException.FileCorruptedException.class,
                    () -> AddCommand.createTask(commandLine));

            assertEquals("Tasklist file is corrupted.", fileException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    class Execute_Test {
        @Test
        @DisplayName("Success")
        public void execute_Success() {
            //add todo task success
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            commandLine.getValue().add("task");

            AddCommand addCommand = assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));
            addCommand.execute();

            assertEquals(1, tasklist.size());
            assertInstanceOf(ToDo.class, tasklist.get(0));

            //add deadline task success
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DEADLINE, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 0000");

            addCommand = assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));
            addCommand.execute();

            assertEquals(2, tasklist.size());
            assertInstanceOf(Deadline.class, tasklist.get(1));

            //add event task success
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("13/10/25 0000");
            commandLine.getValue().add("13/10/25 1200");

            addCommand = assertDoesNotThrow(() -> new AddCommand(commandLine, tasklist));
            addCommand.execute();

            assertEquals(3, tasklist.size());
            assertInstanceOf(Event.class, tasklist.get(2));
        }
    }
}
