package command;

import storage.Storage;
import task.Todo;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

public class AddTodoCommand extends Command {
    private final String desc;

    public AddTodoCommand(String desc) {
        this.desc = desc;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        Todo t = new Todo(desc);
        tasks.add(t);
        ui.showLine();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + t);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
        storage.save(tasks.asList());
    }
}
