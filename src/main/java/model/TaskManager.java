package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import common.exception.InvalidTaskOperationException;
import model.task.ReadOnlyTask;
import model.task.Task;

/**
 * Owns the data model and manages task collection with basic CRUD operations.
 * Tasks are stored in insertion order and accessed by 0-based index internally.
 */
public class TaskManager {
    private final UniqueTaskList tasks = new UniqueTaskList();
    private boolean isSorted = false;

    /**
     * Adds task to end of collection.
     *
     * @param task the task to add
     * @throws InvalidTaskOperationException if task already exists
     */
    public void addTask(Task task) throws InvalidTaskOperationException {
        tasks.add(task);
        isSorted = false;
    }

    /**
     * Removes a task from the list by index.
     *
     * @param userIndex 1-based index as provided by user
     * @throws InvalidTaskOperationException if the index is out of bounds
     * @see #validateIndex(int)
     */
    public void deleteTask(int userIndex) throws InvalidTaskOperationException {
        int actualIndex = toActualIndex(userIndex);
        Task task = tasks.get(actualIndex);
        tasks.remove(task);
    }

    /**
     * Removes all tasks from the list.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Marks a task as completed by index.
     * <p>
     * If the task is already marked as done, an exception is thrown to prevent redundant operations.
     *
     * @param userIndex 1-based index as provided by user
     * @throws InvalidTaskOperationException if the index is out of bounds or
     *                                       the task is already marked as completed
     * @see #validateIndex(int)
     * @see #validateTaskState(int, boolean)
     */
    public void markTaskDone(int userIndex) throws InvalidTaskOperationException {
        int actualIndex = toActualIndex(userIndex);
        validateTaskState(actualIndex, true);
        Task task = tasks.get(actualIndex);
        task.markAsDone();
        isSorted = false;
    }

    /**
     * Unmarks a completed task by index.
     * <p>If the task is already marked as pending, an exception is thrown to prevent redundant operations.
     *
     * @param userIndex 1-based index as provided by user
     * @throws InvalidTaskOperationException if the index is out of bounds or
     *                                       the task is already marked as completed
     * @see #validateIndex(int)
     * @see #validateTaskState(int, boolean)
     */
    public void unmarkTask(int userIndex) throws InvalidTaskOperationException {
        int actualIndex = toActualIndex(userIndex);
        validateTaskState(actualIndex, false);
        Task task = tasks.get(actualIndex);
        task.markAsUndone();
        isSorted = false;
    }

