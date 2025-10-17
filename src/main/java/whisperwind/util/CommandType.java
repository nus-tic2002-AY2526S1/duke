package whisperwind.util;

import whisperwind.model.*;

/**
 * Represents all available commands in the application.
 * Each command has a text command, emoji, and description.
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

    private final String command;
    private final String emoji;
    private final String description;

    /**
     * Creates a command type with its properties.
     * @param command the text command
     * @param emoji the emoji representation
     * @param description what the command does
     */
    CommandType(String command, String emoji, String description) {
        this.command = command;
        this.emoji = emoji;
        this.description = description;
    }

    /**
     * @return the text command
     */
    public String getCommand() { return command; }

    /**
     * @return the emoji for this command
     */
    public String getEmoji() { return emoji; }

    /**
     * @return the description of what this command does
     */
    public String getDescription() { return description; }

    /**
     * Converts a string input to a CommandType.
     * @param input the command string to convert
     * @return the matching CommandType, or UNKNOWN if no match
     */
    public static CommandType fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String normalized = input.trim().toLowerCase();
        for (CommandType cmd : values()) {
            if (cmd.command.equals(normalized) ||
                    (cmd == VIEW_INSTRUCTION && normalized.equals("view instruction"))) {
                return cmd;
            }
        }
        return UNKNOWN;
    }

    /**
     * Checks if a string is a valid command.
     * @param input the command string to check
     * @return true if the command is valid, false otherwise
     */
    public static boolean isValidCommand(String input) {
        return fromString(input) != UNKNOWN;
    }
}