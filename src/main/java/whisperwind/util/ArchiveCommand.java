package whisperwind.util;

/**
 * Represents the different archive commands available in the system.
 * <p>
 * Each command has a string representation and a description explaining its functionality.
 * The enum provides methods to retrieve a command from a string input.
 * </p>
 */
public enum ArchiveCommand {

    /** Archive all tasks. */
    ARCHIVE_ALL("all", "Archive all tasks"),

    /** Archive only completed tasks. */
    ARCHIVE_COMPLETED("completed", "Archive completed tasks"),

    /** Archive only todo tasks. */
    ARCHIVE_TODO("todo", "Archive todo tasks"),

    /** Archive only deadline tasks. */
    ARCHIVE_DEADLINE("deadline", "Archive deadline tasks"),

    /** Archive only event tasks. */
    ARCHIVE_EVENT("event", "Archive event tasks"),

    /** List all archive files. */
    LIST_ARCHIVES("list", "List archive files"),

    /** View contents of a specific archive. */
    VIEW_ARCHIVE("view", "View archive contents"),

    /** Represents an unknown or invalid archive command. */
    UNKNOWN("unknown", "Unknown archive command");

    /** The string representation of the command. */
    private final String command;

    /** Description of the command. */
    private final String description;

    /**
     * Constructs an ArchiveCommand enum instance.
     *
     * @param command     the string representation of the command
     * @param description the description of the command
     */
    ArchiveCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    /**
     * Returns the string representation of the command.
     *
     * @return command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the description of the command.
     *
     * @return command description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converts a string input into the corresponding ArchiveCommand.
     * <p>
     * The input is normalized (trimmed and lowercased) and matched against available commands.
     * Supports special handling for "view" commands that may have additional parameters (e.g., "view 1").
     * </p>
     *
     * @param input the string input
     * @return the corresponding ArchiveCommand, or UNKNOWN if no match is found
     */
    public static ArchiveCommand fromString(String input) {
        // Assert input assumptions
        assert input != null : "Input should not be null in fromString";

        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String normalized = input.trim().toLowerCase();
        assert !normalized.isEmpty() : "Normalized input should not be empty";

        for (ArchiveCommand cmd : values()) {
            if (cmd.command.equals(normalized)) {
                // Assert valid command found
                assert cmd != UNKNOWN : "Should not return UNKNOWN for exact match";
                return cmd;
            }
        }

        // Handle view command with number (e.g., "view 1")
        if (normalized.startsWith("view")) {
            // Assert view command parsing
            assert VIEW_ARCHIVE != UNKNOWN : "VIEW_ARCHIVE should be valid";
            return VIEW_ARCHIVE;
        }

        // Final fallback
        assert true : "No matching archive command for: " + normalized;
        return UNKNOWN;
    }

    // Static initializer to verify enum completeness
    static {
        for (ArchiveCommand cmd : values()) {
            assert cmd != null : "All enum values should be non-null";
            assert cmd.command != null && !cmd.command.isEmpty() : "All commands should have non-empty string";
            assert cmd.description != null && !cmd.description.isEmpty() : "All commands should have description";
        }
    }
}
