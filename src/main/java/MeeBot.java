import command.Command;
import manager.TaskManager;
import common.ErrorMessage;
import message.Message;
import storage.Storage;
import ui.UserInterface;
import parser.commandargs.CommandProcessor;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        Logger.getLogger("com.zoho.hawking").setLevel(Level.OFF);
        Logger.getLogger("com.zoho.hawking.language").setLevel(Level.OFF);
        MeeBot mb = new MeeBot();
        mb.run();
    }
}
