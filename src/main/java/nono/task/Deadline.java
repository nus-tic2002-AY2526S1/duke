package nono.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed before a specific deadline.
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm");

    /**
     * Constructs a Deadline task with a description and due date.
     *
     * @param description The task description.
     * @param by          The due date/time in yyyy-MM-dd HH:mm format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.format(INPUT_FORMAT);
    }
}
