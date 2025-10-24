package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import exception.MeeBotException;
import storage.TaskDeserializer;
import storage.TaskLoadResult;
import storage.TaskSerializer;
import task.ReadOnlyTask;
import task.Task;

/**
 * Handles persistent storage of tasks to and from the file system.
 * <p>
 * This class manages the creation and maintenance of storage directories and files,
 * and provides methods to save and load tasks in JSON format.
 */
public class StorageManager {
    private static final String DEFAULT_DIR = "data";
    private static final String DEFAULT_FILE = "tasks.json";
    private final File dataFile;
    private final TaskManager tm;

    public StorageManager(TaskManager tm) {
        this.tm = tm;
        this.dataFile = initStorage();
    }

    /**
     * Initializes the storage directory if it doesn't exist, and creates an empty
     * storage file initialized with "[]" if the file doesn't exist.
     *
     * @return the File object representing the storage file
     * @throws RuntimeException if the directory cannot be created or if there's
     *                          an IOException while creating or writing to the storage file
     */
    private File initStorage() {
        File dir = new File(DEFAULT_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Unable to create storage directory: " + dir.getAbsolutePath());
        }

        File file = new File(dir, DEFAULT_FILE);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("[]");
            } catch (IOException e) {
                throw new RuntimeException("Unable to create storage file: " + file.getAbsolutePath());
            }
        }
        assert file.exists() : "Storage file must exist after initialization";
        assert file.canWrite() : "Storage file must be writable after initialization";

        return file;
    }

    /**
     * Persists all current tasks to the storage file in JSON format.
     * <p>
     * Retrieves the current task list from the {@link TaskManager}, serializes
     * it to JSON format using {@link TaskSerializer}, and writes the complete
     * JSON array to the storage file. The entire file is overwritten with the current state.
     *
     * @throws RuntimeException if there's an IOException while writing to the file
     * @see TaskSerializer#tasksToJson(List)
     */
    public void saveTasks() {
        try {
            List<ReadOnlyTask> taskList = tm.getReadOnlyList();
            String json = TaskSerializer.tasksToJson(taskList);
            Files.writeString(dataFile.toPath(), json);
            assert Files.size(dataFile.toPath()) > 0 : "Task file must not be empty after save";
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    /**
     * Loads tasks from storage file in JSON format and populates the task list.
     * <p>
     * Reconstruct the appropriate task from the JSON data using {@link TaskDeserializer}
     * and adds it to the task list managed by {@link TaskManager}.
     *
     * @throws RuntimeException if the file cannot be read due to I/O errors
     * @see TaskDeserializer#reconstructTask(String)
     */
    public void loadTasks() {
        try {
            String content = Files.readString(dataFile.toPath()).trim();
            TaskLoadResult loadResult = TaskDeserializer.reconstructTask(content);
            tm.clear();

            for (Task task : loadResult.tasks()) {
                assert task != null : "Task must not be null.";
                tm.addTask(task);
            }
            TaskLoadResult.setCurrent(loadResult);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tasks: ", e);
        } catch (MeeBotException e) {
            System.err.println(e.toErrorMessage());
        }
    }
}
