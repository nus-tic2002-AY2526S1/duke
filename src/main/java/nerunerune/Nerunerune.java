package nerunerune;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nerunerune.command.Command;
import nerunerune.controller.MainWindow;
import nerunerune.exception.NeruneruneException;
import nerunerune.parser.Parser;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Main JavaFX Application class for the Nerunerune task management application.
 * <p>
 * This class extends Application and manages the overall application lifecycle including
 * initialization of core components (UI, storage, task list), loading tasks from persistent
 * storage, and coordinating the GUI display.
 *
 */
public class Nerunerune extends Application {
    private static final String DEFAULT_STORAGE_FILEPATH = "nerunerune/data/tasks.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Constructs a Nerunerune instance with the specified storage file path.
     * Initializes the UI handler, storage manager, and task list.
     * <p>
     * Note: This constructor is primarily used for testing or custom file paths.
     * The default no-arg constructor is used by JavaFX during normal operation.
     *
     * @param filePath The file path where tasks are stored and loaded from
     */
    public Nerunerune(String filePath) {
        assert filePath != null : "file path should not be null";
        assert !filePath.isEmpty() : "file path should not be empty";

        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList(storage, ui);
    }

    /**
     * Default constructor required by JavaFX.
     * Initializes the application with the default storage file path.
     * <p>
     * This constructor is automatically invoked by the JavaFX runtime when
     * launching the application.
     */
    public Nerunerune() {
        this(DEFAULT_STORAGE_FILEPATH);
    }

    /**
     * Application entry point.
     * Launches the JavaFX application
     *
     * @param args Command-line arguments (currently unused)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the application on the launcher thread before the GUI is created.
     * Loads existing tasks from storage. This method is called by JavaFX after
     * the constructor but before start().
     * <p>
     * This method runs on a background thread, making it suitable for time-consuming
     * initialization tasks like file I/O without blocking the JavaFX Application Thread.
     */
    // AI suggest to refactor to initialize tasks on launcher thread
    @Override
    public void init() {
        try {
            taskList.loadTasks();
        } catch (NeruneruneException | IOException e) {
            ui.printMessage("Storage error: " + e.getMessage());
        }
    }

    /**
     * Starts the JavaFX application and displays the main window.
     * Loads the FXML layout, configures the stage (window), and injects
     * dependencies into the controller. This method is called on the
     * JavaFX Application Thread.
     *
     * @param stage The primary stage (window) provided by the JavaFX runtime
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Nerunerune.class.getResource("/nerunerune/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Nerunerune");

            //Setting limit to Window size
            stage.setMinHeight(600);
            stage.setMinWidth(900);

            fxmlLoader.<MainWindow>getController().setNerunerune(this);
            stage.show();
        } catch (Exception e) {
            ui.printMessage("Could not start Nerunerune." + e.getMessage());
        }
    }

    /**
     * Processes user input and returns the response for GUI display.
     * Parses the input into a command, executes it, and captures the output
     * for display in the chat interface.
     *
     * @param input The user's input string from the GUI text field
     * @return The command execution result as a formatted string, or an error message if execution fails
     */
    public String getResponse(String input) {
        try {
            Command userCommand = Parser.parseCommand(input.trim());
            String output = ui.executeAndCapture(userCommand, taskList, storage);
            return output.isEmpty() ? "Command executed successfully!" : output;

        } catch (NeruneruneException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Something went wrong!: " + e.getMessage();
        }
    }

}