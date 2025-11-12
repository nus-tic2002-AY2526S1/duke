package whisperwind.controller;

import whisperwind.controller.TaskStorageHandler;

/**
 * Manages marking and unmarking of tasks in a task list.
 * <p>
 * Provides methods to handle commands for marking tasks as completed
 * or unmarking them as not completed, supporting both single and bulk operations.
 */
public class TaskManager {
    private TaskStorageHandler taskStorage;
    private final TaskMarker taskMarker;

    /**
     * Constructs a TaskManager with a given TaskList and Scanner.
     *
     * @param taskList The TaskList to operate on.
     * @param scanner  Scanner object for user input.
     */
    public TaskManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskStorage = taskStorage;
        this.taskMarker = new TaskMarker(taskList, scanner);
    }

    /**
     * Handles the "mark" command from user input.
     * <p>
     * Supports marking a single task or multiple tasks (comma-separated).
     *
     * @param parts Array of command parts from user input.
     */
    public void handleMarkCommand(String[] parts) {
        if (parts.length > 1) {
            String argument = parts[1].trim();
            if (argument.contains(",")) taskMarker.handleBulkMark(argument, true);
            else taskMarker.handleSingleMark(argument, true);
        }
    }

    /**
     * Handles the "unmark" command from user input.
     * <p>
     * Supports unmarking a single task or multiple tasks (comma-separated).
     *
     * @param parts Array of command parts from user input.
     */
    public void handleUnmarkCommand(String[] parts) {
        if (parts.length > 1) {
            String argument = parts[1].trim();
            if (argument.contains(",")) taskMarker.handleBulkMark(argument, false);
            else taskMarker.handleSingleMark(argument, false);
        }
    }
}
