package moonchester_data;

/**
 * Represents a Todo task.
 * Inherits from the Task class and contains information about a simple task without any specific time or date.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     * 
     * @param description Description of the Todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Prints the details of the Todo task in a formatted string.
     * 
     * @return String Formatted string representation of the Todo task
     */
    @Override
    public String printString() {
        return "[T]" + "[" + getStatusIcon() + "] " + getDescription();
    }

}
