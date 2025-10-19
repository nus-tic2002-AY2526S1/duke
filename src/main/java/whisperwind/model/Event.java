package whisperwind.model;

import whisperwind.util.*;
import whisperwind.exceptions.TaskException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task with start and end times.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs a new Event task with string-based times.
     *
     * @param description The event description
     * @param from The start time string
     * @param to The end time string
     * @throws TaskException If any parameter is invalid or dates are in the past
     */
    public Event(String description, String from, String to) throws TaskException {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new TaskException("Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new TaskException("Event end time cannot be empty!");
        }
        this.from = parseDateTime(InputSanitizer.sanitizeTime(from));
        this.to = parseDateTime(InputSanitizer.sanitizeTime(to));

        // Validate that event times are not in the past
        validateFutureDate(this.from, "Event start time");
        validateFutureDate(this.to, "Event end time");

        if (!hasLogicalTimeOrder()) {
            throw new TaskException("Event end time must be after or equal to start time!");
        }
    }

    /**
     * Constructs a new Event task with LocalDateTime objects.
     *
     * @param description The event description
     * @param from The start time
     * @param to The end time
     * @throws TaskException If description is invalid or dates are in the past
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) throws TaskException {
        super(description);
        // Assert constructor assumptions
        assert from != null : "Start time should not be null";
        assert to != null : "End time should not be null";

        this.from = from;
        this.to = to;

        // Validate that event times are not in the past
        validateFutureDate(this.from, "Event start time");
        validateFutureDate(this.to, "Event end time");

        if (!hasLogicalTimeOrder()) {
            throw new TaskException("Event end time must be after or equal to start time!");
        }

        // Post-constructor state assertions
        assert hasLogicalTimeOrder() : "Event should have logical time order after construction";

    }

    /**
     * Parses date-time strings into LocalDateTime objects.
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
     * Validates that the given date is not in the past.
     *
     * @param dateTime The date to validate
     * @param fieldName The name of the field for error messages
     * @throws TaskException If the date is in the past
     */
    private void validateFutureDate(LocalDateTime dateTime, String fieldName) throws TaskException {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new TaskException(fieldName + " cannot be in the past! Please choose a future date and time.");
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.EVENT.getPrefix() + "[INVALID] Invalid Event Task";
            }
            String displayFrom = (from == null) ? "unspecified" : from.format(OUTPUT_FORMATTER);
            String displayTo = (to == null) ? "unspecified" : to.format(OUTPUT_FORMATTER);
            return TaskType.EVENT.getPrefix() + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
        } catch (Exception e) {
            return TaskType.EVENT.getPrefix() + "[ERROR] Could not display event task";
        }
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromFormatted() {
        return (from != null) ? from.format(OUTPUT_FORMATTER) : "unspecified";
    }

    public String getToFormatted() {
        return (to != null) ? to.format(OUTPUT_FORMATTER) : "unspecified";
    }

    /**
     * Sets the start time from a string.
     *
     * @param from The start time string
     * @throws TaskException If the time string is invalid or in the past
     */
    public void setFrom(String from) throws TaskException {
        if (from == null || from.trim().isEmpty()) {
            throw new TaskException("Event start time cannot be empty!");
        }
        LocalDateTime newFrom = parseDateTime(InputSanitizer.sanitizeTime(from));

        // Validate that new start time is not in the past
        validateFutureDate(newFrom, "Event start time");

        this.from = newFrom;

        if (!hasLogicalTimeOrder()) {
            throw new TaskException("Event end time must be after or equal to start time!");
        }
    }

    /**
     * Sets the end time from a string.
     *
     * @param to The end time string
     * @throws TaskException If the time string is invalid or in the past
     */
    public void setTo(String to) throws TaskException {
        if (to == null || to.trim().isEmpty()) {
            throw new TaskException("Event end time cannot be empty!");
        }
        LocalDateTime newTo = parseDateTime(InputSanitizer.sanitizeTime(to));

        // Validate that new end time is not in the past
        validateFutureDate(newTo, "Event end time");

        this.to = newTo;

        if (!hasLogicalTimeOrder()) {
            throw new TaskException("Event end time must be after or equal to start time!");
        }
    }

    /**
     * Creates a new Event instance with validation.
     */
    public static Event createEvent(String description, String from, String to) throws TaskException {
        if (description == null || description.trim().isEmpty()) {
            throw new TaskException("Event description cannot be empty!");
        }
        if (from == null || from.trim().isEmpty()) {
            throw new TaskException("Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new TaskException("Event end time cannot be empty!");
        }
        return new Event(description.trim(), from.trim(), to.trim());
    }

    public boolean hasLogicalTimeOrder() {
        // Assert state before operation
        assert from != null : "Start time should be initialized";
        assert to != null : "End time should be initialized";

        return from.isBefore(to) || from.isEqual(to);
    }

    @Override
    public boolean isValid() {
        // Assert base class validity
        assert super.isValid() : "Base task should be valid";

        return super.isValid() && from != null && to != null && hasLogicalTimeOrder();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Event event = (Event) obj;
        return from.equals(event.from) && to.equals(event.to);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}