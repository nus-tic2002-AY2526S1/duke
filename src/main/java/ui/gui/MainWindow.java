package ui.gui;

import java.util.Objects;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import meebot.MeeBot;
import message.WelcomeMessage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends BorderPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private MeeBot meebot;
    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/user.png")));
    private final Image meebotImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/meebot.png")));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Platform.runLater(() -> userInput.requestFocus());
    }


    /** Injects the MeeBot instance */
    public void setMeeBot(MeeBot mb) {
        meebot = mb;
        WelcomeMessage welcome = new WelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getMeebotDialog(welcome.message(), meebotImage)
        );
    }

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
    private void handleAboutClicked() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About MeeBot");
        aboutAlert.setHeaderText("MeeBot v1.0 — Your Task Management Helper");
        aboutAlert.setContentText("""
                    Developed as part of TIC2002 Software Engineering Project
                    Author: Tan Jia Huan
                    Repository: https://github.com/thebunnymama/duke
                """);
        aboutAlert.showAndWait();
    }

    @FXML
    private void handleHelpClicked() {
        String response = meebot.getResponse("help");
        dialogContainer.getChildren().add(
                DialogBox.getMeebotDialog(response, meebotImage)
        );
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
