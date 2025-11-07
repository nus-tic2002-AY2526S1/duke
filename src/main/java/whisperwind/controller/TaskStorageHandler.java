package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.exceptions.*;
import java.util.*;

/**
 * Handles storage and management of tasks in memory.
 * Provides methods for adding, removing, listing, and validating tasks.
 */
public class TaskStorageHandler {

    /** List of tasks stored in memory. */
    protected ArrayList<Task> tasks = new ArrayList<>();

    /** Maximum number of tasks allowed in storage. */
    protected static final int MAX_TASKS = 110;

    /** Warning threshold for large number of tasks. */
    protected static final int WARNING_THRESHOLD = 100;

    /**
     * Constructs an empty TaskStorageHandler.
     */
    public TaskStorageHandler() {}

    /**
     * Adds a new task to the storage.
     *
     * @param task the task to add
     * @throws CommandException if the task limit has been reached
     */
    public void addTask(Task task) throws CommandException {
        if (tasks.size() >= MAX_TASKS) {
            throw new CommandException("⚠️ Task limit reached (" + MAX_TASKS + ")");
        }
        tasks.add(task);
    }

    /**
     * Removes a task at the specified index.
     *
     * @param index the 0-based index of the task to remove
     */
    public void removeTask(int index) {
        tasks.remove(index);
    }

    /**
     * Checks if there are no tasks in storage.
     *
     * @return true if storage is empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return task count
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Retrieves a task by its 1-based number.
     *
     * @param num the 1-based task number
     * @return the task at that position
     * @throws IndexOutOfBoundsException if the number is invalid
     */
    public Task getTask(int num) {
        return tasks.get(num - 1);
    }

    /**
     * Returns all tasks as an array.
     *
     * @return array of all tasks
     */
    public Task[] getAllTasks() {
        return tasks.toArray(new Task[0]);
    }

    /**
     * Prints all tasks to the console.
     * Displays a warning if the number of tasks exceeds WARNING_THRESHOLD.
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 Your task list is empty!");
            return;
        }

        System.out.println("📝 Here are your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }

        if (tasks.size() > WARNING_THRESHOLD) {
            System.out.println("⚠️ Warning: You have many tasks! Consider cleaning up.");
        }
    }

    /**
     * Validates the integrity of stored tasks.
     * Removes null or invalid tasks from the list.
     *
     * @return true if all tasks were valid, false if any invalid tasks were removed
     */
    public boolean validateIntegrity() {
        boolean ok = true;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task == null || !task.isValid()) {
                System.out.println("⚠️ Invalid task removed: " + (i + 1));
                tasks.remove(i--);
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Clears all tasks from storage.
     */
    public void clearAllTasks() {
        tasks.clear();
        System.out.println("🧾 All tasks have been cleared.");
    }
}
