package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to add a todo task to the task list.
 * Executes the addition of the todo task and saves the updated task list to storage.
 */
public class AddTodoCommand extends Command {
    private final String taskString;

    /**
     * Constructs an AddTodoCommand with the specified todo description.
     *
     * @param taskString the description of the todo task to be added
     */
    public AddTodoCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the command by adding a todo task to the given task list,
     * then save the updated list to storage.
     *
     * @param tasks   the task list to add the todo task
     * @param ui      the user interface for interaction
     * @param storage the storage instance for saving tasks locally
     * @throws NeruneruneException if adding the todo task fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.addTodo(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
