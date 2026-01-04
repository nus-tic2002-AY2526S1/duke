package bongocat.tasks;

import java.util.ArrayList;

/**
 * Represents the list of tasks.
 */
public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> loaded) {
        this.tasks = (loaded == null) ? new ArrayList<>() : loaded;
    }

    /** Returns all tasks */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /** Returns all tasks */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /** Adds a task */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /** Returns number of tasks */
    public int size() {
        return tasks.size();
    }

    /** Checks if empty */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task deleteTask(int index) {
        assert index > 0 && index <= tasks.size() : "Index out of bounds for deleteTask";
        return tasks.remove(index - 1);
    }

    public Task markTask(int index) {
        assert index > 0 && index <= tasks.size() : "Index out of bounds for markTask";
        Task t = tasks.get(index - 1);
        assert t != null : "Task should not be null after valid index";
        t.markDone();
        return t;
    }

    public Task unmarkTask(int index) {
        assert index > 0 && index <= tasks.size() : "Index out of bounds for unmarkTask";
        Task t = tasks.get(index - 1);
        assert t != null : "Task should not be null after valid index";
        t.markNotDone();
        return t;
    }

    /**
     * Duplicate check by TASK NAME (case-insensitive).
     * This is the correct way for Duke-level projects.
     */
    public boolean isDuplicate(Task newTask) {
        for (Task t : tasks) {
            if (t.getName().equalsIgnoreCase(newTask.getName())) {
                return true;
            }
        }
        return false;
    }

    /** If using max 100 tasks */
    public int spaceLeft() {
        return 100 - tasks.size();
    }

    /**
     * Returns tasks whose name contains the keyword (case-insensitive).
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();

        for (Task t : tasks) {
            if (t.getName().toLowerCase().contains(keyword)) {
                matches.add(t);
            }
        }

        return matches;
    }

}
