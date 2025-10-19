package whisperwind.util;

import whisperwind.model.*;

/**
 * Represents different levels of confirmation required for operations.
 * Higher severity levels require more explicit user confirmation.
 */
public enum ConfirmationLevel {
    /** No confirmation needed for the operation */
    NONE("none", "No confirmation needed", 0),

    /** Simple yes/no confirmation required */
    SIMPLE("simple", "Simple yes/no confirmation", 1),

    /** Strong confirmation with explicit agreement required */
    STRONG("strong", "Strong confirmation required", 2),

    /** Destructive action requiring exact phrase confirmation */
    DESTRUCTIVE("destructive", "Destructive action - type exact phrase", 3);

    private final String level;
    private final String description;
    private final int severity;

    /**
     * Creates a confirmation level with its properties.
     * @param level the level name
     * @param description what this level means
     * @param severity the severity value (higher = more strict)
     */
    ConfirmationLevel(String level, String description, int severity) {
        this.level = level;
        this.description = description;
        this.severity = severity;
    }

    /**
     * @return the level name
     */
    public String getLevel() { return level; }

    /**
     * @return the description of this confirmation level
     */
    public String getDescription() { return description; }

    /**
     * @return the severity value (higher = more strict)
     */
    public int getSeverity() { return severity; }

    /**
     * Determines the confirmation level needed for a delete operation.
     * @param operation the delete operation to check
     * @return the appropriate confirmation level
     */
    public static ConfirmationLevel forOperation(DeleteOperation operation) {
        // Assert input assumptions
        assert operation != null : "DeleteOperation should not be null";
        switch (operation) {
            case ALL:
                // Assert destructive operation mapping
                assert operation.requiresConfirmation() : "ALL operation should require confirmation";
                return DESTRUCTIVE;
            case COMPLETED:
            case PATTERN:
            case BULK:
                // Assert strong confirmation mapping
                assert operation != DeleteOperation.SINGLE : "Non-single operations should have higher confirmation";
                return STRONG;
            case BY_TYPE:
                return SIMPLE;
            case SINGLE:
            default:
                // Assert default case assumptions
                assert operation == DeleteOperation.SINGLE || operation == DeleteOperation.UNKNOWN :
                        "Default case should only handle SINGLE or UNKNOWN";
                return NONE;
        }
    }
    // Assert enum ordering consistency
    static {
        // Verify severity ordering
        assert NONE.severity < SIMPLE.severity : "NONE should have lowest severity";
        assert SIMPLE.severity < STRONG.severity : "SIMPLE should have medium severity";
        assert STRONG.severity < DESTRUCTIVE.severity : "DESTRUCTIVE should have highest severity";
    }
}