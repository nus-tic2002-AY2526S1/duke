package ui.gui;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import meebot.MeeBot;

public class Main extends Application {

    private MeeBot meebot = new MeeBot();

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
            stage.setMinWidth(1050);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
