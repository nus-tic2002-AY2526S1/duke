package commands;

import enums.CommandType;
import exceptions.DeadlineException;
import exceptions.EventException;
import exceptions.ViewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Deadline;
import tasks.Task;
import tasks.ToDo;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>();

    @Nested
    @DisplayName("Command()")
    class Command_Test {
        @Test
        @DisplayName("Add Todo Success")
        public void Command_Add_Success() {
            //create AddCommand for TODO command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.TODO, new ArrayList<>());

            commandLine.getValue().add("task");

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(AddCommand.class, command);

            //create AddCommand for DEADLINE command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DEADLINE, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("14/10/25 0000");

            command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(AddCommand.class, command);

            //create AddCommand for EVENT command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("14/10/25 0000");
            commandLine.getValue().add("15/10/25 0000");

            command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(AddCommand.class, command);
        }

        @Test
        @DisplayName("Mark Success")
        public void CommandTest_Mark_Success() {
            //create MarkTaskCommand for MARK command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.MARK, new ArrayList<>());

            tasklist.add(new ToDo("task"));
            commandLine.getValue().add("1");

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(MarkTaskCommand.class, command);

            //create MarkTaskCommand for UNMARK command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.UNMARK, new ArrayList<>());

            tasklist.add(new ToDo("task"));
            tasklist.get(0).setIsDone(true);
            commandLine.getValue().add("1");

            command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(MarkTaskCommand.class, command);
        }

        @Test
        @DisplayName("List Success")
        public void CommandTest_List_Success() {
            //create ShowListCommand for LIST command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.LIST, new ArrayList<>());

            tasklist.add(new ToDo("task"));

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(ShowListCommand.class, command);

            //create ShowListCommand for VIEW command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>());

            commandLine.getValue().add("14/10/25");
            tasklist.add(new Deadline("task", LocalDateTime.parse("2025-10-14T00:00:00")));

            command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(ShowListCommand.class, command);
        }

        @Test
        @DisplayName("Delete Success")
        public void CommandTest_Delete_Success() {
            //create DeleteCommand for DELETE command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DELETE, new ArrayList<>());

            commandLine.getValue().add("1");
            tasklist.add(new Deadline("task", LocalDateTime.parse("2025-10-14T00:00:00")));

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(DeleteCommand.class, command);
        }

        @Test
        @DisplayName("No Command Success")
        public void CommandTest_NoCommand_Success() {
            //no command created for NONE command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.NONE, new ArrayList<>());

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertNull(command);
        }

        @Test
        @DisplayName("Exit Success")
        public void CommandTest_Exit_Success() {
            //create ExitCommand for BYE command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.BYE, new ArrayList<>());

            Command command = assertDoesNotThrow(() -> Command.createCommand(commandLine, tasklist));

            assertInstanceOf(ExitCommand.class, command);
        }

        @Test
        @DisplayName("Add Deadline Throws InvalidDateTimeException")
        public void CommandTest_InvalidDateTime_InvalidDateTimeException() {
            //incorrect date and time format for DEADLINE command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.DEADLINE, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("14/10/25");

            DeadlineException deadlineException =
                    assertThrows(DeadlineException.InvalidDeadlineDateTimeException.class,
                            () -> Command.createCommand(commandLine, tasklist));

            assertEquals("Invalid date/time. Usage: deadline <task name> /by <dd/MM/yyyy> <HH:mm (24-hour)>",
                    deadlineException.getMessage());

            //incorrect date and time format for EVENT command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.EVENT, new ArrayList<>());

            commandLine.getValue().add("task");
            commandLine.getValue().add("14/10/25");
            commandLine.getValue().add("15/10/25 0000");

            EventException eventException =
                    assertThrows(EventException.InvalidEventDateTimeException.class,
                            () -> Command.createCommand(commandLine, tasklist));

            assertEquals("Invalid date/time. Usage: event <task name> /from <dd/MM/yyyy> <HH:mm (24-hour)> " +
                            "/to <dd/MM/yyyy> <HH:mm (24-hour)>",
                    eventException.getMessage());

            //incorrect date for VIEW command
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>());

            commandLine.getValue().add("101425");
            tasklist.add(new Deadline("task", LocalDateTime.parse("2025-10-14T00:00:00")));

            ViewException viewException = assertThrows(ViewException.InvalidViewDateException.class,
                    () -> Command.createCommand(commandLine, tasklist));

            assertEquals("Invalid date. Usage: view <dd/MM/yyyy>", viewException.getMessage());
        }
    }
}
