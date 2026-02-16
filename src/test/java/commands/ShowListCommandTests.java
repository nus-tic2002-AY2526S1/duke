package commands;

import enums.CommandType;
import exceptions.DateTimeException;
import exceptions.GrootException;
import exceptions.ViewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tasks.Event;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ShowListCommandTests {
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine;
    ArrayList<Task> tasklist = new ArrayList<>();

    public ShowListCommandTests() {
        LocalDateTime startDateTime = LocalDateTime.parse("2025-10-14T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2025-10-21T00:00:00");
        Event event = new Event("task", startDateTime, endDateTime);
        tasklist.add(event);
    }

    @Nested
    @DisplayName("ShowListCommand()")
    class ShowListCommand_Test {
        @Test
        @DisplayName("Display Success")
        public void ShowListCommand_Success() {
            //list
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.LIST, new ArrayList<>());

            assertDoesNotThrow(() -> new ShowListCommand(commandLine, tasklist));

            //view
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>());

            commandLine.getValue().add("14-10-25");

            assertDoesNotThrow(() -> new ShowListCommand(commandLine, tasklist));
        }

        @Test
        @DisplayName("Throws DateTimeException")
        public void ShowListCommand_InvalidDateTime_DateTimeException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>());

            commandLine.getValue().add("141025");

            DateTimeException dateTimeException = assertThrows(DateTimeException.class,
                    () -> new ShowListCommand(commandLine, tasklist));

            assertEquals("", dateTimeException.getMessage());
        }

        @Test
        @DisplayName("Throws EmptyListException")
        public void ShowListCommand_EmptyList_EmptyListException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.LIST, new ArrayList<>());

            tasklist.clear();

            GrootException grootException = assertThrows(GrootException.EmptyListException.class,
                    () -> new ShowListCommand(commandLine, tasklist));

            assertEquals("There are no tasks yet.", grootException.getMessage());
        }

        @Test
        @DisplayName("Throws NoTasksForViewException")
        public void ShowListCommand_NoTasksOnViewDate_NoTasksForViewException() {
            commandLine = new AbstractMap.SimpleEntry<>(CommandType.VIEW, new ArrayList<>());

            commandLine.getValue().add("01-12-25");

            ViewException viewException = assertThrows(ViewException.NoTaskForViewException.class,
                    () -> new ShowListCommand(commandLine, tasklist));

            assertEquals("No task available for given date.", viewException.getMessage());
        }
    }

}
