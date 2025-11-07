package whisperwind.controller;

import whisperwind.Whisperwind;
import whisperwind.util.InstructionManager;

/**
 * Handles user commands in the Whisperwind application.
 * Delegates task-related, schedule-related, and instruction commands
 * to the appropriate methods or managers.
 */
public class CommandHandler {
    private final Whisperwind app;

    /**
     * Constructs a CommandHandler for the given Whisperwind application instance.
     *
     * @param app Instance of the Whisperwind application.
     */
    public CommandHandler(Whisperwind app) {
        this.app = app;
    }

    /**
     * Handles a user command by dispatching it to the corresponding method.
     *
     * @param command The primary command keyword (e.g., "list", "todo", "mark").
     * @param parts   Array containing the command and its arguments.
     * @return {@code true} if the command signals program termination (e.g., "bye"); {@code false} otherwise.
     */
    public boolean handleCommand(String command, String[] parts) {
        switch (command) {
            case "list":
                handleListCommand();
                break;
            case "mark":
                handleMarkCommand(parts);
                break;
            case "unmark":
                handleUnmarkCommand(parts);
                break;
            case "delete":
                handleDeleteCommand(parts);
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
                handleSaveCommand();
                break;
            case "bye":
                return handleByeCommand();
            case "view":
                handleViewCommand(parts);
                break;
            case "find":
                handleFindCommand(parts);
                break;
            case "schedule":
                handleScheduleCommand(parts);
                break;
            default:
                handleUnknownCommand();
                break;
        }
        return false;
    }

    // Private helper methods

    /** Lists all tasks in the task list. */
    private void handleListCommand() {
        app.getTasks().listTasks();
    }

    /**
     * Marks specified tasks as completed.
     *
     * @param parts Command parts including task numbers to mark.
     */
    private void handleMarkCommand(String[] parts) {
        if (parts.length > 1) {
            app.getTaskManager().handleMarkCommand(parts);
            app.autoSaveTasks();
        } else {
            System.out.println("Wait, which task are we marking?");
            System.out.println("💡 You can mark multiple: mark 1,3,5");
        }
    }

    /**
     * Unmarks specified tasks as incomplete.
     *
     * @param parts Command parts including task numbers to unmark.
     */
    private void handleUnmarkCommand(String[] parts) {
        if (parts.length > 1) {
            app.getTaskManager().handleUnmarkCommand(parts);
            app.autoSaveTasks();
        } else {
            System.out.println("Give me the number so I can unmark it.");
            System.out.println("💡 You can unmark multiple: unmark 1,3,5");
        }
    }

    /**
     * Deletes specified tasks or shows delete instructions.
     *
     * @param parts Command parts including task numbers or "instruction".
     */
    private void handleDeleteCommand(String[] parts) {
        if (parts.length > 1) {
            if (parts[1].equalsIgnoreCase("instruction")) {
                InstructionManager.showDeleteInstructions();
            } else {
                app.getDeleteManager().handleDeleteCommand(parts);
                app.autoSaveTasks();
            }
        } else {
            System.out.println("Wait, what do you want to delete?");
            app.getDeleteManager().showDeleteHelp();
        }
    }

    /**
     * Adds a new todo task.
     *
     * @param parts Command parts containing the task description.
     */
    private void handleTodoCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            app.handleTodoCommand(parts);
            app.autoSaveTasks();
        } else {
            System.out.println("Wait, what's the todo? Give me the details!");
        }
    }

    /**
     * Adds a new deadline task.
     *
     * @param parts Command parts containing the task description and deadline.
     */
    private void handleDeadlineCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            app.handleDeadlineCommand(parts);
            app.autoSaveTasks();
        } else {
            System.out.println("Wait, what's the deadline? Give me the details!");
        }
    }

    /**
     * Adds a new event task.
     *
     * @param parts Command parts containing the event description and timing.
     */
    private void handleEventCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            app.handleEventCommand(parts);
            app.autoSaveTasks();
        } else {
            System.out.println("Wait, what's the event? Give me the details!");
        }
    }

    /** Saves all tasks to persistent storage. */
    private void handleSaveCommand() {
        app.saveTasks();
    }

    /**
     * Handles the "bye" command, saving tasks before exiting.
     *
     * @return {@code true} indicating the application should terminate.
     */
    private boolean handleByeCommand() {
        app.saveTasks();
        return true;
    }

    /**
     * Handles the "view" command to show instructions.
     *
     * @param parts Command parts to determine instruction type.
     */
    private void handleViewCommand(String[] parts) {
        if (parts.length > 1 && parts[1].equalsIgnoreCase("instruction")) {
            InstructionManager.showBasicInstructions();
        } else {
            System.out.println("Did you mean 'view instruction'?");
        }
    }

    /**
     * Handles the "find" command to search tasks by keyword or date.
     *
     * @param parts Command parts containing the search keyword or date.
     */
    private void handleFindCommand(String[] parts) {
        if (parts.length > 1) {
            if (parts[1].startsWith("on ")) {
                app.handleFindOnDateCommand(parts[1].substring(3).trim());
            } else {
                app.handleFindCommand(parts[1]);
            }
        } else {
            System.out.println("Usage: find KEYWORD or find on YYYY-MM-DD");
            System.out.println("Examples: find book, find on 2024-12-25");
        }
    }

    /**
     * Handles the "schedule" command to display tasks in a given date range.
     *
     * @param parts Command parts specifying the schedule period or date.
     */
    private void handleScheduleCommand(String[] parts) {
        if (parts.length > 1) {
            app.handleScheduleCommand(parts[1]);
        } else {
            System.out.println("Usage: schedule [today|tomorrow|upcoming|YYYY-MM-DD|YYYY-MM-DD to YYYY-MM-DD]");
        }
    }

    /** Handles unknown or unsupported commands by showing a prompt. */
    private void handleUnknownCommand() {
        System.out.println("I don't know that command! Type 'view instruction' to see what I can do.");
    }
}
