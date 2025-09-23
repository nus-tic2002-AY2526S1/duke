package storage;

import exception.FileContentException;
import exception.FileContentException.ErrorType;
import exception.MeeBotException;
import parser.DateTimeParser;
import task.*;
import task.Recurrence.RecurrenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to deserialize JSON-like task objects into Task instances.
 * <p>
 * This class provides methods to convert JSON string representations back into
 * Task objects. It handles parsing of the JSON array format and reconstruction
 * of different task types (TodoTask, DeadlineTask, EventTask) based on the
 * type field in the JSON data.
 * </p>
 * <p>The deserialization process is resilient to individual task failures - if some
 * tasks cannot be parsed, the method will continue processing remaining tasks
 * and report the number of failed tasks.
 *
 * @see TaskSerializer
 * @see SimpleJsonObject
 * @see TokenizerUtil
 */
public class TaskDeserializer {

    private TaskDeserializer() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Reconstructs a list of Task objects from a JSON array string.
     * <p>
     * Parses the provided JSON array string and attempts to deserialize each
     * task object. Failed tasks are skipped and counted, while successfully
     * parsed tasks are added to the returned list.
     *
     * @param jsonArray the JSON array string containing task data
     * @return a list of successfully deserialized Task objects
     */
    public static List<Task> reconstructTask(String jsonArray) {
        List<SimpleJsonObject> objects = TokenizerUtil.tokenize(jsonArray);
        List<Task> tasks = new ArrayList<>();
        int failedTasks = 0;

        for (SimpleJsonObject obj : objects) {
            try {
                tasks.add(deserialize(obj));
            } catch (MeeBotException e) {
                failedTasks++;
            }
        }
        if (failedTasks > 0) {
            System.out.printf("%d tasks failed to load%n", failedTasks);
        }
        return tasks;
    }

    /**
     * Deserializes a single SimpleJsonObject into the appropriate Task instance.
     */
    static Task deserialize(SimpleJsonObject obj) throws MeeBotException {
        String type = requireNonEmpty(obj, "type").toLowerCase();

        Task task = switch (type) {
            case "todo" -> new TodoTask(
                    requireNonEmpty(obj, "description")
            );
            case "deadline" -> new DeadlineTask(
                    requireNonEmpty(obj, "description"),
                    DateTimeParser.parse(requireNonEmpty(obj, "deadline"))
            );
            case "event" -> new EventTask(
                    requireNonEmpty(obj, "description"),
                    DateTimeParser.parse(requireNonEmpty(obj, "start")),
                    DateTimeParser.parse(requireNonEmpty(obj, "end"))
            );
            default -> throw new FileContentException(FileContentException.ErrorType.INVALID_INPUT);
        };

        if (Boolean.parseBoolean(requireNonEmpty(obj, "done"))) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Validates that a required field exists and is not empty in the SimpleJsonObject.
     * before creating Task objects.
     */
    private static String requireNonEmpty(SimpleJsonObject obj, String key) {
        String value = obj.get(key);
        if (value == null || value.isBlank()) {
            throw new FileContentException(FileContentException.ErrorType.INVALID_INPUT);
        }
        return value;
    }
}
