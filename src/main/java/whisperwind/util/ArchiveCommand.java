package whisperwind.util;

/**
 * Represents different archive commands
 */
public enum ArchiveCommand {
    ARCHIVE_ALL("all", "Archive all tasks"),
    ARCHIVE_COMPLETED("completed", "Archive completed tasks"),
    ARCHIVE_TODO("todo", "Archive todo tasks"),
    ARCHIVE_DEADLINE("deadline", "Archive deadline tasks"),
    ARCHIVE_EVENT("event", "Archive event tasks"),
    LIST_ARCHIVES("list", "List archive files"),
    VIEW_ARCHIVE("view", "View archive contents"),
    UNKNOWN("unknown", "Unknown archive command");

    private final String command;
    private final String description;

    ArchiveCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() { return command; }
    public String getDescription() { return description; }

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

    // Assert enum completeness
    static {
        // Verify all archive operations are covered
        for (ArchiveCommand cmd : values()) {
            assert cmd != null : "All enum values should be non-null";
            assert cmd.command != null && !cmd.command.isEmpty() : "All commands should have non-empty string";
            assert cmd.description != null && !cmd.description.isEmpty() : "All commands should have description";
        }
    }
}