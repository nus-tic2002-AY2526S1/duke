package nerunerune.task;

import java.time.LocalDate;

/**
 * Abstract base class representing a task.
 * Contains common properties and methods for all task types,
 * such as description, completion status, and abstract methods for specific behavior.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
    }

    /**
     * Constructs a Task with the given description and completion status.
     *
     * @param description the description of the task
     * @param isDone      true if the task is completed, false otherwise
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the status icon representing completion state.
     *
     * @return "X" if done, otherwise a space character
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the description of the task.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is marked as done.
     *
     * @return true if done, false otherwise
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Abstract method to identify the type of schedule item.
     *
     * @return the type of schedule item as a string
     */
    public abstract String getScheduleItem();

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of the task for display.
     *
     * @return string representation of the task
     */
    @Override
    public abstract String toString();

    /**
     * Returns a string representation of the task for storage.
     *
     * @return string for storage
     */
    public abstract String toStorageString();

    /**
     * Checks if this task is backdated (date/time has passed).
     * Default implementation returns false for tasks without dates.
     *
     * @return true if the task is backdated, false otherwise
     */
    public boolean isBackdated() {
        return false;
    }

    /**
     * Checks if this task occurs on the specified date.
     *
     * @param date The date to check
     * @return true if the task occurs on this date, false otherwise
     */
    public abstract boolean occursOn(LocalDate date);
}
