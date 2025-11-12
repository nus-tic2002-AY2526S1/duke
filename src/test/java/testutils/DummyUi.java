package testutils;

import ui.Ui;

/**
 * A dummy Ui class that does not perform any actual UI operations.
 * Used for testing purposes.
 */
public class DummyUi extends Ui {

    public void showAddMessage(String taskString, int totalTasks) {
    }

    public void showError(String errorMessage) {
    }
}
