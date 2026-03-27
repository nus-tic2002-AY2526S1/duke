package kev.command;

import kev.task.TaskList;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

/**
 * command to exit the chatbot/application.
 */
public class ExitCommand extends Command {
    /**
     * displays goodbye message. No task modifications occur.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        ui.showMessage("Bye :( Hope to see you again soon!");
    }

    /**
     * @return true, because this command ends the program.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
