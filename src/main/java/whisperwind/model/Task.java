package whisperwind.model;

import whisperwind.exceptions.TaskException;

/**
 * Represents an abstract task with a description and completion status.
 * This is the base class for all specific task types in the application.
 * Tasks can be marked as done or undone, and have validation for their descriptions.
 *
 * @author Your Name
 * @version 1.0
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task; cannot be null or empty
     * @throws TaskException if the description is null, empty, or contains only whitespace
     */
    public Task(String description) throws TaskException {
        if (description == null || description.trim().isEmpty()) {
            throw new TaskException("Task description cannot be empty!");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    /**
     * Returns the type of this task.
     * Concrete subclasses must implement this method to specify their task type.
     *
     * @return the TaskType representing the type of this task
     */
    public abstract TaskType getTaskType();

    /**
     * Returns a string representation of the task's completion status.
     *
     * @return "[X]" if the task is done, "[ ]" if the task is not done
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Marks this task as completed.
     *
     * @throws TaskException if the task is already marked as done
     */
    public void markAsDone() throws TaskException {
        if (this.isDone) {
            throw new TaskException("This task is already marked as done!");
        } else {
            this.isDone = true;
        }
    }

    /**
     * Marks this task as not completed.
     *
     * @throws TaskException if the task is already marked as not done
     */
    public void markAsUndone() throws TaskException {
        if (!this.isDone) {
            throw new TaskException("This task is already marked as not done!");
        } else {
            this.isDone = false;
        }
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this task.
     *
     * @param description the new description for the task; cannot be null or empty
     * @throws TaskException if the description is null, empty, or contains only whitespace
     */
    public void setDescription(String description) throws TaskException {
        if (description == null || description.trim().isEmpty()) {
            throw new TaskException("Task description cannot be empty!");
        }
        this.description = description.trim();
    }

    /**
     * Checks if this task is completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a string representation of this task.
     * The format includes the status icon and description.
     *
     * @return a string in the format "[Status] Description"
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }

    /**
     * Compares this task to another object for equality.
     * Two tasks are considered equal if they have the same description.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return description.equals(task.description);
    }

    /**
     * Returns a hash code value for this task.
     * The hash code is based on the task description.
     *
     * @return a hash code value for this task
     */
    @Override
    public int hashCode() {
        return description.hashCode();
    }

    /**
     * Checks if this task is valid.
     * A task is considered valid if it has a non-null, non-empty description.
     *
     * @return true if the task is valid, false otherwise
     */
    public boolean isValid() {
        return description != null && !description.trim().isEmpty();
    }
}