package commands;

import common.Priority;
import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Sets the priority of a task.
 */
public class SetPriorityCommand extends Command {

    private Integer index;
    private Priority priority;

    /**
     * Constructor for SetPriorityCommand.
     *
     * @param index of the task
     * @param priority to be set
     */
    public SetPriorityCommand(Integer index, Priority priority) {
        this.index = index;
        this.priority = priority;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.get(index).setPriority(priority);
        saveToStorage(tasks, ui, storage);
        return ui.showPriorityMessage(tasks.get(index).getTaskString());
    }
}
