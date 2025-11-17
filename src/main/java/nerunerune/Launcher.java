package nerunerune;

import javafx.application.Application;

/**
 * A launcher class to Nerunerune JavaFX application to workaround classpath issues.
 * <p>
 * This class serves as the entry point for the application and provides a workaround
 * for classpath and module path issues that can occur when running JavaFX applications
 * packaged as JAR files without embedding the JavaFX runtime.
 * <p>
 * By using a separate launcher class that does not extend Application, the JVM can
 * properly initialize the JavaFX runtime before loading the main Application class.
 */
public class Launcher {

    /**
     * Application entry point that launches the Nerunerune JavaFX application.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(Nerunerune.class, args);
    }
}