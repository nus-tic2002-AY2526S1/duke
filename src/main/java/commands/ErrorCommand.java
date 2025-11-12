package commands;

import static common.Messages.ERROR_OUT_OF_BOUNDS;
import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Displays an error message.
 */
public class ErrorCommand extends Command {

    private final String description;

    public ErrorCommand(String description) {
        this.description = description;
    }

    /**
     * Displays an error message.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        if (description.equals(ERROR_OUT_OF_BOUNDS)) {
            return ui.showErrorMessage(String.format(description, tasks.size()));
        } else {
            return ui.showErrorMessage(description);
        }
    }
}
