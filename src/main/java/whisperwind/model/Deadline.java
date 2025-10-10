package whisperwind.model;
import whisperwind.util.*;
public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Deadline time cannot be empty!");
        }
        this.by = InputSanitizer.sanitizeTime(by);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.DEADLINE.getPrefix() + "[INVALID] Invalid model.Deadline model.Task";
            }
            if (by == null || by.trim().isEmpty()) {
                return TaskType.DEADLINE.getPrefix() + super.toString() + " (by: unspecified)";
            }
            return TaskType.DEADLINE.getPrefix() + super.toString() + " (by: " + by + ")";
        } catch (Exception e) {
            return TaskType.DEADLINE.getPrefix() + "[ERROR] Could not display deadline task";
        }
    }

    public String getBy() {
        return (by != null) ? by : "unspecified";
    }

    public void setBy(String by) {
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Deadline time cannot be empty!");
        }
        this.by = InputSanitizer.sanitizeTime(by);
    }

    public static Deadline createDeadline(String description, String by) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Deadline description cannot be empty!");
        }
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Deadline time cannot be empty!");
        }
        return new Deadline(description.trim(), by.trim());
    }

    public boolean hasReasonableTimeFormat() {
        if (by == null) return false;
        return by.length() >= 2 &&
                by.length() <= 100 &&
                !by.matches(".*[\\x00-\\x1F\\x7F].*");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Deadline deadline = (Deadline) obj;
        return by.equals(deadline.by);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + by.hashCode();
        return result;
    }
}