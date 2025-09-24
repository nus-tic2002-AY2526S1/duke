package message;

import common.CommandDocs;
import command.CommandType;

/**
 * Generates a formatted help menu showing available commands and their descriptions.
 * Excludes HELP command to prevent recursion.
 */
public class HelpMessage implements Message {
    private final CommandType[] commands;
    private final CommandType cmd;

    // Overload constructor to show help overview
    public HelpMessage(CommandType[] commands) {
        this.commands = commands;
        this.cmd = null;
    }

    // Overload constructor to show targeted help text
    public HelpMessage(CommandType cmd) {
        this.commands = null;
        this.cmd = cmd;
    }

    @Override
    public String message() {
        if (cmd == null) {
            return overview();
        }
        return details(cmd);
    }

    private String overview() {
        StringBuilder content = new StringBuilder("Here's what mee can do for you:\n");
        String end = """
                Tip: Type the command keyword (e.g. event, delete) to get the detailed usage and examples.
                """;

        int maxKeywordLength = 0;
        for (CommandType command : commands) {
            maxKeywordLength = Math.max(maxKeywordLength, command.getKeyword().length());
        }
        for (CommandType command : commands) {
            // Skip display for help command
            if (command == CommandType.HELP) continue;
            String keyword = command.getKeyword();
            String helpText = CommandDocs.getSummary(command);
            content.append(String.format("• %-" + maxKeywordLength + "s : %s\n",
                    keyword, helpText));
        }
        content.append(end);
        return content.toString();
    }

    private String details(CommandType command) {
        String content = "\uD83C\uDD98 Unlike IKEA instructions, this help actually makes sense.\n";
        return content + CommandDocs.getDetails(command);
    }
}
