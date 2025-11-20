package sy;

import parser.Parser;
import command.Command;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

/**
 * {@code Sy} class is the main entry point of the chatbot
 * application.
 * initializes core components (UI, Storage, TaskList,
 * and Parser),
 * running the main interaction loop, handling user input, and executing
 * commands.
 * 
 * Usage: Run this class to start the chatbot:
 * {@code java sy.Sy}
 */
public class Sy {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Sy() {
        ui = new Ui();
        storage = new Storage();
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand == null) {
                    ui.showGoodbye();
                    break;
                }
                fullCommand = fullCommand.trim();
                if (fullCommand.isEmpty()) {
                    ui.showLine();
                    continue;
                }

                ui.showLine();
                Command c = Parser.parse(fullCommand); // may throw DukeException
                c.execute(tasks, ui, storage); // may throw DukeException
                isExit = c.isExit();

            } catch (DukeException e) {
                ui.showError(e.getMessage());

            } catch (Exception e) {
                ui.showError("Oops, something went wrong: " + e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts Duke application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new Sy().run();
    }
}
