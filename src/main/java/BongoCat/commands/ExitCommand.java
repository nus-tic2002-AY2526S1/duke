
package bongocat.commands;

import bongocat.ui.Ui;
import bongocat.tasks.TaskList;
import bongocat.storage.Storage;

/**
 * Exits BongoCat
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }
    @Override
    public boolean isExit() { return true; }
}
