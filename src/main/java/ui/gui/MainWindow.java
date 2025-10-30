package ui.gui;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import command.CommandType;
import common.CommandDocs;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import meebot.MeeBot;
import message.WelcomeMessage;

/**
 * Controller for the main GUI window of the MeeBot application.
 * <p>
 * This controller manages the chat interface, handling user input, displaying dialog boxes,
 * and providing menu actions (About, Help, Exit). The controller maintains references to
 * the MeeBot backend and manages the scrollable dialog container that displays the
 * conversation history.
 *
 * @see DialogBox
 * @see MeeBot
 */
public class MainWindow extends BorderPane {
    private static final Map<String, String> COMMAND_HINTS = Arrays.stream(CommandType.values())
            .collect(Collectors.toMap(
                    type -> type.name().toLowerCase(),
                    CommandDocs::getFormatHint
            ));

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/user.png")));
    private final Image meebotImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/meebot.png")));

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Label hintLabel;

    private MeeBot meebot;


    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Platform.runLater(() -> userInput.requestFocus());
        setupHintListener();
    }

    /**
     * Injects the MeeBot instance and displays the welcome message.
     */
    public void setMeeBot(MeeBot mb) {
        meebot = mb;
        WelcomeMessage welcome = new WelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getMeebotDialog(welcome.message(), meebotImage)
        );
    }

    private void setupHintListener() {
        userInput.textProperty().addListener((obs, oldText, newText) -> {
            String trimmed = newText.strip().toLowerCase();
            if (trimmed.isEmpty()) {
                userInput.setPromptText("");
                return;
            }

            String[] parts = trimmed.split("\\s+", 2);
            String command = parts[0];
            String hint = COMMAND_HINTS.get(command);

            if (hint != null && (trimmed.equals(command) || trimmed.equals(command + " "))) {
                hintLabel.setText(hint);
            } else {
                userInput.setPromptText("");
            }
        });
    }

    /**
     * Handles user input submission from the text field.
     * <p>
     * When the user presses Enter, this method processes the input through MeeBot,
     * displays both the user's message and MeeBot's response as dialog boxes, and
     * clears the input field. Blank input is ignored.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) return;
        MeeBot.ExecutionResult result = meebot.getExecutionResult(input);
        String response = result.output().message();

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMeebotDialog(response, meebotImage)
        );

        userInput.clear();
        hintLabel.setText("");

        if (result.isExit()) {
            handleExit();
        }
    }

    /**
     * Displays the About dialog with application information.
     * <p>
     * Shows an information alert containing the application version, author details,
     * and repository link.
     */
    @FXML
    private void handleAboutClicked() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About MeeBot");
        aboutAlert.setHeaderText("MeeBot v1.0 — Your Task Management Helper");
        aboutAlert.setContentText("""
                    Developed as part of TIC2002 Software Engineering project
                    Author: Tan Jia Huan
                    Repository: https://github.com/thebunnymama/duke
                """);
        aboutAlert.showAndWait();
    }

    /**
     * Handles the Help menu action by displaying help information.
     * <p>
     * This method sends a "help" command to MeeBot and displays the response
     * in the chat interface.
     */
    @FXML
    private void handleHelpClicked() {
        String response = meebot.getResponse("help");
        dialogContainer.getChildren().add(
                DialogBox.getMeebotDialog(response, meebotImage)
        );
    }

    /**
     * Handles the Exit menu action with user confirmation.
     * <p>
     * Displays a confirmation dialog before exiting the application. If confirmed,
     * the application terminates.
     */
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
