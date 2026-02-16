package tasks;

/**
 * Todo task
 */
public class ToDo extends Task {
    /**
     * Initialise todo task with task name
     *
     * @param description task name
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Print format for todo task
     *
     * @return todo task in string
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
