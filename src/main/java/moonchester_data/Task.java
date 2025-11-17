package moonchester_data;
import moonchester_utils.MoonchesterException;

/**
 * Represents a general Task object
 * Parent class for all types of tasks such as Todo, Deadline, and Event
 * Contains the basic attributes and methods that are common across all task types
 */
public class Task {
    protected String description;
    protected boolean status;

    /**
     * Constructs a Task object with the specified description
     * Throws a MoonchesterException exception if the description is empty or null
     * 
     * @param description Description of the task
     * @throws MoonchesterException If the description is empty or null
     */
    public Task(String description) throws MoonchesterException {
        if (description.trim().isEmpty() == true || description == null) {
            throw new MoonchesterException("[!] Description cannot be empty.");
        }
        this.description = description;
        this.status = false;
    }

    /**
     * Retrieves the description of the task
     * 
     * @return description Description of the task
     */
    public String getDescription() {
        assert this.description.isEmpty() == false : "Description of task should not be empty.";
        return this.description;
    }

    /**
     * Retrieves the status icon of the task
     * Returns "X" if the task is completed, or a blank space otherwise
     * 
     * @return statusIcon "X" if done, " " if not done
     */
    public String getStatusIcon() {
        if (this.status == true) {
            return "X";
        }
        return " ";
    }

    /**
     * Updates the completion status of the task
     * 
     * @param status Boolean value indicating if the task is completed
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Returns the task details in a formatted string
     * This method is meant to be overridden by subclasses for more specific formats
     * 
     * @return String Formatted string containing the task details
     */
    public String printString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

}
