package model.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import logic.parser.datetime.ParsedDateTime;

/**
 * Task with a specific date/time.
 */
public class DeadlineTask extends Task {
    private final LocalDateTime deadline;
    private final boolean hasTime;

    public DeadlineTask(
            String description,
            ParsedDateTime pdt,
            Recurrence recurrence
    ) {
        super(description, recurrence);
        this.deadline = pdt.dateTime();
        this.hasTime = pdt.hasTime();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public List<LocalDateTime> getDates() {
        return Collections.singletonList(deadline);
    }

    /**
     * Creates a copy of this deadline task with no recurrence.
     *
     * @return a new {@link DeadlineTask} instance with the same description and deadline,
     *         but with recurrence set to {@link Recurrence#none(LocalDate)}
     */
    @Override
    protected Task copy(LocalDateTime start, LocalDateTime end) {
        ParsedDateTime instancePdt = new ParsedDateTime(start, this.hasTime);
        return new DeadlineTask(this.getDescription(), instancePdt, null);
    }

    /**
     * Generates JSON field representation of the deadline.
     * The deadline is formatted as either a full datetime string (if time is specified)
     * or as a date-only string (if no time is specified).
     */
    @Override
    public String toJsonFields() {
        String deadlineStr = hasTime
                ? deadline.toString()
                : deadline.toLocalDate().toString();
        return String.format(",\n  \"deadline\":\"%s\"", deadlineStr);
    }

    /**
     * Extends base format with deadline information in the format "(by: deadline)"
     * and includes recurrence information if applicable.
     */
    @Override
    public String toString() {
        String deadlineString = ParsedDateTime.format(deadline, hasTime);
        return super.toString()
                + String.format(" (by: %s)", deadlineString)
                + formatRecurrence();
    }
}
