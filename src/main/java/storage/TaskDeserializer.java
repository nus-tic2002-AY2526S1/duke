package storage;

import java.io.BufferedWriter;
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

    private TaskDeserializer() {}

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
    static TaskLoadResult reconstructTask(String jsonArray) throws FileContentException {
        List<Task> tasks = new ArrayList<>();
        List<String> errorLog = new ArrayList<>();
        SimpleJsonParser parser = new SimpleJsonParser(new JsonTokenizer(jsonArray));
        List<SimpleJsonObject> objects = parser.parseJson();

        for (SimpleJsonObject obj : objects) {
            try {
                tasks.add(deserialize(obj));
                assert tasks.get(tasks.size() - 1) != null : "Deserialized task must not be null";
            } catch (MeeBotException e) {
                errorLog.add(formatError(e, obj));
            }
        }

        writeErrorLog(errorLog);
        String errorLogFile = errorLog.isEmpty() ? null : ERROR_LOG_PATH.getFileName().toString();
        return new TaskLoadResult(tasks, errorLog.size(), errorLogFile);
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
    static Task deserialize(SimpleJsonObject obj) throws MeeBotException {
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
        assert task != null : "Task must not be null after deserialization";
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
            Path dir = ERROR_LOG_PATH.getParent();
            if (dir != null && Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(
                    ERROR_LOG_PATH,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {
                writer.write("=== TASK LOADING ERRORS ===\n");
                for (String error : errors) {
                    writer.write(error);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write error log: " + e.getMessage());
        }
    }
}
