package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import exception.InvalidTaskOperationException;
import exception.InvalidTaskOperationException.ErrorType;
import task.ReadOnlyTask;
import task.Task;

/**
 * Represents a list of tasks that enforces uniqueness among its elements.
 * The uniqueness constraint is determined by the {@link Task#equals(Object)} method.
 * <p>
 * <em>Solution adapted from:
 * <a href="https://github.com/se-edu/addressbook-level2/tree/master">
 * se-edu addressbook-level2</a></em></p>
 */
public class UniqueTaskList {
    private final List<Task> taskList = new ArrayList<>();

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     * @throws InvalidTaskOperationException if task already exists
     */
    public void add(Task task) throws InvalidTaskOperationException {
        Objects.requireNonNull(task);
        if (taskList.contains(task)) {
            throw new InvalidTaskOperationException(ErrorType.DUPLICATE_TASK, task);
        }
        taskList.add(task);
    }

    /**
     * Removes the specified task from the list.
     *
     * @param task the task to remove
     * @throws InvalidTaskOperationException if the task is not found in the list
     */
    public void remove(Task task) throws InvalidTaskOperationException {
        Objects.requireNonNull(task);
        if (!taskList.remove(task)) {
            throw new InvalidTaskOperationException(ErrorType.TASK_NOT_FOUND);
        }
    }

    /**
     * Returns the task at the specified position in this list.
     */
    public Task get(int index) {
        return taskList.get(index);
    }

    /**
     * Returns the total number of tasks in this list.
     *
     * @return the number of tasks in this list
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Sorts the internal list in place using the given comparator.
     * The sort is stable: it does not reorder equal elements.
     */
    public void sort(Comparator<Task> comparator) {
        taskList.sort(comparator);
    }

    /**
     * Returns an unmodifiable view of the task list.
     * Attempts to modify the returned list will result in an
     * {@link UnsupportedOperationException}.
     * <p>
     * This method provides a read-only interface to the internal task list,
     * preventing external modification while allowing observation of the list contents.
     */
    public List<ReadOnlyTask> asUnmodifiableList() {
        return Collections.unmodifiableList(taskList);
    }
}
