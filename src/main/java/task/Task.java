package task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Base class for all task types.
 * Tasks are created incomplete and can be toggled between done/undone states.
 * Provides standardized string representation for display purpose.
 */
public abstract class Task {
    private final String description;
    private final Recurrence recurrence;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * Tasks are created in an incomplete state by default.
     *
     * @param description The task description (should not be null or empty)
     */
    public Task(String description, Recurrence recurrence) {
        this.description = description;
        this.recurrence = recurrence != null ? recurrence : Recurrence.none();  // defaults to not recurring
        this.isDone = false;    // tasks are incomplete by default
    }

    /**
     * Gets the type of task - must be implemented by subclasses
     */
    public abstract TaskType getTaskType();

    /**
     * Gets the dates for the task - must be implemented by subclasses
     */
    public abstract List<LocalDateTime> getDates();

    /**
     * Returns a string representation of date - must be implemented by subclasses
     */
    public abstract String toJsonFields();

    /**
     * Create a new Task with the same fields - must be implemented by subclasses
     */
    public abstract Task copy();

    public String getDescription() {
        return description;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return true if the task is completed, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as completed.
     * Once marked as done, the task can still be unmarked if needed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Reverts a completed task back to incomplete state.
     */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Formats the recurrence information for display in string representations.
     * If the task has no recurrence, returns an empty string.
     * Otherwise, returns the recurrence pattern in a human-readable format.
     *
     * @return empty string if no recurrence, otherwise a formatted string
     *         in the format: " (recurs {type} × {frequency})"
     *         where type is repeat frequency (e.g. "daily", "weekly").
     */
    protected String formatRecurrence() {
        return recurrence.isNone()
                ? ""
                : String.format(" (recurs %s × %d)",
                recurrence.type().name().toLowerCase(),
                recurrence.frequency());
    }

    /**
     * Returns a common string representation showing task prefix, status and description for all tasks.
     * Format: "[TaskPrefix][X] Task description" for completed, "[TaskPrefix][ ] Task description" for pending.
     * Subclasses may extend this format with additional information.
     */
    @Override
    public String toString() {
        return String.format("[%s][%s] %s",
                getTaskType().getTaskPrefix(),
                isDone ? "X" : " ",
                description);
    }
}
