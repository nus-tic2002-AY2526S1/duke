package commands;

import exceptions.FileException;
import manager.FileManager;
import tasks.Task;
import ui.UserInteraction;

import java.util.ArrayList;

/**
 * Used to end the program
 */
public class ExitCommand extends Command {

    /**
     * Populate parameter
     *
     * @param tasklist task list to be saved to tasklist file
     * @throws FileException if there are any errors in saving the tasklist
     */
    protected ExitCommand(ArrayList<Task> tasklist) throws FileException {
        super(tasklist);

        FileManager.saveFile(tasklist); //ensure tasklist file updated before exit
    }

    /**
     * Execution of command.
     * Save tasklist to file and end program
     *
     */
    @Override
    public void execute() {
        UserInteraction.exit();
    }
}
