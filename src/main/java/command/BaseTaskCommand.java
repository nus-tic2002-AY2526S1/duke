package command;

import manager.TaskManager;
import message.HelpMessage;
import message.Message;

/**
 * Base class for commands that operate on tasks.
 * <p>
 * Provides common dependency injection for {@link TaskManager} access and command
 * arguments handling. Subclasses should focus on implementing the specific command
 * logic in their {@link #execute()} method.
 *
 * @see Command
 */
public abstract class BaseTaskCommand implements Command {
    protected final TaskManager taskManager;
    protected final String args;

    public BaseTaskCommand(TaskManager taskManager, String args) {
        this.taskManager = taskManager;
        this.args = args;
    }

    /**
     * Checks if help text should be displayed based on the command arguments.
     * <p>
     * Returns help information when the user provides no arguments after command
     * keyword, indicating they want to know how to use the command.
     *
     * @param cmdType the command type to generate help text for
     * @return {@link HelpMessage} if help should be shown, or {@code null} if
     *         the command should proceed with normal execution
     * @see HelpMessage
     * @see CommandType
     */
    protected Message showHelpText(CommandType cmdType) {
        if (args == null || args.isBlank()) {
            return new HelpMessage(cmdType);
        }
        return null;
    }
}
