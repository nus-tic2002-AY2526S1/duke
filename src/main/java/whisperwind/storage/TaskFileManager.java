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
        ensureDataDirectoryExists();
        createBackup(tasks);

        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i + 1);
                if (task != null && task.isValid()) {
                    writer.println(serializeTask(task));
                }
            }
            lastSaveTime = System.currentTimeMillis();
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
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            throw new IOException("No saved tasks file found");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;
            int errorCount = 0;

            while ((line = reader.readLine()) != null) {
                try {
                    Task task = deserializeTask(line);
                    if (task != null && task.isValid()) {
                        tasks.addTaskDirectly(task);
                        loadedCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    System.out.println("⚠️  Skipping invalid task data: " + line.substring(0, Math.min(50, line.length())));
                }
            }

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
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSaveTime > MAX_AUTO_SAVE_INTERVAL) {
            saveTasks(tasks);
        }
    }

    /**
     * Creates a backup of the current tasks file.
     * @param tasks the current task list
     * @throws IOException if backup fails
     */
    public void createBackup(TaskList tasks) throws IOException {
        ensureDataDirectoryExists();
        try {
            File original = new File(TASKS_FILE);
            if (original.exists()) {
                try (InputStream in = new FileInputStream(original);
                     OutputStream out = new FileOutputStream(BACKUP_FILE)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
                System.out.println("💾 Backup created: " + BACKUP_FILE);
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
        }
    }

    private void restoreBackup(TaskList tasks) throws IOException {
        File backup = new File(BACKUP_FILE);
        if (backup.exists()) {
            try (InputStream in = new FileInputStream(backup);
                 OutputStream out = new FileOutputStream(TASKS_FILE)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            System.out.println("✅ Restored backup file.");
            loadTasks(tasks);
        } else {
            System.out.println("❌ No backup found to restore.");
        }
    }

    private String serializeTask(Task task) {
        String doneFlag = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T | " + doneFlag + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            String byString = deadline.getBy().format(STORAGE_FORMATTER);
            return "D | " + doneFlag + " | " + task.getDescription() + " | " + byString;
        } else if (task instanceof Event) {
            Event event = (Event) task;
            String fromString = event.getFrom().format(STORAGE_FORMATTER);
            String toString = event.getTo().format(STORAGE_FORMATTER);
            return "E | " + doneFlag + " | " + task.getDescription() + " | " + fromString + " | " + toString;
        }
        return null;
    }

    private Task deserializeTask(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 3) return null;

            String type = parts[0].trim();
            String doneFlag = parts[1].trim();
            String description = parts[2].trim();

            boolean isDone = doneFlag.equals("1");
            Task task = null;

            switch (type) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        String byString = parts[3].trim();
                        LocalDateTime by = LocalDateTime.parse(byString, STORAGE_FORMATTER);
                        task = new Deadline(description, by);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        String fromString = parts[3].trim();
                        String toString = parts[4].trim();
                        LocalDateTime from = LocalDateTime.parse(fromString, STORAGE_FORMATTER);
                        LocalDateTime to = LocalDateTime.parse(toString, STORAGE_FORMATTER);
                        task = new Event(description, from, to);
                    }
                    break;
                default:
                    return null;
            }

            if (task != null && isDone) {
                task.markAsDone();
            }
            return task;
        } catch (Exception e) {
            return null;
        }
    }
}