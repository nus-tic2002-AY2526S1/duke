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
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String normalized = input.trim().toLowerCase();
        for (ArchiveCommand cmd : values()) {
            if (cmd.command.equals(normalized)) {
                return cmd;
            }
        }

        // Handle view command with number (e.g., "view 1")
        if (normalized.startsWith("view")) {
            return VIEW_ARCHIVE;
        }

        return UNKNOWN;
    }
}