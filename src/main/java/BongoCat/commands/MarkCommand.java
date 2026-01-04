
package bongocat.commands;

import bongocat.tasks.*;
import bongocat.storage.Storage;
import bongocat.ui.Ui;
import bongocat.BongoException;

import java.io.IOException;
/**
 * Marks Tasks
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        Task t = tasks.markTask(taskNumber);
        try { storage.save(tasks.getAllTasks()); } catch (IOException e) {}
        ui.showTaskMarked(t);
    }
}
