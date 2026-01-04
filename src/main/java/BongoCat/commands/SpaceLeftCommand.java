
package bongocat.commands;

import bongocat.ui.Ui;
import bongocat.storage.Storage;
import bongocat.tasks.TaskList;
/**
 * Returns no. of space left in arraylist
 */
public class SpaceLeftCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showSpaceLeft(tasks.spaceLeft());
    }
}
