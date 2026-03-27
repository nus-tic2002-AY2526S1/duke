package kev;

import javafx.application.Application;

/**
 * Launcher class for JavaFX.
 * Delegates to KevGui to start the GUI.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(KevGui.class, args);
    }
}
