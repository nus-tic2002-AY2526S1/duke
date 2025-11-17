package nerunerune.command;

import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to display the user guide or help message.
 * Executes the command by printing the user guide message via the UI.
 */
public class ViewAllCommand extends Command {
    /**
     * Executes the command by displaying the user guide message to the user.
     *
     * @param tasks   the task list
     * @param ui      the user interface to display the user guide message
     * @param storage the storage component
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printMessage(ui.getUserGuideMsg());
    }
}
