package tasklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import task.Task;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Adds the given task to the task list.
     * 
     * @param t the task to add
     */
    public void add(Task t) {
        assert t != null : "Cannot add a null task";
        tasks.add(t);
    }

    /**
     * Removes the task at the given index (0-based) in the task list and
     * returns the removed task.
     * 
     * @param indexZeroBased the index of the task to remove (0-based)
     * @return the task that was removed from the task list
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Task remove(int indexZeroBased) {
        return tasks.remove(indexZeroBased);
    }

    /**
     * Returns the task at the given index (0-based) in the task list.
     * 
     * @param indexZeroBased the index of the task to return (0-based)
     * @return the task at the given index in the task list
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Task get(int indexZeroBased) {
        return tasks.get(indexZeroBased);
    }

    /**
     * Returns the number of tasks in the task list.
     * 
     * @return the number of tasks in the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable list view of the tasks in the task list.
     * 
     * @return an unmodifiable list view of the tasks in the task list
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
