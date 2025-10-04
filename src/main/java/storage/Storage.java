package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import exception.MeeBotException;
import manager.TaskManager;
import task.ReadOnlyTask;
import task.Task;

/**
 * Handles persistent storage of tasks to and from the file system.
 * <p>This class manages the creation and maintenance of storage directories and files,
 * and provides methods to save tasks in JSON format. It uses a key-based access pattern
 * to ensure that only authorized operations can modify the underlying task data.
 */
public class Storage {
    private static final String DEFAULT_DIR = "data";
    private static final String DEFAULT_FILE = "tasks.json";
    private final File dataFile;
    private final TaskManager tm;

    public Storage(TaskManager tm) {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            List<ReadOnlyTask> taskList = tm.getReadOnlyList();
            writer.write(TaskSerializer.tasksToJson(taskList));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    /**
     * Loads tasks from storage file in JSON format and populates the task list.
     * <p>
     * Reconstruct the appropriate task from the JSON data using TaskDeserializer
     * and adds it to the task list managed by {@link TaskManager}.
     *
     * @throws RuntimeException if the file cannot be read due to I/O errors
     * @see TaskDeserializer#reconstructTask(String)
     */
    public void loadTasks() {
        try {
            String content = Files.readString(dataFile.toPath()).trim();
            List<Task> tasks = TaskDeserializer.reconstructTask(content);
            for (Task task : tasks) {
                tm.addTask(task);
            }
            System.out.printf("%d tasks loaded%n", tm.getTotalTasks());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load tasks: ", e);
        } catch (MeeBotException e) {
            System.err.println(e.toErrorMessage());
        }
    }
}
