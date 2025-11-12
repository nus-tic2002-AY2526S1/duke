package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import whisperwind.exceptions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Handles all task-related operations such as adding, deleting,
 * marking/unmarking, and evaluating performance of tasks.
 * <p>
 * Works in conjunction with {@link TaskStorageHandler} to manage
 * task persistence and integrity.
 */
public class TaskOperationHandler {
    private final TaskStorageHandler storage;

    /**
     * Constructs a TaskOperationHandler with a storage handler.
     *
     * @param storage The {@link TaskStorageHandler} to manage task storage.
     */
    public TaskOperationHandler(TaskStorageHandler storage) {
        this.storage = storage;
    }

    // === Add operations ===

    /**
     * Adds a new Todo task.
     *
     * @param description The task description.
     * @throws TaskException    If the task cannot be created.
     * @throws CommandException If the command format is invalid.
     */
    public void addTodo(String description) throws TaskException, CommandException {
        String sanitized = InputSanitizer.sanitizeDescription(description);
        Todo todo = new Todo(sanitized);
        storage.addTask(todo);
        System.out.println("✅ Added Todo: " + todo);
    }

    /**
     * Adds a new Deadline task.
     *
     * @param input Input string in the format: description /by YYYY-MM-DD HH:mm
     * @throws TaskException    If the task cannot be created.
     * @throws CommandException If the input format is invalid.
     */
    public void addDeadline(String input) throws TaskException, CommandException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2) throw new CommandException("❌ Invalid deadline format. Use: deadline <desc> /by <date>");
        Deadline d = new Deadline(parts[0], parts[1]);
        storage.addTask(d);
        System.out.println("✅ Added Deadline: " + d);
    }

    /**
     * Adds a new Event task.
     *
     * @param input Input string in the format: description /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm
     * @throws TaskException    If the task cannot be created.
     * @throws CommandException If the input format is invalid.
     */
    public void addEvent(String input) throws TaskException, CommandException {
        String[] parts = input.split(" /from | /to ");
        if (parts.length < 3) throw new CommandException("❌ Invalid event format. Use: event <desc> /from <start> /to <end>");
        Event e = new Event(parts[0], parts[1], parts[2]);
        storage.addTask(e);
        System.out.println("✅ Added Event: " + e);
    }

    // === Delete operations ===

    /**
     * Deletes a task by its number.
     *
     * @param num Task number (1-based index).
     * @throws CommandException If the task number is invalid.
     */
    public void deleteTask(int num) throws CommandException {
        if (num <= 0 || num > storage.getTaskCount()) {
            throw new CommandException("❌ Invalid task number: " + num);
        }
        Task deleted = storage.getTask(num);
        storage.removeTask(num - 1);
        System.out.println("🗑️ Deleted: " + deleted);
    }

    /**
     * Deletes multiple tasks given an array of task numbers.
     *
     * @param numbers Array of task numbers (1-based index).
     * @throws CommandException If any task number is invalid.
     */
    public void deleteMultipleTasks(int[] numbers) throws CommandException {
        Arrays.sort(numbers);
        for (int i = numbers.length - 1; i >= 0; i--) {
            int n = numbers[i];
            if (n > 0 && n <= storage.getTaskCount()) {
                storage.removeTask(n - 1);
            }
        }
        System.out.println("🧹 Multiple tasks deleted successfully.");
    }

    /**
     * Deletes tasks matching a search pattern with optional wildcards.
     *
     * @param pattern The search pattern (e.g., "book*", "*meeting", "*urgent*").
     * @throws CommandException If no tasks match the pattern or input is invalid.
     */
    public void deleteTasksByPattern(String pattern) throws CommandException {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new CommandException("Please provide a search pattern (e.g., 'book*', '*meeting', '*urgent*')");
        }

        String searchPattern = pattern.trim().toLowerCase();
        ArrayList<Task> matchingTasks = new ArrayList<>();
        ArrayList<Integer> matchingIndexes = new ArrayList<>();

        for (int i = 0; i < storage.getTaskCount(); i++) {
            Task task = storage.getTask(i + 1);
            if (task != null && matchesPattern(task, searchPattern)) {
                matchingTasks.add(task);
                matchingIndexes.add(i);
            }
        }

        if (matchingTasks.isEmpty()) {
            throw new CommandException("No tasks found matching pattern: " + pattern);
        }

        System.out.println("🔍 Found " + matchingTasks.size() + " tasks matching '" + pattern + "':");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + matchingTasks.get(i));
        }

        System.out.print("🗑️  Delete these " + matchingTasks.size() + " tasks? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            for (int i = matchingIndexes.size() - 1; i >= 0; i--) {
                storage.removeTask(matchingIndexes.get(i));
            }
            System.out.println("✅ Removed " + matchingTasks.size() + " tasks matching pattern: " + pattern);
        } else {
            System.out.println("😅 Deletion cancelled.");
        }
    }

    /**
     * Deletes tasks of a specific type.
     *
     * @param taskType The {@link TaskType} to delete.
     */
    public void deleteTasksByType(TaskType taskType) {
        Task[] allTasks = storage.getAllTasks();
        int count = 0;

        for (int i = allTasks.length - 1; i >= 0; i--) {
            Task t = allTasks[i];
            boolean match = false;

            switch (taskType) {
                case TODO -> match = t instanceof Todo;
                case DEADLINE -> match = t instanceof Deadline;
                case EVENT -> match = t instanceof Event;
                default -> { }
            }

            if (match) {
                storage.removeTask(i);
                count++;
            }
        }

        if (count > 0) {
            System.out.println("🧹 Deleted " + count + " " + taskType + " task(s).");
        } else {
            System.out.println("✅ No tasks of type " + taskType + " found.");
        }
    }

    /**
     * Deletes all completed tasks.
     */
    public void deleteCompletedTasks() {
        Task[] allTasks = storage.getAllTasks();
        int count = 0;
        for (int i = allTasks.length - 1; i >= 0; i--) {
            Task t = allTasks[i];
            if (t.isDone()) {
                storage.removeTask(i);
                count++;
            }
        }
        if (count > 0) {
            System.out.println("🧹 Deleted " + count + " completed task(s).");
        } else {
            System.out.println("✅ No completed tasks found to delete.");
        }
    }

    // === Mark/unmark operations ===

    /**
     * Marks a task as done.
     *
     * @param num Task number (1-based index).
     * @throws TaskException    If task cannot be marked.
     * @throws CommandException If task number is invalid.
     */
    public void markTask(int num) throws TaskException, CommandException {
        if (num <= 0 || num > storage.getTaskCount())
            throw new CommandException("❌ Invalid task number.");
        Task t = storage.getTask(num);
        t.markAsDone();
        System.out.println("✅ Marked as done: " + t);
    }

    /**
     * Marks a task as not done.
     *
     * @param num Task number (1-based index).
     * @throws TaskException    If task cannot be unmarked.
     * @throws CommandException If task number is invalid.
     */
    public void unmarkTask(int num) throws TaskException, CommandException {
        if (num <= 0 || num > storage.getTaskCount())
            throw new CommandException("❌ Invalid task number.");
        Task t = storage.getTask(num);
        t.markAsUndone();
        System.out.println("🔄 Marked as not done: " + t);
    }

    /**
     * Evaluates and prints the completion performance of all tasks.
     * Provides feedback based on completion rate.
     */
    public void checkPerformance() {
        Task[] allTasks = storage.getAllTasks();
        if (allTasks.length == 0) {
            System.out.println("📭 No tasks to evaluate performance.");
            return;
        }

        int completed = 0;
        for (Task t : allTasks) {
            if (t.isDone()) {
                completed++;
            }
        }

        double completionRate = (completed * 100.0) / allTasks.length;

        System.out.println("📊 Task Performance Summary:");
        System.out.println("-----------------------------");
        System.out.println("Total tasks     : " + allTasks.length);
        System.out.println("Completed tasks : " + completed);
        System.out.printf("Completion rate : %.2f%%\n", completionRate);

        if (completionRate == 100) {
            System.out.println("🌟 Excellent! All tasks completed!");
        } else if (completionRate >= 70) {
            System.out.println("💪 Good job! Keep it up!");
        } else if (completionRate >= 40) {
            System.out.println("🕓 You’re making progress, stay consistent!");
        } else {
            System.out.println("🚀 Let’s pick up the pace!");
        }
    }

    /**
     * Helper method to check if a task description matches a search pattern.
     *
     * @param task    The task to check.
     * @param pattern The pattern to match, supports '*' wildcards.
     * @return True if the task matches the pattern, false otherwise.
     */
    private boolean matchesPattern(Task task, String pattern) {
        if (task == null || pattern == null) return false;

        String desc = task.getDescription().toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.startsWith("*") && pattern.endsWith("*")) {
            return desc.contains(pattern.substring(1, pattern.length() - 1));
        } else if (pattern.startsWith("*")) {
            return desc.endsWith(pattern.substring(1));
        } else if (pattern.endsWith("*")) {
            return desc.startsWith(pattern.substring(0, pattern.length() - 1));
        } else {
            return desc.equals(pattern);
        }
    }
}
