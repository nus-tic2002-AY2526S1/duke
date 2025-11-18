package chatbot.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import chatbot.DateTimeParser;
import chatbot.QianException;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) throws QianException {
        super(description);
        this.from = DateTimeParser.parse(from);
        this.to = DateTimeParser.parse(to);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");
        return "[E]" + super.toString()
                + " (from: " + from.format(fmt)
                + " to: " + to.format(fmt) + ")";
    }

    public String getFromRaw() {
        return from.toString();
    }

    public String getToRaw() {
        return to.toString();
    }

    @Override
    public Optional<LocalDateTime[]> getBusyPeriod() {
        return Optional.of(new LocalDateTime[]{from, to});
    }

}