package storage;

import exception.FileContentException;
import exception.FileContentException.ErrorType;
import exception.MeeBotException;
import parser.json.JsonTokenizer;
import parser.json.SimpleJsonObject;
import parser.json.SimpleJsonParser;
import task.Task;
import task.factory.DeadlineCreator;
import task.factory.EventCreator;
import task.factory.TaskCreator;
import task.factory.TodoCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to deserialize JSON-like task objects into Task instances.
 * <p>
 * This class provides methods to convert JSON string representations back into
 * Task objects. It handles parsing of the JSON array format and reconstruction
 * of different task types (TodoTask, DeadlineTask, EventTask) based on the
 * type field in the JSON data.
 * <p>
 * The deserialization process is resilient to individual task failures - if some
 * tasks cannot be parsed, the method will continue processing remaining tasks
 * and report the number of failed tasks.</p>
 *
 * @see TaskSerializer
 * @see SimpleJsonObject
 */
public class TaskDeserializer {

    private TaskDeserializer() {}

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
        SimpleJsonParser parser = new SimpleJsonParser(new JsonTokenizer(jsonArray));
        List<SimpleJsonObject> objects = parser.parseJson();
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
        String type = obj.requireNonEmpty("type").toLowerCase();
        TaskCreator creator = switch (type) {
            case "todo" -> new TodoCreator();
            case "deadline" -> new DeadlineCreator();
            case "event" -> new EventCreator();
            default -> throw new FileContentException(ErrorType.INVALID_INPUT);
        };

        Task task = creator.createFromJson(obj);
        String done = obj.requireNonEmpty("done");
        if (Boolean.parseBoolean(done)) {
            task.markAsDone();
        }
        return task;
    }
}
