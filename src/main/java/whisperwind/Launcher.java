package whisperwind;

import javafx.application.Application;

public class Launcher {
    static {
        // Set system properties to suppress warnings
        System.setProperty("javafx.verbose", "false");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Launching WhisperWind...");
            Application.launch(Main.class, args);
        } catch (Exception e) {
            System.err.println("Failed to launch application:");
            e.printStackTrace();
            // Print the root cause
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            System.err.println("Root cause: " + cause.getMessage());
        }
    }
}