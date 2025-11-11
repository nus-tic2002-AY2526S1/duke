package nono.command;

/**
 * Represents a user command parsed from input.
 * Stores the command type and relevant information.
 */
public class Command {
    /**
     * Enumeration of supported command types.
     */
    public enum Type {
        LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, BYE, FIND
    }

    private Type type;
    private String description;
    private String[] details;
    private int taskIndex;

    /**
     * Constructs a Command with a type only.
     *
     * @param type The command type.
     */
    public Command(Type type) {
        assert type != null : "Command type should not be null";
        this.type = type;
    }

    /**
     * Constructs a Command with a description.
     *
     * @param type        The command type.
     * @param description The task description.
     */
    public Command(Type type, String description) {
        assert type != null : "Command type should not be null";
        this.type = type;
        this.description = description;
    }

    /**
     * Constructs a Command with a task index.
     *
     * @param type      The command type.
     * @param taskIndex The task index.
     */
    public Command(Type type, int taskIndex) {
        assert type != null : "Command type should not be null";
        assert taskIndex >= 0 : "Task index should not be negative";
        this.type = type;
        this.taskIndex = taskIndex;
    }

    /**
     * Constructs a Command with details (e.g. date/time info).
     *
     * @param type    The command type.
     * @param details The command details.
     */
    public Command(Type type, String[] details) {
        assert type != null : "Command type should not be null";
        assert details != null : "Details should not be null";
        this.type = type;
        this.details = details;
    }

    /**
     * @return The command type.
     */
    public Type getType() {
        assert type != null : "Command type should not be null";
        return type;
    }

    /**
     * @return The command description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The command details array.
     */
    public String[] getDetails() {
        return details;
    }

    /**
     * @return The task index.
     */
    public int getTaskIndex() {
        assert taskIndex >= 0 : "Task index should not be negative";
        return taskIndex;
    }
}