package task;

/**
 * Represents a list of Tasks.
 */
public class TaskList extends java.util.ArrayList<Task> {
    /**
     * Creates an empty list.
     */
    public TaskList() {
        super();
    }

    public void addTask(Task t) {
        this.add(t);
    }

    /**
     * Removes a task from the list and updates the index of the subsequent tasks.
     * @param index of target of deletion.
     */
    public void deleteTask(int index) {
        this.remove(index);

        // Updates index of subsequent tasks
        for (int i = index; i < this.size(); i++) {
            this.get(i).setIndex(index + 1);
        }
    }
}
