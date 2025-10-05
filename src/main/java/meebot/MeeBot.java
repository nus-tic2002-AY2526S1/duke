package meebot;

import java.util.logging.Level;
import java.util.logging.Logger;

import command.Command;
import common.ErrorMessage;
import manager.TaskManager;
import message.GoodbyeMessage;
import message.Message;
import parser.commandargs.CommandProcessor;
import storage.Storage;
import ui.cli.UserInterface;

/**
 * Entry point of MeeBot application.
 * Initializes the application and starts the interaction with the user.
 */
public class MeeBot {

    private final UserInterface ui = new UserInterface();
    private final TaskManager tm = new TaskManager();
    private final Storage storage = new Storage(tm);
    private final CommandProcessor processor = new CommandProcessor(tm);

    public MeeBot() {
        Logger.getLogger("com.zoho.hawking").setLevel(Level.OFF);
        Logger.getLogger("com.zoho.hawking.language").setLevel(Level.OFF);
        storage.loadTasks();
    }

    // CLI entry point
    public void run() {
        ui.displayWelcome();

        while (true) {
            String input = ui.readUserInput();
            String response = getResponse(input);
            ui.displayMessage(() -> response);
            if (input.trim().equalsIgnoreCase("bye")) {
                break;
            }
        }
    }

    public String getResponse(String input) {
        try {
            Command cmd = processor.parseCommand(input);
            Message msg = cmd.execute();
            storage.saveTasks();

            if (cmd.isExit()) {
                ui.displayMessage(new GoodbyeMessage());
            }
            return msg.message();
        } catch (RuntimeException e) {
            ErrorMessage err = new ErrorMessage("Mee-stakes happen lah, try again!");
            return err.message();
        }
    }

    public static void main(String[] args) {
        MeeBot mb = new MeeBot();
        mb.run();
    }
}
