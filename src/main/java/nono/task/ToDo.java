package nono.task;

/**
 * Represents a simple task without any time constraint.
 */
public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the given description.
     *
     * @param description The task description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }

    @Override
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
