package command;

import message.GoodbyeMessage;
import message.Message;

/**
 * Command to terminate the application session with goodbye message.
 */
public class ExitCmd implements Command {

    @Override
    public Message execute() {
        return new GoodbyeMessage();
    }

    /**
     * Signals that the application should terminate after this command.
     *
     * @return true to indicate application isExit
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
