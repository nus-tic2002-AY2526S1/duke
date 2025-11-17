package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to add a deadline task to the task list.
 * Executes the addition of the deadline task and saves the updated task list to storage.
 */
public class AddDeadlineCommand extends Command {
    private final String taskString;

    /**
     * Constructs an AddDeadlineCommand with the specified task description.
     *
     * @param taskString the description of the deadline task to be added
     */
    public AddDeadlineCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by adding a deadline task to the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list to add the deadline task
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if adding the deadline task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.addDeadline(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
