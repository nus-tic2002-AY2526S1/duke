package command;

import storage.Storage;
import task.Event;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

public class AddEventCommand extends Command {
    private final String desc, from, to;

    public AddEventCommand(String desc, String from, String to) {
        this.desc = desc;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        Event e = new Event(desc, from, to);
        tasks.add(e);
        ui.showLine();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + e);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
        storage.save(tasks.asList());
    }
}
