package commands;

import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Deletes a task from the TaskList.
 */
public class DeleteCommand extends Command {

    private Integer index;

    public DeleteCommand(Integer index) {
        this.index = index;
    }

    /**
     * Shows the deletion message. Deletes the targetted index task. Saves new
     * list to storage.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        String delMsg = ui.showDeleteMessage(tasks.get(index).getTaskString(), tasks.size() - 1);
        tasks.deleteTask(index);
        saveToStorage(tasks, ui, storage);
        return delMsg;
    }
}
