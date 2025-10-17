package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import whisperwind.exceptions.TaskException;
import whisperwind.exceptions.CommandException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@code TaskList} class manages a collection of tasks and provides operations
 * for adding, deleting, marking, and listing tasks.
 * <p>
 * This class maintains task integrity, validates operations, and provides statistics
 * about the task collection.
 * </p>
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private static final int MAX_TASKS = 1000;
    private static final int WARNING_THRESHOLD = 500;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new todo task to the list.
     *
     * @param description The description of the todo task
     * @throws TaskException If the task cannot be created
     * @throws CommandException If task limit is reached
     */
    public void addTodo(String description) throws TaskException, CommandException {
        if (tasks.size() >= MAX_TASKS) {
            throw new CommandException("Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
        }

        String sanitizedDesc = InputSanitizer.sanitizeDescription(description);
        if (sanitizedDesc.isEmpty()) {
            throw new CommandException("Wait, what's the todo? Give me the details!");
        }

        try {
            Todo newTask = new Todo(sanitizedDesc);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (TaskException e) {
            throw new TaskException("Couldn't add the todo: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new deadline task to the list.
     *
     * @param input The input string containing description and deadline time
     * @throws TaskException If the task cannot be created
     * @throws CommandException If task limit is reached or input is invalid
     */
    public void addDeadline(String input) throws TaskException, CommandException {
        if (tasks.size() >= MAX_TASKS) {
            throw new CommandException("Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            throw new CommandException("Wait, what's the deadline? Give me the details!");
        }

        String[] deadlineParts = sanitizedInput.split(" /by ", 2);
        if (deadlineParts.length < 2) {
            throw new CommandException("Deadline format should be: deadline DESCRIPTION /by TIME");
        }

        String description = InputSanitizer.sanitizeDescription(deadlineParts[0]);
        String by = InputSanitizer.sanitizeTime(deadlineParts[1]);

        if (description.isEmpty()) {
            throw new CommandException("Wait, what's the deadline description? Give me the details!");
        }
        if (by.isEmpty()) {
            throw new CommandException("When is this deadline due? Add the time after /by!");
        }

        try {
            Deadline newTask = new Deadline(description, by);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (TaskException e) {
            // Provide more user-friendly error messages
            if (e.getMessage().contains("cannot be in the past")) {
                throw new TaskException("❌ " + e.getMessage() + "\n💡 " + InputSanitizer.getFutureDateTimeFormatExamples());
            } else {
                throw new TaskException("Couldn't add the deadline: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Adds a new event task to the list.
     *
     * @param input The input string containing description and event times
     * @throws TaskException If the task cannot be created
     * @throws CommandException If task limit is reached or input is invalid
     */
    public void addEvent(String input) throws TaskException, CommandException {
        if (tasks.size() >= MAX_TASKS) {
            throw new CommandException("Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            throw new CommandException("Wait, what's the event? Give me the details!");
        }

        String[] eventParts = sanitizedInput.split(" /from ", 2);
        if (eventParts.length < 2) {
            throw new CommandException("Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            throw new CommandException("Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
        }

        String description = InputSanitizer.sanitizeDescription(eventParts[0]);
        String from = InputSanitizer.sanitizeTime(timeParts[0]);
        String to = InputSanitizer.sanitizeTime(timeParts[1]);

        if (description.isEmpty()) {
            throw new CommandException("Wait, what's the event description? Give me the details!");
        }
        if (from.isEmpty()) {
            throw new CommandException("When does this event start? Add the start time after /from!");
        }
        if (to.isEmpty()) {
            throw new CommandException("When does this event end? Add the end time after /to!");
        }

        try {
            Event newTask = new Event(description, from, to);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (TaskException e) {
            // Provide more user-friendly error messages
            if (e.getMessage().contains("cannot be in the past")) {
                throw new TaskException("❌ " + e.getMessage() + "\n💡 " + InputSanitizer.getFutureDateTimeFormatExamples());
            } else {
                throw new TaskException("Couldn't add the event: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Deletes a single task by its number.
     *
     * @param taskNumber The number of the task to delete (1-based index)
     * @throws CommandException If task number is invalid
     */
    public void deleteTask(int taskNumber) throws CommandException {
        if (taskNumber <= 0) {
            throw new CommandException("Task numbers start from 1.");
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task taskToDelete = tasks.get(index);
                System.out.println("🗑️  Noted. I've removed this task:");
                System.out.println("  " + taskToDelete.toString());

                tasks.remove(index);

                System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");

                if (tasks.isEmpty()) {
                    System.out.println("🎉 Your task list is now empty! Time to relax or add new goals?");
                }

            } catch (Exception e) {
                throw new CommandException("Something went wrong deleting the task: " + e.getMessage());
            }
        } else {
            throw new CommandException("That task number doesn't exist.");
        }
    }

    /**
     * Deletes multiple tasks specified by their numbers.
     *
     * @param taskNumbers Array of task numbers to delete
     * @throws CommandException If no valid tasks to delete
     */
    public void deleteMultipleTasks(int[] taskNumbers) throws CommandException {
        if (taskNumbers == null || taskNumbers.length == 0) {
            throw new CommandException("No task numbers provided for deletion.");
        }

        if (tasks.isEmpty()) {
            throw new CommandException("Your task list is already empty!");
        }

        Arrays.sort(taskNumbers);
        int[] uniqueNumbers = Arrays.stream(taskNumbers).distinct().toArray();

        ArrayList<Task> tasksToDelete = new ArrayList<>();
        ArrayList<Integer> validIndexes = new ArrayList<>();

        for (int i = uniqueNumbers.length - 1; i >= 0; i--) {
            int taskNumber = uniqueNumbers[i];
            if (taskNumber > 0 && taskNumber <= tasks.size()) {
                tasksToDelete.add(tasks.get(taskNumber - 1));
                validIndexes.add(taskNumber - 1);
            } else {
                System.out.println("⚠️  Skipping invalid task number: " + taskNumber);
            }
        }

        if (tasksToDelete.isEmpty()) {
            throw new CommandException("No valid task numbers to delete.");
        }

        System.out.println("🔍 The following tasks will be deleted:");
        for (int i = 0; i < tasksToDelete.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + tasksToDelete.get(i).toString());
        }

        System.out.print("🗑️  Delete these " + tasksToDelete.size() + " tasks? (yes/no): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            for (int index : validIndexes) {
                tasks.remove(index);
            }
            System.out.println("✅ Successfully removed " + tasksToDelete.size() + " tasks.");
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } else {
            System.out.println("😅 Bulk deletion cancelled.");
        }
    }

    /**
     * Deletes all completed tasks from the list.
     * @throws CommandException If no completed tasks found
     */
    public void deleteCompletedTasks() throws CommandException {
        ArrayList<Task> completedTasks = new ArrayList<>();
        ArrayList<Integer> completedIndexes = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task != null && task.isDone()) {
                completedTasks.add(task);
                completedIndexes.add(i);
            }
        }

        if (completedTasks.isEmpty()) {
            throw new CommandException("No completed tasks found to delete!");
        }

        System.out.println("🔍 Found " + completedTasks.size() + " completed tasks:");
        for (int i = 0; i < completedTasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + completedTasks.get(i).toString());
        }

        System.out.print("🗑️  Delete all completed tasks? (yes/no): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            for (int i = completedIndexes.size() - 1; i >= 0; i--) {
                tasks.remove(completedIndexes.get(i).intValue());
            }
            System.out.println("✅ Removed " + completedTasks.size() + " completed tasks.");
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");

            if (tasks.isEmpty()) {
                System.out.println("🎉 All done! Your task list is completely empty! Time to celebrate! 🥳");
            }
        } else {
            System.out.println("😅 Completed tasks deletion cancelled.");
        }
    }

    /**
     * Deletes all tasks from the list after confirmation.
     * @throws CommandException If task list is already empty
     */
    public void clearAllTasks() throws CommandException {
        if (tasks.isEmpty()) {
            throw new CommandException("Your task list is already empty!");
        }

        System.out.println("🚨 You're about to delete ALL " + tasks.size() + " tasks!");
        System.out.println("🔍 This action cannot be undone!");
        System.out.print("❓ Are you absolutely sure? (type 'DELETE ALL' to confirm): ");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equals("DELETE ALL")) {
            int previousCount = tasks.size();
            tasks.clear();
            System.out.println("🗑️  Cleared all " + previousCount + " tasks. Fresh start! 🌟");
            System.out.println("💫 Your task list is now empty and ready for new adventures!");
        } else {
            System.out.println("😅 Phew! Operation cancelled. Your " + tasks.size() + " tasks are safe.");
        }
    }

    /**
     * Deletes tasks that match a specified pattern in their description.
     * @param pattern The pattern to match against task descriptions
     * @throws CommandException If pattern is invalid or no matches found
     */
    public void deleteTasksByPattern(String pattern) throws CommandException {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new CommandException("Please provide a search pattern (e.g., 'book*', '*meeting', '*urgent*')");
        }

        String searchPattern = pattern.trim().toLowerCase();
        ArrayList<Task> matchingTasks = new ArrayList<>();
        ArrayList<Integer> matchingIndexes = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task != null && task.isValid() && matchesPattern(task, searchPattern)) {
                matchingTasks.add(task);
                matchingIndexes.add(i);
            }
        }

        if (matchingTasks.isEmpty()) {
            throw new CommandException("No tasks found matching pattern: " + pattern);
        }

        System.out.println("🔍 Found " + matchingTasks.size() + " tasks matching '" + pattern + "':");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + matchingTasks.get(i).toString());
        }

        System.out.print("🗑️  Delete these " + matchingTasks.size() + " tasks? (yes/no): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            for (int i = matchingIndexes.size() - 1; i >= 0; i--) {
                tasks.remove(matchingIndexes.get(i).intValue());
            }
            System.out.println("✅ Removed " + matchingTasks.size() + " tasks matching pattern: " + pattern);
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } else {
            System.out.println("😅 Deletion cancelled.");
        }
    }

    /**
     * Deletes all tasks of a specific type.
     * @param taskType The type of tasks to delete
     * @throws CommandException If invalid task type or no tasks of that type found
     */
    public void deleteTasksByType(TaskType taskType) throws CommandException {
        if (taskType == TaskType.UNKNOWN) {
            throw new CommandException("Invalid task type specified.");
        }

        int deletedCount = 0;
        for (int i = tasks.size() - 1; i >= 0; i--) {
            Task task = tasks.get(i);
            TaskType currentType = TaskType.fromClass(task.getClass());

            if (currentType == taskType) {
                tasks.remove(i);
                deletedCount++;
            }
        }

        if (deletedCount > 0) {
            System.out.printf("✅ Removed %d %s tasks.%n", deletedCount, taskType.getDisplayName().toLowerCase());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } else {
            throw new CommandException("No " + taskType.getDisplayName().toLowerCase() + " tasks found to delete.");
        }
    }

    /**
     * Marks a task as completed.
     *
     * @param taskNumber The number of the task to mark (1-based index)
     * @throws CommandException If task number is invalid
     * @throws TaskException If task cannot be marked
     */
    public void markTask(int taskNumber) throws CommandException, TaskException {
        if (taskNumber <= 0) {
            throw new CommandException("Task numbers start from 1.");
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (task.isDone()) {
                    throw new TaskException("This task is already marked as done!");
                } else {
                    task.markAsDone();
                    System.out.println("✅ Slay, you completed the task. Let's go!");
                    System.out.println("  " + task.toString());
                }
            } catch (TaskException e) {
                throw e;
            } catch (Exception e) {
                throw new TaskException("Something went wrong marking the task: " + e.getMessage());
            }
        } else {
            throw new CommandException("That task number doesn't exist.");
        }
    }

    /**
     * Marks a task as not completed.
     *
     * @param taskNumber The number of the task to unmark (1-based index)
     * @throws CommandException If task number is invalid
     * @throws TaskException If task cannot be unmarked
     */
    public void unmarkTask(int taskNumber) throws CommandException, TaskException {
        if (taskNumber <= 0) {
            throw new CommandException("Task numbers start from 1.");
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (!task.isDone()) {
                    throw new TaskException("This task is already marked as not done!");
                } else {
                    task.markAsUndone();
                    System.out.println("🔄 No cap, this one's un-marked. Back to the grind.");
                    System.out.println("  " + task.toString());
                }
            } catch (TaskException e) {
                throw e;
            } catch (Exception e) {
                throw new TaskException("Something went wrong unmarking the task: " + e.getMessage());
            }
        } else {
            throw new CommandException("That task number doesn't exist.");
        }
    }

    /**
     * Lists all tasks in the task list with statistics and delete help.
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 Your task list is empty! Time to add some tasks?");
            System.out.println("💡 Try 'todo', 'deadline', or 'event' to get started!");
            return;
        }

        validateTaskIntegrity();

        System.out.println("📋 Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            TaskType type = TaskType.fromClass(task.getClass());
            System.out.printf("%d.%s %s%n", (i + 1), type.getPrefix(), task.toString().substring(3));
        }

        showTaskStatistics();
        showDeleteHelp();
    }

    /**
     * Displays statistics about the tasks in the list.
     */
    private void showTaskStatistics() {
        int todoCount = 0, deadlineCount = 0, eventCount = 0, completedCount = 0;

        for (Task task : tasks) {
            TaskType type = TaskType.fromClass(task.getClass());
            switch (type) {
                case TODO: todoCount++; break;
                case DEADLINE: deadlineCount++; break;
                case EVENT: eventCount++; break;
            }
            if (task.isDone()) completedCount++;
        }

        System.out.printf("📊 Statistics: %d todos, %d deadlines, %d events, %d completed%n",
                todoCount, deadlineCount, eventCount, completedCount);
    }

    /**
     * Displays help information for delete commands.
     */
    private void showDeleteHelp() {
        System.out.println("💡 Extended delete commands:");
        for (DeleteOperation op : DeleteOperation.values()) {
            if (op != DeleteOperation.UNKNOWN) {
                System.out.printf("   %s delete %-12s - %s%n",
                        op.getEmoji(), op.getOperation(), op.getDescription());
            }
        }
    }

    /**
     * Checks if a task's description matches the given pattern.
     *
     * @param task The task to check
     * @param pattern The pattern to match against
     * @return true if the task matches the pattern, false otherwise
     */
    private boolean matchesPattern(Task task, String pattern) {
        String description = task.getDescription().toLowerCase();

        if (pattern.equals(description)) {
            return true;
        }

        if (!pattern.startsWith("*") && pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return description.startsWith(prefix);
        }

        if (pattern.startsWith("*") && !pattern.endsWith("*")) {
            String suffix = pattern.substring(1);
            return description.endsWith(suffix);
        }

        if (pattern.startsWith("*") && pattern.endsWith("*")) {
            String contains = pattern.substring(1, pattern.length() - 1);
            return description.contains(contains);
        }

        return description.contains(pattern);
    }

    /**
     * Validates the integrity of all tasks in the list.
     *
     * @return true if all tasks are valid, false if any corrupted tasks were found and removed
     */
    public boolean validateTaskIntegrity() {
        boolean integrityOk = true;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task == null || !task.isValid()) {
                System.out.println("⚠️  Found corrupted task at position " + (i+1) + ", removing...");
                tasks.remove(i);
                i--;
                integrityOk = false;
            }
        }
        return integrityOk;
    }

    /**
     * Checks performance metrics and provides warnings if needed.
     */
    public void checkPerformance() {
        if (tasks.size() > WARNING_THRESHOLD) {
            System.out.println("🐢 You have " + tasks.size() + " tasks. Consider deleting or archiving old ones.");
        }

        int todoCount = (int) tasks.stream()
                .filter(task -> TaskType.fromClass(task.getClass()) == TaskType.TODO)
                .count();

        if (todoCount > tasks.size() * 0.7) {
            System.out.println("💡 Consider using deadlines or events for time-sensitive tasks.");
        }

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();

        if (usedMemory > maxMemory * 0.7) {
            System.out.println("⚠️  Memory usage is high! Consider deleting some tasks.");
        }
    }

    /**
     * Adds a task directly to the list without validation (for internal use).
     *
     * @param task The task to add
     * @throws CommandException If task limit is reached
     */
    public void addTaskDirectly(Task task) throws CommandException {
        if (task != null && task.isValid() && tasks.size() < MAX_TASKS) {
            tasks.add(task);
        } else if (tasks.size() >= MAX_TASKS) {
            throw new CommandException("Task limit reached while loading from storage");
        }
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets a task by its number.
     *
     * @param taskNumber The task number (1-based index)
     * @return The task at the specified position, or null if not found
     */
    public Task getTask(int taskNumber) {
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Gets all tasks as an array.
     *
     * @return Array of all tasks
     */
    public Task[] getAllTasks() {
        return tasks.toArray(new Task[0]);
    }

    /**
     * Finds tasks that contain the given search term in their description.
     *
     * @param searchTerm The term to search for
     * @return List of matching tasks
     */
    public ArrayList<Task> findTasks(String searchTerm) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerSearchTerm = searchTerm.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerSearchTerm)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}