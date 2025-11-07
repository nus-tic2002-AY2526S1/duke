package whisperwind;

import whisperwind.controller.*;
import whisperwind.util.*;
import whisperwind.storage.*;
import java.util.Scanner;

/**
 * The main application class for Whisperwind.
 * <p>
 * This class manages the application lifecycle, including startup, command processing,
 * task management, scheduling, archiving, and saving/loading tasks to/from files.
 * It delegates specific operations to various managers such as {@link TaskManager},
 * {@link DeleteManager}, {@link ScheduleManager}, and {@link ArchiveManager}.
 * </p>
 */
public class Whisperwind {
    private TaskList tasks;
    private Scanner scanner;
    private TaskFileManager fileManager;
    private DeleteManager deleteManager;
    private TaskManager taskManager;
    private ScheduleManager scheduleManager;
    private ArchiveManager archiveManager;
    private CommandDispatcher commandDispatcher;

    private long lastCommandTime = 0;
    private static final long MIN_COMMAND_INTERVAL = 300;

    /**
     * Constructs a new Whisperwind application instance.
     * Initializes all managers and data structures.
     */
    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
        this.fileManager = new TaskFileManager();
        this.deleteManager = new DeleteManager(tasks, scanner);
        this.taskManager = new TaskManager(tasks, scanner);
        this.scheduleManager = new ScheduleManager(tasks);
        this.archiveManager = new ArchiveManager();
        this.commandDispatcher = new CommandDispatcher(this);
    }

    /**
     * Starts the main application loop.
     * Loads tasks from storage and listens for user input.
     */
    public void start() {
        try {
            InstructionManager.showWelcomeMessage();
            loadTasksOnStartup();

            boolean isExit = false;
            while (!isExit) {
                try {
                    System.out.print("> ");
                    String input = scanner.nextLine().trim();

                    if (input == null) {
                        System.out.println("End of input reached. Exiting...");
                        break;
                    }

                    isExit = commandDispatcher.dispatchCommand(input);
                    tasks.checkPerformance();

                } catch (java.util.NoSuchElementException e) {
                    System.out.println("No input detected. Exiting...");
                    break;
                } catch (IllegalStateException e) {
                    System.out.println("Input stream closed. Exiting...");
                    break;
                }
            }
            InstructionManager.showGoodbyeMessage();
        } catch (Exception e) {
            System.out.println("❌ Critical error in application: " + e.getMessage());
        }
    }

    // =========================
    // Command Handlers
    // =========================

    /**
     * Handles adding a todo task from user input.
     * @param parts the command arguments
     */
    public void handleTodoCommand(String[] parts) {
        try {
            tasks.addTodo(parts[1]);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Handles adding a deadline task from user input.
     * @param parts the command arguments
     */
    public void handleDeadlineCommand(String[] parts) {
        try {
            tasks.addDeadline(parts[1]);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Handles adding an event task from user input.
     * @param parts the command arguments
     */
    public void handleEventCommand(String[] parts) {
        try {
            tasks.addEvent(parts[1]);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Handles finding tasks on a specific date.
     * @param dateString the date string in yyyy-MM-dd format
     */
    public void handleFindOnDateCommand(String dateString) {
        fileManager.findTasksOnDate(tasks, dateString.trim());
    }

    /**
     * Handles finding tasks that contain a search term.
     * @param searchTerm the search keyword
     */
    public void handleFindCommand(String searchTerm) {
        tasks.displayMatchingTasks(searchTerm);
    }

    /**
     * Handles schedule command to show tasks for specific dates or ranges.
     * @param argument the schedule argument
     */
    public void handleScheduleCommand(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Usage: schedule [today|tomorrow|upcoming|YYYY-MM-DD|YYYY-MM-DD to YYYY-MM-DD]");
            return;
        }
        scheduleManager.showScheduleForDate(argument.trim());
    }

    // =========================
    // Utility Methods
    // =========================

    /**
     * Checks if user input commands are being entered too quickly.
     * @return true if allowed, false if rate-limited
     */
    public boolean checkRateLimit() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCommandTime < MIN_COMMAND_INTERVAL) {
            System.out.println("⏳ Whoa there! Slow down a bit...");
            return false;
        }
        lastCommandTime = currentTime;
        return true;
    }

    /**
     * Loads tasks from storage during startup.
     * Prints status messages based on success or failure.
     */
    private void loadTasksOnStartup() {
        try {
            System.out.println("🔍 Looking for saved tasks...");
            fileManager.loadTasks(tasks);
            System.out.println("✅ Loaded " + tasks.getTaskCount() + " tasks from previous session");
        } catch (Exception e) {
            System.out.println("💫 Starting fresh! No previous tasks found or error loading.");
        }
    }

    /**
     * Saves all tasks to storage.
     * Handles validation and prints status messages.
     */
    public void saveTasks() {
        try {
            if (tasks.validateTaskIntegrity()) {
                fileManager.saveTasks(tasks);
                System.out.println("💾 Tasks saved successfully!");
            } else {
                System.out.println("⚠️  Fixed some task issues before saving.");
                fileManager.saveTasks(tasks);
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Auto-saves tasks if needed based on internal timing.
     */
    public void autoSaveTasks() {
        try {
            if (tasks.getTaskCount() > 0) {
                fileManager.autoSaveTasks(tasks);
            }
        } catch (Exception e) {
            System.out.println("⚠️  Auto-save failed: " + e.getMessage());
        }
    }

    // =========================
    // Getters
    // =========================

    /** @return the current task list */
    public TaskList getTasks() { return tasks; }

    /** @return the application's Scanner instance */
    public Scanner getScanner() { return scanner; }

    /** @return the delete manager */
    public DeleteManager getDeleteManager() { return deleteManager; }

    /** @return the task manager */
    public TaskManager getTaskManager() { return taskManager; }

    /** @return the archive manager */
    public ArchiveManager getArchiveManager() { return archiveManager; }

    /**
     * Main entry point for the application.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new Whisperwind().start();
    }
}
