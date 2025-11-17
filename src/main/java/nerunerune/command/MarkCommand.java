package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to mark a task as completed in the task list.
 * Executes the marking of the specified task and saves the updated task list to storage.
 */
public class MarkCommand extends Command {
    private final String taskString;

    /**
     * Constructs a MarkCommand with the specified task identifier or description.
     *
     * @param taskString the identifier or description of the task to be marked as done
     */
    public MarkCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by marking the specified task as completed in the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list containing the task to mark
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if marking the task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.markTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
