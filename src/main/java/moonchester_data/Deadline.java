package moonchester_data;
import java.time.LocalDateTime;

import moonchester_utils.MoonchesterDate;

/**
 * Represents a Deadline task
 * Deadline is a Task that has a specific due date and time
 * Provides methods to retrieve the due date as a readable string or as a LocalDateTime object
 */
public class Deadline extends Task {

    public LocalDateTime by;

    /**
     * Constructs a Deadline task with a description and a "by" date (due date)
     * 
     * @param description Description of the Deadline task
     * @param by Due date and time of the Deadline
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Retrieves the due date/time of the Deadline as a readable string
     * 
     * @return String Formatted due date/time of the Deadline
     */
    public String getBy() {
        return MoonchesterDate.readableDate(this.by);
    }

    /**
     * Retrieves the due date/time of the Deadline as a LocalDateTime object
     * Useful for comparisons with other date objects
     * 
     * @return LocalDateTime Due date/time of the Deadline
     */
    public LocalDateTime getByObject() {
        return this.by;
    }

    /**
     * Returns the Deadline task details in a formatted string
     * Overrides the printString method from the Task superclass
     * 
     * @return String Formatted string containing Deadline details including description, status, and due date/time
     */
    @Override
    public String printString() {
        return "[D]" + "[" + getStatusIcon() + "] " + getDescription() + " (by: " + getBy() + "hrs" +")";
    }

}
