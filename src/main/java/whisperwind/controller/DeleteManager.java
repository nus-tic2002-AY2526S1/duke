package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import java.util.ArrayList;
import whisperwind.exceptions.CommandException;

/**
 * Manages deletion of tasks from a {@link TaskList}.
 * Supports single, bulk, completed, all, pattern-based, and type-based deletions.
 * Also handles user confirmation levels for different destructive operations.
 */
public class DeleteManager {
    private TaskList taskList;
    private java.util.Scanner scanner;

    /**
     * Constructs a DeleteManager with the given task list and scanner.
     *
     * @param taskList TaskList instance to perform deletions on.
     * @param scanner  Scanner for reading user input for confirmations.
     */
    public DeleteManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

    /**
     * Handles a delete command based on user input arguments.
     * Supports single, bulk, completed, all, pattern, and type deletions.
     *
     * @param parts Array of command parts, where the first element is "delete"
     *              and the second element specifies the deletion operation or arguments.
     */
    public void handleDeleteCommand(String[] parts) {
        if (parts == null || parts.length < 2) {
            showDeleteHelp();
            return;
        }

        String argument = parts[1].trim();
        DeleteOperation operation = DeleteOperation.fromString(argument);

        switch (operation) {
            case SINGLE:
                try {
                    handleSingleDelete(argument);
                } catch (CommandException e) {
                    System.out.println("❌ " + e.getMessage());
                }
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
            default:
                System.out.println("❌ Invalid delete command: " + argument);
                showDeleteHelp();
                break;
        }
    }

    /**
     * Displays help information for delete commands, including examples and supported operations.
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

    // --- Private helper methods ---

    private void handleSingleDelete(String argument) throws CommandException {
        int taskNumber = Integer.parseInt(argument);
        taskList.deleteTask(taskNumber);
    }

    private void handleBulkDelete(String argument) {
        String[] numberStrings = argument.split(",");
        ArrayList<Integer> validNumbers = new ArrayList<>();

        for (String numStr : numberStrings) {
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num > 0) validNumbers.add(num);
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Skipping invalid number: " + numStr);
            }
        }

        if (validNumbers.isEmpty()) {
            System.out.println("❌ No valid task numbers found in: " + argument);
            return;
        }

        int[] taskNumbers = validNumbers.stream().mapToInt(i -> i).toArray();
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.BULK);
        if (requiresConfirmation(level, "delete " + taskNumbers.length + " tasks")) {
            try {
                taskList.deleteMultipleTasks(taskNumbers);
            } catch (CommandException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void handleCompletedDelete() {
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.COMPLETED);
        if (requiresConfirmation(level, "delete all completed tasks")) {
            taskList.deleteCompletedTasks();
        }
    }

    private void handleAllDelete() {
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.ALL);
        if (requiresConfirmation(level, "delete ALL tasks")) {
            taskList.clearAllTasks();
        }
    }

    private void handlePatternDelete(String argument) {
        String pattern = argument.substring(0, argument.length() - 1);
        ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.PATTERN);
        if (requiresConfirmation(level, "delete tasks matching pattern: " + pattern)) {
            try {
                taskList.deleteTasksByPattern(pattern);
            } catch (CommandException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void handleTypeDelete(String argument) {
        TaskType taskType = TaskType.fromString(argument);
        if (taskType != TaskType.UNKNOWN) {
            ConfirmationLevel level = ConfirmationLevel.forOperation(DeleteOperation.BY_TYPE);
            if (requiresConfirmation(level, "delete all " + taskType.getDisplayName().toLowerCase() + " tasks")) {
                taskList.deleteTasksByType(taskType);
            }
        } else {
            System.out.println("❌ Invalid task type: " + argument);
            System.out.println("💡 Valid types: todo, deadline, event");
        }
    }

    private boolean requiresConfirmation(ConfirmationLevel level, String action) {
        switch (level) {
            case NONE: return true;
            case SIMPLE: return confirmSimple(action);
            case STRONG: return confirmStrong(action);
            case DESTRUCTIVE: return confirmDestructive(action);
            default: return false;
        }
    }

    private boolean confirmSimple(String action) {
        System.out.print("❓ Delete " + action + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes") || confirmation.equals("y");
    }

    private boolean confirmStrong(String action) {
        System.out.println("🚨 You're about to " + action + "!");
        System.out.print("❓ Are you sure? This cannot be undone! (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes") || confirmation.equals("y");
    }

    private boolean confirmDestructive(String action) {
        System.out.println("💥 CRITICAL: You're about to " + action + "!");
        System.out.println("🔥 This will PERMANENTLY remove data!");
        System.out.print("🚨 Type 'DELETE " + action.toUpperCase() + "' to confirm: ");
        String confirmation = scanner.nextLine().trim();
        return confirmation.equals("DELETE " + action.toUpperCase());
    }
}
