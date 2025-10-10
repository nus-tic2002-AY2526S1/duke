public enum TaskType {
    TODO("T", "Todo", "📝", "[T]", "Simple task without time"),
    DEADLINE("D", "Deadline", "⏰", "[D]", "Task with due date"),
    EVENT("E", "Event", "🎉", "[E]", "Event with start and end times"),
    UNKNOWN("U", "Unknown", "❓", "[?]", "Unknown task type");

    private final String code;
    private final String displayName;
    private final String emoji;
    private final String prefix;
    private final String description;

    TaskType(String code, String displayName, String emoji, String prefix, String description) {
        this.code = code;
        this.displayName = displayName;
        this.emoji = emoji;
        this.prefix = prefix;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public String getEmoji() { return emoji; }
    public String getPrefix() { return prefix; }
    public String getDescription() { return description; }

    public static TaskType fromCode(String code) {
        if (code == null) return UNKNOWN;
        for (TaskType type : values()) {
            if (type.code.equals(code.toUpperCase())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static TaskType fromClass(Class<? extends Task> taskClass) {
        if (taskClass == Todo.class) return TODO;
        if (taskClass == Deadline.class) return DEADLINE;
        if (taskClass == Event.class) return EVENT;
        return UNKNOWN;
    }

    public static TaskType fromString(String input) {
        if (input == null) return UNKNOWN;
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