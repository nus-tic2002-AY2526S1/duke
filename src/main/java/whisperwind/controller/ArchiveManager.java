package whisperwind.controller;

import whisperwind.model.Task;
import whisperwind.model.TaskType;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages archiving of tasks to files for record keeping.
 * <p>
 * This class provides functionality to archive tasks in various ways:
 * archiving all tasks, completed tasks only, or tasks by specific type.
 * Archives are saved as readable text files in the ./data/archive/ directory.
 * </p>
 */
public class ArchiveManager {
    private static final String ARCHIVE_DIR = "./data/archive";
    private static final DateTimeFormatter ARCHIVE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Archives all current tasks from the specified task list.
     * <p>
     * This method creates a comprehensive archive of all tasks in the task list,
     * including their status, type, and description. The archive is saved as a
     * timestamped text file in the archive directory.
     * </p>
     *
     * @param taskList the task list containing tasks to be archived
     * @return the file path where the archive was saved
     * @throws IllegalStateException if the task list is empty
     * @throws RuntimeException if an I/O error occurs during archiving
     */
    public String archiveAllTasks(whisperwind.controller.TaskList taskList) {
        assert taskList != null : "TaskList should not be null";
        if (taskList.isEmpty()) {
            throw new IllegalStateException("No tasks to archive - task list is empty");
        }

        try {
            ensureArchiveDirectoryExists();
            String archiveFileName = generateArchiveFileName();
            String archivePath = ARCHIVE_DIR + File.separator + archiveFileName;

            List<Task> tasksToArchive = getAllTasks(taskList);
            writeTasksToArchive(tasksToArchive, archivePath);

            // Post-condition assertion
            assert new File(archivePath).exists() : "Archive file should be created";
            assert new File(archivePath).length() > 0 : "Archive file should not be empty";

            System.out.println("📦 Successfully archived " + tasksToArchive.size() +
                    " tasks to: " + archiveFileName);
            return archivePath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to archive tasks: " + e.getMessage(), e);
        }
    }

    /**
     * Archives only completed tasks from the specified task list.
     * <p>
     * This method filters and archives only tasks that are marked as completed,
     * allowing users to clean up their task list while keeping records of finished work.
     * </p>
     *
     * @param taskList the task list containing tasks to be archived
     * @return the file path where the archive was saved
     * @throws IllegalStateException if no completed tasks are found
     * @throws RuntimeException if an I/O error occurs during archiving
     */
    public String archiveCompletedTasks(whisperwind.controller.TaskList taskList) {
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
     * Archives tasks of a specific type from the specified task list.
     * <p>
     * This method filters and archives tasks based on their type (TODO, DEADLINE, or EVENT),
     * allowing users to organize and archive tasks by category.
     * </p>
     *
     * @param taskList the task list containing tasks to be archived
     * @param taskType the type of tasks to archive (TODO, DEADLINE, or EVENT)
     * @return the file path where the archive was saved
     * @throws IllegalStateException if no tasks of the specified type are found
     * @throws RuntimeException if an I/O error occurs during archiving
     */
    public String archiveTasksByType(whisperwind.controller.TaskList taskList, TaskType taskType) {
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
     * Lists all available archive files in the archive directory.
     * <p>
     * This method displays a formatted list of all archive files, including their
     * names and file sizes, helping users identify and manage their archived tasks.
     * </p>
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
            System.out.printf("%d. %s (%.1f KB)%n",
                    i + 1, displayName, fileSize / 1024.0);
        }
    }

    /**
     * Displays the contents of a specific archive file.
     * <p>
     * This method reads and displays the contents of an archive file identified
     * by its index in the archive file list, allowing users to review archived tasks.
     * </p>
     *
     * @param archiveIndex the index of the archive file to view (1-based)
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

    // ========== PRIVATE HELPER METHODS ==========

    /**
     * Ensures the archive directory exists, creating it if necessary.
     *
     * @throws IOException if the directory cannot be created
     */
    private void ensureArchiveDirectoryExists() throws IOException {

        assert ARCHIVE_DIR != null : "Archive directory path should be set";

        File dir = new File(ARCHIVE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create archive directory: " + ARCHIVE_DIR);
        }

        assert dir.exists() : "Archive directory should exist after creation";
        assert dir.isDirectory() : "Archive path should be a directory";
    }

    /**
     * Generates a unique archive file name based on current timestamp.
     *
     * @return a unique file name for the archive
     */
    private String generateArchiveFileName() {
        return "archive_" + LocalDateTime.now().format(ARCHIVE_FORMATTER) + ".txt";
    }

    /**
     * Retrieves all tasks from the specified task list.
     *
     * @param taskList the task list to retrieve tasks from
     * @return a list containing all tasks from the task list
     */
    private List<Task> getAllTasks(whisperwind.controller.TaskList taskList) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            tasks.add(taskList.getTask(i));
        }
        return tasks;
    }

    /**
     * Retrieves only completed tasks from the specified task list.
     *
     * @param taskList the task list to retrieve completed tasks from
     * @return a list containing only completed tasks
     */
    private List<Task> getCompletedTasks(whisperwind.controller.TaskList taskList) {
        List<Task> completed = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (task.isDone()) {
                completed.add(task);
            }
        }
        return completed;
    }

    /**
     * Retrieves tasks of a specific type from the specified task list.
     *
     * @param taskList the task list to retrieve tasks from
     * @param taskType the type of tasks to retrieve
     * @return a list containing tasks of the specified type
     */
    private List<Task> getTasksByType(whisperwind.controller.TaskList taskList, TaskType taskType) {
        List<Task> typedTasks = new ArrayList<>();
        for (int i = 1; i <= taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (TaskType.fromClass(task.getClass()) == taskType) {
                typedTasks.add(task);
            }
        }
        return typedTasks;
    }

    /**
     * Writes tasks to an archive file in a formatted manner.
     *
     * @param tasks the list of tasks to write to the archive
     * @param archivePath the file path where the archive should be saved
     * @throws IOException if an I/O error occurs during writing
     */
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

    /**
     * Formats a task for archival storage.
     *
     * @param task the task to format
     * @return a formatted string representation of the task for archiving
     */
    private String formatTaskForArchive(Task task) {
        String status = task.isDone() ? "✅" : "⏳";
        String type = TaskType.fromClass(task.getClass()).getDisplayName();
        String description = task.getDescription();

        return String.format("[%s] %s: %s", status, type, description);
    }
}