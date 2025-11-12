package whisperwind.controller;

import whisperwind.model.Task;
import whisperwind.model.TaskType;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages archiving of tasks to files for record keeping.
 * Supports archiving all tasks, completed tasks, or tasks by type,
 * as well as listing and viewing archived files.
 */
public class ArchiveManager {
    private static final String ARCHIVE_DIR = "./data/archive";
    private static final DateTimeFormatter ARCHIVE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Archives all tasks from the given task list.
     * Creates an archive file containing all tasks and returns its path.
     *
     * @param taskList The list of tasks to archive.
     * @return Path to the archive file created.
     * @throws IllegalStateException If the task list is empty.
     * @throws RuntimeException      If archiving fails due to an I/O error.
     */
    public String archiveAllTasks(TaskList taskList) {
        if (taskList.isEmpty()) {
            throw new IllegalStateException("No tasks to archive - task list is empty");
        }

        try {
            ensureArchiveDirectoryExists();
            String archiveFileName = generateArchiveFileName();
            String archivePath = ARCHIVE_DIR + File.separator + archiveFileName;

            List<Task> tasksToArchive = getAllTasks(taskList);
            writeTasksToArchive(tasksToArchive, archivePath);

            System.out.println("📦 Successfully archived " + tasksToArchive.size() +
                    " tasks to: " + archiveFileName);
            return archivePath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to archive tasks: " + e.getMessage(), e);
        }
    }

    /**
     * Archives only completed tasks from the given task list.
     * Creates an archive file containing completed tasks and returns its path.
     *
     * @param taskList The list of tasks to check for completed tasks.
     * @return Path to the archive file created.
     * @throws IllegalStateException If there are no completed tasks to archive.
     * @throws RuntimeException      If archiving fails due to an I/O error.
     */
    public String archiveCompletedTasks(TaskList taskList) {
        List<Task> completedTasks = getCompletedTasks(taskList);

        if (completedTasks.isEmpty()) {
            throw new IllegalStateException("No completed tasks to archive");
        }

        try {
            ensureArchiveDirectoryExists();
            String archiveFileName = "completed_" + generateArchiveFileName();
            String archivePath = ARCHIVE_DIR + File.separator + archiveFileName;

            writeTasksToArchive(completedTasks, archivePath);

            System.out.println("✅ Successfully archived " + completedTasks.size() +
                    " completed tasks to: " + archiveFileName);
            return archivePath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to archive completed tasks: " + e.getMessage(), e);
        }
    }

    /**
     * Archives tasks of a specific type from the given task list.
     * Creates an archive file containing tasks of the specified type and returns its path.
     *
     * @param taskList The list of tasks to archive from.
     * @param taskType The type of tasks to archive (TODO, DEADLINE, EVENT).
     * @return Path to the archive file created.
     * @throws IllegalStateException If there are no tasks of the specified type to archive.
     * @throws RuntimeException      If archiving fails due to an I/O error.
     */
    public String archiveTasksByType(TaskList taskList, TaskType taskType) {
        List<Task> typedTasks = getTasksByType(taskList, taskType);

        if (typedTasks.isEmpty()) {
            throw new IllegalStateException("No " + taskType.getDisplayName().toLowerCase() +
                    " tasks to archive");
        }

        try {
            ensureArchiveDirectoryExists();
            String archiveFileName = taskType.getDisplayName().toLowerCase() + "_" + generateArchiveFileName();
            String archivePath = ARCHIVE_DIR + File.separator + archiveFileName;

            writeTasksToArchive(typedTasks, archivePath);

            System.out.println("📁 Successfully archived " + typedTasks.size() +
                    " " + taskType.getDisplayName().toLowerCase() +
                    " tasks to: " + archiveFileName);
            return archivePath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to archive tasks by type: " + e.getMessage(), e);
        }
    }

    /**
     * Lists all archive files in the archive directory.
     * Displays the name and size of each archive file.
     */
    public void listArchiveFiles() {
        File archiveDir = new File(ARCHIVE_DIR);
        if (!archiveDir.exists() || !archiveDir.isDirectory()) {
            System.out.println("📭 No archive files found.");
            return;
        }

        File[] archiveFiles = archiveDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archiveFiles == null || archiveFiles.length == 0) {
            System.out.println("📭 No archive files found.");
            return;
        }

        System.out.println("📚 Available Archive Files:");
        System.out.println("────────────────────────────");
        for (int i = 0; i < archiveFiles.length; i++) {
            String fileName = archiveFiles[i].getName();
            String displayName = fileName.replace(".txt", "");
            long fileSize = archiveFiles[i].length();
            System.out.printf("%d. %s (%.1f KB)%n", i + 1, displayName, fileSize / 1024.0);
        }
    }

    /**
     * Displays the contents of a specific archive file based on its index.
     *
     * @param archiveIndex The 1-based index of the archive file to view.
     *                     Must correspond to a file listed by {@link #listArchiveFiles()}.
     */
    public void viewArchive(int archiveIndex) {
        File archiveDir = new File(ARCHIVE_DIR);
        File[] archiveFiles = archiveDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archiveFiles == null || archiveIndex < 1 || archiveIndex > archiveFiles.length) {
            System.out.println("❌ Invalid archive file number.");
            return;
        }

        File archiveFile = archiveFiles[archiveIndex - 1];
        try (BufferedReader reader = new BufferedReader(new FileReader(archiveFile))) {
            System.out.println("📖 Contents of " + archiveFile.getName() + ":");
            System.out.println("────────────────────────────");

            String line;
            int taskCount = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    taskCount++;
                    System.out.println(taskCount + ". " + line);
                }
            }

            if (taskCount == 0) {
                System.out.println("   (Empty archive)");
            } else {
                System.out.println("────────────────────────────");
                System.out.println("Total tasks: " + taskCount);
            }

        } catch (IOException e) {
            System.out.println("❌ Error reading archive file: " + e.getMessage());
        }
    }

    // Private helper methods
    private void ensureArchiveDirectoryExists() throws IOException {
        File dir = new File(ARCHIVE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create archive directory: " + ARCHIVE_DIR);
        }
    }

    private String generateArchiveFileName() {
        return "archive_" + LocalDateTime.now().format(ARCHIVE_FORMATTER) + ".txt";
    }

    private List<Task> getAllTasks(TaskList taskList) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            tasks.add(taskList.getTask(i));
        }
        return tasks;
    }

    private List<Task> getCompletedTasks(TaskList taskList) {
        List<Task> completed = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (task.isDone()) {
                completed.add(task);
            }
        }
        return completed;
    }

    private List<Task> getTasksByType(TaskList taskList, TaskType taskType) {
        List<Task> typedTasks = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (TaskType.fromClass(task.getClass()) == taskType) {
                typedTasks.add(task);
            }
        }
        return typedTasks;
    }

    private void writeTasksToArchive(List<Task> tasks, String archivePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivePath))) {
            writer.println("📦 Whisperwind Archive - " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Total tasks: " + tasks.size());
            writer.println();

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                writer.printf("%d. %s%n", i + 1, formatTaskForArchive(task));
            }
        }
    }

    private String formatTaskForArchive(Task task) {
        String status = task.isDone() ? "✅" : "⏳";
        String type = TaskType.fromClass(task.getClass()).getDisplayName();
        String description = task.getDescription();
        return String.format("[%s] %s: %s", status, type, description);
    }
}