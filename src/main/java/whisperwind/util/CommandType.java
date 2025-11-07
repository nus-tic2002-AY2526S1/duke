package whisperwind.util;

import whisperwind.model.*;

/**
 * Represents all available commands in the application.
 * Each command has a textual representation, an emoji, and a description.
 * <p>
 * This enum provides utility methods to convert strings to commands and validate inputs.
 * </p>
 */
public enum CommandType {

    /** View all tasks in the list */
    LIST("list", "🗒️", "View all tasks"),

    /** Mark a task as completed */
    MARK("mark", "✅", "Complete a task"),

    /** Mark a task as not completed */
    UNMARK("unmark", "🔄", "Uncomplete a task"),

    /** Remove tasks from the list */
    DELETE("delete", "🗑️", "Remove tasks"),

    /** Add a simple todo task */
    TODO("todo", "📝", "Add simple task"),

    /** Add a task with a deadline */
    DEADLINE("deadline", "⏰", "Add task with deadline"),

    /** Add an event with start and end times */
    EVENT("event", "🎉", "Add event with times"),

    /** Force save tasks to file */
    SAVE("save", "💾", "Force save tasks"),

    /** Show help instructions */
    VIEW_INSTRUCTION("view instruction", "❓", "Show help"),

    /** Exit the application */
    BYE("bye", "👋", "Exit application"),

    /** Unknown or invalid command */
    UNKNOWN("unknown", "❓", "Unknown command");

    /** The textual command */
    private final String command;

    /** The emoji associated with the command */
    private final String emoji;

    /** The description explaining what the command does */
    private final String description;

    /**
     * Constructs a CommandType with its properties.
     *
     * @param command     the textual command
     * @param emoji       the emoji representing the command
     * @param description a brief description of the command's function
     */
    CommandType(String command, String emoji, String description) {
        this.command = command;
        this.emoji = emoji;
        this.description = description;
    }

    /**
     * Returns the textual command.
     *
     * @return the command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the emoji associated with this command.
     *
     * @return the emoji string
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Returns the description of what this command does.
     *
     * @return the command description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converts a string input to a corresponding {@link CommandType}.
     * <p>
     * The input is normalized (trimmed and lowercased) before matching.
     * Special handling is included for "view instruction".
     * </p>
     *
     * @param input the command string to convert
     * @return the matching {@link CommandType}, or {@link #UNKNOWN} if no match
     */
    public static CommandType fromString(String input) {
        assert input != null : "Input should not be null in fromString";

        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String normalized = input.trim().toLowerCase();
        for (CommandType cmd : values()) {
            if (cmd.command.equals(normalized) ||
                    (cmd == VIEW_INSTRUCTION && normalized.equals("view instruction"))) {
                assert cmd != UNKNOWN : "Should not return UNKNOWN for valid command";
                return cmd;
            }
        }

        assert !normalized.isEmpty() : "Normalized input should not be empty";
        return UNKNOWN;
    }

    /**
     * Checks whether a string represents a valid command.
     *
     * @param input the string to check
     * @return true if the string corresponds to a known command, false otherwise
     */
    public static boolean isValidCommand(String input) {
        assert input != null : "Input should not be null in isValidCommand";

        CommandType result = fromString(input);

        assert (result == UNKNOWN) == !isValidCommandInternal(input) :
                "isValidCommand should match fromString result";

        return result != UNKNOWN;
    }

    /**
     * Internal helper method for validating commands.
     *
     * @param input the string to validate
     * @return true if the input corresponds to a known command, false otherwise
     */
    private static boolean isValidCommandInternal(String input) {
        assert input != null : "Input should not be null";
        return fromString(input) != UNKNOWN;
    }
}
