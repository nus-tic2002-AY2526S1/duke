package whisperwind.controller;

import whisperwind.model.Task;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles marking and unmarking of tasks in a TaskList.
 * <p>
 * Supports both single-task operations and bulk operations with user confirmation.
 */
public class TaskMarker {
    private final TaskList taskList;
    private final Scanner scanner;

    /**
     * Constructs a TaskMarker for a given TaskList.
     *
     * @param taskList The TaskList to operate on.
     * @param scanner  Scanner object for reading user input.
     */
    public TaskMarker(TaskList taskList, Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

    /**
     * Marks a specific task as completed.
     *
     * @param taskNumber The index of the task to mark (1-based).
     * @throws TaskException    If task operation fails.
     * @throws CommandException If command cannot be executed.
     */
    public void markTask(int taskNumber) throws TaskException, CommandException {
        taskList.markTask(taskNumber);
    }

    /**
     * Unmarks a specific task as not completed.
     *
     * @param taskNumber The index of the task to unmark (1-based).
     * @throws TaskException    If task operation fails.
     * @throws CommandException If command cannot be executed.
     */
    public void unmarkTask(int taskNumber) throws TaskException, CommandException {
        taskList.unmarkTask(taskNumber);
    }

    /**
     * Handles marking or unmarking a single task based on user input.
     *
     * @param argument   The task number as a string.
     * @param markAsDone True to mark as completed, false to unmark.
     */
    public void handleSingleMark(String argument, boolean markAsDone) {
        try {
            int taskNumber = Integer.parseInt(argument);
            if (markAsDone) markTask(taskNumber);
            else unmarkTask(taskNumber);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid task number: " + argument);
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * Handles marking or unmarking multiple tasks in bulk with confirmation.
     *
     * @param argument   Comma-separated task numbers (e.g., "1,3,5").
     * @param markAsDone True to mark as completed, false to unmark.
     */
    public void handleBulkMark(String argument, boolean markAsDone) {
        int[] taskNumbers = parseBulkNumbers(argument);
        if (taskNumbers.length == 0) {
            System.out.println("❌ No valid task numbers found in: " + argument);
            return;
        }

        String action = markAsDone ? "mark" : "unmark";
        ArrayList<Task> tasksToProcess = new ArrayList<>();
        ArrayList<Integer> validIndexes = new ArrayList<>();

        for (int taskNumber : taskNumbers) {
            if (taskNumber > 0 && taskNumber <= taskList.getTaskCount()) {
                Task task = taskList.getTask(taskNumber);
                tasksToProcess.add(task);
                validIndexes.add(taskNumber);
                System.out.println("  " + taskNumber + ". " + task);
            } else {
                System.out.println("⚠️  Skipping invalid task number: " + taskNumber);
            }
        }

        if (tasksToProcess.isEmpty()) {
            System.out.println("❌ No valid task numbers to " + action + ".");
            return;
        }

        System.out.print("🔄 " + (markAsDone ? "Mark" : "Unmark") + " these " + tasksToProcess.size() + " tasks? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            int successCount = 0;
            for (int taskNumber : validIndexes) {
                try {
                    if (markAsDone) markTask(taskNumber);
                    else unmarkTask(taskNumber);
                    successCount++;
                } catch (Exception e) {
                    System.out.println("⚠️  Failed to process task " + taskNumber + ": " + e.getMessage());
                }
            }
            System.out.println("✅ " + (markAsDone ? "Marked" : "Unmarked") + " " + successCount + " tasks successfully.");
        } else {
            System.out.println("😅 Operation cancelled.");
        }
    }

    /**
     * Parses a comma-separated string of task numbers into an integer array.
     *
     * @param argument Comma-separated task numbers (e.g., "1,3,5").
     * @return Array of valid task numbers.
     */
    private int[] parseBulkNumbers(String argument) {
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

        return validNumbers.stream().mapToInt(i -> i).toArray();
    }
}
