
package bongocat.commands;

import bongocat.ui.Ui;
import bongocat.tasks.TaskList;
import bongocat.storage.Storage;
import bongocat.BongoException;
/**
 * Lists Tasks
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        if (tasks.isEmpty()) throw new BongoException("Nothing in list.");
        ui.showList(tasks);
    }
}
