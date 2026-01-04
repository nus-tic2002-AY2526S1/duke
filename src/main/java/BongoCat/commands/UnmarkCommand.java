
package bongocat.commands;

import bongocat.tasks.*;
import bongocat.storage.Storage;
import bongocat.ui.Ui;
import bongocat.BongoException;

import java.io.IOException;
/**
 * Unmarks task
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        Task t = tasks.unmarkTask(taskNumber);
        try { storage.save(tasks.getAllTasks()); } catch (IOException e) {}
        ui.showTaskUnmarked(t);
    }
}
