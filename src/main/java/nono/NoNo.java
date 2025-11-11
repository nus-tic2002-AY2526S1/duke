package nono;

import nono.command.Command;
import nono.command.Parser;
import nono.exception.UserInputException;
import nono.task.Deadline;
import nono.task.Event;
import nono.task.Task;
import nono.task.ToDo;
import nono.util.Storage;
import nono.util.TaskList;
import nono.util.Ui;

/**
 * The main class of the NoNo chatbot application.
 * Handles initialization, command parsing, execution, and data persistence.
 */
public class NoNo {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a NoNo chatbot instance.
     *
     * @param filePath The path of the file used to save and load tasks.
     */
    public NoNo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot main loop until the user exits.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parse(input);
                isExit = executeCommand(command);
            } catch (UserInputException error) {
                ui.showError(error.getMessage());
            }
        }
        ui.showGoodbye();
    }

    /**
     * Executes a given command and updates data or UI accordingly.
     *
     * @param command The command to execute.
     * @return True if the command is a BYE command (to exit), false otherwise.
     * @throws UserInputException If an invalid command or index is encountered.
     */
    private boolean executeCommand(Command command) throws UserInputException {
        switch (command.getType()) {
        case BYE:
            storage.save(tasks.getAllTasks());
            return true;

        case LIST:
            ui.showTaskList(tasks);
            break;

        case MARK:
            tasks.markTask(command.getTaskIndex() - 1, true);
            ui.showMarkResult(tasks.getTask(command.getTaskIndex() - 1), true);
            storage.save(tasks.getAllTasks());
            break;

        case UNMARK:
            tasks.markTask(command.getTaskIndex() - 1, false);
            ui.showMarkResult(tasks.getTask(command.getTaskIndex() - 1), false);
            storage.save(tasks.getAllTasks());
            break;

        case TODO:
            Task todo = new ToDo(command.getDescription());
            tasks.addTask(todo);
            ui.showAddResult(todo);
            storage.save(tasks.getAllTasks());
            break;

        case DEADLINE:
            String[] deadlineDetails = command.getDetails();
            Task deadline = new Deadline(deadlineDetails[0], deadlineDetails[1]);
            tasks.addTask(deadline);
            ui.showAddResult(deadline);
            storage.save(tasks.getAllTasks());
            break;

        case EVENT:
            String[] eventDetails = command.getDetails();
            Task event = new Event(eventDetails[0], eventDetails[1], eventDetails[2]);
            tasks.addTask(event);
            ui.showAddResult(event);
            storage.save(tasks.getAllTasks());
            break;

        case DELETE:
            Task deletedTask = tasks.deleteTask(command.getTaskIndex() - 1);
            ui.showDeleteResult(deletedTask, tasks.size());
            storage.save(tasks.getAllTasks());
            break;

        case FIND:
            ui.showFindResults(tasks, command.getDescription());
            break;
        }
        return false;
    }

    /**
     * The main entry point of the program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new NoNo("./data/nono.txt").run();
    }
}