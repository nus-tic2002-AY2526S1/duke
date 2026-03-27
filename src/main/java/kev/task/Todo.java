package kev.task;

/**
 * a basic Todo task with no date attached.
 */
public class Todo extends Task {
    /**
     * creates a new Todo task.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) { super(description); }

    /**
     * converts the todo task into storage format.
     *
     * @return Formatted todo string.
     */
    public String toFileString() { return "T | " + (isDone ? "1" : "0") + " | " + description; }

    /**
     * returns a more easily referenced/readable representation of the todo task.
     *
     * @return Todo display string.
     */
    public String toString() { return "[T][" + getStatusIcon() + "] " + description; }
}