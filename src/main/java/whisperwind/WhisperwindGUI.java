package whisperwind;

import whisperwind.controller.*;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.*;
import whisperwind.storage.*;
import whisperwind.util.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * GUI version of Whisperwind that works without Scanner and captures output
 */
public class WhisperwindGUI {
    private Whisperwind backend;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private PrintStream originalOut;

    public WhisperwindGUI() {
        this.backend = new Whisperwind();
        this.outputStream = new ByteArrayOutputStream();
        this.printStream = new PrintStream(outputStream, true);
        this.originalOut = System.out;
    }

    /**
     * Process a command and return the output
     */
    public String processCommand(String input) {
        // Redirect System.out to capture output
        System.setOut(printStream);
        outputStream.reset();

        try {
            // Use reflection or direct method calls to process the command
            processCommandDirectly(input);

            // Get the captured output
            String output = outputStream.toString();
            outputStream.reset();

            return output;

        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Direct command processing without Scanner
     */
    private void processCommandDirectly(String input) {
        String sanitizedInput = InputSanitizer.sanitizeInput(input);

        if (sanitizedInput.isEmpty()) {
            System.out.println("It's giving 'silent treatment'. Say something!");
            return;
        }

        String[] parts = sanitizedInput.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        try {
            switch (command) {
                case "list":
                    getTaskList().listTasks();
                    break;
                case "mark":
                    if (!argument.isEmpty()) {
                        getTaskManager().handleMarkCommand(parts);
                    } else {
                        System.out.println("Wait, which task are we marking?");
                        System.out.println("💡 You can mark multiple: mark 1,3,5");
                    }
                    break;
                case "unmark":
                    if (!argument.isEmpty()) {
                        getTaskManager().handleUnmarkCommand(parts);
                    } else {
                        System.out.println("Give me the number so I can unmark it.");
                        System.out.println("💡 You can unmark multiple: unmark 1,3,5");
                    }
                    break;
                case "delete":
                    if (!argument.isEmpty()) {
                        if (argument.equalsIgnoreCase("instruction")) {
                            InstructionManager.showDeleteInstructions();
                        } else {
                            getDeleteManager().handleDeleteCommand(parts);
                        }
                    } else {
                        System.out.println("Wait, what do you want to delete?");
                        getDeleteManager().showDeleteHelp();
                    }
                    break;
                case "todo":
                    if (!argument.isEmpty()) {
                        getTaskList().addTodo(argument);
                    } else {
                        System.out.println("Wait, what's the todo? Give me the details!");
                    }
                    break;
                case "deadline":
                    if (!argument.isEmpty()) {
                        getTaskList().addDeadline(argument);
                    } else {
                        System.out.println("Wait, what's the deadline? Give me the details!");
                    }
                    break;
                case "event":
                    if (!argument.isEmpty()) {
                        getTaskList().addEvent(argument);
                    } else {
                        System.out.println("Wait, what's the event? Give me the details!");
                    }
                    break;
                case "find":
                    if (!argument.isEmpty()) {
                        if (argument.startsWith("on ")) {
                            getFileManager().findTasksOnDate(getTaskList(), argument.substring(3).trim());
                        } else {
                            getTaskList().displayMatchingTasks(argument);
                        }
                    } else {
                        System.out.println("Usage: find KEYWORD or find on YYYY-MM-DD");
                        System.out.println("Examples: find book, find on 2024-12-25");
                    }
                    break;
                case "schedule":
                    if (!argument.isEmpty()) {
                        handleScheduleCommand(argument);
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
                case "save":
                    saveTasks();
                    break;
                case "view":
                    if (argument.equals("instruction")) {
                        InstructionManager.showBasicInstructions();
                    } else {
                        System.out.println("Did you mean 'view instruction'?");
                    }
                    break;
                case "bye":
                    // Auto-save before exit
                    saveTasks();
                    System.out.println("👋 Goodbye! Thanks for using WhisperWind! 🌸");
                    break;
                default:
                    System.out.println("I don't know that command! Type 'view instruction' to see what I can do.");
            }
        } catch (Exception e) {
            System.out.println("❌ Oops! Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Handle schedule commands with special keywords
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
                    getScheduleManager().showScheduleForToday();
                    break;
                case "tomorrow":
                    getScheduleManager().showScheduleForTomorrow();
                    break;
                case "upcoming":
                    getScheduleManager().showUpcomingSchedule();
                    break;
                default:
                    if (scheduleArg.contains(" to ")) {
                        // Handle date range
                        String[] dates = scheduleArg.split(" to ");
                        if (dates.length == 2) {
                            getScheduleManager().showScheduleForDateRange(dates[0].trim(), dates[1].trim());
                        } else {
                            System.out.println("❌ Invalid date range format. Use: schedule YYYY-MM-DD to YYYY-MM-DD");
                        }
                    } else {
                        // Handle single date
                        getScheduleManager().showScheduleForDate(scheduleArg);
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Error displaying schedule: " + e.getMessage());
            System.out.println("💡 Make sure to use the correct date format: YYYY-MM-DD");
        }
    }

    /**
     * Load tasks on startup
     */
    public String loadTasksOnStartup() {
        System.setOut(printStream);
        outputStream.reset();

        System.out.println("🔍 Looking for saved tasks...");
        try {
            // Get the file manager and task list
            TaskFileManager fileManager = getFileManager();
            TaskList taskList = getTaskList();

            // Load tasks using the file manager
            fileManager.loadTasks(taskList);
            System.out.println("✅ Loaded " + taskList.getTaskCount() + " tasks from previous session");

        } catch (Exception e) {
            System.out.println("💫 Starting fresh! No previous tasks found or error loading.");
            System.out.println("💡 Error details: " + e.getMessage());
        }

        String output = outputStream.toString();
        outputStream.reset();
        System.setOut(originalOut);

        return output;
    }

    /**
     * Manually save tasks
     */
    public String saveTasks() {
        System.setOut(printStream);
        outputStream.reset();

        try {
            TaskFileManager fileManager = getFileManager();
            TaskList taskList = getTaskList();

            fileManager.saveTasks(taskList);
            System.out.println("💾 Tasks saved successfully!");

        } catch (Exception e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        }

        String output = outputStream.toString();
        outputStream.reset();
        System.setOut(originalOut);

        return output;
    }

    // Helper methods to access backend components
    private TaskList getTaskList() {
        try {
            java.lang.reflect.Field taskListField = backend.getClass().getDeclaredField("tasks");
            taskListField.setAccessible(true);
            return (TaskList) taskListField.get(backend);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access task list", e);
        }
    }

    private TaskManager getTaskManager() {
        try {
            java.lang.reflect.Field taskManagerField = backend.getClass().getDeclaredField("taskManager");
            taskManagerField.setAccessible(true);
            return (TaskManager) taskManagerField.get(backend);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access task manager", e);
        }
    }

    private DeleteManager getDeleteManager() {
        try {
            java.lang.reflect.Field deleteManagerField = backend.getClass().getDeclaredField("deleteManager");
            deleteManagerField.setAccessible(true);
            return (DeleteManager) deleteManagerField.get(backend);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access delete manager", e);
        }
    }

    private TaskFileManager getFileManager() {
        try {
            java.lang.reflect.Field fileManagerField = backend.getClass().getDeclaredField("fileManager");
            fileManagerField.setAccessible(true);
            return (TaskFileManager) fileManagerField.get(backend);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access file manager", e);
        }
    }

    private ScheduleManager getScheduleManager() {
        try {
            java.lang.reflect.Field scheduleManagerField = backend.getClass().getDeclaredField("scheduleManager");
            scheduleManagerField.setAccessible(true);
            return (ScheduleManager) scheduleManagerField.get(backend);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access schedule manager", e);
        }
    }

    /**
     * Get initial welcome message
     */
    public String getWelcomeMessage() {
        System.setOut(printStream);
        outputStream.reset();

        InstructionManager.showWelcomeMessage();

        String welcome = outputStream.toString();
        outputStream.reset();
        System.setOut(originalOut);

        return welcome;
    }
}