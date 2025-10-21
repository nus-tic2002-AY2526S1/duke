package ui.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import meebot.MeeBot;

/**
 * JavaFX application main class for the MeeBot GUI.
 * <p>
 * This class extends {@link Application} and manages the JavaFX application lifecycle,
 * including stage initialization and FXML loading. The MeeBot instance must be injected
 * via {@link #setMeeBot(MeeBot)} before the application starts.
 * <p>
 * The application loads its UI from {@code /view/MainWindow.fxml} and sets up
 * the primary stage.
 *
 * @see Application
 * @see MainWindow
 */
public class Main extends Application {
    private static MeeBot meebot;

    /**
     * Injects the MeeBot instance before Application.launch
     */
    public static void setMeeBot(MeeBot mb) {
        meebot = mb;
    }

    /**
     * Initializes and displays the primary stage of the JavaFX application.
     * <p>
     * This method loads the main window FXML, injects the MeeBot instance into
     * the controller, and configures the stage with appropriate dimensions and title.
     *
     * @param stage the primary stage for this application, provided by the JavaFX platform
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            BorderPane ap = fxmlLoader.load();

            fxmlLoader.<MainWindow>getController().setMeeBot(meebot);   // inject backend

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("MeeBot");
            stage.setMinHeight(700);
            stage.setMinWidth(1080);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
