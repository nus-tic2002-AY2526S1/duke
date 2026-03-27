package kev.command;

import kev.task.TaskList;
import kev.task.Task;
import kev.storage.Storage;
import kev.ui.Ui;
import kev.exception.KevException;
import java.io.IOException;

/**
 * command to mark a task as done.
 */
public class MarkCommand extends Command {
    private int index;

    /**
     * creates a MarkCommand with the user input/target index.
     *
     * @param index Index of task to mark done.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * marks task as completed and saves the updated task list.
     *
     * @param tasks Task list to retrieve the target index/task from
     * @param ui UI for user interaction
     * @param storage Storage to save the task status
     * @throws KevException If index is invalid or saving fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        if (index < 0 || index >= tasks.size()) {
            throw new KevException("Please review tasklist and provide a valid task index. " +
                    "Use command list to view tasklist.");
        }

        Task task = tasks.get(index);
        task.markAsDone();
        ui.showTaskMarked(task);

        try {
            storage.saveTasks(tasks.getAll());
        } catch (IOException e) {
            throw new KevException("Error saving tasks: " + e.getMessage());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}