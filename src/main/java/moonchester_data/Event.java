package moonchester_data;
import java.time.LocalDateTime;

import moonchester_utils.MoonchesterDate;

/**
 * Represents an Event task
 * Event is a Task with a specific start and end date/time
 * Provides methods to retrieve formatted date strings and original date objects
 */
public class Event extends Task {

    public LocalDateTime from;
    public LocalDateTime to;

    /**
     * Constructs an Event task with a description, start date/time, and end date/time
     * 
     * @param description Description of the Event task
     * @param from Start date and time of the Event
     * @param to End date and time of the Event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Retrieves the start date/time of the Event as a readable string
     * 
     * @return String Formatted start date/time of the Event
     */
    public String getFrom() {
        return MoonchesterDate.readableDate(this.from);
    }

    /**
     * Retrieves the "to" date of the Event as a readable string
     * 
     * @return String Formatted "to" date of the Event
     */
    public String getTo() {
        return MoonchesterDate.readableDate(this.to);
    }

    /**
     * Retrieves the "to" date of the Event as a LocalDateTime object
     * Useful for comparisons with other date objects
     * 
     * @return LocalDateTime of the "to" date of the Event
     */
    public LocalDateTime getToObject() {
        return this.to;
    }

    /**
     * Retrieves the "from" date of the Event as a LocalDateTime object
     * Useful for comparisons with other date objects
     * 
     * @return LocalDateTime "from" date of the Event
     */
    public LocalDateTime getFromObject() {
        return this.from;
    }
    
    /**
     * Returns the Event task details in a formatted string
     * Overrides the printString method from the Task superclass
     * 
     * @return String Formatted string containing Event details including description, status, and date/time range
     */
    @Override
    public String printString() {
        return "[E]" + "[" + getStatusIcon() + "] " + getDescription() + " (from: " + getFrom() + "hrs" + " to: "+ getTo() + "hrs" +")";
    }

}
