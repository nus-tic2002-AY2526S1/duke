package ui;

import exceptions.FileException;
import exceptions.GrootException;
import manager.FileManager;
import manager.TaskManager;

import java.util.Scanner;

/**
 * Handles all the input and output of program
 */
public class UserInteraction {
    private static TaskManager taskManager;
    private static boolean isRunning = true;

    /**
     * Start program with greet and end when exit command is given
     */
    public static void run() {
        greet();
        try {
            taskManager = new TaskManager();
        } catch (FileException.FileCorruptedException e) {
            UserInteraction.printMessage(e.getMessage());
            UserInteraction.fileCorruptedHandler();
        } catch (GrootException e) {
            UserInteraction.printMessage(e.getMessage());
        }
        Scanner input = new Scanner(System.in);
        while (isRunning) {
            String userInput = input.nextLine();
            manageUserInput(userInput);
        }
    }

    /*
     * Pass user's input to task manager for further process
     */
    private static void manageUserInput(String userInput) {
        userInput = userInput.trim(); // remove blank spaces in the front and back from user input
        taskManager.manageTask(userInput);
    }

    /**
     * Handles if tasklist file is corrupted.
     * Asks user whether to continue with empty tasklist, else end program
     */
    public static void fileCorruptedHandler() {
        printMessage("Do you want to continue with an empty list? (y/n)");
        Scanner sc = new Scanner(System.in);
        String userResponse = sc.nextLine();
        if (userResponse.equalsIgnoreCase("y")) {
            TaskManager.clearTasklist();
            try {
                FileManager.emptyFile();
            } catch (FileException e) {
                printMessage(e.getMessage());
            }
        } else if (userResponse.equalsIgnoreCase("n")) {
            exit();
        } else {
            printMessage("Invalid input");
            fileCorruptedHandler();
        }
    }

    /*
     * Default section for messages. All messages start with "I am Groot."
     */
    private static void iAmGroot() {
        System.out.print("I am Groot. ");
    }

    /**
     * output messages
     *
     * @param messages output of program
     */
    public static void printMessage(String... messages) {
        for (String message : messages) {
            echo(message);
        }
    }

    /*
     * Greet user
     */
    private static void greet() {
        iAmGroot();
        System.out.println("(Hello. What can I do for you?)");
    }

    /**
     * Main printing of messages. All messages are printed using this function
     *
     * @param text message to be printed
     */
    public static void echo(String text) { //static so other classes can use for output
        iAmGroot();
        System.out.println("(" + text + ")");
    }

    /**
     * Exit program with message
     */
    public static void exit() {
        System.out.println("We are Groot. (Goodbye.)");
        isRunning = false;
    }
}
