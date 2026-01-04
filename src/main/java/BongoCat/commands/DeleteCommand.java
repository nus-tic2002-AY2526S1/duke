
package bongocat.commands;

import bongocat.tasks.*;
import bongocat.storage.Storage;
import bongocat.ui.Ui;
import bongocat.BongoException;

import java.io.IOException;

/**
 * Deletes task
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        Task removed = tasks.deleteTask(taskNumber);
        try { storage.save(tasks.getAllTasks()); } catch (IOException e) { }
        ui.showTaskDeleted(removed, tasks.size());
    }
}
