package ui.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Custom JavaFX component representing a chat dialog box in the MeeBot interface.
 * <p>
 * A DialogBox displays a message bubble with an associated avatar image and timestamp.
 * Dialog boxes automatically include a timestamp showing the current time in 12-hour
 * format (e.g., "3:45 PM") when created.
 *
 * @see MainWindow
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    @FXML
    private Label timestamp;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        // Chat timestamp
        String time = java.time.LocalTime.now()
                .withSecond(0)
                .withNano(0)
                .format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
        timestamp.setText(time);
    }

    /**
     * Creates a user-styled dialog box with right-aligned layout.
     * <p>
     * The dialog box is styled with the "user-bubble" CSS class and maintains
     * the default layout with the avatar on the right side.
     *
     * @param text the user's message text
     * @param img the user's avatar image
     * @return a DialogBox configured for displaying user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.dialog.getStyleClass().add("user-bubble");
        return db;
    }

    /**
     * Creates a bot-styled dialog box with left-aligned layout.
     * <p>
     * The dialog box is styled with the "bot-bubble" CSS class and has its
     * layout flipped so the avatar appears on the left side.
     *
     * @param text MeeBot's response text
     * @param img  MeeBot's avatar image
     * @return a DialogBox configured for displaying bot messages
     */
    public static DialogBox getMeebotDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.dialog.getStyleClass().add("bot-bubble");
        db.flip();
        return db;
    }

    /**
     * Flips the dialog box layout so the avatar appears on the left and text on the right.
     * <p>
     * This method reverses the order of child nodes and adjusts the alignment to
     * create a left-aligned dialog box appearance for bot messages.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        ((VBox) tmp.get(1)).setAlignment(Pos.TOP_LEFT);
    }
}
