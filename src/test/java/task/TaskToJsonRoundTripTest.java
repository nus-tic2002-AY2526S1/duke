package task;

import common.exception.FileContentException;
import common.exception.InvalidDateTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import logic.parser.datetime.DateTimeParser;
import model.task.EventTask;
import model.task.Recurrence;
import model.task.Task;
import model.task.TodoTask;
import storage.TaskDeserializer;
import storage.TaskLoadResult;
import storage.TaskSerializer;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskToJsonRoundTripTest {

    @Test
    void todoSerializationDeserialization() throws FileContentException {
        Task todo = new TodoTask(
                "water plants",
                Recurrence.none(LocalDate.of(2025, 11, 11)));
        todo.markAsDone();

        // Serialize
        String json = TaskSerializer.tasksToJson(List.of(todo));

        // Deserialize
        TaskLoadResult result = TaskDeserializer.reconstructTask(json);

        assertEquals(1, result.tasks().size());
        Task deserialized = result.tasks().get(0);

        // Check values
        assertEquals(todo.getDescription(), deserialized.getDescription());
        assertEquals(todo.isDone(), deserialized.isDone());
        assertEquals(Recurrence.RecurrenceType.NONE, deserialized.getRecurrence().type());
    }

    @Test
    void eventSerializationDeserialization() throws InvalidDateTimeException, FileContentException {
        Task event = new EventTask(
                "project meeting",
                DateTimeParser.parse("2025-12-02T18:00"),
                DateTimeParser.parse("2025-12-02T20:00"),
                new Recurrence(
                        Recurrence.RecurrenceType.WEEKLY,
                        7,
                        DateTimeParser.parse("2025-12-02T20:00").dateTime().toLocalDate(),
                        null)
        );
        // Serialize
        String json = TaskSerializer.tasksToJson(List.of(event));

        // Deserialize
        TaskLoadResult result = TaskDeserializer.reconstructTask(json);
        Task deserialized = result.tasks().get(0);

        // Check values
        assertEquals(1, result.tasks().size());
        assertInstanceOf(EventTask.class, deserialized);
        assertEquals("project meeting", deserialized.getDescription());
        assertEquals(event.getRecurrence().type(), deserialized.getRecurrence().type());
        assertEquals(event.getRecurrence().frequency(), deserialized.getRecurrence().frequency());
    }

    @Test
    @DisplayName("No valid tasks should be loaded with malformed JSON structure")
    void shouldNotLoadWhenJsonFileIsMalformed() {
        String invalidJson = "{ not valid json ]";
        assertThrows(FileContentException.class, () -> {
            TaskDeserializer.reconstructTask(invalidJson);
        });
    }

    @Test
    @DisplayName("No valid tasks should be loaded with malformed JSON components")
    void invalidWhenMissingJsonComponents() throws FileContentException {
        String invalidTodoJson = """
                    [{
                        "type": "deadline",
                        "done": false,
                        "description": "sign-up hackathon",
                        "deadline":"2025-10-01T18:00"
                    }]
                """;
        TaskLoadResult result = TaskDeserializer.reconstructTask(invalidTodoJson);
        assertTrue(result.tasks().isEmpty());
    }
}
