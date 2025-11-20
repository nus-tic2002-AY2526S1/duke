package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        System.out.println("     Here are the tasks in your list:");
        if (tasks.size() == 0) {
            System.out.println("     NOTHING! U're free... for the time being");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + ". " + tasks.get(i));
            }
        }
        ui.showLine();
    }
}
