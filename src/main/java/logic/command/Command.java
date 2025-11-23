package logic.command;

import common.ErrorMessage;
import common.message.Message;

/**
 * Interface for executable user commands. All commands follow the Command pattern,
 * encapsulating a request as an object and providing a uniform interface for execution.
 * <p>
 * <b>Implementing classes should:</b>
 * <ul>
 *     <li>Process user input and validate parameters</li>
 *     <li>Perform the requested operation</li>
 *     <li>Return a user-friendly {@link Message} describing the result</li>
 *     <li>Handle errors gracefully by returning {@link ErrorMessage} instances</li>
 */
public interface Command {
    Message execute();

    /**
     * Indicates whether this command should terminate the application session.
     *
     * @return {@code false} by default. Only {@link ExitCmd} should return {@code true}.
     */
    default boolean isExit() {
        return false;
    }
}
