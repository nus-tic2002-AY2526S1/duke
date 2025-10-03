package message;

import command.CommandType;
import command.HelpCmd;
import common.CommandDocs;

import java.util.Objects;

/**
 * Generates formatted help documentation for available commands in the application.
 * <p>
 * This class provides two modes of help display:
 * <ul>
 *     <li><strong>Overview mode:</strong> Summary of all available commands with brief descriptions.
 *     Invoked by {@link HelpCmd} when user requests general help.</li>
 *     <li><strong>Detail mode:</strong> Comprehensive usage information for a specific command.
 *     Invoked by individual command classes when user types a command keyword without arguments.</li>
 * </ul>
 * <p>
 * The overview mode automatically excludes the HELP command itself to prevent recursion.
 * All help documentations are retrieved from {@link CommandDocs}.
 *
 * @see CommandDocs
 * @see CommandType
 */
public class HelpMessage implements Message {
    private final CommandType[] commands;
    private final CommandType cmd;

    /**
     * Creates a help message displaying an overview of all available commands.
     * <p>
     * The resulting help message will show a formatted list of all commands
     * (excluding HELP) with their brief descriptions. Commands are aligned in
     * columns for improved readability.
     *
     * @param commands array of all available command types to display
     */
    public HelpMessage(CommandType[] commands) {
        this.commands = commands;
        this.cmd = null;
    }

    /**
     * Creates a help message displaying detailed information for a specific command.
     * <p>
     * The resulting help message will show comprehensive documentation for the
     * specified command, including usage syntax, parameter descriptions, and examples.
     *
     * @param cmd the specific command type to show detailed help for
     */
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
        for (CommandType command : Objects.requireNonNull(commands)) {
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
