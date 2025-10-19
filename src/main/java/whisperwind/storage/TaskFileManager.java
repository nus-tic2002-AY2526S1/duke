package whisperwind.storage;

import whisperwind.model.Task;
import whisperwind.model.Todo;
import whisperwind.model.Deadline;
import whisperwind.model.Event;
import whisperwind.controller.TaskList;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages saving and loading tasks to/from files.
 * Handles file operations including backups and auto-saving.
 */
public class TaskFileManager {
    private static final String DATA_DIR = "./data";
    private static final String TASKS_FILE = DATA_DIR + File.separator + "whisperwind_tasks.txt";
    private static final String BACKUP_FILE = DATA_DIR + File.separator + "whisperwind_tasks_backup.txt";
    private static final int MAX_AUTO_SAVE_INTERVAL = 30000;

    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private long lastSaveTime = 0;

    /**
     * Saves all tasks to the tasks file.
     * @param tasks the task list to save
     * @throws IOException if saving fails
     */
    public void saveTasks(TaskList tasks) throws IOException {
        // Assert preconditions
        assert tasks != null : "TaskList should not be null";
        assert !tasks.isEmpty() : "Should not save empty task list without reason";

        ensureDataDirectoryExists();
        createBackup(tasks);

        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i + 1);
                // Assert task retrieval assumptions
                assert task != null : "Retrieved task should not be null";

