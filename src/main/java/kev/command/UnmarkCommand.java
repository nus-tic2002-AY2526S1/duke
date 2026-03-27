package kev.command;

import kev.task.TaskList;
import kev.task.Task;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

/**
 * command that marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private int index;

    /**
     * creates UnmarkCommand for a specific index.
     *
     * @param index Index of task to unmark.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * marks the task as not done and saves the task list.
     *
     * @throws KevException If the index is invalid or saving fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        if (index < 0 || index >= tasks.size()) {
            throw new KevException("Please review tasklist and provide a valid task index." +
                    "Use command list to view tasklist.");
        }

        Task task = tasks.get(index);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);

        try {
            storage.saveTasks(tasks.getAllTasks());
        } catch (Exception e) {
            throw new KevException("Error saving tasks: " + e.getMessage());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}