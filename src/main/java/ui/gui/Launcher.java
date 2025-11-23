package ui.gui;

import javafx.application.Application;

/**
 * This class serves as a workaround for running the JavaFX application in environments
 * where launching directly from a class that extends {@link Application} may cause issues
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
