package whisperwind.model;

/**
 * Represents the different types of tasks supported by the application.
 * Each task type has a unique code, display name, emoji, prefix, and description.
 *
 * @author Your Name
 * @version 1.0
 */
public enum TaskType {
    /** Simple task without any date or time constraints */
    TODO("T", "Todo", "📝", "[T]", "Simple task without time"),

    /** Task with a specific due date or deadline */
    DEADLINE("D", "Deadline", "⏰", "[D]", "Task with due date"),

    /** Event with specific start and end times */
    EVENT("E", "Event", "🎉", "[E]", "Event with start and end times"),

    /** Unknown or unrecognized task type */
    UNKNOWN("U", "Unknown", "❓", "[?]", "Unknown task type");

    private final String code;
    private final String displayName;
    private final String emoji;
    private final String prefix;
    private final String description;

    /**
     * Constructs a TaskType with all its associated metadata.
     *
     * @param code the single-character code used for serialization
     * @param displayName the human-readable name of the task type
     * @param emoji the emoji representation of the task type
     * @param prefix the prefix used in string representations
     * @param description a detailed description of the task type
     */
    TaskType(String code, String displayName, String emoji, String prefix, String description) {
        this.code = code;
        this.displayName = displayName;
        this.emoji = emoji;
        this.prefix = prefix;
        this.description = description;
    }

    /**
     * Returns the single-character code for this task type.
     * Used for serialization and storage.
     *
     * @return the task type code
     */
    public String getCode() { return code; }

    /**
     * Returns the human-readable display name of this task type.
     *
     * @return the display name
     */
    public String getDisplayName() { return displayName; }

    /**
     * Returns the emoji representation of this task type.
     * Used for visual enhancement in user interfaces.
     *
     * @return the emoji string
     */
    public String getEmoji() { return emoji; }

    /**
     * Returns the prefix used in string representations of this task type.
     * Typically used in formatted output like "[T]" for Todo tasks.
     *
     * @return the prefix string
     */
    public String getPrefix() { return prefix; }

    /**
     * Returns a detailed description of this task type.
     * Explains the purpose and characteristics of the task type.
     *
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * Converts a string code to its corresponding TaskType.
     * Case-insensitive matching is performed. Returns UNKNOWN if no match is found.
     *
     * @param code the code to convert (e.g., "T", "D", "E")
     * @return the corresponding TaskType, or UNKNOWN if no match found
     * @see #UNKNOWN
     */
    public static TaskType fromCode(String code) {
        // Assert input assumptions
        assert code != null : "Code should not be null in fromCode";

        for (TaskType type : values()) {
            if (type.code.equals(code.toUpperCase())) {
                // Assert valid result
                assert type != UNKNOWN : "Should not return UNKNOWN for valid code";
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Determines the TaskType from a Task class.
     * Maps concrete Task subclasses to their corresponding TaskType.
     *
     * @param taskClass the class of the task object
     * @return the corresponding TaskType, or UNKNOWN if no match found
     * @see #UNKNOWN
     */
    public static TaskType fromClass(Class<? extends Task> taskClass) {
        // Assert input assumptions
        assert taskClass != null : "Task class should not be null";

        if (taskClass == Todo.class) return TODO;
        if (taskClass == Deadline.class) return DEADLINE;
        if (taskClass == Event.class) return EVENT;

        // This should only happen for unknown task classes
        assert false : "Unknown task class: " + taskClass.getName();
        return UNKNOWN;
    }

    /**
     * Converts a string input to its corresponding TaskType.
     * Attempts to match against display names and enum names in a case-insensitive manner.
     * Returns UNKNOWN if no match is found.
     *
     * @param input the input string to convert (e.g., "todo", "DEADLINE", "event")
     * @return the corresponding TaskType, or UNKNOWN if no match found
     * @see #UNKNOWN
     */
    public static TaskType fromString(String input) {
        // Assert input assumptions
        assert input != null : "Input should not be null";

        String normalized = input.trim().toLowerCase();
        for (TaskType type : values()) {
            if (type.displayName.toLowerCase().equals(normalized) ||
                    type.name().toLowerCase().equals(normalized)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}