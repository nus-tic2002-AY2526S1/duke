package echo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task with a description and a due date/time.
 * Stores the date/time as a LocalDateTime and formats it for display and storage.
 */
public class Deadline extends Task {

    /** The due date and time of the deadline */
    protected LocalDateTime by;

    /** Formatter for parsing input from user or file */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Formatter for displaying the date/time to the user */
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Constructs a new Deadline task with the given description and due date/time.
     *
     * @param description The task description
     * @param by          The due date/time in format yyyy-MM-dd HHmm
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }

    /**
     * Returns a string representation of the deadline task for display.
     *
     * @return Formatted string with type, status, description, and due date/time
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Returns a string representation of the deadline task for saving to file.
     *
     * @return Formatted string suitable for storage
     */
    @Override
    public String toSaveFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.format(INPUT_FORMAT);
    }
}
