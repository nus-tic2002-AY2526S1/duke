package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.exceptions.*;
import java.util.*;

/**
 * Handles searching and deleting tasks based on keywords.
 * Uses TaskStorageHandler to access and modify stored tasks.
 */
public class TaskSearchHandler {

    /** Storage handler to access and modify tasks. */
    private final TaskStorageHandler storage;

    /**
     * Constructs a TaskSearchHandler with the given storage handler.
     *
     * @param storage the task storage handler
     */
    public TaskSearchHandler(TaskStorageHandler storage) {
        this.storage = storage;
    }

    /**
     * Displays tasks whose descriptions contain the given keyword.
     *
     * @param keyword the search keyword
     */
    public void displayMatchingTasks(String keyword) {
        if (storage.isEmpty()) {
            System.out.println("📭 No tasks available to search.");
            return;
        }

        System.out.println("🔍 Searching for: " + keyword);
        boolean found = false;
        Task[] allTasks = storage.getAllTasks();

        for (int i = 0; i < allTasks.length; i++) {
            Task t = allTasks[i];
            if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("😕 No matching tasks found for: " + keyword);
        }
    }

    /**
     * Deletes all tasks whose descriptions contain the given keyword.
     *
     * @param keyword the keyword to search for deletion
     * @throws CommandException if there are no tasks to delete
     */
    public void deleteTasksContaining(String keyword) throws CommandException {
        if (storage.isEmpty()) {
            throw new CommandException("❌ No tasks to delete.");
        }

        boolean anyDeleted = false;
        Task[] allTasks = storage.getAllTasks();

        // Iterate backwards to safely remove tasks by index
        for (int i = allTasks.length - 1; i >= 0; i--) {
            Task t = allTasks[i];
            if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                storage.removeTask(i);
                anyDeleted = true;
            }
        }

        if (anyDeleted) {
            System.out.println("🧹 Tasks containing \"" + keyword + "\" have been deleted.");
        } else {
            System.out.println("😕 No tasks matched the keyword: " + keyword);
        }
    }
}
