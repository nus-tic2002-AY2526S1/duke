package whisperwind.util;

import whisperwind.model.TaskType;

/**
 * Represents different types of delete operations that can be performed on tasks.
 * <p>
 * Each operation has a textual name, description, emoji representation, and a flag
 * indicating whether confirmation is required before performing the action.
 * </p>
 */
public enum DeleteOperation {

    /** Delete a single task (no confirmation required) */
    SINGLE("single", "Delete single task", "🗑️", false),

    /** Delete multiple tasks in bulk (no destructive confirmation) */
    BULK("bulk", "Delete multiple tasks", "🗑️🗑️", false),

    /** Delete all completed tasks (requires confirmation) */
    COMPLETED("completed", "Delete completed tasks", "✅🗑️", true),

    /** Delete all tasks (destructive, requires confirmation) */
    ALL("all", "Delete all tasks", "🧹", true),

    /** Delete tasks matching a specific pattern (no confirmation required) */
    PATTERN("pattern", "Delete by pattern", "🔍🗑️", false),

    /** Delete tasks by their type (no confirmation required) */
    BY_TYPE("type", "Delete by task type", "📁🗑️", false),

    /** Unknown or invalid delete operation */
    UNKNOWN("unknown", "Unknown operation", "❓", false);

    /** Textual representation of the delete operation */
    private final String operation;

    /** Description of what the operation does */
    private final String description;

    /** Emoji representing the operation */
    private final String emoji;

    /** Whether the operation requires confirmation */
    private final boolean isConfirmationRequired;

    /**
     * Constructs a delete operation with properties.
     *
     * @param operation            textual operation name
     * @param description          description of the operation
     * @param emoji                emoji representing the operation
     * @param isConfirmationRequired whether confirmation is required
     */
    DeleteOperation(String operation, String description, String emoji, boolean isConfirmationRequired) {
        this.operation = operation;
        this.description = description;
        this.emoji = emoji;
        this.isConfirmationRequired = isConfirmationRequired;
    }

    /**
     * @return the textual name of the delete operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @return description of the delete operation
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return emoji representing the delete operation
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * @return true if the operation requires confirmation, false otherwise
     */
    public boolean isConfirmationRequired() {
        return isConfirmationRequired;
    }

    /**
     * Converts a string input to a corresponding {@link DeleteOperation}.
     * Handles both exact matches and pattern-based detection.
     *
     * @param input the input string representing the delete operation
     * @return the corresponding DeleteOperation, or {@link #UNKNOWN} if no match
     */
    public static DeleteOperation fromString(String input) {
        assert input != null : "Input should not be null in fromString";
        if (input == null || input.trim().isEmpty()) return UNKNOWN;

        String normalized = input.trim().toLowerCase();
        assert !normalized.isEmpty() : "Normalized input should not be empty";

        for (DeleteOperation op : values()) {
            if (op.operation.equals(normalized)) {
                assert op != UNKNOWN : "Should not return UNKNOWN for exact match";
                return op;
            }
        }

        // Pattern-based detection
        if (normalized.equals("done") || normalized.equals("finished")) {
            assert COMPLETED.isConfirmationRequired : "COMPLETED should require confirmation";
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
            assert !SINGLE.isConfirmationRequired : "SINGLE should not require confirmation";
            return SINGLE;
        }

        // Fallback
        assert true : "No matching operation found for: " + normalized;
        return UNKNOWN;
    }

    /**
     * Determines if a given delete operation is considered destructive
     * and requires confirmation.
     *
     * @param input the input string representing the delete operation
     * @return true if the operation is destructive, false otherwise
     */
    public static boolean isDestructiveOperation(String input) {
        assert input != null : "Input should not be null";

        DeleteOperation operation = fromString(input);
        boolean result = operation.isConfirmationRequired;

        assert (operation == ALL || operation == COMPLETED) == result :
                "Destructive flag should match operation type";

        return result;
    }
}
