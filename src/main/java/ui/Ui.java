package ui;

import java.util.Scanner;

public class Ui {
    private final Scanner sc = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Hello! I'm Sy");
        System.out.println("What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return sc.nextLine().trim();
    }

    public void showLine() {
        System.out.println("___________________________");
    }

    public void showError(String msg) {
        showLine();
        System.out.println("      " + msg);
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println("      Bye! And don't come again!");
        showLine();
    }

    public void showLoadingError() {
        showError("Couldn't load saved data. Starting with an empty list.");
    }
}
