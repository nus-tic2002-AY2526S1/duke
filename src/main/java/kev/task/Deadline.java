package kev.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * a task with a deadline.
 * deadline is stored as a {@link LocalDate}.
 */
public class Deadline extends Task {

    /** date in which the task must be completed by. */
    private LocalDate by; // store date as LocalDate

    /**
     * creates a new Deadline task.
     *
     * @param description Description of the task.
     * @param by The due date in YYYY-MM-DD format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by); // expects yyyy-MM-dd format
    }

    /**
     * returns the due date of the task.
     *
     * @return The due date as a LocalDate.
     */
    public LocalDate getBy() {
        return by;
    }

    /** sets/updates task with new date for deadline.*/
    public void setBy(LocalDate by) {
        this.by = by;
    }

    /**
     * converts the deadline into a savable string format for file storage.
     *
     * @return Formatted string representing the deadline.
     */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    /**
     * returns a more easily referenced/readable representation of the deadline task.
     *
     * @return Display string for the deadline.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}
