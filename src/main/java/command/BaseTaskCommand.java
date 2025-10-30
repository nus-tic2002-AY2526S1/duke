package command;

import java.util.Objects;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.HelpMessage;
import message.Message;

/**
 * Base class for commands that operate on tasks.
 * <p>
 * Provides common dependency injection for {@link TaskManager} access and command
 * arguments handling. This class implements a template method pattern where the
 * {@link #execute()} method defines the execution flow, while subclasses provide
 * specific implementations through {@link #executes()}.
 *
 * @see Command
 */
public abstract class BaseTaskCommand implements Command {
    protected final TaskManager taskManager;
    protected final String args;

    public BaseTaskCommand(TaskManager taskManager, String args) {
        this.taskManager = Objects.requireNonNull(taskManager, "TaskManager must not be null");
        this.args = args;
    }

    /**
     * Executes the command following a standardized validation and error handling flow.
     * <p>
     * This method ensures consistent behavior across all task commands by:
     * <ul>
     *     <li>Validating that arguments are provided</li>
     *     <li>Checking task list requirements via {@link #requiresNonEmptyList()}</li>
     *     <li>Delegating to subclass implementation via {@link #executes()}</li>
     *     <li>Converting exceptions to appropriate error messages</li>
     *
     * @return a {@link Message} representing the command result, which may be a
     *         {@link HelpMessage}, {@link ErrorMessage}, or subclass-specific message
     */
    @Override
    public final Message execute() {
        if (args == null || args.isBlank()) {
            return new HelpMessage(getCommandType());
        }

        if (requiresNonEmptyList() && taskManager.isEmpty()) {
            return new ErrorMessage(ErrorMessage.EMPTY_LIST);
        }

        try {
            return executes();
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }

    /**
     * Determines whether this command requires a non-empty task list to execute.
     * <p>
     * The default implementation returns {@code true}, which is appropriate for
     * most commands that operate on existing tasks. Subclasses that can operate
     * on empty lists (i.e., add commands) should override this to return {@code false}.
     *
     * @return {@code true} if the task list must be non-empty before execution,
     *         {@code false} otherwise
     */
    protected boolean requiresNonEmptyList() {
        return true;
    }

    /**
     * Implements the command-specific business logic.
     * <p>
     * This method is called by {@link #execute()} after all validation checks
     * have passed. Subclasses should implement their core functionality here
     * without worrying about common validation or error handling concerns.
     *
     * @return a {@link Message} representing the command's execution result
     * @throws MeeBotException if command execution fails due to invalid input
     *                         or business logic errors
     */
    protected abstract Message executes() throws MeeBotException;

    /**
     * Returns the command type that this command represents.
     * Subclasses must implement this to identify their specific command type
     * for help text generation and command routing.
     */
    protected abstract CommandType getCommandType();
}
