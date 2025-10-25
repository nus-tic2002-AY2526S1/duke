package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import exception.InvalidDateTimeException;

/**
 * Abstract base class representing a mutable task entity.
 * <p>
 * Each task has a description, completion state and recurrence rule.
 * This abstract class provides common functionality for all task types while
 * requiring subclasses {@link TodoTask} {@link DeadlineTask} {@link EventTask}
 * to implement type-specific behaviour.
 */
public abstract class Task implements ReadOnlyTask {
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
    Task(String description, Recurrence recurrence) {
        this.description = description;
        this.recurrence = recurrence != null
                ? recurrence
                : Recurrence.none(LocalDate.now());
        this.isDone = false;    // tasks are incomplete by default
    }

    /* =============== Abstract Methods =============== */

    @Override
    public abstract TaskType getTaskType();

    @Override
    public abstract List<LocalDateTime> getDates();

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

    @Override
    public String formatRecurrence() {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task other)) return false;

        return Objects.equals(description, other.description)
                && Objects.equals(getTaskType(), other.getTaskType())
                && Objects.equals(getDates(), other.getDates())
                && Objects.equals(recurrence, other.recurrence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, getTaskType(), getDates(), recurrence);
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

    @Override
    public boolean isDone() {
        return isDone;
    }
}
