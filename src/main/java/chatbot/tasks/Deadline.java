package chatbot.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import chatbot.DateTimeParser;
import chatbot.QianException;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, String by) throws QianException {
        super(description);
        this.by = DateTimeParser.parse(by);
    }

    public String getBy() {
        return by.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma"));
    }

    public String getByRaw() {
        return by.toString(); // 2025-11-10T18:30
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getBy() + ")";
    }

    @Override
    public Optional<LocalDateTime[]> getBusyPeriod() {
        LocalDateTime start = by.minusHours(1);
        LocalDateTime end = by;
        return Optional.of(new LocalDateTime[]{start, end});
    }

}