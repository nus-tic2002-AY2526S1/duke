package nerunerune.command;

import java.util.ArrayList;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.task.Task;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to find tasks containing a specific keyword in their description.
 * Searches through the task list and displays all matching tasks to the user.
 */
public class FindCommand extends Command {
    private final String taskString;

    /**
     * Constructs a FindCommand with the specified search keyword.
     *
     * @param taskString The keyword to search for in task descriptions.
     */
    public FindCommand(String taskString) {
        this.taskString = taskString;
    }

    /**
     * Executes the find command by searching for tasks containing the keyword
     * and displaying the results through the UI.
     *
     * @param tasks   The task list to search through.
     * @param ui      The user interface handler for displaying results.
     * @param storage The storage handler (not used in this command).
     * @throws NeruneruneException If an error occurs during execution.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        ArrayList<Task> foundResult = tasks.filterTasksByKeyword(taskString);
        ui.showMatchingTasks(foundResult);
    }
}
