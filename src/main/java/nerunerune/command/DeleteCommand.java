package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to delete a task from the task list.
 * Executes the deletion of the specified task and saves the updated task list to storage.
 */
public class DeleteCommand extends Command {
    private final String taskString;

    /**
     * Constructs a DeleteCommand with the specified task identifier or description.
     *
     * @param taskString the identifier or description of the task to be deleted
     */
    public DeleteCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by deleting the specified task from the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list to delete the task from
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if deleting the task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.deleteTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
