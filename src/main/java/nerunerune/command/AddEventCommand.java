package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to add an event task to the task list.
 * Executes the addition of the event task and saves the updated task list to storage.
 */
public class AddEventCommand extends Command {
    private final String taskString;

    /**
     * Constructs an AddEventCommand with the specified event description.
     *
     * @param taskString the description of the event task to be added
     */
    public AddEventCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by adding an event task to the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list to add the event task
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if adding the event task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.addEvent(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
