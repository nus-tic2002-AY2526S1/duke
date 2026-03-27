package kev.command;

import kev.task.Task;
import kev.task.TaskList;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import java.io.IOException;

/**
 * command to delete tasks from the task list.
 */
public class DeleteCommand extends Command {
    private int index;

    /**
     * creates an DeleteCommand with the user input/target index.
     * @param index The index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command: verifies that index is valid, deteles the task,
     * displays confirmation and saves updated task list to storage.
     *
     * @param tasks Task list to remove the task from
     * @param ui UI for user interaction
     * @param storage Storage to delete the task
     * @throws KevException if index is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        if (index < 0 || index >= tasks.size()) {
            throw new KevException("Please review tasklist and provide a valid task index.");
        }

        Task removed = tasks.deleteTask(index);
        ui.showTaskDeleted(removed, tasks.size());
        try {
            storage.saveTasks(tasks.getAllTasks());
        } catch (IOException e) {
            throw new KevException("Error saving tasks: " + e.getMessage());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
