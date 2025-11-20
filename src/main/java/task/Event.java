package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import util.DateTime;

public class Event extends Task {
    private final String fromRaw;
    private final String toRaw;

    public Event(String description, String from, String to) {
        super(description);
        this.fromRaw = from.trim();
        this.toRaw = to.trim();
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.fromRaw = from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        this.toRaw = to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }

    public LocalDateTime getFromDateTime() {
        return DateTime.parseDateTime(fromRaw);
    }

    public LocalDateTime getToDateTime() {
        return DateTime.parseDateTime(toRaw);
    }

    public String getFromRaw() {
        return fromRaw;
    }

    public String getToRaw() {
        return toRaw;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateTime.formatRawDateTimeSafe(fromRaw)
                + " to: " + DateTime.formatRawDateTimeSafe(toRaw) + ")";
    }
}
