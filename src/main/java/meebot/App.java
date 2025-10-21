package meebot;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import manager.TaskManager;
import storage.Storage;
import ui.cli.CliRunner;
import ui.cli.UserInterface;
import ui.gui.Main;

/**
 * Main application entry point for the MeeBot task management system.
 * <p>
 * This class initializes the core components and routes execution to either
 * a CLI or GUI based on the provided command-line arguments.
 * <p>
 * The application sets up the task manager, storage layer, and bot controller
 * before launching the appropriate user interface.
 *
 */
public class App {
    public static void main(String[] args) {
        Logger.getLogger("com.zoho.hawking").setLevel(Level.OFF);
        Logger.getLogger("com.zoho.hawking.language").setLevel(Level.OFF);

        // Core wiring
        TaskManager tm = new TaskManager();
        Storage storage = new Storage(tm);
        MeeBot meebot = new MeeBot(tm, storage);
        storage.loadTasks();

        // CLI GUI routing
        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            new CliRunner(meebot, new UserInterface()).run();
        } else {
            Main.setMeeBot(meebot);
            Application.launch(Main.class, args);
        }
    }
}
