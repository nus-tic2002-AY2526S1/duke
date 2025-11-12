package commands;

import static common.Messages.ERROR_ALREADY_UNMARKED;
import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Changes the status of a task to NOT completed.
 */
public class UnmarkCommand extends Command {

    private Integer index;

    public UnmarkCommand(Integer index) {
        this.index = index;
    }

    /**
     * Changes the status of a task to incomplete. Displays a message when
     * successfully unmarked. Displays an error if task was already incomplete.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.get(index).getStatus()) {
            tasks.get(index).setStatus(false);
            saveToStorage(tasks, ui, storage);
            return ui.showUnmarkMessage(tasks.get(index).getTaskString());
        } else {
            return ui.showErrorMessage(String.format(ERROR_ALREADY_UNMARKED, tasks.get(index).getDescription()));
        }
    }
}
