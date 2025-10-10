public enum DeleteOperation {
    SINGLE("single", "Delete single task", "🗑️", false),
    BULK("bulk", "Delete multiple tasks", "🗑️🗑️", false),
    COMPLETED("completed", "Delete completed tasks", "✅🗑️", true),
    ALL("all", "Delete all tasks", "🧹", true),
    PATTERN("pattern", "Delete by pattern", "🔍🗑️", false),
    BY_TYPE("type", "Delete by task type", "📁🗑️", false),
    UNKNOWN("unknown", "Unknown operation", "❓", false);

    private final String operation;
    private final String description;
    private final String emoji;
    private final boolean requiresConfirmation;

    DeleteOperation(String operation, String description, String emoji, boolean requiresConfirmation) {
        this.operation = operation;
        this.description = description;
        this.emoji = emoji;
        this.requiresConfirmation = requiresConfirmation;
    }

    public String getOperation() { return operation; }
    public String getDescription() { return description; }
    public String getEmoji() { return emoji; }
    public boolean requiresConfirmation() { return requiresConfirmation; }

    public static DeleteOperation fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String normalized = input.trim().toLowerCase();

        for (DeleteOperation op : values()) {
            if (op.operation.equals(normalized)) {
                return op;
            }
        }

        if (normalized.equals("done")) return COMPLETED;
        if (normalized.equals("finished")) return COMPLETED;
        if (normalized.endsWith("*")) return PATTERN;
        if (normalized.contains(",")) return BULK;

        if (TaskType.fromString(normalized) != TaskType.UNKNOWN) {
            return BY_TYPE;
        }

        if (InputSanitizer.isValidTaskNumber(normalized, Integer.MAX_VALUE)) {
            return SINGLE;
        }

        return UNKNOWN;
    }

    public static boolean isDestructiveOperation(String input) {
        DeleteOperation op = fromString(input);
        return op.requiresConfirmation;
    }
}