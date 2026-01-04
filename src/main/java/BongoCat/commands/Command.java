
package bongocat.commands;

import bongocat.storage.Storage;
import bongocat.tasks.TaskList;
import bongocat.ui.Ui;
import bongocat.BongoException;
/**
 * Main Command class
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException;
    public boolean isExit() { return false; }
}
