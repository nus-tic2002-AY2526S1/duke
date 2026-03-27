package kev.task;

/**
 * an abstract task in the task list.
 * each task contains a description and a completion status.
 */
public abstract class Task {
    /** description of the task. */
    protected String description;
    /** completion status of the task. */
    protected boolean isDone;

    /**
     * creates a new Task.
     *
     * @param description Description of the task.
     */
    public Task(String description) { this.description = description; this.isDone = false; }

    /** marks the task as completed. */
    public void markAsDone() { isDone = true; }

    /** marks the task as not completed. */
    public void markAsNotDone() { isDone = false; }

    /**
     * returns the task's completion status icon (X for done, space for not done).
     *
     * @return The status icon.
     */
    public String getStatusIcon() { return (isDone ? "X" : " "); }

    /**
     * converts the task into a format suitable for file storage.
     *
     * @return A formatted storage string.
     */
    public abstract String toFileString();

    /**
     * returns a more easily referenced/readable string representation of the task.
     *
     * @return Task display string.
     */
    public abstract String toString();
}