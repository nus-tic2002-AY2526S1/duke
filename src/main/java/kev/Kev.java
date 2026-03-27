package kev;

import kev.storage.Storage;
import kev.ui.Ui;
import kev.task.TaskList;
import kev.command.Command;
import kev.exception.KevException;

/**
 * main Kev application class responsible for initializing
 * and running the application.
 */
public class Kev {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private static final String DATA_PATH = "data/duke.txt";

    /**
     * creates a new instance of the Kev application.
     * initializes the UI, storage, and task list.
     * loads the tasks from the file specified by the filePath.
     *
     * @param filePath The path of the file where tasks are stored.
     */
    public Kev(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (KevException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * runs the main event loop of the application.
     * waits for user input and executes the corresponding command.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                assert fullCommand != null && !fullCommand.isBlank() : "User input should not be null or empty";

                ui.showLine();
                Command c = Parser.parse(fullCommand);
                assert c != null : "Parsed command should not be null";

                c.execute(tasks, ui, storage);
                isExit = c.isExit();
                assert !isExit || c.isExit() : "isExit flag should be consistent";
            } catch (KevException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * returns a welcome message for GUI version of Kev.
     *
     * @return the welcome message as a string
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Kev\nWhat can I do for you?";
    }

    /**
     * processes a user input command and returns Kev's response as a string.
     * used for GUI integration.
     *
     * @param input the user input
     * @return the response message from Kev
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        try {
            ui.clearLastOutput();
            Command command = Parser.parse(input);
            assert command != null : "Parser returned null command";

            command.execute(tasks, ui, storage);
            assert ui.getLastOutput() != null : "UI last output should not be null after command execution";
            return ui.getLastOutput();
        } catch (KevException e) {
            return e.getMessage();
        }
    }

    /**
     * returns the Ui instance.
     * Needed for GUI operations like clearing last output.
     *
     * @return the Ui object
     */
    public Ui getUi() {
        return ui;
    }

    /**
     * checks if a given input is an exit command.
     *
     * @param input The user input
     * @return true if it is an exit command, false otherwise
     */
    public boolean isExit(String input) {
        try {
            Command command = Parser.parse(input);
            return command.isExit();
        } catch (KevException e) {
            return false;
        }
    }

    /**
     * main method that starts the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Kev(DATA_PATH).run();
    }
}