package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
 * and report the number of failed tasks.
 *
 * @see TaskSerializer
 * @see SimpleJsonObject
 */
public class TaskDeserializer {
    private static final Path ERROR_LOG_PATH = Paths.get("logs").resolve("task_load_errors.log");

    private TaskDeserializer() {
    }

    /**
     * Reconstructs a list of Task objects from a JSON array string.
     * <p>
     * Parses the provided JSON array string and attempts to deserialize each
     * task object into its appropriate Task type. The process is fault-tolerant:
     * individual task failures do not halt the entire operation.
     *
     * @param jsonArray the JSON array string containing task data to deserialize
     * @return a list of successfully deserialized Task objects (may be empty if all tasks failed)
     * @throws FileContentException if the JSON array format itself is invalid (not individual tasks)
     */
    public static TaskLoadResult reconstructTask(String jsonArray) throws FileContentException {
        SimpleJsonParser parser = new SimpleJsonParser(new JsonTokenizer(jsonArray));
        List<SimpleJsonObject> objects = parser.parseJson();
        List<String> errors = new ArrayList<>();
        List<Task> tasks = deserializeTasks(objects, errors);

        writeErrorLog(errors);
        String errorLogFile = errors.isEmpty()
                ? null
                : ERROR_LOG_PATH.getFileName().toString();

        return new TaskLoadResult(tasks, errors.size(), errorLogFile);
    }

    private static List<Task> deserializeTasks(List<SimpleJsonObject> jsonObjects, List<String> errors) {
        List<Task> tasks = new ArrayList<>();
        for (SimpleJsonObject jsonObj : jsonObjects) {
            try {
                Task task = deserialize(jsonObj);
                assert task != null : "Deserialized task must not be null";
                tasks.add(task);
            } catch (MeeBotException e) {
                errors.add(formatError(e, jsonObj));
            }
        }
        return tasks;
    }

    /**
     * Deserializes a single SimpleJsonObject into the appropriate Task instance.
     *
     * @param obj the JSON object representing a single task
     * @return a fully constructed Task instance with appropriate type, completion status and recurrence pattern
     * @throws FileContentException if required fields are missing, the task type is unrecognized,
     *                              or the JSON structure is invalid
     * @throws MeeBotException      if task-specific validation fails (e.g., invalid dates)
     * @see TodoCreator#createFromJson
     * @see DeadlineCreator#createFromJson
     * @see EventCreator#createFromJson
     */
    private static Task deserialize(SimpleJsonObject obj) throws MeeBotException {
        String type = obj.requireNonEmpty("type").toLowerCase();

        TaskCreator creator = switch (type) {
            case "todo" -> new TodoCreator();
            case "deadline" -> new DeadlineCreator();
            case "event" -> new EventCreator();
            default -> throw new FileContentException(ErrorType.INVALID_INPUT);
        };
        Task task = creator.createFromJson(obj);
        if (Boolean.parseBoolean(obj.requireNonEmpty("done"))) {
            task.markAsDone();
        }

        return task;
    }

    private static String formatError(MeeBotException e, SimpleJsonObject obj) {
        return String.format("""
                        Task failed
                        Reason: %s
                        Raw JSON: %s
                        """,
                e.getMessage(), obj);
    }

    /**
     * Writes task loading errors to a log file in the log directory.
     * <p>
     * <strong>Log Format:</strong> Each error entry includes:
     * <ul>
     *     <li>The exception message explaining the failure</li>
     *     <li>The raw JSON that failed to parse</li>
     * </ul>
     * <p>
     * If writing the log file fails (e.g., permission issues, I/O errors), an error message
     * is printed to {@code System.err} but no exception is thrown to avoid disrupting the
     * task loading process.
     *
     * @param errors a list of formatted error messages to write to the log file
     */
    private static void writeErrorLog(List<String> errors) {
        if (errors.isEmpty()) return;

        try {
            // Ensure the directory for the error log exists
            Path logFile = ERROR_LOG_PATH;
            Files.createDirectories(logFile.getParent());

            List<String> errorContent = new ArrayList<>();
            errorContent.add("=== TASK LOADING ERRORS ===\n");
            errorContent.addAll(errors);

            Files.write(logFile, errorContent,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.err.println("Failed to write error log: " + e.getMessage());
        }
    }
}
