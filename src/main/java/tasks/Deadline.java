package tasks;

import parser.DateTimeParser;

import java.time.LocalDateTime;

/**
 * Deadline task
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Initialise deadline task with task name and by-date and time
     *
     * @param description task name
     * @param by          by-date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);

        assert by != null;

        this.by = by;
    }

    /**
     * Get by-date and time of task
     *
     * @return by-date and time
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Setter function for by-date
     *
     * @param by to set by-date as
     */
    public void setBy(LocalDateTime by) {
        assert by != null;

        this.by = by;
    }

    /**
     * Print format of deadline task
     *
     * @return deadline task in string
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " | by: " + DateTimeParser.formatDateTime(by);
    }
}
