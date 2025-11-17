package nerunerune.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import nerunerune.parser.DateTimeParser;

/**
 * Represents an event task with a start and an end time.
 * Extends Task and includes from and to timing.
 */
public class Event extends Task {
    private LocalDateTime fromTiming;
    private LocalDateTime toTiming;

    /**
     * Constructs an Event with description, start time, end time, and completion status.
     *
     * @param eventDescription description of the event
     * @param eventFromTiming  start date and time
     * @param eventToTiming    end date and time
     * @param isDone           whether the task is marked as done
     */
    public Event(String eventDescription, LocalDateTime eventFromTiming, LocalDateTime eventToTiming, boolean isDone) {
        super(eventDescription, isDone);
        this.fromTiming = eventFromTiming;
        this.toTiming = eventToTiming;
    }

    /**
     * Constructs an Event with description, start time, and end time. Defaults to not done.
     *
     * @param eventDescription description of the event
     * @param eventFromTiming  start date and time
     * @param eventToTiming    end date and time
     */
    public Event(String eventDescription, LocalDateTime eventFromTiming, LocalDateTime eventToTiming) {
        super(eventDescription);
        this.fromTiming = eventFromTiming;
        this.toTiming = eventToTiming;

    }

    /**
     * Constructs an Event with description and completion status. Timing is unspecified.
     *
     * @param eventDescription description of the event
     * @param isDone           whether the task is marked as done
     */
    public Event(String eventDescription, boolean isDone) {
        super(eventDescription, isDone);
    }

    /**
     * Returns the schedule item type, "event".
     *
     * @return "event"
     */
    @Override
    public String getScheduleItem() {
        return "event";
    }

    /**
     * Returns a string representation of the event for display, including status icon,
     * description, and formatted from and to timings.
     *
     * @return display string of the event
     */
    @Override
    public String toString() {
        return "[E]" + "[" + getStatusIcon() + "] " + getDescription() + " (from: "
                + DateTimeParser.formatForDisplay(fromTiming) + " to: " + DateTimeParser.formatForDisplay(toTiming) + ")";
    }

    /**
     * Returns a string suitable for storage, including task type, status, description,
     * and formatted from/to timings.
     *
     * @return storage string of the event
     */
    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s | %s",
                getIsDone() ? 1 : 0,
                getDescription(),
                DateTimeParser.formatForStorage(fromTiming),
                DateTimeParser.formatForStorage(toTiming));
    }

    /**
     * Returns the start date and time for this Event task.
     *
     * @return the start LocalDateTime indicating when the event begins
     */
    public LocalDateTime getEventFromDateTime() {
        return fromTiming;
    }

    /**
     * Returns the end date and time for this Event task.
     *
     * @return the end LocalDateTime indicating when the event concludes
     */
    public LocalDateTime getEventToDateTime() {
        return toTiming;
    }

    /**
     * Checks if this event has ended.
     * Compares the event's end time with the current date and time.
     *
     * @return true if the event's end time is before the current date and time, false otherwise
     */
    @Override
    public boolean isBackdated() {
        return this.toTiming.isBefore(LocalDateTime.now());
    }

    /**
     * Checks whether this event task occurs on the specified date.
     * Compares only the date portion of the deadline, ignoring the time component.
     * An event occurs on a date if the date falls within the event's duration
     * from the start date to the end date.
     *
     * @param date the date to check
     * @return true if the date is within the event's start and end dates, false otherwise
     */
    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate fromDate = this.fromTiming.toLocalDate();
        LocalDate toDate = this.toTiming.toLocalDate();

        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }
}
