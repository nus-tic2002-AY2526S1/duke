package echo;

import java.util.Scanner;

public class Ui {
    private final Scanner sc;

    public Ui(){
        sc = new Scanner(System.in);
    }

    public void showWelcome(){
        horizontalLine();
        System.out.println("Hello! I'm Echo");
        System.out.println("What can I do for you?");
        horizontalLine();
    }
    public String readCommand() {
        return sc.nextLine();
    }

    public void showError(String error){
        horizontalLine();
        System.out.println(error);
        horizontalLine();
    }

    public void showBye(){
        horizontalLine();
        System.out.println("Bye. Hope to see you again soon!");
        horizontalLine();
    }

    public void horizontalLine() {
        System.out.println("___________________________________________");
    }
}
