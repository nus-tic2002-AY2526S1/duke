package nono.task;

/**
 * Represents an abstract task with a description and completion status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     *
     * @param description The task description.
     */
    public Task(String description) {
        assert description != null : "Task description should not be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if the task is done, " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as done or not done.
     *
     * @param status True if done, false otherwise.
     */
    public void markAsDone(boolean status) {
        isDone = status;
    }

    /**
     * @return The string representation of the task.
     */
    public String toString() {
        return this.description;
    }

    /**
     * Converts the task to a formatted string for file saving.
     *
     * @return The formatted data string.
     */
    public abstract String toDataString();
}