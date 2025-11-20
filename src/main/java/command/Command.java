package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

/**
 * Represents an abstract user command in the Duke application.
 * 
 * Each {@code Command} subclass encapsulates a specific user action
 * (e.g. adding a task, listing tasks, marking as done, exiting, etc.).
 * This class defines the shared interface that all commands must follow.
 * 
 * Typical usage example:
 * 
 * {@code Command c = Parser.parse("deadline return book /by 2025-10-23");
 * c.execute(tasks, ui, storage);
 * if (c.isExit()) {
 *     // exit the application
 * }
 * 
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException;

    public boolean isExit() {
        return false;
    }
}
