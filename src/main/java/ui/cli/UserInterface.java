package ui.cli;

import java.util.Scanner;

import common.message.Message;
import common.message.WelcomeMessage;

/**
 * Handles console-based user interactions for MeeBot.
 * Provides input/output operations with consistent message formatting and display.
 */
public class UserInterface {
    private static final int MAX_WIDTH = 60;      // For MeeBot text wrap
    private static final int CONSOLE_WIDTH = 110;
    private final MessageRenderer renderer;
    private final Scanner sc = new Scanner(System.in);

    public UserInterface() {
        this.renderer = new MessageRenderer(MAX_WIDTH, CONSOLE_WIDTH);
    }

    /**
     * Prompts user for input and returns the trimmed response.
     *
     * @return user input with leading/trailing whitespace removed
     */
    public String readUserInput() {
        System.out.print("You: ");
        return sc.nextLine().trim();
    }

    /**
     * Displays a message using the configured renderer for consistent formatting.
     */
    public void displayMessage(Message msg) {
        renderer.render(msg);
    }

    /**
     * Shows the welcome screen with ASCII logo and initial greeting.
     * Logo is displayed only once during application startup.
     */
    public void displayWelcome() {
        String noodleArt = """
                                    ___  ___ _____  _____ ______  _____  _____\s
                                    |  \\/  ||  ___||  ___|| ___ \\|  _  ||_   _|
                                    | .  . || |__  | |__  | |_/ /| | | |  | | \s
                                    | |\\/| ||  __| |  __| | ___ \\| | | |  | | \s
                                    | |  | || |___ | |___ | |_/ /\\ \\_/ /  | | \s
                                    \\_|  |_/\\____/ \\____/ \\____/  \\___/   \\_/ \s
                """;
        System.out.print(noodleArt);
        displayMessage(new WelcomeMessage());
    }
}
