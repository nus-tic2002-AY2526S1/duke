package task;

import exception.InvalidDateTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base class representing a mutable task entity.
 * <p>
 * Each task has a description, completion state and recurrence rule.
 * This abstract class provides common functionality for all task types while
 * requiring subclasses {@link TodoTask} {@link DeadlineTask} {@link EventTask}
 * to implement type-specific behaviour.
 */
public abstract class Task implements ReadOnlyTask{
    private final String description;
    private final Recurrence recurrence;
    private boolean isDone;

    /**
     * Creates a new task with the given description and recurrence pattern.
     * Tasks are created in an incomplete state and as a non-repeating task by default.
     *
     * @param description The task description (should not be null or empty)
     * @param recurrence  The recurrence pattern for this task
     * @see Recurrence
     */
    public Task(String description, Recurrence recurrence) {
        this.description = description;
        this.recurrence = recurrence != null
                ? recurrence
                : Recurrence.none(LocalDate.now());
        this.isDone = false;    // tasks are incomplete by default
    }

    /* =============== Abstract Methods =============== */

    /**
     * Gets the type of task.
     */
    @Override
    public abstract TaskType getTaskType();

    /**
     * Gets the date/time associated with the task.
     * The returned list structure depends on the task type:
     * <ul>
     *     <li>Single date task: {@link DeadlineTask} with one due date/time</li>
     *     <li>Date range task: {@link EventTask} with start and end date/time</li>
     *
     * @return a list of {@link LocalDateTime} objects representing task dates;
     *         may be empty for tasks without date/time information
     */
    @Override
    public abstract List<LocalDateTime> getDates();

    /**
     * Returns the JSON field representation of task's date/time.
     */
    @Override
    public abstract String toJsonFields();

    /**
     * Create a copy of this Task with modified date/time.
     * <p>
     * This method is used internally for creating task instances based on
     * recurrence patterns. Subclasses should preserve all task properties
     * except for the date/time information.
     *
     * @param start the new start date/time for the copied task
     * @param end   the new end date/time for the copied task
     * @return a new Task instance with the specified dates
     */
    protected abstract Task copy(LocalDateTime start, LocalDateTime end) throws InvalidDateTimeException;

    /* =============== Public Methods =============== */

    /**
     * Attempts to materialize this task as it would occur on a specific date,
     * taking into account its recurrence rules.
     * <p>
     * This method first checks whether the task is scheduled to occur on the given
     * {@code filterDate} using {@link #occursOn(LocalDate)}. If the task does not
     * occur on that date, an {@link Optional#empty()} is returned.
     * <p>
     * If the task does occur, a new instance is created by shifting the task's
     * original start/end dates forward according to the recurrence interval.
     * For non-recurring tasks, the returned instance is simply a copy of the original task.
     *
     * @param filterDate the date to check for a possible occurrence (must not be null)
     * @return an {@code Optional<ReadOnlyTask>} containing a new task instance that falls on
     *         {@code filterDate}, or {@code Optional.empty()} if the task does not
     *         occur on that date
     */
    @Override
    public Optional<ReadOnlyTask> createInstance(LocalDate filterDate)
            throws InvalidDateTimeException {
        if (!occursOn(filterDate)) return Optional.empty();

        List<LocalDateTime> dates = getDates();
        LocalDateTime originalStart = dates.get(0);
        LocalDateTime originalEnd = (dates.size() > 1) ? dates.get(1) : originalStart;
        if (recurrence.isNone()) return Optional.of(copy(originalStart, originalEnd));

        ChronoUnit unit = recurrence.type().getChronoUnit();
        long timeUnitDiff = unit.between(dates.get(0).toLocalDate(), filterDate);
        LocalDateTime instanceStart = dates.get(0).plus(timeUnitDiff, unit);
        LocalDateTime instanceEnd = dates.size() > 1
                ? dates.get(1).plus(timeUnitDiff, unit)
                : instanceStart;

        return Optional.of(copy(instanceStart, instanceEnd));
    }

    /**
     * Determines whether this task occurs on the given date, taking into account
     * both its intrinsic dates and its recurrence rules.
     * <p>
     * Delegates to the associated {@link Recurrence#occursOn} method, which performs
     * recurrence-aware calculations to determine whether an occurrence exists on
     * {@code filterDate}.</p>
     *
     * @param filterDate the date to check for occurrence (must not be null)
     * @return {@code true} if this task should be considered as occurring on
     *         {@code filterDate}, {@code false} otherwise
     */
    @Override
    public boolean occursOn(LocalDate filterDate) {
        List<LocalDateTime> dates = getDates();
        if (dates.isEmpty()) return false;

        LocalDate originalStart = dates.get(0).toLocalDate();
        LocalDate originalEnd = dates.size() > 1
                ? dates.get(1).toLocalDate()
                : originalStart;
        return recurrence.occursOn(filterDate, originalStart, originalEnd);
    }

    /**
     * Marks this task as completed. Once marked as done, the task can still
     * be reverted to incomplete.
     *
     * @see #markAsUndone()
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Reverts a completed task back to incomplete state.
     *
     * @see #markAsDone()
     */
    public void markAsUndone() {
        isDone = false;
    }

    /* =============== Utility Methods =============== */


    /**
     * Formats the recurrence information for display in string representations.
     * <p>
     * This utility method is intended for use by subclasses in their {@code toString()}
     * implementations to provide consistent recurrence formatting.
     *
     * @return empty string if no recurrence, otherwise a formatted string
     *         in the format: " (recurs {type} × {frequency})"
     *         where type is recurrence pattern (e.g. "daily", "weekly").
     */
    protected String formatRecurrence() {
        return recurrence.isNone()
                ? ""
                : String.format(" (repeats %s × %d)",
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

    /* =========== Getters (Simple Accessor) =========== */


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Recurrence getRecurrence() {
        return recurrence;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return true if the task is completed, false otherwise
     */
    @Override
    public boolean isDone() {
        return isDone;
    }
}
