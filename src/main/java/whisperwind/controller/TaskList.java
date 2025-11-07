package whisperwind.controller;

import whisperwind.model.*;
import whisperwind.util.*;
import whisperwind.exceptions.*;

/**
 * Represents a list of tasks and provides operations for adding, deleting,
 * marking, unmarking, searching, and listing tasks.
 */
public class TaskList {
    private final TaskStorageHandler storageHandler;
    private final TaskOperationHandler operationHandler;
    private final TaskSearchHandler searchHandler;

    /**
     * Constructs an empty TaskList with underlying handlers for storage,
     * operations, and search.
     */
    public TaskList() {
        this.storageHandler = new TaskStorageHandler();
        this.operationHandler = new TaskOperationHandler(storageHandler);
        this.searchHandler = new TaskSearchHandler(storageHandler);
    }

    // === Add operations ===

    /**
     * Adds a new Todo task with the given description.
     *
     * @param desc Description of the todo task.
     * @throws TaskException    If the task is invalid.
     * @throws CommandException If the command input is invalid.
     */
    public void addTodo(String desc) throws TaskException, CommandException {
        operationHandler.addTodo(desc);
    }

    /**
     * Adds a new Deadline task from input string.
     *
     * @param input Input string specifying description and deadline.
     * @throws TaskException    If the task is invalid.
     * @throws CommandException If the command input is invalid.
     */
    public void addDeadline(String input) throws TaskException, CommandException {
        operationHandler.addDeadline(input);
    }

    /**
     * Adds a new Event task from input string.
     *
     * @param input Input string specifying description, start, and end times.
     * @throws TaskException    If the task is invalid.
     * @throws CommandException If the command input is invalid.
     */
    public void addEvent(String input) throws TaskException, CommandException {
        operationHandler.addEvent(input);
    }

    /**
     * Adds a task directly to the list without parsing.
     *
     * @param task Task object to add.
     * @throws CommandException If the task cannot be added.
     */
    public void addTaskDirectly(Task task) throws CommandException {
        if (task != null) {
            storageHandler.addTask(task);
        } else {
            System.out.println("⚠️ Cannot add a null task.");
        }
    }

    // === Delete operations ===

    /**
     * Deletes the task at the given position.
     *
     * @param taskNumber Task number to delete.
     * @throws CommandException If deletion fails.
     */
    public void deleteTask(int taskNumber) throws CommandException {
        operationHandler.deleteTask(taskNumber);
    }

    /**
     * Deletes tasks that match a given pattern.
     *
     * @param pattern Pattern to match task descriptions.
     * @throws CommandException If deletion fails.
     */
    public void deleteTasksByPattern(String pattern) throws CommandException {
        operationHandler.deleteTasksByPattern(pattern);
    }

    /**
     * Deletes multiple tasks by their numbers.
     *
     * @param taskNumbers Array of task numbers to delete.
     * @throws CommandException If deletion fails.
     */
    public void deleteMultipleTasks(int[] taskNumbers) throws CommandException {
        operationHandler.deleteMultipleTasks(taskNumbers);
    }

    /**
     * Deletes tasks containing a keyword.
     *
     * @param keyword Keyword to search in task descriptions.
     * @throws CommandException If deletion fails.
     */
    public void deleteTasksContaining(String keyword) throws CommandException {
        searchHandler.deleteTasksContaining(keyword);
    }

    // === Mark/unmark ===

    /**
     * Marks the task as completed.
     *
     * @param taskNumber Task number to mark.
     * @throws TaskException    If task cannot be marked.
     * @throws CommandException If marking fails.
     */
    public void markTask(int taskNumber) throws TaskException, CommandException {
        operationHandler.markTask(taskNumber);
    }

    /**
     * Unmarks the task as not completed.
     *
     * @param taskNumber Task number to unmark.
     * @throws TaskException    If task cannot be unmarked.
     * @throws CommandException If unmarking fails.
     */
    public void unmarkTask(int taskNumber) throws TaskException, CommandException {
        operationHandler.unmarkTask(taskNumber);
    }

    /**
     * Deletes all completed tasks from the list.
     */
    public void deleteCompletedTasks() {
        operationHandler.deleteCompletedTasks();
    }

    /**
     * Deletes all tasks of a specific type.
     *
     * @param taskType Type of tasks to delete.
     */
    public void deleteTasksByType(TaskType taskType) {
        operationHandler.deleteTasksByType(taskType);
    }

    // === Listing and searching ===

    /**
     * Lists all tasks in the task list.
     */
    public void listTasks() {
        storageHandler.listTasks();
    }

    /**
     * Displays tasks that match a search term.
     *
     * @param searchTerm Term to search for in tasks.
     */
    public void displayMatchingTasks(String searchTerm) {
        searchHandler.displayMatchingTasks(searchTerm);
    }

    // === Getters ===

    /**
     * Returns the number of tasks in the list.
     *
     * @return Task count.
     */
    public int getTaskCount() {
        return storageHandler.getTaskCount();
    }

    /**
     * Checks whether the task list is empty.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return storageHandler.isEmpty();
    }

    /**
     * Gets a task by its number.
     *
     * @param num Task number to retrieve.
     * @return Task object.
     */
    public Task getTask(int num) {
        return storageHandler.getTask(num);
    }

    /**
     * Returns all tasks in the list.
     *
     * @return Array of all tasks.
     */
    public Task[] getAllTasks() {
        return storageHandler.getAllTasks();
    }

    // === Utility ===

    /**
     * Clears all tasks from the list.
     */
    public void clearAllTasks() {
        storageHandler.clearAllTasks();
    }

    /**
     * Checks performance of task operations (internal diagnostics).
     */
    public void checkPerformance() {
        operationHandler.checkPerformance();
    }

    /**
     * Validates integrity of tasks in storage.
     *
     * @return True if all tasks are valid, false otherwise.
     */
    public boolean validateTaskIntegrity() {
        return storageHandler.validateIntegrity();
    }
}
