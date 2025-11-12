
import java.io.IOException;

import app.DefinitelyRealRon;
import gui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML. Code adapted from
 * https://se-education.org/guides/tutorials/javaFx.html
 */
public class Main extends Application {

    private final DefinitelyRealRon ron = new DefinitelyRealRon();

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("DefinitelyRealRon");
            Image icon = new Image(this.getClass().getResourceAsStream("/images/icon.png"));
            stage.getIcons().add(icon);

            stage.setMinHeight(500);
            stage.setMinWidth(500);
            stage.setWidth(500);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setRon(ron);  // inject the Ron instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
