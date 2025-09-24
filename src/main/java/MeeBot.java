import command.Command;
import manager.TaskManager;
import common.ErrorMessage;
import message.Message;
import storage.Storage;
import ui.UserInterface;
import command.CommandProcessor;

/**
 * Entry point of MeeBot application.
 * Initializes the application and starts the interaction with the user.
 */
public class MeeBot {

    private final UserInterface ui = new UserInterface();
    private final TaskManager tm = new TaskManager();
    private final Storage storage = new Storage(tm);

    public void run() {
        storage.loadTasks();
        ui.displayWelcome();
        CommandProcessor processor = new CommandProcessor(tm);

        while (true) {
            String input = ui.readUserInput();
            Command cmd = processor.parseCommand(input);
            try {
                Message msg = cmd.execute();
                ui.displayMessage(msg);
                storage.saveTasks();

                if (cmd.isExit()) {
                    break;
                }
            } catch (RuntimeException e) {
                ui.displayMessage(new ErrorMessage("Mee-stakes happen lah, try again!"));
            }
        }
    }

    public static void main(String[] args) {
        MeeBot mb = new MeeBot();
        mb.run();
    }
}
