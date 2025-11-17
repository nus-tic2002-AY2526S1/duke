package nerunerune.controller;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import nerunerune.Nerunerune;
import nerunerune.ui.Ui;

/**
 * Controller for the main GUI window of the Nerunerune application.
 * Manages user interactions, displays chat messages in dialog boxes,
 * and coordinates communication between the UI and the application logic.
 * <p>
 * This controller is linked to MainWindow.fxml and handles the display
 * of user messages and bot responses in a scrollable chat interface.
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

    private Nerunerune nerunerune;
    private Image userImage;
    private Image neruneruneImage;

    /**
     * Initializes the controller after FXML elements have been loaded.
     * Sets up the auto-scrolling behavior for the dialog container and
     * displays the welcome message when the application starts.
     * <p>
     * This method is automatically called by the FXMLLoader after all
     * <p>
     * FXML tag annotated fields have been injected.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Load images with null checks
        loadImages();

        String welcomeMessage = Ui.getStartMsg();
        dialogContainer.getChildren().add(
                DialogBox.getNeruneruneDialog(welcomeMessage, neruneruneImage)
        );
    }

    /**
     * Loads the profile images for user and bot.
     * Provides error handling if image resources cannot be found.
     */
    // implemented with help of AI
    private void loadImages() {
        try {
            InputStream userImageStream = this.getClass().getResourceAsStream("/nerunerune/images/hachiware.jpg");
            InputStream botImageStream = this.getClass().getResourceAsStream("/nerunerune/images/usagi.jpg");

            if (userImageStream != null) {
                userImage = new Image(userImageStream);
            } else {
                System.err.println("Error: Could not load user image (hachiware.jpg)");
                userImage = createDefaultImage();
            }

            if (botImageStream != null) {
                neruneruneImage = new Image(botImageStream);
            } else {
                System.err.println("Error: Could not load bot image (usagi.jpg)");
                neruneruneImage = createDefaultImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            userImage = createDefaultImage();
            neruneruneImage = createDefaultImage();
        }
    }

    /**
     * Creates a default placeholder image when actual images cannot be loaded.
     * Uses a generic profile icon as fallback.
     *
     * @return A default profile icon Image
     */
    // implemented with help of AI
    private Image createDefaultImage() {
        try {
            InputStream defaultStream = this.getClass()
                    .getResourceAsStream("/nerunerune/images/default-profile.png");

            if (defaultStream != null) {
                return new Image(defaultStream);
            }
        } catch (Exception e) {
            System.err.println("Could not load default profile icon");
        }

        // last resort: the transparent 1x1 pixel
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
    }


    /**
     * Injects the Nerunerune application instance into the controller.
     * This allows the controller to communicate with the application logic
     * for processing user commands.
     *
     * @param n The Nerunerune application instance
     */
    public void setNerunerune(Nerunerune n) {
        nerunerune = n;
    }

    /**
     * Handles user input from the text field.
     * Creates two dialog boxes: one displaying the user's input and another
     * containing the bot's response. Both dialog boxes are appended to the
     * dialog container for display. The input field is cleared after processing.
     * <p>
     * This method is triggered when the user presses Enter in the text field
     * or clicks the Send button.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = nerunerune.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNeruneruneDialog(response, neruneruneImage)
        );
        userInput.clear();
    }
}