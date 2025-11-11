package nono.util;

import java.util.ArrayList;

import nono.exception.UserInputException;
import nono.task.Task;

/**
 * Manages the list of tasks in memory.
 * Supports adding, deleting, marking, and retrieving tasks.
 * Now checks for duplicate tasks before adding a new one.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList initialized with existing tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Task list should not be null";
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     * Checks for duplicates before adding.
     *
     * @param task The task to add.
     * @throws UserInputException If a duplicate task already exists.
     */
    public void addTask(Task task) throws UserInputException {
        assert task != null : "Task to add should not be null";
        for (Task existing : tasks) {
            if (existing.toString().equalsIgnoreCase(task.toString())) {
                throw new UserInputException("This task already exists: " + existing + "\n");
            }
        }
        tasks.add(task);
    }

    /**
     * Deletes a task from the list.
     *
     * @param index The index of the task (0-based).
     * @return The deleted task.
     * @throws UserInputException If the index is invalid.
     */
    public Task deleteTask(int index) throws UserInputException {
        assert index >= 0 : "Index should not be negative";
        if (index >= tasks.size()) {
            throw new UserInputException("Sorry, invalid task number.\n");
        }
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list.
     *
     * @param index The index of the task (0-based).
     * @return The requested task.
     * @throws UserInputException If the index is invalid.
     */
    public Task getTask(int index) throws UserInputException {
        assert index >= 0 : "Index should not be negative";
        if (index >= tasks.size()) {
            throw new UserInputException("Sorry, invalid task number.\n");
        }
        return tasks.get(index);
    }

    /**
     * Marks or unmarks a task as done.
     *
     * @param index  The task index (0-based).
     * @param status True to mark as done, false otherwise.
     * @throws UserInputException If the index is invalid.
     */
    public void markTask(int index, boolean status) throws UserInputException {
        assert index >= 0 : "Index should not be negative";
        if (index >= tasks.size()) {
            throw new UserInputException("Sorry, invalid task number.\n");
        }
        tasks.get(index).markAsDone(status);
    }

    /**
     * Returns the total number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return True if there are no tasks, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns all tasks in the list.
     *
     * @return An ArrayList of all tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
}