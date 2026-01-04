package bongocat.tasks;

public class Event extends Task {

    private final String from;
    private final String to;

    public Event(String name, String from, String to) {
        super(name);

        assert from != null : "Event /from should not be null";
        assert to != null  : "Event /to should not be null";

        this.from = from.trim();
        this.to = to.trim();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "[E]";
    }

    @Override
    public String toSaveFormat() {
        return "E | " + (isDone ? "[X]" : "[ ]")
                + " | " + name + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return getTypeIcon()
                + statusBox()
                + " " + name
                + " (from: " + from + " to: " + to + ")";
    }
}
