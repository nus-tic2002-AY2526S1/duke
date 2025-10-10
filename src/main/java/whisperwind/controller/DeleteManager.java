package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DeleteManager {
    private TaskList taskList;
    private java.util.Scanner scanner;

    public DeleteManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

    public void handleDeleteCommand(String[] parts) {
        String argument = parts[1].trim();
        DeleteOperation operation = DeleteOperation.fromString(argument);

        try {
            switch (operation) {
                case DeleteOperation.SINGLE:
                    handleSingleDelete(argument);
                    break;
                case DeleteOperation.BULK:
                    handleBulkDelete(argument);
                    break;
                case DeleteOperation.COMPLETED:
                    handleCompletedDelete();
                    break;
                case DeleteOperation.ALL:
                    handleAllDelete();
                    break;
                case DeleteOperation.PATTERN:
                    handlePatternDelete(argument);
                    break;
                case DeleteOperation.BY_TYPE:
                    handleTypeDelete(argument);
                    break;
                case DeleteOperation.UNKNOWN:
                default:
                    System.out.println("❌ Invalid delete command: " + argument);
                    showDeleteHelp();
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Error processing delete command: " + e.getMessage());
        }
    }

    private void handleSingleDelete(String argument) {
        try {
            int taskNumber = Integer.parseInt(argument);
            taskList.deleteTask(taskNumber);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid task number: " + argument);
        }
    }

    private void handleBulkDelete(String argument) {
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
            taskList.deleteTasksByPattern(pattern);
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
            case ConfirmationLevel.NONE:
                return true;
            case ConfirmationLevel.SIMPLE:
                return confirmSimple(action);
            case ConfirmationLevel.STRONG:
                return confirmStrong(action);
            case ConfirmationLevel.DESTRUCTIVE:
                return confirmDestructive(action);
            default:
                return false;
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

    private int[] parseBulkDeleteNumbers(String argument) {
        String[] numberStrings = argument.split(",");
        ArrayList<Integer> validNumbers = new ArrayList<>();

        for (String numStr : numberStrings) {
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
        return result;
    }
}