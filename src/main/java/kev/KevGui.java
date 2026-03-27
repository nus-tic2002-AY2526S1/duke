package kev;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.application.Platform;
import java.net.URL;

/**
 * JavaFX GUI for Kev chatbot.
 * <p>
 * GUI allows the user to interact with the chatbot using a text field
 * for input and a scrollable area to display both user messages and chatbot responses.
 * </p>
 */
public class KevGui extends Application {

    /** Instance of the Kev chatbot. */
    private Kev kev;

    /**
     * Entry point for the JavaFX application.
     * Initializes the Duke chatbot and sets up the GUI layout and event handlers.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        kev = new Kev("data/tasks.txt"); // initialize chatbot

        // --- Layout Containers ---
        VBox root = new VBox(12);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("root-container");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox dialogContainer = new VBox(10);
        dialogContainer.getStyleClass().add("dialog-container");
        scrollPane.setContent(dialogContainer);

        // --- Input Section ---
        TextField userInput = new TextField();
        userInput.setPromptText("Type a message...");
        userInput.getStyleClass().add("input-field");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");

        HBox inputContainer = new HBox(8, userInput, sendButton);
        inputContainer.getStyleClass().add("input-container");

        root.getChildren().addAll(scrollPane, inputContainer);

        // --- Display Welcome Message ---
        Label welcomeLabel = new Label("Kev: " + kev.getWelcomeMessage());
        welcomeLabel.getStyleClass().add("kev-bubble");
        dialogContainer.getChildren().add(welcomeLabel);

        // --- Event Handlers ---
        sendButton.setOnAction(e -> handleUserInput(userInput, dialogContainer, scrollPane));
        userInput.setOnAction(e -> handleUserInput(userInput, dialogContainer, scrollPane));

        // --- Stage Setup ---
        Scene scene = new Scene(root, 430, 550);

        // --- Load cute.css ---
        URL cssUrl = getClass().getResource("/cute.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Warning: cute.css not found, proceeding without custom styles.");
        }

        stage.setScene(scene);
        stage.setTitle("Kev Chatbot");
        stage.show();
    }

    /**
     * handles user input by sending it to chatbot, capturing the response,
     * and displaying both the user's message and chatbot's response in the dialog container.
     *
     * @param userInput      the TextField containing the user's input
     * @param dialogContainer the VBox container where dialog messages are displayed
     */
    private void handleUserInput(TextField userInput, VBox dialogContainer, ScrollPane scrollPane) {
        String input = userInput.getText().trim();
        assert input != null : "User input should not be null";

        if (input.isEmpty()) {
            return;
        }

        // Display user's message
        Label userLabel = new Label("You: " + input);
        userLabel.getStyleClass().add("user-bubble");
        dialogContainer.getChildren().add(userLabel);

        // Clear previous output
        kev.getUi().clearLastOutput();

        // Get response from Duke
        String response = kev.getResponse(input);
        Label botLabel = new Label("Kev: " + response);
        botLabel.getStyleClass().add("kev-bubble");
        assert response != null : "Kev response should not be null";
        dialogContainer.getChildren().add(botLabel);

        // Clear the input field
        userInput.clear();

        // Auto-scroll to bottom
        scrollPane.layout();
        scrollPane.setVvalue(1.0);

        // exits if the command is an exit command
        if (kev.isExit(input)) {
            Platform.exit();
        }
    }

    /** Adds a user bubble to the dialog container */
    private void addUserMessage(VBox container, String message) {
        Label label = new Label(message);
        label.getStyleClass().add("user-bubble");
        label.setWrapText(true);
        container.getChildren().add(label);
    }

    /** Adds a bot bubble to the dialog container */
    private void addBotMessage(VBox container, String message) {
        Label label = new Label(message);
        label.getStyleClass().add("kev-bubble");
        label.setWrapText(true);
        container.getChildren().add(label);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
