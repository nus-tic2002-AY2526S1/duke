package command;

import storage.Storage;
import task.Task;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

public class DeleteCommand extends Command {
    private final int idx;

    public DeleteCommand(int idx) {
        this.idx = idx;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        if (idx < 0 || idx >= tasks.size())
            throw new DukeException("No such task number: " + (idx + 1));
        Task removed = tasks.remove(idx);
        ui.showLine();
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + removed);
        ui.showLine();
        storage.save(tasks.asList());
    }
}
