package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import java.util.ArrayList;
import java.util.Scanner;


public class TaskList {
    private ArrayList<Task> tasks;
    private static final int MAX_TASKS = 1000;
    private static final int WARNING_THRESHOLD = 500;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTodo(String description) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ model.Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
            return;
        }

        String sanitizedDesc = InputSanitizer.sanitizeDescription(description);
        if (sanitizedDesc.isEmpty()) {
            System.out.println("Wait, what's the todo? Give me the details!");
            return;
        }

        try {
            Todo newTask = new Todo(sanitizedDesc);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the todo: " + e.getMessage());
        }
    }

    public void addDeadline(String input) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ model.Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
            return;
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            System.out.println("Wait, what's the deadline? Give me the details!");
            return;
        }

        String[] deadlineParts = sanitizedInput.split(" /by ", 2);
        if (deadlineParts.length < 2) {
            System.out.println("⏰ model.Deadline format should be: deadline DESCRIPTION /by TIME");
            return;
        }

        String description = InputSanitizer.sanitizeDescription(deadlineParts[0]);
        String by = InputSanitizer.sanitizeTime(deadlineParts[1]);

        if (description.isEmpty()) {
            System.out.println("Wait, what's the deadline description? Give me the details!");
            return;
        }
        if (by.isEmpty()) {
            System.out.println("When is this deadline due? Add the time after /by!");
            return;
        }

        try {
            Deadline newTask = new Deadline(description, by);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the deadline: " + e.getMessage());
        }
    }

    public void addEvent(String input) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ model.Task limit reached (" + MAX_TASKS + ")! Complete or delete some tasks first.");
            return;
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            System.out.println("Wait, what's the event? Give me the details!");
            return;
        }

        String[] eventParts = sanitizedInput.split(" /from ", 2);
        if (eventParts.length < 2) {
            System.out.println("🎉 model.Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            System.out.println("🎉 model.Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String description = InputSanitizer.sanitizeDescription(eventParts[0]);
        String from = InputSanitizer.sanitizeTime(timeParts[0]);
        String to = InputSanitizer.sanitizeTime(timeParts[1]);

        if (description.isEmpty()) {
            System.out.println("Wait, what's the event description? Give me the details!");
            return;
        }
        if (from.isEmpty()) {
            System.out.println("When does this event start? Add the start time after /from!");
            return;
        }
        if (to.isEmpty()) {
            System.out.println("When does this event end? Add the end time after /to!");
            return;
        }

        try {
            Event newTask = new Event(description, from, to);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the event: " + e.getMessage());
        }
    }

    public void deleteTask(int taskNumber) {
        if (taskNumber <= 0) {
            System.out.println("❌ It's giving 'error'. model.Task numbers start from 1.");
            return;
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
                System.out.println("❌ Oops! Something went wrong deleting the task: " + e.getMessage());
            }
        } else {
            System.out.println("❌ It's giving 'error'. That task number doesn't exist.");
        }
    }

    public void deleteMultipleTasks(int[] taskNumbers) {
        if (taskNumbers == null || taskNumbers.length == 0) {
            System.out.println("❌ No task numbers provided for deletion.");
            return;
        }

        if (tasks.isEmpty()) {
            System.out.println("💫 Your task list is already empty!");
            return;
        }

        java.util.Arrays.sort(taskNumbers);
        int[] uniqueNumbers = java.util.Arrays.stream(taskNumbers).distinct().toArray();

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
            System.out.println("❌ No valid task numbers to delete.");
            return;
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

    public void deleteCompletedTasks() {
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
            System.out.println("💫 No completed tasks found to delete!");
            return;
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

    public void clearAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("💫 Your task list is already empty!");
            return;
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

    public void deleteTasksByPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            System.out.println("❌ Please provide a search pattern (e.g., 'book*', '*meeting', '*urgent*')");
            return;
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
            System.out.println("🔍 No tasks found matching pattern: " + pattern);
            return;
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

    public void deleteTasksByType(TaskType taskType) {
        if (taskType == TaskType.UNKNOWN) {
            System.out.println("❌ Invalid task type specified.");
            return;
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
            System.out.println("💫 No " + taskType.getDisplayName().toLowerCase() + " tasks found to delete.");
        }
    }

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

    private void showDeleteHelp() {
        System.out.println("💡 Extended delete commands:");
        for (DeleteOperation op : DeleteOperation.values()) {
            if (op != DeleteOperation.UNKNOWN) {
                System.out.printf("   %s delete %-12s - %s%n",
                        op.getEmoji(), op.getOperation(), op.getDescription());
            }
        }
    }

    public void markTask(int taskNumber) {
        if (taskNumber <= 0) {
            System.out.println("❌ It's giving 'error'. model.Task numbers start from 1.");
            return;
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (task.isDone()) {
                    System.out.println("ℹ️  This task is already marked as done!");
                } else {
                    task.markAsDone();
                    System.out.println("✅ Slay, you completed the task. Let's go!");
                    System.out.println("  " + task.toString());
                }
            } catch (Exception e) {
                System.out.println("❌ Oops! Something went wrong marking the task: " + e.getMessage());
            }
        } else {
            System.out.println("❌ It's giving 'error'. That task number is sus.");
        }
    }

    public void unmarkTask(int taskNumber) {
        if (taskNumber <= 0) {
            System.out.println("❌ It's giving 'error'. model.Task numbers start from 1.");
            return;
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (!task.isDone()) {
                    System.out.println("ℹ️  This task is already marked as not done!");
                } else {
                    task.markAsUndone();
                    System.out.println("🔄 No cap, this one's un-marked. Back to the grind.");
                    System.out.println("  " + task.toString());
                }
            } catch (Exception e) {
                System.out.println("❌ Oops! Something went wrong unmarking the task: " + e.getMessage());
            }
        } else {
            System.out.println("❌ It's giving 'error'. That task number is sus.");
        }
    }

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

    public void addTaskDirectly(Task task) {
        if (task != null && task.isValid() && tasks.size() < MAX_TASKS) {
            tasks.add(task);
        }
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task getTask(int taskNumber) {
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }
}