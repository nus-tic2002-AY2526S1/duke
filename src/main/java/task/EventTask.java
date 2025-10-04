package task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import exception.InvalidDateTimeException;
import exception.InvalidDateTimeException.ErrorType;
import parser.datetime.ParsedDateTime;

/**
 * Task with a start and end date/time.
 */
public class EventTask extends Task {
    private final LocalDateTime start, end;
    private final boolean hasTime;

    public EventTask(
            String description,
            ParsedDateTime start,
            ParsedDateTime end,
            Recurrence recurrence) throws InvalidDateTimeException {

        super(description, recurrence);
        this.start = start.dateTime();
        this.end = end.dateTime();
        this.hasTime = start.hasTime() && end.hasTime();
        if (this.end.isBefore(this.start)) {
            throw new InvalidDateTimeException(ErrorType.END_BEFORE_START, "");
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public List<LocalDateTime> getDates() {
        return Arrays.asList(start, end);
    }

    /**
     * Creates a copy of this event task with no recurrence.
     *
     * @return a new {@link EventTask} instance with the same description and date-times,
     *         but with recurrence set to {@link Recurrence#none(java.time.LocalDate)}
     */
    @Override
    protected Task copy(LocalDateTime start, LocalDateTime end)
            throws InvalidDateTimeException {
        ParsedDateTime instanceStart = new ParsedDateTime(start, this.hasTime);
        ParsedDateTime instanceEnd = new ParsedDateTime(end, this.hasTime);
        return new EventTask(this.getDescription(), instanceStart, instanceEnd, null);
    }

    /**
     * Generates JSON field representation of the event start and end.
     * The event is formatted as either a full datetime string (if time is specified)
     * or as a date-only string (if no time is specified).
     */
    @Override
    public String toJsonFields() {
        String startStr = hasTime
                ? start.toString()
                : start.toLocalDate().toString();
        String endStr = hasTime
                ? end.toString()
                : end.toLocalDate().toString();

        return String.format(",\n  \"start\":\"%s\",\n  \"end\":\"%s\"", startStr, endStr);
    }

    /**
     * Extends base format with event information in the format "(from: eventStart to: eventEnd)"
     * and includes recurrence information if applicable.
     */
    @Override
    public String toString() {
        String eventStart = ParsedDateTime.format(start, hasTime);
        String eventEnd = ParsedDateTime.format(end, hasTime);
        return super.toString()
                + String.format(" (from: %s to: %s)", eventStart, eventEnd)
                + formatRecurrence();
    }
}
