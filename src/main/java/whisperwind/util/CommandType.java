package whisperwind.util;

import whisperwind.model.*;


public enum CommandType {
    LIST("list", "🗒️", "View all tasks"),
    MARK("mark", "✅", "Complete a task"),
    UNMARK("unmark", "🔄", "Uncomplete a task"),
    DELETE("delete", "🗑️", "Remove tasks"),
    TODO("todo", "📝", "Add simple task"),
    DEADLINE("deadline", "⏰", "Add task with deadline"),
    EVENT("event", "🎉", "Add event with times"),
    SAVE("save", "💾", "Force save tasks"),
    VIEW_INSTRUCTION("view instruction", "❓", "Show help"),
    BYE("bye", "👋", "Exit application"),
    UNKNOWN("unknown", "❓", "Unknown command");

    private final String command;
    private final String emoji;
    private final String description;

    CommandType(String command, String emoji, String description) {
        this.command = command;
        this.emoji = emoji;
        this.description = description;
    }

    public String getCommand() { return command; }
    public String getEmoji() { return emoji; }
    public String getDescription() { return description; }

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

    public static boolean isValidCommand(String input) {
        return fromString(input) != UNKNOWN;
    }
}