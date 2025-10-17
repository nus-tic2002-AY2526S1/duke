package whisperwind.model;

import whisperwind.exceptions.TaskException;

/**
 * Represents a simple todo task without any date or time constraints.
 * This is the most basic type of task that only requires a description.
 * Todo tasks are marked with the {@link TaskType#TODO} type and use "📝" emoji.
 */
public class Todo extends Task {
    /**
     * Constructs a new Todo task with the specified description.
     * The description is validated to ensure it's not null or empty.
     *
     * @param description the description of the todo task; cannot be null or empty
     * @throws TaskException if the description is null, empty, or contains only whitespace
     * @see #createTodo(String)
     */
    public Todo(String description) throws TaskException {
        super(description);
    }

    /**
     * Returns the type of this task, which is always {@link TaskType#TODO}.
     *
     * @return the TaskType.TODO constant
     */

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    /**
     * Returns a string representation of this todo task.
     * The format includes the todo prefix and the task's string representation.
     * If the task is invalid, returns an appropriate error message.
     *
     * <p>Example outputs:
     * <pre>
     * {@code
     * "[T][X] Buy groceries"  // Completed todo
     * "[T][ ] Read book"      // Incomplete todo
     * "[T][INVALID] Invalid Todo Task"  // Invalid task
     * }
     * </pre>
     *
     * @return a formatted string representation of the todo task
     */

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.TODO.getPrefix() + "[INVALID] Invalid Todo Task";
            }
            return TaskType.TODO.getPrefix() + super.toString();
        } catch (Exception e) {
            return TaskType.TODO.getPrefix() + "[ERROR] Could not display task";
        }
    }

    /**
     * Factory method to create a new Todo instance with validation.
     * This provides an alternative way to create Todo objects with explicit validation.
     *
     * @param description the description of the todo task; cannot be null or empty
     * @return a new validated Todo instance
     * @throws TaskException if the description is null, empty, or contains only whitespace
     * @see #Todo(String)
     */

    public static Todo createTodo(String description) throws TaskException {
        if (description == null || description.trim().isEmpty()) {
            throw new TaskException("Todo description cannot be empty!");
        }
        return new Todo(description.trim());
    }

    /**
     * Checks if this todo task meets all requirements for validity.
     * A todo is considered valid if it has a non-null, non-empty description
     * that does not exceed 1000 characters in length.
     *
     * <p>This method provides additional validation beyond the basic {@link #isValid()}
     * method from the parent class.
     *
     * @return true if the todo meets all requirements, false otherwise
     * @see #isValid()
     */

    public boolean meetsTodoRequirements() {
        String desc = getDescription();
        return desc != null &&
                !desc.trim().isEmpty() &&
                desc.length() <= 1000;
    }
}