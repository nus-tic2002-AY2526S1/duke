package ui.cli;

import meebot.MeeBot;

/**
 * Presentation logic for CLI
 */
public class CliRunner {
    private final MeeBot meebot;
    private final UserInterface ui;

    public CliRunner(MeeBot meebot, UserInterface ui) {
        this.meebot = meebot;
        this.ui = ui;
    }

    public void run() {
        ui.displayWelcome();

        while (true) {
            String input = ui.readUserInput();
            MeeBot.ExecutionResult result = meebot.execute(input);
            ui.displayMessage(result.output());

            if (result.isExit()) {
                break;
            }
        }
    }
}
