package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to unmark a task as not completed in the task list.
 * Executes the unmarking of the specified task and saves the updated task list to storage.
 */
public class UnmarkCommand extends Command {
    private final String taskString;

    /**
     * Constructs an UnmarkCommand with the specified task identifier or description.
     *
     * @param taskString the identifier or description of the task to be unmarked
     */
    public UnmarkCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by unmarking the specified task as not completed in the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list containing the task to unmark
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if unmarking the task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.unmarkTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
