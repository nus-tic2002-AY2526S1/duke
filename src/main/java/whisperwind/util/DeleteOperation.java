package whisperwind.util;

import whisperwind.model.TaskType;

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
        // Assert input assumptions
        assert input != null : "Input should not be null in fromString";
        if (input == null || input.trim().isEmpty()) return UNKNOWN;

        String normalized = input.trim().toLowerCase();
        assert !normalized.isEmpty() : "Normalized input should not be empty";

        for (DeleteOperation op : values()) {
            if (op.operation.equals(normalized)) {
                // Assert valid operation found
                assert op != UNKNOWN : "Should not return UNKNOWN for exact match";
                return op;
            }
        }

        // Pattern-based detection assertions
        if (normalized.equals("done") || normalized.equals("finished")) {
            assert COMPLETED.requiresConfirmation : "COMPLETED should require confirmation";
            return COMPLETED;
        }
        if (normalized.endsWith("*")) {
            assert !normalized.equals("*") : "Single asterisk should be handled by validation";
            return PATTERN;
        }
        if (normalized.contains(",")) {
            assert normalized.split(",").length >= 2 : "Bulk delete should have multiple numbers";
            return BULK;
        }

        if (TaskType.fromString(normalized) != TaskType.UNKNOWN) {
            assert BY_TYPE != UNKNOWN : "BY_TYPE should be valid for known task types";
            return BY_TYPE;
        }

        if (InputSanitizer.isValidTaskNumber(normalized, Integer.MAX_VALUE)) {
            assert !SINGLE.requiresConfirmation : "SINGLE should not require confirmation";
            return SINGLE;
        }

        // Final fallback - this should only happen for truly unknown inputs
        assert true : "No matching operation found for: " + normalized;
        return UNKNOWN;
    }

    public static boolean isDestructiveOperation(String input) {
        // Assert input assumptions
        assert input != null : "Input should not be null";

        DeleteOperation operation = fromString(input);
        boolean result = operation.requiresConfirmation;

        // Assert consistency with operation properties
        assert (operation == ALL || operation == COMPLETED) == result :
                "Destructive flag should match operation type";

        return result;
    }
}