                if (task != null && task.isValid()) {
                    String serialized = serializeTask(task);
                    assert serialized != null : "Serialized task should not be null";
                    assert !serialized.trim().isEmpty() : "Serialized task should not be empty";
                    writer.println(serialized);
                }
            }
            lastSaveTime = System.currentTimeMillis();

            // Assert postconditions
            assert new File(TASKS_FILE).exists() : "Task file should exist after save";
            assert new File(TASKS_FILE).length() > 0 : "Task file should not be empty after save";

            System.out.println("💾 Tasks saved to: " + TASKS_FILE);
        } catch (IOException e) {
            throw new IOException("Failed to save tasks to file: " + e.getMessage(), e);
        }
    }

    /**
     * Loads tasks from the tasks file.
     * @param tasks the task list to load into
     * @throws IOException if loading fails
     */
    public void loadTasks(TaskList tasks) throws IOException {
        // Assert preconditions
        assert tasks != null : "TaskList should not be null";
        assert tasks.isEmpty() : "TaskList should be empty before loading";

        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            throw new IOException("No saved tasks file found");
        }

        // Assert file state
        assert file.canRead() : "Task file should be readable";
        assert file.length() > 0 : "Task file should not be empty";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;
            int errorCount = 0;

            while ((line = reader.readLine()) != null) {
                // Assert line reading assumptions
                assert line != null : "Read line should not be null";
                assert !line.trim().isEmpty() : "Should not process empty lines";

                try {
                    Task task = deserializeTask(line);
                    if (task != null && task.isValid()) {
                        tasks.addTaskDirectly(task);
                        loadedCount++;

                        // Assert task addition
                        assert tasks.getTaskCount() == loadedCount : "Task count should match loaded count";
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    System.out.println("⚠️  Skipping invalid task data: " + line.substring(0, Math.min(50, line.length())));
                }
            }

            // Assert loading results
            assert loadedCount + errorCount > 0 : "Should process at least one line";
            assert tasks.getTaskCount() == loadedCount : "Final task count should match loaded count";

            if (errorCount > 0) {
                System.out.println("⚠️  Skipped " + errorCount + " invalid tasks during load");
                if (errorCount > 5) {
                    System.out.println("⚠️ Too many corrupted lines detected. Attempting to restore from backup...");
                    restoreBackup(tasks);
                }
            }
            if (loadedCount > 0) {
                System.out.println("✅ Successfully loaded " + loadedCount + " tasks");
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Tasks file not found", e);
        } catch (IOException e) {
            throw new IOException("Error reading tasks file: " + e.getMessage(), e);
        }
    }

    /**
     * Auto-saves tasks if enough time has passed since last save.
     * @param tasks the task list to save
     * @throws IOException if saving fails
     */
    public void autoSaveTasks(TaskList tasks) throws IOException {
        // Assert preconditions
        assert tasks != null : "TaskList should not be null for auto-save";

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSaveTime > MAX_AUTO_SAVE_INTERVAL) {
            // Assert auto-save conditions
            assert currentTime > lastSaveTime : "Current time should be after last save time";
            assert MAX_AUTO_SAVE_INTERVAL > 0 : "Auto-save interval should be positive";

            saveTasks(tasks);

            // Assert auto-save completed
            assert lastSaveTime > 0 : "Last save time should be updated";
        }
    }

    /**
     * Creates a backup of the current tasks file.
     * @param tasks the current task list
     * @throws IOException if backup fails
     */
    public void createBackup(TaskList tasks) throws IOException {
        // Assert preconditions
        assert tasks != null : "TaskList should not be null for backup";

        ensureDataDirectoryExists();
        try {
            File original = new File(TASKS_FILE);
            if (original.exists()) {
                // Assert original file state
                assert original.canRead() : "Original file should be readable for backup";
                assert original.length() > 0 : "Original file should not be empty for backup";

                try (InputStream in = new FileInputStream(original);
                     OutputStream out = new FileOutputStream(BACKUP_FILE)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        assert length > 0 : "Should read positive number of bytes";
                        assert length <= buffer.length : "Read length should not exceed buffer size";
                        out.write(buffer, 0, length);
                    }
                }

                // Assert backup creation
                File backup = new File(BACKUP_FILE);
                assert backup.exists() : "Backup file should exist after creation";
                assert backup.length() == original.length() : "Backup should be same size as original";
                assert backup.canRead() : "Backup file should be readable";

                System.out.println("💾 Backup created: " + BACKUP_FILE);
            } else {
                // Assert no original file case
                System.out.println("💡 No existing tasks file to backup");
            }
        } catch (IOException e) {
            throw new IOException("Failed to create backup: " + e.getMessage(), e);
        }
    }

    /**
     * Finds tasks that occur on a specific date.
     * @param tasks the task list to search
     * @param dateString the date in yyyy-MM-dd format
     */
    public void findTasksOnDate(TaskList tasks, String dateString) {
        try {
            java.time.LocalDate targetDate = java.time.LocalDate.parse(dateString);
            System.out.println("📅 Tasks occurring on " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

            boolean found = false;
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i + 1);
                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    if (deadline.getBy().toLocalDate().equals(targetDate)) {
                        System.out.println("  " + (i + 1) + ". " + task);
                        found = true;
                    }
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    if (event.getFrom().toLocalDate().equals(targetDate) ||
                            event.getTo().toLocalDate().equals(targetDate)) {
                        System.out.println("  " + (i + 1) + ". " + task);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("  No tasks found for this date.");
            }
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use yyyy-MM-dd format (e.g., 2019-12-02)");
        }
    }

    private void ensureDataDirectoryExists() throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Failed to create data directory at " + DATA_DIR);
            }
            // Assert directory creation
            assert dir.exists() : "Data directory should exist after creation";
            assert dir.isDirectory() : "Data path should be a directory";
        }

        // Assert directory state
        assert dir.exists() : "Data directory should exist";
        assert dir.isDirectory() : "Data path should be a directory";
        assert dir.canWrite() : "Data directory should be writable";
    }

    private void restoreBackup(TaskList tasks) throws IOException {
        // Assert preconditions
        assert tasks != null : "TaskList should not be null for restore";
        assert tasks.isEmpty() : "TaskList should be empty before restore";

        File backup = new File(BACKUP_FILE);
        if (backup.exists()) {
            // Assert backup file state
            assert backup.canRead() : "Backup file should be readable";
            assert backup.length() > 0 : "Backup file should not be empty";

            try (InputStream in = new FileInputStream(backup);
                 OutputStream out = new FileOutputStream(TASKS_FILE)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }

            // Assert restore completion
            File restored = new File(TASKS_FILE);
            assert restored.exists() : "Restored file should exist";
            assert restored.length() == backup.length() : "Restored file should be same size as backup";

            System.out.println("✅ Restored backup file.");
            loadTasks(tasks);
        } else {
            System.out.println("❌ No backup found to restore.");
        }
    }

    private String serializeTask(Task task) {
        // Assert input assumptions
        assert task != null : "Task to serialize should not be null";
        assert task.isValid() : "Task to serialize should be valid";

        String doneFlag = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            String result = "T | " + doneFlag + " | " + task.getDescription();
            assert result.startsWith("T | ") : "Todo serialization should start with 'T | '";
            return result;
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            assert deadline.getBy() != null : "Deadline time should not be null";

            String byString = deadline.getBy().format(STORAGE_FORMATTER);
            String result = "D | " + doneFlag + " | " + task.getDescription() + " | " + byString;
            assert result.startsWith("D | ") : "Deadline serialization should start with 'D | '";
            assert result.split("\\|").length == 4 : "Deadline should have 4 parts";
            return result;
        } else if (task instanceof Event) {
            Event event = (Event) task;
            assert event.getFrom() != null : "Event start time should not be null";
            assert event.getTo() != null : "Event end time should not be null";

            String fromString = event.getFrom().format(STORAGE_FORMATTER);
            String toString = event.getTo().format(STORAGE_FORMATTER);
            String result = "E | " + doneFlag + " | " + task.getDescription() + " | " + fromString + " | " + toString;
            assert result.startsWith("E | ") : "Event serialization should start with 'E | '";
            assert result.split("\\|").length == 5 : "Event should have 5 parts";
            return result;
        }

        assert false : "Unknown task type for serialization: " + task.getClass().getSimpleName();
        return null;
    }

    private Task deserializeTask(String line) {
        // Assert input assumptions
        assert line != null : "Line to deserialize should not be null";
        assert !line.trim().isEmpty() : "Line to deserialize should not be empty";

        try {
            String[] parts = line.split("\\|");
            assert parts.length >= 3 : "Serialized line should have at least 3 parts: " + line;

            String type = parts[0].trim();
            String doneFlag = parts[1].trim();
            String description = parts[2].trim();

            // Assert basic field validity
            assert type != null && !type.isEmpty() : "Task type should not be empty";
            assert doneFlag.equals("0") || doneFlag.equals("1") : "Done flag should be 0 or 1";
            assert description != null && !description.isEmpty() : "Description should not be empty";

            boolean isDone = doneFlag.equals("1");
            Task task = null;

            switch (type) {
                case "T":
                    assert parts.length == 3 : "Todo should have exactly 3 parts";
                    task = new Todo(description);
                    break;
                case "D":
                    assert parts.length >= 4 : "Deadline should have at least 4 parts";
                    String byString = parts[3].trim();
                    assert !byString.isEmpty() : "Deadline time should not be empty";

                    LocalDateTime by = LocalDateTime.parse(byString, STORAGE_FORMATTER);
                    task = new Deadline(description, by);
                    break;
                case "E":
                    assert parts.length >= 5 : "Event should have at least 5 parts";
                    String fromString = parts[3].trim();
                    String toString = parts[4].trim();
                    assert !fromString.isEmpty() : "Event start time should not be empty";
                    assert !toString.isEmpty() : "Event end time should not be empty";

                    LocalDateTime from = LocalDateTime.parse(fromString, STORAGE_FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(toString, STORAGE_FORMATTER);
                    task = new Event(description, from, to);
                    break;
                default:
                    assert false : "Unknown task type in deserialization: " + type;
                    return null;
            }

            assert task != null : "Deserialized task should not be null";
            assert task.isValid() : "Deserialized task should be valid";

            if (task != null && isDone) {
                task.markAsDone();
                assert task.isDone() : "Task should be marked as done after deserialization";
            }
            return task;
        } catch (Exception e) {
            return null;
        }
    }
}