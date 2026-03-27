package kev.command;

import kev.task.TaskList;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

/**
 * an executable command issued by the user.
 * specific commands (e.g., list, add, mark, delete)
 * extend this class and implement the {@code execute} method.
 */
public abstract class Command {

    /**
     * executes the command using the given task list, UI, and storage handler.
     *
     * @param tasks   The list of tasks to operate on.
     * @param ui      The UI handling user interaction.
     * @param storage The storage handler used to save or load tasks.
     * @throws KevException If an error occurs during command execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws KevException;

    /**
     * indicates whether the command instructs the application to exit.
     *
     * @return {@code true} if this is an exit command; {@code false} otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
