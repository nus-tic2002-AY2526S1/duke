package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with an end date.
 */
public class Deadline extends Task {

    private final String deadlineDisplayString;
    private final LocalDateTime deadline;

    /**
     * @param desc Description of the task.
     * @param ind Index of task in a TaskList.
     * @param deadline of the task.
     */
    public Deadline(String desc, int ind, LocalDateTime deadline) {
        super(desc, ind);
        this.deadline = deadline;
        this.deadlineDisplayString = deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy, HHmm"));
    }

    @Override
    public String getTaskString() {
        String taskString;
        if (this.getPriority() != null) {
            if (this.getStatus()) {
                taskString = String.format("   [D][X][%s] ", this.getPriority().toString());
            } else {
                taskString = String.format("   [D][ ][%s] ", this.getPriority().toString());
            }
        } else {
            if (this.getStatus()) {
                taskString = "   [D][X][ ] ";
            } else {
                taskString = "   [D][ ][ ] ";
            }
        }

        return (taskString + this.getDescription() + "\n   (by: " + deadlineDisplayString + " hrs)");
    }

    @Override
    public String getTaskStringWithIndex() {
        String taskString;
        if (this.getPriority() != null) {
            if (this.getStatus()) {
                taskString = " " + this.getIndex() + String.format(". [D][X][%s] ", this.getPriority().toString());
            } else {
                taskString = " " + this.getIndex() + String.format(". [D][ ][%s] ", this.getPriority().toString());
            }
        } else {
            if (this.getStatus()) {
                taskString = " " + this.getIndex() + ". [D][X][ ] ";
            } else {
                taskString = " " + this.getIndex() + ". [D][ ][ ] ";
            }
        }
        return (taskString + this.getDescription() + "\n    (by: " + deadlineDisplayString + " hrs)");
    }

    public String getDeadlineString() {
        return deadlineDisplayString;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }
}
