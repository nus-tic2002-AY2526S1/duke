package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task with a start and end date.
 */
public class Event extends Task {

    private String toDateString;
    private String fromDateString;
    private LocalDateTime toDate;
    private LocalDateTime fromDate;

    /**
     * @param description of the task.
     * @param index of task in a TaskList.
     * @param fromDate start date of task.
     * @param toDate end date of task.
     */
    public Event(String desc, int ind, LocalDateTime fromDate, LocalDateTime toDate) {
        super(desc, ind);
        this.fromDate = fromDate;
        this.fromDateString = this.fromDate.format(DateTimeFormatter.ofPattern("MMM d yyyy, HHmm"));
        this.toDate = toDate;
        this.toDateString = this.toDate.format(DateTimeFormatter.ofPattern("MMM d yyyy, HHmm"));
    }

    @Override
    public String getTaskString() {
        String taskString;
        if (this.getPriority() != null) {
            if (this.getStatus()) {
                taskString = String.format("   [E][X][%s] ", this.getPriority().toString());
            } else {
                taskString = String.format("   [E][ ][%s] ", this.getPriority().toString());
            }
        } else {
            if (this.getStatus()) {
                taskString = "   [E][X][ ] ";
            } else {
                taskString = "   [E][ ][ ] ";
            }
        }
        return (taskString + this.getDescription() + "\n"
            + "   (from: " + fromDateString + " hrs | to: " + toDateString + " hrs)");
    }

    @Override
    public String getTaskStringWithIndex() {
        String taskString;
        if (this.getPriority() != null) {
            if (this.getStatus()) {
                taskString = " " + this.getIndex() + String.format(". [E][X][%s] ", this.getPriority().toString());
            } else {
                taskString = " " + this.getIndex() + String.format(". [E][ ][%s] ", this.getPriority().toString());
            }
        } else {
            if (this.getStatus()) {
                taskString = " " + this.getIndex() + ". [E][X][ ] ";
            } else {
                taskString = " " + this.getIndex() + ". [E][ ][ ] ";
            }
        }
        return (taskString + this.getDescription() + "\n"
            + "    (from: " + fromDateString + " hrs | to: " + toDateString + "hrs)");
    }

    public String getToDateString() {
        return toDateString;
    }

    public String getFromDateString() {
        return fromDateString;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }
}
