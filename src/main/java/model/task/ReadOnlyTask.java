package model.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import common.exception.InvalidDateTimeException;

/**
 * Interface for read-only view of a task. Provides methods to query task properties
 * without allowing modifications.
 * <p>
 * <em>Solution inspired by:
 * <a href="https://github.com/se-edu/addressbook-level2/tree/master">
 * se-edu addressbook-level2</a></em>
 */
public interface ReadOnlyTask {

    /**
     * Returns the type of task.
     */
    TaskType getTaskType();

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
    List<LocalDateTime> getDates();

    String getDescription();

    /**
     * Returns the recurrence pattern of this task, if any.
     *
     * @return the Recurrence object representing how often this task repeats
     */
    Recurrence getRecurrence();

    /**
     * Returns whether this task has been completed.
     *
     * @return true if the task is completed, false otherwise
     */
    boolean isDone();

    /**
     * Returns the JSON field representation of task's date/time.
     */
    String toJsonFields();

    /**
     * Determines whether this task occurs on the given date, taking into account
     * both its intrinsic dates and its recurrence rules.
     * <p>
     * Delegates to the associated {@link Recurrence#occursOn} method, which performs
     * recurrence-aware calculations to determine whether an occurrence exists on
     * {@code filterDate}.
     *
     * @param filterDate the date to check for occurrence (must not be null)
     * @return {@code true} if this task should be considered as occurring on
     *         {@code filterDate}, {@code false} otherwise
     */
    boolean occursOn(LocalDate filterDate);

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
    Optional<ReadOnlyTask> createInstance(LocalDate filterDate) throws InvalidDateTimeException;

    /**
     * Formats the recurrence information for display in string representations.
     * <p>
     * This utility method is intended for use by subclasses in their {@code toString()}
     * implementations to provide consistent recurrence formatting.
     *
     * @return empty string if no recurrence, otherwise a formatted string
     *         in the format: {@code (recurs {type} × {frequency})}
     *         where type is recurrence pattern (e.g. "daily", "weekly") and
     *         frequency is a non-zero value.
     */
    String formatRecurrence();
}
