package whisperwind;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Platform;

public class Main extends Application {
    private WhisperwindGUI whisperwindApp;
    private TextArea outputArea;
    private TextField inputField;
    private Button sendButton;
    private VBox mainLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the GUI backend
        whisperwindApp = new WhisperwindGUI();

        // Set up close handler to save on exit
        primaryStage.setOnCloseRequest(event -> {
            saveBeforeExit();
        });

        // Create the main layout
        mainLayout = createMainLayout();

        // Create the scene
        Scene scene = new Scene(mainLayout, 900, 700);

        // Set up the stage
        primaryStage.setTitle("WhisperWind - Your Personal Task Manager 🌸");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show welcome message and load tasks
        initializeApplication();
    }

    private VBox createMainLayout() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: #f8f5ff;");

        // Create title
        Label titleLabel = new Label("🌸 WhisperWind Task Manager 🌸");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #6a5acd;");

        // Create output area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12px; -fx-control-inner-background: #ffffff;");
        outputArea.setPrefHeight(450);

        // Create scroll pane for output area
        ScrollPane scrollPane = new ScrollPane(outputArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #ffffff; -fx-border-color: #d1c4e9;");

        // Create input area
        HBox inputLayout = new HBox(10);
        inputField = new TextField();
        inputField.setPromptText("Enter command (e.g., 'todo Buy groceries', 'list', 'mark 1')...");
        inputField.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 13px;");
        inputField.setOnAction(e -> handleUserInput());

        sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #6a5acd; -fx-text-fill: white; -fx-font-weight: bold;");
        sendButton.setOnAction(e -> handleUserInput());

        inputLayout.getChildren().addAll(inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        // Create button panel for common commands
        HBox quickActionPanel = createQuickActionPanel();
        HBox taskTypePanel = createTaskTypePanel();
        HBox viewPanel = createViewPanel();

        // Add all components to main layout
        mainLayout.getChildren().addAll(
                titleLabel,
                quickActionPanel,
                taskTypePanel,
                viewPanel,
                scrollPane,
                inputLayout
        );
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return mainLayout;
    }

    private HBox createQuickActionPanel() {
        HBox panel = new HBox(8);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: #e8eaf6; -fx-padding: 10; -fx-background-radius: 10;");

        String[] buttonConfigs = {
                "📋 List Tasks", "list",
                "✅ Mark Done", "mark ",
                "🔄 Unmark", "unmark ",
                "🗑️ Delete", "delete ",
                "💾 Save", "save",
                "👋 Exit", "bye"
        };

        for (int i = 0; i < buttonConfigs.length; i += 2) {
            Button button = new Button(buttonConfigs[i]);
            button.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 5 10;");
            final String command = buttonConfigs[i + 1];
            button.setOnAction(e -> {
                if (command.endsWith(" ")) {
                    inputField.setText(command);
                    inputField.requestFocus();
                } else {
                    processCommand(command);
                }
            });
            panel.getChildren().add(button);
        }

        return panel;
    }

    private HBox createTaskTypePanel() {
        HBox panel = new HBox(8);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: #f3e5f5; -fx-padding: 10; -fx-background-radius: 10;");

        String[] buttonConfigs = {
                "📝 Add Todo", "todo ",
                "⏰ Add Deadline", "deadline ",
                "🎉 Add Event", "event "
        };

        for (int i = 0; i < buttonConfigs.length; i += 2) {
            Button button = new Button(buttonConfigs[i]);
            button.setStyle("-fx-background-color: #8e24aa; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 5 10;");
            final String command = buttonConfigs[i + 1];
            button.setOnAction(e -> {
                inputField.setText(command);
                inputField.requestFocus();
            });
            panel.getChildren().add(button);
        }

        return panel;
    }

    private HBox createViewPanel() {
        HBox panel = new HBox(8);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: #e0f2f1; -fx-padding: 10; -fx-background-radius: 10;");

        String[] buttonConfigs = {
                "🔍 Find Tasks", "find ",
                "📅 Today", "schedule today",
                "📆 Tomorrow", "schedule tomorrow",
                "📈 Upcoming", "schedule upcoming",
                "❓ Help", "view instruction"
        };

        for (int i = 0; i < buttonConfigs.length; i += 2) {
            Button button = new Button(buttonConfigs[i]);
            button.setStyle("-fx-background-color: #00897b; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 5 10;");
            final String command = buttonConfigs[i + 1];
            button.setOnAction(e -> {
                if (command.endsWith(" ")) {
                    inputField.setText(command);
                    inputField.requestFocus();
                } else {
                    processCommand(command);
                }
            });
            panel.getChildren().add(button);
        }

        return panel;
    }

    private void handleUserInput() {
        String input = inputField.getText().trim();
        if (!input.isEmpty()) {
            processCommand(input);
            inputField.clear();
        }
    }

    private void processCommand(String input) {
        appendToOutput("> " + input);

        // Process the command through the real backend
        String output = whisperwindApp.processCommand(input);

        if (output != null && !output.trim().isEmpty()) {
            appendToOutput(output);
        }

        // Handle exit command
        if (input.trim().equalsIgnoreCase("bye")) {
            Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // Give time to see the goodbye message
                    Platform.exit();
                } catch (InterruptedException e) {
                    Platform.exit();
                }
            });
        }
    }

    private void initializeApplication() {
        // Show welcome message
        String welcomeMessage = whisperwindApp.getWelcomeMessage();
        appendToOutput(welcomeMessage);

        // Load tasks
        String loadOutput = whisperwindApp.loadTasksOnStartup();
        appendToOutput(loadOutput);
    }

    private void saveBeforeExit() {
        try {
            String saveResult = whisperwindApp.saveTasks();
            System.out.println("Auto-save on exit: " + saveResult);
        } catch (Exception e) {
            System.out.println("⚠️ Could not save tasks on exit: " + e.getMessage());
        }
    }

    private void appendToOutput(String text) {
        Platform.runLater(() -> {
            outputArea.appendText(text);
            // Auto-scroll to bottom
            outputArea.setScrollTop(Double.MAX_VALUE);
        });
    }
}