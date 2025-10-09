//AI write this for Error Handling Improvement in my system
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskFileManager {
    private static final String TASKS_FILE = "whisperwind_tasks.txt";
    private static final String BACKUP_FILE = "whisperwind_tasks_backup.txt";
    private static final int MAX_AUTO_SAVE_INTERVAL = 30000; // 30 seconds

    private long lastSaveTime = 0;

    public void saveTasks(TaskList tasks) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            for (int i = 0; i < tasks.getTaskCount(); i++) {
                Task task = tasks.getTask(i + 1);
                if (task != null && task.isValid()) {
                    writer.println(serializeTask(task));
                }
            }
            lastSaveTime = System.currentTimeMillis();
        } catch (IOException e) {
            throw new IOException("Failed to save tasks to file: " + e.getMessage(), e);
        }
    }

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

    public void autoSaveTasks(TaskList tasks) throws IOException {
        long currentTime = System.currentTimeMillis();
        // Only auto-save if enough time has passed to prevent excessive I/O
        if (currentTime - lastSaveTime > MAX_AUTO_SAVE_INTERVAL) {
            saveTasks(tasks);
        }
    }

    public void createBackup(TaskList tasks) throws IOException {
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

    private String serializeTask(Task task) {
        // Simple serialization format: TYPE|DESCRIPTION|STATUS|EXTRA_DATA
        if (task instanceof Todo) {
            return "T|" + task.getDescription() + "|" + task.isDone();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D|" + task.getDescription() + "|" + task.isDone() + "|" + deadline.getBy();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E|" + task.getDescription() + "|" + task.isDone() + "|" + event.getFrom() + "|" + event.getTo();
        }
        return null;
    }

    private Task deserializeTask(String line) {
        try {
            String[] parts = line.split("\\|", -1); // -1 to keep trailing empty strings
            if (parts.length < 3) return null;

            String type = parts[0];
            String description = parts[1];
            boolean isDone = Boolean.parseBoolean(parts[2]);

            Task task = null;
            switch (type) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        task = new Deadline(description, parts[3]);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        task = new Event(description, parts[3], parts[4]);
                    }
                    break;
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