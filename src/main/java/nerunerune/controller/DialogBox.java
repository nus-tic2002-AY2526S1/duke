package nerunerune.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a Label containing text from the speaker.
 * <p>
 * This custom control extends HBox and loads its layout from DialogBox.fxml.
 * It provides factory methods to create dialog boxes for both user messages
 * (image on right) and bot responses (image on left).
 */
public class DialogBox extends HBox {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");
    @FXML
    private Label dialog;
    @FXML
    private Label timestamp;
    @FXML
    private ImageView displayPicture;
    @FXML
    private VBox messageContainer;

    /**
     * Creates a DialogBox with the specified text and image.
     * The dialog box is loaded from DialogBox.fxml with this instance
     * set as both the controller and root node.
     *
     * @param text The message text to display in the dialog box
     * @param img  The profile image to display alongside the text
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/nerunerune/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        // set current time as timestamp
        String currentTime = LocalDateTime.now().format(TIME_FORMATTER);
        timestamp.setText(currentTime + " ✓✓");
    }

    /**
     * Creates a dialog box for user messages.
     * The dialog box is aligned to the right with the image on the right side.
     *
     * @param text The user's message text
     * @param img  The user's profile image
     * @return A DialogBox formatted for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for bot responses.
     * The dialog box is aligned to the left with the image on the left side,
     * achieved by flipping the default layout.
     *
     * @param text The bot's response text
     * @param img  The bot's profile image
     * @return A DialogBox formatted for bot responses
     */
    public static DialogBox getNeruneruneDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Flips the dialog box layout such that the ImageView is on the left and text on the right.
     * Also changes the alignment to TOP_LEFT and applies the "reply-label" CSS style class
     * to visually distinguish bot responses from user messages.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");

        // flip timestamp to the left for bot reply
        messageContainer.setAlignment(Pos.TOP_LEFT);
    }
}