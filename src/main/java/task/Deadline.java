package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import util.DateTime;

public class Deadline extends Task {
    private final String byRaw;

    public String getByRaw() {
        return byRaw;
    }

    public Deadline(String description, String byStr) {
        super(description);
        this.byRaw = byStr.trim();
    }

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.byRaw = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }

    public LocalDateTime getParsedBy() {
        return DateTime.parseDateTime(byRaw);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + DateTime.formatRawDateTimeSafe(byRaw) + ")";
    }
}
