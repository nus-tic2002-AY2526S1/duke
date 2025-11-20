package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        System.out.println("      Bye! And don't come again!");
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
