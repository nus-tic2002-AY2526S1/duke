package whisperwind.controller;

import whisperwind.exceptions.CommandException;
import whisperwind.model.*;
import whisperwind.util.*;
import java.util.ArrayList;

/**
 * The {@code DeleteManager} class handles various delete commands related to task management.
 * It supports deleting single, multiple, completed, or all tasks, as well as deletions by pattern or task type.
 * <p>
 * This class provides confirmation prompts for different levels of deletion severity.
 * </p>
 */
public class DeleteManager {
    private TaskList taskList;
    private java.util.Scanner scanner;

    /**
     * Constructs a {@code DeleteManager} instance with the specified task list and scanner.
     *
     * @param taskList the {@link TaskList} containing all user tasks
     * @param scanner  the {@link java.util.Scanner} used for reading user confirmation input
     */
    public DeleteManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

    /**
     * Handles the user input for the delete command.
     * Parses the operation type and executes the corresponding delete logic.
     *
     * @param parts an array containing the command and its arguments
     */
    public void handleDeleteCommand(String[] parts) {
        assert parts != null : "Command parts should not be null";
        assert parts.length >= 2 : "Delete command should have arguments";

        String argument = parts[1].trim();
        DeleteOperation operation = DeleteOperation.fromString(argument);

        try {
            switch (operation) {
                case SINGLE:
                    handleSingleDelete(argument);
                    break;
                case BULK:
                    handleBulkDelete(argument);
                    break;
                case COMPLETED:
                    handleCompletedDelete();
                    break;
                case ALL:
                    handleAllDelete();
                    break;
                case PATTERN:
                    handlePatternDelete(argument);
                    break;
                case BY_TYPE:
                    handleTypeDelete(argument);
                    break;
                case UNKNOWN:
                default:
                    // This should never happen if fromString works correctly
                    assert false : "Unhandled operation type: " + operation;
                    System.out.println("❌ Invalid delete command: " + argument);
                    showDeleteHelp();
                    break;
            }
        }catch (Exception e) {
            System.out.println("❌ Error processing delete command: " + e.getMessage());
        }
    }

