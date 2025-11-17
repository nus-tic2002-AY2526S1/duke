package nerunerune.command;

import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to exit the application.
 * Executes the exit process by displaying the end message and signals the program to terminate.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command by displaying the end message to the user.
     *
     * @param tasks   the task list
     * @param ui      the user interface to display the end message
     * @param storage the storage component
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.endMsg();
    }

    /**
     * Indicates that this command causes the application to exit.
     *
     * @return true always, signaling the program should terminate
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
