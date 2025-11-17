package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents an abstract base class for commands in the application.
 * Defines the structure for executing commands and determining if a command exits the program.
 */
public abstract class Command {
    /**
     * Executes the command using the provided task list, UI, and storage.
     *
     * @param tasks   the task list to be operated on
     * @param ui      the user interface component for interaction
     * @param storage the storage component for saving or loading tasks locally
     * @throws NeruneruneException if an error occurs during execution
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException;

    /**
     * Indicates whether this command causes the application to exit.
     * Default return as false.
     *
     * @return true if the command causes the program to exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}
