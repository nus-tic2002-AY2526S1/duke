package whisperwind.model;
import whisperwind.util.*;
public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event end time cannot be empty!");
        }
        this.from = InputSanitizer.sanitizeTime(from);
        this.to = InputSanitizer.sanitizeTime(to);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.EVENT.getPrefix() + "[INVALID] Invalid model.Event model.Task";
            }
            String displayFrom = (from == null || from.trim().isEmpty()) ? "unspecified" : from;
            String displayTo = (to == null || to.trim().isEmpty()) ? "unspecified" : to;
            return TaskType.EVENT.getPrefix() + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
        } catch (Exception e) {
            return TaskType.EVENT.getPrefix() + "[ERROR] Could not display event task";
        }
    }

    public String getFrom() {
        return (from != null) ? from : "unspecified";
    }

    public String getTo() {
        return (to != null) ? to : "unspecified";
    }

    public void setFrom(String from) {
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event start time cannot be empty!");
        }
        this.from = InputSanitizer.sanitizeTime(from);
    }

    public void setTo(String to) {
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event end time cannot be empty!");
        }
        this.to = InputSanitizer.sanitizeTime(to);
    }

    public static Event createEvent(String description, String from, String to) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event description cannot be empty!");
        }
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Event end time cannot be empty!");
        }
        return new Event(description.trim(), from.trim(), to.trim());
    }

    public boolean hasLogicalTimeOrder() {
        if (from == null || to == null) return false;
        return from.compareTo(to) <= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Event event = (Event) obj;
        return from.equals(event.from) && to.equals(event.to);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}