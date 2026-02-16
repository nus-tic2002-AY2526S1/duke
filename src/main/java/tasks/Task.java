package tasks;

/**
 * Parent class for all task type
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initialise task with default information
     * No task name and marked as not done
     */
    public Task() {
        description = "";
        isDone = false;
    }

    /**
     * Initialise task with task name
     *
     * @param description task name
     */
    public Task(String description) {
        assert description != null;

        description = description.trim();
        this.description = description;
        isDone = false;
    }

    /**
     * Status icon for task marked as done or not done
     *
     * @return "X" if task is marked as done, else " "
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Getter function for task name
     *
     * @return task name
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter function for isDone
     *
     * @return isDone
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Setter function for description
     *
     * @param description to set description as
     */
    public void setDescription(String description) {
        assert description != null;

        this.description = description;
    }

    /**
     * Setter function for isDone
     *
     * @param isDone to set isDone as
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Default print format for tasks
     *
     * @return tasks in string
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }


}
