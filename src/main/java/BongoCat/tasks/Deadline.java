package bongocat.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Deadline Class - Extends as a type of Task
 */
public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String name, String by) {
        super(name);
        this.by = LocalDate.parse(by); // parse yyyy-mm-dd
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return getTypeIcon()
                + statusBox()
                + " " + name
                + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

    @Override
    public String toSaveFormat() {
        return "D | " + (isDone ? "[X]" : "[ ]") + " | " + name + " | " + by.toString();
    }

    public LocalDate getBy() {
        return by;
    }
}