    /**
     * Sorts the task list in chronological order based on their first date.
     * This is a stable sort and uses natural ordering, i.e. tasks without date are placed last.
     * <p>
     * <em>Comparator implementation adapted from:
     * <a href="https://dev.java/learn/lambdas/writing-comparators/">
     * Oracle: Writing and Combining Comparators</a></em>
     */
    public void sortByDate() {
        tasks.sort(Comparator.comparing(
                task -> task.getDates().isEmpty() ? null : task.getDates().get(0),
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        isSorted = true;
    }

    /**
     * Sorts the task list by completion status. This is a stable sort.
     * <p>Tasks are sorted with incomplete tasks appearing first, followed by completed tasks.
     */
    public void sortByStatus() {
        tasks.sort(Comparator.comparing(Task::isDone));
        isSorted = true;
    }

    /**
     * Filters tasks from the task list based on the provided predicate condition.
     * <p>
     * Creates a new list containing only {@link ReadOnlyTask} instances that satisfy
     * the given condition. The original task list remains unchanged. Tasks are evaluated in
     * their current order, and matching tasks maintain their relative ordering in the result.
     *
     * @param condition a {@link Predicate} that defines the filtering criteria;
     *                  tasks for which this returns {@code true} are included
     * @return a new list containing only {@link ReadOnlyTask} that match the condition, or
     *         an empty list if no tasks match
     */
    public List<ReadOnlyTask> filter(Predicate<ReadOnlyTask> condition) {
        List<ReadOnlyTask> filteredResults = new ArrayList<>();
        for (ReadOnlyTask task : getReadOnlyList()) {
            if (condition.test(task)) {
                filteredResults.add(task);
            }
        }
        return filteredResults;
    }

    /**
     * Searches for tasks whose descriptions contain any of the specified search terms.
     * <p>
     * Performs a case-insensitive substring search across task descriptions. A task is
     * included in the results if its description contains at least one of the provided
     * search terms. This method is a convenience wrapper around {@link #filter(Predicate)}
     * that applies an OR-based matching strategy.
     *
     * @param terms an array of search terms to match against task descriptions.
     *              Must not be null, but may be empty (which returns an empty list).
     * @return a new list of {@link ReadOnlyTask} instances whose descriptions contain
     *         at least one of the search terms, or an empty list if no matches are found
     */
    public List<ReadOnlyTask> search(String... terms) {
        assert terms != null : "Search terms array should not be null";

        return filter(task -> {
            String desc = task.getDescription().toLowerCase();
            for (String term : terms) {
                if (desc.contains(term)) {
                    return true;
                }
            }
            return false;
        });
    }

    /* ==================== Getters ==================== */

    /**
     * Returns an unmodifiable view of all tasks as {@link ReadOnlyTask} instances.
     * <p>
     * Each task is exposed through the {@link ReadOnlyTask} interface, which restricts access
     * to query methods only, preventing any state modifications. List prevents external
     * modification while allowing safe iteration and access for UI components.
     * <p>
     * Any attempt to modify the returned list will result in an {@code UnsupportedOperationException}.
     *
     * @return an unmodifiable view of the current task list where each element is a
     *         {@link ReadOnlyTask} providing read-only access to task data
     */
    public List<ReadOnlyTask> getReadOnlyList() {
        return tasks.asUnmodifiableList();
    }

    /**
     * Retrieves a read-only view of a task by its position in the list. The method accepts a
     * 1-based index (as displayed to user) and internally converts to 0-based indexing for list access.
     * <p>
     * Returns the task as a {@link ReadOnlyTask}, which exposes only query methods
     * and prevents direct modification of the task's state.
     *
     * @param userIndex 1-based index of the task
     * @throws InvalidTaskOperationException if the index is out of bounds (<1 or >the list size)
     * @see #validateIndex(int)
     */
    public ReadOnlyTask getTask(int userIndex) throws InvalidTaskOperationException {
        return tasks.get(toActualIndex(userIndex));
    }

    /**
     * Checks if the task list was sorted.
     */
    public boolean isSorted() {
        return isSorted;
    }

    /**
     * Checks if the task list is empty.
     *
     * @return {@code true} if no task exist
     */
    public boolean isEmpty() {
        return getTotalTasks() < 1;
    }

    /**
     * Returns total number of tasks (complete and pending).
     */
    public int getTotalTasks() {
        return tasks.size();
    }

    /* ==================== Validators ==================== */

    private int toActualIndex(int userIndex) throws InvalidTaskOperationException {
        int actualIndex = userIndex - 1;    // convert to 0-based index
        validateIndex(actualIndex);
        return actualIndex;
    }

    /**
     * Validates that the given index is within the valid range of task list.
     *
     * @param index 0-based index of the task to validate
     * @throws InvalidTaskOperationException if index is negative or
     *                                       greater than or equal to the list size
     */
    private void validateIndex(int index) throws InvalidTaskOperationException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskOperationException(
                    InvalidTaskOperationException.ErrorType.TASK_NOT_FOUND,
                    index + 1
            );
        }
    }

    /**
     * Validates that a task is not already in the desired completion state to prevent
     * redundant mark/unmark operations.
     *
     * @param index    0-based index of the task to validate
     * @param markDone the desired completion state ({@code true} for done,
     *                 {@code false} for pending)
     * @throws InvalidTaskOperationException if task already in desired state
     */
    private void validateTaskState(int index, boolean markDone) throws InvalidTaskOperationException {
        Task task = tasks.get(index);
        if (markDone == task.isDone()) {
            String state = markDone ? "done" : "unmarked";
            String undo = markDone ? "unmark" : "mark";
            throw new InvalidTaskOperationException(
                    InvalidTaskOperationException.ErrorType.TASK_STATE,
                    state, undo, index + 1
            );
        }
    }
}
