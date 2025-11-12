package gui;

import app.DefinitelyRealRon;
import static common.Messages.MESSAGE_WELCOME;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI. Code adapted from
 * https://se-education.org/guides/tutorials/javaFx.html
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

    private DefinitelyRealRon ron;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));
    private final Image chatbotImage = new Image(this.getClass().getResourceAsStream("/images/Chatbot.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the DefinitelyRealRon instance into the controller.
     */
    public void setRon(DefinitelyRealRon r) {
        ron = r;
        dialogContainer.getChildren().addAll(
                DialogBox.getChatbotDialog(MESSAGE_WELCOME, chatbotImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * the chatbot's reply and then appends them to the dialog container. Clears
     * the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = ron.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getChatbotDialog(response, chatbotImage)
        );
        userInput.clear();
    }
}
