package ui.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import meebot.MeeBot;
import message.WelcomeMessage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private ImageView logoutButton;

    private MeeBot meebot;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image meebotImage = new Image(this.getClass().getResourceAsStream("/images/meebot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Platform.runLater(() -> userInput.requestFocus());
    }

    /** Injects the MeeBot instance */
    public void setMeeBot(MeeBot m) {
        meebot = m;

        WelcomeMessage welcome = new WelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getMeebotDialog(welcome.message(), meebotImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing MeeBot's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) return;

        String response = meebot.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMeebotDialog(response, meebotImage)
        );
        userInput.clear();
    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Do you really want to leave MeeBot?");
        alert.setContentText("Your tasks have been saved automatically.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
}
