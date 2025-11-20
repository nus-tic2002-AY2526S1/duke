package command;

import storage.Storage;
import task.Deadline;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

public class AddDeadlineCommand extends Command {
    private final String desc, by;

    public AddDeadlineCommand(String desc, String by) {
        this.desc = desc;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        Deadline d = new Deadline(desc, by);
        tasks.add(d);
        ui.showLine();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + d);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
        storage.save(tasks.asList());
    }
}