    /**
     * Deletes a single task based on its number.
     *
     * @param argument the string containing the task number
     */
    private void handleSingleDelete(String argument) {
        assert argument != null : "Argument should not be null";
        assert !argument.trim().isEmpty() : "Argument should not be empty";

        try {
            int taskNumber = Integer.parseInt(argument);
            assert taskNumber > 0 : "Task number should be positive";
            taskList.deleteTask(taskNumber);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid task number: " + argument);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the deletion of multiple tasks specified in a comma-separated list.
     *
     * @param argument the string containing multiple task numbers (e.g. "1,2,3")
     */
    private void handleBulkDelete(String argument) throws CommandException {
        int[] taskNumbers = parseBulkDeleteNumbers(argument);
        if (taskNumbers.length > 0) {
            ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.BULK);
            if (requiresConfirmation(level, "delete " + taskNumbers.length + " tasks")) {
                taskList.deleteMultipleTasks(taskNumbers);
            }
        } else {
            System.out.println("❌ No valid task numbers found in: " + argument);
        }
    }

    /**
     * Handles deletion of all completed tasks after confirmation.
     */
    private void handleCompletedDelete() throws CommandException {
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.COMPLETED);
        if (requiresConfirmation(level, "delete all completed tasks")) {
            try {
                taskList.deleteCompletedTasks();
            } catch (CommandException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles deletion of all tasks after confirmation.
     */
    private void handleAllDelete() throws CommandException {
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.ALL);
        if (requiresConfirmation(level, "delete ALL tasks")) {
            try {
                taskList.clearAllTasks();
            } catch (CommandException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles deletion of tasks matching a specific name pattern.
     *
     * @param argument the argument containing the task name pattern
     */
    private void handlePatternDelete(String argument) throws CommandException {
        String pattern = argument.substring(0, argument.length() - 1);
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.PATTERN);
        if (requiresConfirmation(level, "delete tasks matching pattern: " + pattern)) {
            taskList.deleteTasksByPattern(pattern);
        }
    }

    /**
     * Handles deletion of all tasks of a specific type (e.g. todo, deadline, event).
     *
     * @param argument the argument representing the task type
     */
    private void handleTypeDelete(String argument) {
        TaskType taskType = TaskType.fromString(argument);
        if (taskType != TaskType.UNKNOWN) {
            ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.BY_TYPE);
            if (requiresConfirmation(level, "delete all " + taskType.getDisplayName().toLowerCase() + " tasks")) {
                try {
                    taskList.deleteTasksByType(taskType);
                } catch (CommandException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("❌ Invalid task type: " + argument);
            System.out.println("💡 Valid types: todo, deadline, event");
        }
    }

    /**
     * Checks whether confirmation is required before performing the specified action.
     *
     * @param level  the {@link ConfirmationLevel} required for the operation
     * @param action a description of the action being confirmed
     * @return {@code true} if the user confirmed the operation, otherwise {@code false}
     */
    private boolean requiresConfirmation(ConfirmationLevel level, String action) {
        assert level != null : "Confirmation level should not be null";
        assert action != null && !action.isEmpty() : "Action description required";

        switch (level) {
            case NONE:
                return true;
            case SIMPLE:
                return confirmSimple(action);
            case STRONG:
                return confirmStrong(action);
            case DESTRUCTIVE:
                return confirmDestructive(action);
            default:
                assert false : "Unhandled confirmation level: " + level;
                return false;
        }
    }

    /**
     * Prompts the user for a simple yes/no confirmation.
     *
     * @param action the action being confirmed
     * @return {@code true} if the user entered "yes" or "y"
     */
    private boolean confirmSimple(String action) {
        System.out.print("❓ Delete " + action + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes") || confirmation.equals("y");
    }

    /**
     * Prompts the user with a strong confirmation message before executing a potentially irreversible action.
     *
     * @param action the action being confirmed
     * @return {@code true} if the user confirmed with "yes" or "y"
     */
    private boolean confirmStrong(String action) {
        System.out.println("🚨 You're about to " + action + "!");
        System.out.print("❓ Are you sure? This cannot be undone! (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes") || confirmation.equals("y");
    }

    /**
     * Prompts the user with a critical confirmation requiring exact phrase input for destructive actions.
     *
     * @param action the action being confirmed
     * @return {@code true} if the user correctly typed the confirmation phrase
     */
    private boolean confirmDestructive(String action) {
        System.out.println("💥 CRITICAL: You're about to " + action + "!");
        System.out.println("🔥 This will PERMANENTLY remove data!");
        System.out.print("🚨 Type 'DELETE " + action.toUpperCase() + "' to confirm: ");
        String confirmation = scanner.nextLine().trim();
        return confirmation.equals("DELETE " + action.toUpperCase());
    }

    /**
     * Displays help information for all available delete commands.
     */
    public void showDeleteHelp() {
        System.out.println("💡 Delete commands:");
        for (DeleteOperation op : DeleteOperation.values()) {
            if (op != DeleteOperation.UNKNOWN) {
                System.out.printf("   %s delete %-12s - %s%n",
                        op.getEmoji(), op.getOperation(), op.getDescription());
            }
        }
        System.out.println("   Examples: delete 3, delete 1,3,5, delete completed, delete all, delete book*");
    }

    /**
     * Parses a string of comma-separated task numbers into an integer array.
     * Invalid numbers are skipped with a warning message.
     *
     * @param argument a comma-separated string of task numbers (e.g. "1,2,3")
     * @return an array of valid task numbers
     */
    private int[] parseBulkDeleteNumbers(String argument) {
        // Assertions for internal method contracts
        assert argument != null : "Argument should not be null in private method";

        String[] numberStrings = argument.split(",");
        ArrayList<Integer> validNumbers = new ArrayList<>();

        for (String numStr : numberStrings) {
            assert numStr != null : "Split result should not contain null";
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num > 0) {
                    validNumbers.add(num);
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Skipping invalid number: " + numStr);
            }
        }

        int[] result = new int[validNumbers.size()];
        for (int i = 0; i < validNumbers.size(); i++) {
            result[i] = validNumbers.get(i);
        }

        assert result != null : "Result array should be initialized";
        return result;
    }
}
