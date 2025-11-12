package whisperwind.controller;

import whisperwind.Whisperwind;
import whisperwind.util.ArchiveCommand;
import whisperwind.model.TaskType;
import java.util.Scanner;

/**
 * Handles archive-related commands for the Whisperwind application.
 * Provides functionality to archive all tasks, completed tasks, tasks by type,
 * view archives, and list existing archive files.
 */
public class ArchiveHandler {
    private final Whisperwind app;

    /**
     * Constructs an ArchiveHandler for the given Whisperwind application instance.
     *
     * @param app Instance of the Whisperwind application.
     */
    public ArchiveHandler(Whisperwind app) {
        this.app = app;
    }

    /**
     * Processes the specified archive command argument and executes the corresponding action.
     *
     * @param argument Archive command argument string.
     */
    public void handleArchiveCommand(String argument) {
        ArchiveCommand command = ArchiveCommand.fromString(argument);

        switch (command) {
            case ARCHIVE_ALL:
                handleArchiveAll();
                break;
            case ARCHIVE_COMPLETED:
                handleArchiveCompleted();
                break;
            case ARCHIVE_TODO:
                handleArchiveByType(TaskType.TODO);
                break;
            case ARCHIVE_DEADLINE:
                handleArchiveByType(TaskType.DEADLINE);
                break;
            case ARCHIVE_EVENT:
                handleArchiveByType(TaskType.EVENT);
                break;
            case LIST_ARCHIVES:
                app.getArchiveManager().listArchiveFiles();
                break;
            case VIEW_ARCHIVE:
                handleViewArchive(argument);
                break;
            default:
                System.out.println("❌ Invalid archive command: " + argument);
                showArchiveHelp();
                break;
        }
    }

    /**
     * Archives all tasks in the task list after user confirmation.
     * Clears the main task list and auto-saves after archiving.
     */
    private void handleArchiveAll() {
        if (app.getTasks().isEmpty()) {
            System.out.println("📭 No tasks to archive!");
            return;
        }

        System.out.println("🚨 You're about to archive ALL " + app.getTasks().getTaskCount() + " tasks!");
        System.out.print("❓ This will create a backup and clear your current list. Continue? (yes/no): ");

        try {
            String confirmation = getScanner().nextLine().trim().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                String archivePath = app.getArchiveManager().archiveAllTasks(app.getTasks());
                app.getTasks().clearAllTasks();
                app.autoSaveTasks();
                System.out.println("✨ Archive complete! Your task list is now fresh and empty.");
                System.out.println("💾 Archive saved to: " + archivePath);
            } else {
                System.out.println("😅 Archive operation cancelled.");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to archive tasks: " + e.getMessage());
        }
    }

    /**
     * Archives all completed tasks in the task list.
     * Removes completed tasks from the main list and auto-saves.
     */
    private void handleArchiveCompleted() {
        try {
            String archivePath = app.getArchiveManager().archiveCompletedTasks(app.getTasks());
            app.getTasks().deleteCompletedTasks();
            app.autoSaveTasks();
            System.out.println("✨ Removed completed tasks from main list.");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * Archives tasks of a specified type.
     * Removes archived tasks of that type from the main list and auto-saves.
     *
     * @param taskType The type of tasks to archive (TODO, DEADLINE, EVENT).
     */
    private void handleArchiveByType(TaskType taskType) {
        try {
            String archivePath = app.getArchiveManager().archiveTasksByType(app.getTasks(), taskType);
            app.getTasks().deleteTasksByType(taskType);
            app.autoSaveTasks();
            System.out.println("✨ Removed archived " + taskType.getDisplayName().toLowerCase() + " tasks from main list.");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * Views the content of a specific archive file.
     * If the archive number is not provided or invalid, lists all archive files.
     *
     * @param argument Command argument specifying which archive to view.
     */
    private void handleViewArchive(String argument) {
        try {
            String[] parts = argument.split(" ");
            if (parts.length > 1) {
                int archiveIndex = Integer.parseInt(parts[1]);
                app.getArchiveManager().viewArchive(archiveIndex);
            } else {
                System.out.println("❌ Please specify which archive to view: archive view [number]");
                app.getArchiveManager().listArchiveFiles();
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please provide a valid archive number.");
            app.getArchiveManager().listArchiveFiles();
        }
    }

    /**
     * Displays the available archive commands and usage instructions.
     */
    public void showArchiveHelp() {
        System.out.println("💡 Archive commands:");
        System.out.println("   archive all       - Archive all tasks and clear list");
        System.out.println("   archive completed - Archive only completed tasks");
        System.out.println("   archive todo      - Archive all todo tasks");
        System.out.println("   archive deadline  - Archive all deadline tasks");
        System.out.println("   archive event     - Archive all event tasks");
        System.out.println("   archive list      - List all archive files");
        System.out.println("   archive view 1    - View contents of archive file 1");
        System.out.println("💾 Archives are saved in: ./data/archive/");
    }

    /**
     * Returns the scanner instance from the Whisperwind application.
     *
     * @return Scanner instance for reading user input.
     */
    private Scanner getScanner() {
        return app.getScanner();
    }
}
