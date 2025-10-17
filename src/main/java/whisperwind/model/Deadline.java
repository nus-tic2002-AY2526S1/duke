package whisperwind.model;

import whisperwind.util.*;
import whisperwind.exceptions.TaskException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a {@link Task} with a specific deadline.
 * <p>
 * A {@code Deadline} task must include a date and time by which it is due.
 * Supports multiple date input formats and provides formatted output for display.
 */
public class Deadline extends Task {
    private LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs a new {@code Deadline} task using a description and a string-based deadline.
     *
     * @param description The task description.
     * @param by The deadline string (e.g., "2/12/2019 1800" or "2025-10-10 2359").
     * @throws TaskException If the {@code by} parameter is empty or has an invalid format.
     */
    public Deadline(String description, String by) throws TaskException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new TaskException("Deadline date cannot be empty!");
        }
        this.by = parseDateTime(InputSanitizer.sanitizeTime(by));
    }

    /**
     * Constructs a new {@code Deadline} task using a {@link LocalDateTime} object.
     *
     * @param description The task description.
     * @param by The {@link LocalDateTime} representing the deadline.
     * @throws TaskException If description is invalid.
     */
    public Deadline(String description, LocalDateTime by) throws TaskException {
        super(description);
        this.by = by;
    }

    /**
     * Parses the given date-time string into a {@link LocalDateTime} object.
     * Accepts multiple date-time formats for user flexibility.
     *
     * @param dateTimeStr The input date-time string to parse.
     * @return A {@link LocalDateTime} representing the parsed deadline.
     * @throws TaskException If the format is invalid or unsupported.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) throws TaskException {
        try {
            // Try format like "2/12/2019 1800"
            return LocalDateTime.parse(dateTimeStr, INPUT_FORMATTER);
        } catch (DateTimeParseException e1) {
            try {
                // Try format "yyyy-MM-dd HHmm"
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (DateTimeParseException e2) {
                throw new TaskException("Invalid date format. Use either 'd/M/yyyy HHmm' (e.g., 2/12/2019 1800) or 'yyyy-MM-dd HHmm'");
            }
        }
    }

    /**
     * Returns the {@link LocalDateTime} of the deadline.
     *
     * @return The {@link LocalDateTime} representing when the task is due.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns a user-friendly formatted deadline string.
     *
     * @return The formatted deadline (e.g., "Dec 02 2019, 6:00PM") or "unspecified" if not set.
     */
    public String getByFormatted() {
        return (by != null) ? by.format(OUTPUT_FORMATTER) : "unspecified";
    }

    /**
     * Returns the {@link TaskType} for this task, which is {@code DEADLINE}.
     *
     * @return The {@link TaskType#DEADLINE}.
     */
    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    /**
     * Returns a string representation of this {@code Deadline} task.
     * Displays the type, description, and formatted deadline.
     *
     * @return A formatted string representing the deadline task.
     */
    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.DEADLINE.getPrefix() + "[INVALID] Invalid Deadline Task";
            }
            return TaskType.DEADLINE.getPrefix() + super.toString() + " (by: " + getByFormatted() + ")";
        } catch (Exception e) {
            return TaskType.DEADLINE.getPrefix() + "[ERROR] Could not display task";
        }
    }

    /**
     * Checks whether this deadline task is valid.
     * A valid task has a non-empty description and a non-null deadline.
     *
     * @return {@code true} if valid; {@code false} otherwise.
     */
    @Override
    public boolean isValid() {
        return super.isValid() && by != null;
    }

    /**
     * Creates and returns a new {@code Deadline} instance after validating input fields.
     *
     * @param description The task description.
     * @param by The deadline string.
     * @return A new {@code Deadline} object.
     * @throws TaskException If description or date string is invalid.
     */
    public static Deadline createDeadline(String description, String by) throws TaskException {
        if (description == null || description.trim().isEmpty()) {
            throw new TaskException("Deadline description cannot be empty!");
        }
        if (by == null || by.trim().isEmpty()) {
            throw new TaskException("Deadline date cannot be empty!");
        }
        return new Deadline(description.trim(), by.trim());
    }

    /**
     * Checks whether this deadline task meets all required conditions.
     * Used for validation beyond simple null checks.
     *
     * @return {@code true} if the task meets all deadline requirements; {@code false} otherwise.
     */
    public boolean meetsDeadlineRequirements() {
        return super.isValid() &&
                by != null &&
                getDescription().length() <= 1000;
    }

    /**
     * Determines whether another object is equal to this {@code Deadline}.
     *
     * @param obj The object to compare.
     * @return {@code true} if both objects represent the same deadline task; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Deadline deadline = (Deadline) obj;
        return by.equals(deadline.by);
    }

    /**
     * Generates a hash code for this {@code Deadline} instance.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + by.hashCode();
        return result;
    }
}