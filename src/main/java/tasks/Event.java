package tasks;

import parser.DateTimeParser;

import java.time.LocalDateTime;

/**
 * Event task
 */
public class Event extends Task {
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;

    /**
     * Initialise event task with task name, start-date and time, end-date and time
     *
     * @param description   task name
     * @param startDateTime start-date and time
     * @param endDateTime   end-date and time
     */
    public Event(String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(description);

        assert startDateTime != null;
        assert endDateTime != null;

        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Getter function
     *
     * @return start-date and time
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Getter function
     *
     * @return end-date and time
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Setter function for start-date
     *
     * @param startDateTime to set by-date as
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        assert startDateTime != null;

        this.startDateTime = startDateTime;
    }

    /**
     * Setter function for start-date
     *
     * @param endDateTime to set by-date as
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        assert endDateTime != null;

        this.endDateTime = endDateTime;
    }

    /**
     * Print format for event task
     *
     * @return event task in string
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " | from: " + DateTimeParser.formatDateTime(startDateTime) +
                ", to: " + DateTimeParser.formatDateTime(endDateTime);
    }

}
