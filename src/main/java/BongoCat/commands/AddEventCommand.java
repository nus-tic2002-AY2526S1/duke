
package bongocat.commands;

import bongocat.tasks.*;
import bongocat.storage.Storage;
import bongocat.ui.Ui;
import bongocat.BongoException;
import java.io.IOException;
/**
 * Adds Event Task
 */
public class AddEventCommand extends Command {
    private final String name;
    private final String from;
    private final String to;

    public AddEventCommand(String name, String from, String to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        Task t = new Event(name, from, to);
        tasks.addTask(t);
        try { storage.save(tasks.getAllTasks()); } catch (IOException e) { }
        ui.showTaskAdded(t, tasks.size());
    }
}
