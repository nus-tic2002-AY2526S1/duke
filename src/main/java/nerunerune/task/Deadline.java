package nerunerune.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import nerunerune.parser.DateTimeParser;

/**
 * Represents a deadline task with a specific date and time by which it must be completed.
 * Extends the Task class with an additional time attribute.
 */
public class Deadline extends Task {
    private LocalDateTime byTiming;

    /**
     * Constructs a Deadline task with the given description and deadline timing.
     *
     * @param deadlineDescription description of the deadline task
     * @param byTiming            the date and time by which the task should be completed
     */
    public Deadline(String deadlineDescription, LocalDateTime byTiming) {
        super(deadlineDescription);
        this.byTiming = byTiming;
    }

    /**
     * Constructs a Deadline task with the given description and completion status.
     *
     * @param deadlineDescription description of the deadline task
     * @param isDone              whether the task is marked as done
     */
    public Deadline(String deadlineDescription, boolean isDone) {
        super(deadlineDescription, isDone);
    }

    /**
     * Constructs a Deadline task with description, deadline timing, and completion status.
     *
     * @param deadlineDescription description of the deadline task
     * @param byTiming            the date and time by which the task should be completed
     * @param isDone              whether the task is marked as done
     */
    public Deadline(String deadlineDescription, LocalDateTime byTiming, boolean isDone) {
        super(deadlineDescription, isDone);
        this.byTiming = byTiming;
    }

    /**
     * Returns the type of schedule item this task represents.
     *
     * @return the string "deadline"
     */
    @Override
    public String getScheduleItem() {
        return "deadline";
    }

    /**
     * Returns a string representation of the deadline task for display,
     * including type, status, description, and formatted deadline timing.
     *
     * @return string representation of the deadline
     */
    @Override
    public String toString() {
        return "[D]" + "[" + getStatusIcon() + "] " + getDescription() + " (by: " + DateTimeParser.formatForDisplay(byTiming)
                + ")";
    }

    /**
     * Returns a string representation of the deadline task for storage,
     * including type, done status, description, and formatted timing.
     *
     * @return string for storage representation
     */
    @Override
    public String toStorageString() {
        return String.format("D | %d | %s | %s",
                getIsDone() ? 1 : 0,
                getDescription(),
                DateTimeParser.formatForStorage(byTiming));
    }

    /**
     * Returns the deadline date and time for this Deadline task.
     *
     * @return the deadline LocalDateTime indicating when the task is due
     */
    public LocalDateTime getDeadlineByDateTime() {
        return byTiming;
    }

    /**
     * Checks if this deadline has passed.
     * Compares the deadline's due date with the current date and time.
     *
     * @return true if the deadline is before the current date and time, false otherwise
     */
    @Override
    public boolean isBackdated() {
        return this.byTiming.isBefore(LocalDateTime.now());
    }

    /**
     * Checks whether this deadline task occurs on the specified date.
     * Compares only the date portion of the deadline, ignoring the time component.
     *
     * @param date the date to check
     * @return true if the deadline's date matches the specified date, false otherwise
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return this.byTiming.toLocalDate().equals(date);
    }
}