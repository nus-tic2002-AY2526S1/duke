package commands;

import enums.CommandType;
import exceptions.UpdateException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCommandTests {
    ArrayList<Task> tasklist = new ArrayList<>(List.of(new ToDo("task1"),
            new Deadline("task2", LocalDateTime.parse(
            "2025-10-13T00:00:00")), new Event("task3", LocalDateTime.parse("2025-10-13T00:00:00"),
            LocalDateTime.parse("2025-10-14T00:00:00"))));
    AbstractMap.SimpleEntry<CommandType, ArrayList<String>> updateInput =
            new AbstractMap.SimpleEntry<>(CommandType.UPDATE, new ArrayList<>());
    @Nested
    @DisplayName("UpdateCommand()")
    class UpdateCommand_Test{

        @Test
        @DisplayName("Success")
        public void UpdateCommand_Success(){
            //update todo taskName
            updateInput.getValue().add("1");
            updateInput.getValue().add("taskName: task4");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update deadline taskName
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("taskName: task5");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update deadline by
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("by: 13/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update deadline taskName and by
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("taskName: task4");
            updateInput.getValue().add("by: 13/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event taskName
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task4");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event start
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("start: 13/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("end: 13/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event taskName and start
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task4");
            updateInput.getValue().add("start: 13/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event taskName and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task5");
            updateInput.getValue().add("end: 14/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event start and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("start: 13/10/25 0000");
            updateInput.getValue().add("end: 14/10/25 0000");

            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));

            //update event taskName, start and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task4");
            updateInput.getValue().add("start: 13/10/25 0000");
            updateInput.getValue().add("end: 14/10/25 0000");
            assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
        }

        @Test
        @DisplayName("Missing Update Field Throws InvalidUpdateFieldException")
        public void UpdateCommand_MissingUpdateField_InvalidUpdateFieldException(){
            //invalid for todo task
            updateInput.getValue().add("1");
            updateInput.getValue().add(":task4");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateTodoFieldException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid update field for todo task. Usage: update <task number>, taskName: " +
                            "<update-info>", updateException.getMessage());

            //invalid for deadline task
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add(": task4");

            updateException = assertThrows(UpdateException.InvalidUpdateDeadlineFieldException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid update field for deadline task. Usage: update <task number>, taskName: " +
                            "<update-info>, by: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());

            //invalid for event task
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add(": task4");

            updateException = assertThrows(UpdateException.InvalidUpdateEventFieldException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid update field for event task. Usage: update <task number>, taskName: " +
                            "<update-info>, start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }

        @Test
        @DisplayName("Missing Update Info Throws InvalidUpdateInfoException")
        public void UpdateCommand_MissingInfo_MissingUpdateInfoException(){
            //missing info for todo task
            updateInput.getValue().add("1");
            updateInput.getValue().add("taskName:");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateTodoInfoException.class,
                    ()->new UpdateCommand(updateInput, tasklist));
            assertEquals("Invalid update info for todo task. Usage: update <task number>, taskName: " +
                            "<update-info>", updateException.getMessage());

            //missing info for deadline task
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("taskName:");

            updateException = assertThrows(UpdateException.InvalidUpdateDeadlineInfoException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid update info for deadline task. Usage: update <task number>, taskName: " +
                            "<update-info>, by: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());

            //missing info for event task
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName:");

            updateException = assertThrows(UpdateException.InvalidUpdateEventInfoException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid update info for event task. Usage: update <task number>, taskName: " +
                            "<update-info>, start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }


        @Test
        @DisplayName("Invalid Date Throws InvalidUpdate<task>InfoException")
        public void UpdateCommand_InvalidDate_InvalidUpdateDateException(){
            //invalid date time for deadline task
            updateInput.getValue().add("2");
            updateInput.getValue().add("by: 13/10/25");

            UpdateException updateException = assertThrows(UpdateException.InvalidUpdateDeadlineDateException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid date for deadline task. Usage: update <task number>, taskName: " +
                            "<update-info>, by: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());

            //invalid date time for event task
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("start: 13/10/25");

            updateException = assertThrows(UpdateException.InvalidUpdateEventDateException.class,
                    ()->new UpdateCommand(updateInput, tasklist));

            assertEquals("Invalid date for event task. Usage: update <task number>, taskName: " +
                            "<update-info>, start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>",
                    updateException.getMessage());
        }
    }

    @Nested
    @DisplayName("execute()")
    class ExecuteTest {
        @Test
        @DisplayName("Update Todo Success")
        public void execute_Success(){
            //update todo taskName
            updateInput.getValue().add("1");
            updateInput.getValue().add("taskName: task4");

            Command command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task4", tasklist.get(0).getDescription());

            //update deadline taskName
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("taskName: task5");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task5", tasklist.get(1).getDescription());

            //update deadline by
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("by: 21/10/25 0800");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals(LocalDateTime.parse("2025-10-21T08:00:00"), ((Deadline)tasklist.get(1)).getBy());

            //update deadline taskName and by
            updateInput.getValue().clear();
            updateInput.getValue().add("2");
            updateInput.getValue().add("taskName: task50");
            updateInput.getValue().add("by: 21/10/25 0801");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task50",  tasklist.get(1).getDescription());
            assertEquals(LocalDateTime.parse("2025-10-21T08:01:00"), ((Deadline)tasklist.get(1)).getBy());

            //update event taskName
            updateInput.getValue().clear();
            updateInput.getValue().add("3"); //Event task
            updateInput.getValue().add("taskName: task6");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task6", tasklist.get(2).getDescription());

            //update event start
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("start: 21/10/25 0800");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals(LocalDateTime.parse("2025-10-21T08:00:00"), ((Event)tasklist.get(2)).getStartDateTime());

            //update event end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("end: 21/10/25 0800");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals(LocalDateTime.parse("2025-10-21T08:00:00"), ((Event)tasklist.get(2)).getEndDateTime());

            //update event taskName and start
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task60");
            updateInput.getValue().add("start: 21/10/25 0801");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task60",  tasklist.get(2).getDescription());
            assertEquals(LocalDateTime.parse("2025-10-21T08:01:00"),
                    ((Event)tasklist.get(2)).getStartDateTime());

            //update event taskName and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task61");
            updateInput.getValue().add("end: 21/10/25 0802");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task61",  tasklist.get(2).getDescription());
            assertEquals(LocalDateTime.parse("2025-10-21T08:02:00"),
                    ((Event)tasklist.get(2)).getEndDateTime());

            //update event start and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("start: 21/10/25 0000");
            updateInput.getValue().add("end: 21/10/25 0803");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals(LocalDateTime.parse("2025-10-21T00:00:00"),
                    ((Event)tasklist.get(2)).getStartDateTime());
            assertEquals(LocalDateTime.parse("2025-10-21T08:03:00"),
                    ((Event)tasklist.get(2)).getEndDateTime());

            //update event taskName, start and end
            updateInput.getValue().clear();
            updateInput.getValue().add("3");
            updateInput.getValue().add("taskName: task6");
            updateInput.getValue().add("start: 21/10/25 0000");
            updateInput.getValue().add("end: 21/10/25 0800");

            command = assertDoesNotThrow(()->new UpdateCommand(updateInput, tasklist));
            assertDoesNotThrow(command::execute);

            assertEquals("task6",  tasklist.get(2).getDescription());
            assertEquals(LocalDateTime.parse("2025-10-21T00:00:00"), ((Event)tasklist.get(2)).getStartDateTime());
            assertEquals(LocalDateTime.parse("2025-10-21T08:00:00"), ((Event)tasklist.get(2)).getEndDateTime());
        }
    }
}
