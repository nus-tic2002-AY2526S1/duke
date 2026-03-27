package kev.command;

import kev.task.TaskList;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

/**
 * command to handle the listing/display of all existing tasks stored in the tasklist.
 */
public class ListCommand extends Command {

    /**
     * lists/displays all tasks in the tasklist.
     *
     * @throws KevException not used here but required by method signature.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        ui.showLine();
        if (tasks.size() == 0) {
            ui.showMessage("YAY! You have no tasks in your list!");
        } else {
            ui.showMessage("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                ui.showMessage((i + 1) + ". " + tasks.getAll().get(i));
            }
        }
        ui.showLine();
    }
}
