package whisperwind;

import whisperwind.controller.*;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.*;
import whisperwind.storage.*;
import whisperwind.util.*;
import java.util.Scanner;
import java.io.IOException;

/**
 * The {@code Whisperwind} class is the main entry point for the Whisperwind task management application.
 * It handles user input, command processing, and coordinates between different components of the system.
 * <p>
 * This class manages the application lifecycle including startup, command processing, and shutdown.
 * </p>
 */
public class Whisperwind {

    private TaskList tasks;
    private Scanner scanner;
    private TaskFileManager fileManager;
    private DeleteManager deleteManager;
    private TaskManager taskManager;
    private ScheduleManager scheduleManager;
    private long lastCommandTime = 0;
    private static final long MIN_COMMAND_INTERVAL = 300;

    /**
     * Constructs a new Whisperwind application instance.
     * Initializes all necessary components including task list, file manager, and command handlers.
     */
    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
        this.fileManager = new TaskFileManager();
        this.deleteManager = new DeleteManager(tasks, scanner);
        this.taskManager = new TaskManager(tasks, scanner);
        this.scheduleManager = new ScheduleManager(tasks);
    }

    /**
     * Starts the main application loop.
     * <p>
     * This method displays the welcome message, loads existing tasks, and processes
     * user commands until the exit command is received.
     * </p>
     */
    public void start() {
        try {
            InstructionManager.showWelcomeMessage();
            loadTasksOnStartup();

            boolean isExit = false;
            while (!isExit) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                input = InputSanitizer.sanitizeInput(input);

                if (input.isEmpty()) {
                    System.out.println("It's giving 'silent treatment'. Say something!");
                    continue;
                }

                if (!checkRateLimit()) {
                    continue;
                }

                // Handle instruction commands first
                if (input.equalsIgnoreCase("view instruction")) {
                    InstructionManager.showBasicInstructions();
                    continue;
                }

                if (input.equalsIgnoreCase("delete instruction")) {
                    InstructionManager.showDeleteInstructions();
                    continue;
                }

                String[] parts = input.split(" ", 2);
                String command = parts[0].toLowerCase();

                try {
                    switch (command) {
                        case "list":
                            tasks.listTasks();
                            break;
                        case "mark":
                            if (parts.length > 1) {
                                taskManager.handleMarkCommand(parts);
                                autoSaveTasks();
                            } else {
                                System.out.println("Wait, which task are we marking?");
                                System.out.println("💡 You can mark multiple: mark 1,3,5");
                            }
                            break;
                        case "unmark":
                            if (parts.length > 1) {
                                taskManager.handleUnmarkCommand(parts);
                                autoSaveTasks();
                            } else {
                                System.out.println("Give me the number so I can unmark it.");
                                System.out.println("💡 You can unmark multiple: unmark 1,3,5");
                            }
                            break;
                        case "delete":
                            if (parts.length > 1) {
                                if (parts[1].equalsIgnoreCase("instruction")) {
                                    InstructionManager.showDeleteInstructions();
                                } else {
                                    deleteManager.handleDeleteCommand(parts);
                                    autoSaveTasks();
                                }
                            } else {
                                System.out.println("Wait, what do you want to delete?");
                                deleteManager.showDeleteHelp();
                            }
                            break;
                        case "todo":
                            handleTodoCommand(parts);
                            break;
                        case "deadline":
                            handleDeadlineCommand(parts);
                            break;
                        case "event":
                            handleEventCommand(parts);
                            break;
                        case "save":
                            saveTasks();
                            break;
                        case "bye":
                            saveTasks();
                            isExit = true;
                            break;
                        case "view":
                            if (parts.length > 1 && parts[1].equalsIgnoreCase("instruction")) {
                                InstructionManager.showBasicInstructions();
                            } else {
                                System.out.println("Did you mean 'view instruction'?");
                            }
                            break;
                        case "find":
                            if (parts.length > 1 && parts[1].startsWith("on ")) {
                                handleFindOnDateCommand(parts[1].substring(3).trim());
                            } else {
                                System.out.println("Usage: find on YYYY-MM-DD");
                                System.out.println("Example: find on 2024-12-25");
                            }
                            break;
                        case "schedule":
                            if (parts.length > 1) {
                                handleScheduleCommand(parts[1]);
                            } else {
                                System.out.println("Usage: schedule [today|tomorrow|upcoming|YYYY-MM-DD|YYYY-MM-DD to YYYY-MM-DD]");
                                System.out.println("Examples:");
                                System.out.println("  schedule today");
                                System.out.println("  schedule tomorrow");
                                System.out.println("  schedule upcoming");
                                System.out.println("  schedule 2024-12-25");
                                System.out.println("  schedule 2024-12-20 to 2024-12-25");
                            }
                            break;
                        default:
                            System.out.println("I don't know that command! Type 'view instruction' to see what I can do.");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Oops! Something went wrong. Let's try that again!");
                    System.out.println("💡 Error: " + e.getMessage());
                }

                tasks.checkPerformance();
            }
            InstructionManager.showGoodbyeMessage();
        } catch (Exception e) {
            System.out.println("❌ Critical error in application: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * Handles the todo command to add a new simple task.
     *
     * @param parts The command parts containing the todo description
     */
    private void handleTodoCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            try {
                tasks.addTodo(parts[1]);
            } catch (TaskException e) {
                throw new RuntimeException(e);
            } catch (CommandException e) {
                throw new RuntimeException(e);
            }
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the todo? Give me the details!");
        }
    }

    /**
     * Handles the deadline command to add a new task with a deadline.
     *
     * @param parts The command parts containing the deadline description and time
     */
    private void handleDeadlineCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            try {
                tasks.addDeadline(parts[1]);
            } catch (TaskException e) {
                throw new RuntimeException(e);
            } catch (CommandException e) {
                throw new RuntimeException(e);
            }
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the deadline? Give me the details!");
        }
    }

    /**
     * Handles the event command to add a new event with start and end times.
     *
     * @param parts The command parts containing the event description and times
     */
    private void handleEventCommand(String[] parts) throws TaskException, CommandException {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            tasks.addEvent(parts[1]);
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the event? Give me the details!");
        }
    }

    /**
     * Handles the find on command to search for tasks on a specific date.
     *
     * @param dateString The date string in YYYY-MM-DD format
     */
    private void handleFindOnDateCommand(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            System.out.println("Please provide a date in YYYY-MM-DD format");
            return;
        }
        fileManager.findTasksOnDate(tasks, dateString.trim());
    }

    /**
     * Handles the schedule command to view tasks in schedule format.
     *
     * @param argument The schedule argument (date, range, or keyword)
     */
    private void handleScheduleCommand(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Usage: schedule [today|tomorrow|upcoming|YYYY-MM-DD|YYYY-MM-DD to YYYY-MM-DD]");
            return;
        }

        String scheduleArg = argument.trim().toLowerCase();

        try {
            switch (scheduleArg) {
                case "today":
                    scheduleManager.showScheduleForToday();
                    break;
                case "tomorrow":
                    scheduleManager.showScheduleForTomorrow();
                    break;
                case "upcoming":
                    scheduleManager.showUpcomingSchedule();
                    break;
                default:
                    if (scheduleArg.contains(" to ")) {
                        // Handle date range
                        String[] dates = scheduleArg.split(" to ");
                        if (dates.length == 2) {
                            scheduleManager.showScheduleForDateRange(dates[0].trim(), dates[1].trim());
                        } else {
                            System.out.println("❌ Invalid date range format. Use: schedule YYYY-MM-DD to YYYY-MM-DD");
                        }
                    } else {
                        // Handle single date
                        scheduleManager.showScheduleForDate(scheduleArg);
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Error displaying schedule: " + e.getMessage());
            System.out.println("💡 Make sure to use the correct date format: YYYY-MM-DD");
        }
    }

    /**
     * Checks if the command rate limit has been exceeded.
     *
     * @return true if the command can proceed, false if rate limited
     */
    private boolean checkRateLimit() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCommandTime < MIN_COMMAND_INTERVAL) {
            System.out.println("⏳ Whoa there! Slow down a bit...");
            return false;
        }
        lastCommandTime = currentTime;
        return true;
    }

    /**
     * Loads tasks from storage on application startup.
     */
    private void loadTasksOnStartup() {
        try {
            System.out.println("🔍 Looking for saved tasks...");
            fileManager.loadTasks(tasks);
            System.out.println("✅ Loaded " + tasks.getTaskCount() + " tasks from previous session");
        } catch (IOException e) {
            System.out.println("💫 Starting fresh! No previous tasks found or error loading.");
        }
    }

    /**
     * Manually saves all tasks to persistent storage.
     */
    private void saveTasks() {
        try {
            if (tasks.validateTaskIntegrity()) {
                fileManager.saveTasks(tasks);
                System.out.println("💾 Tasks saved successfully!");
            } else {
                System.out.println("⚠️  Fixed some task issues before saving.");
                fileManager.saveTasks(tasks);
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("❌ No permission to save tasks file!");
        }
    }

    /**
     * Automatically saves tasks based on internal timing logic.
     */
    private void autoSaveTasks() {
        try {
            if (tasks.getTaskCount() > 0) {
                fileManager.autoSaveTasks(tasks);
            }
        } catch (Exception e) {
            System.out.println("⚠️  Auto-save failed: " + e.getMessage());
        }
    }

    /**
     * The main entry point for the Whisperwind application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            new Whisperwind().start();
        } catch (Exception e) {
            System.out.println("💥 Critical error! Whisperwind has to close. Bye!");
            System.out.println("Error: " + e.getMessage());
        }
    }
}