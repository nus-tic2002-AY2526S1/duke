package echo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task with a description, start time (/from), and end time (/to).
 * Stores the times as LocalDateTime objects and formats them for display and storage.
 */
public class Event extends Task {

    /** Start date/time of the event */
    protected LocalDateTime from;

    /** End date/time of the event */
    protected LocalDateTime to;

    /** Formatter for parsing input date/time */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Formatter for displaying date/time to user */
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Constructs a new Event with the given description and start/end times.
     *
     * @param description The event description
     * @param from        The start time in yyyy-MM-dd HHmm format
     * @param to          The end time in yyyy-MM-dd HHmm format
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDateTime.parse(from, INPUT_FORMAT);
        this.to = LocalDateTime.parse(to, INPUT_FORMAT);
    }

    /**
     * Returns a string representation of the event task for display.
     *
     * @return Formatted string with type, status, description, and start/end times
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(OUTPUT_FORMAT)
                + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Returns a string representation of the event task for saving to file.
     *
     * @return Formatted string suitable for storage
     */
    @Override
    public String toSaveFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | "
                + from.format(INPUT_FORMAT) + " | " + to.format(INPUT_FORMAT);
    }
}
