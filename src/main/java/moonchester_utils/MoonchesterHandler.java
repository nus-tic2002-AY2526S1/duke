package moonchester_utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import moonchester_data.Deadline;
import moonchester_data.Event;
import moonchester_data.Task;
import moonchester_data.Todo;
import moonchester_data.UserList;

public class MoonchesterHandler {
    private final Scanner scanner;
    private final UserList userList;
    public MoonchesterStorage storage;

    public MoonchesterHandler(UserList userList) {
        this.userList = userList;
        this.scanner = new Scanner(System.in);
        this.storage = new MoonchesterStorage();
    }

    /**
     * Starts the Moonchester program. The method first displays a greeting message,
     * then continuously prompts the user for commands or task inputs.
     * Commands are parsed and delegated to the appropriate functions for listing 
     * tasks, adding todos, deadlines, and events, marking/unmarking tasks,
     * deleting tasks, querying by date or keyword, and exiting the application. 
     * Exceptions for unknown commands or invalid inputs are caught and displayed to 
     * the user.
     */
    public void start() {
        userGreeting();

        while (true) {
            System.out.print("Command / Add : ");
            String userItem = scanner.nextLine();
            String[] splittedString = stringSplitter(userItem, " ");

            try {
                String command = splittedString[0].toLowerCase();
                switch (command) {
                    case "list": 
                        printList(); 
                        break;
                    case "exit": 
                        userExit(); 
                        return;
                    case "mark":
                    case "m":
                        handleMarking(splittedString, true); 
                        break;
                    case "unmark":
                    case "um":
                        handleMarking(splittedString, false); 
                        break;
                    case "todo":
                    case "t":
                        addTodo(splittedString); 
                        break;
                    case "deadline":
                    case "d":
                        addDeadline(joinFromSecond(splittedString)); 
                        break;
                    case "event":
                    case "e":
                        addEvent(joinFromSecond(splittedString)); 
                        break;
                    case "date":
                    case "da":
                        try {
                            queryDate(splittedString[1]); 
                        } catch (Exception e) {
                            System.err.println("[!] Missing paramaters - your date query command should be: date/da [date dd/MM/yyyy]");
                        }
                        break;
                    case "find":
                    case "f": 
                        try {
                            queryKeyword(splittedString[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println("[!] Missing paramaters - your keyword query command should be: find/f [keyword]");
                        }
                        break;
                    case "delete":
                    case "del":
                        try {
                            handleDelete(Integer.parseInt(splittedString[1]));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new MoonchesterException("[!] Please select a valid S/N");
                        } catch (IndexOutOfBoundsException e) {
                            throw new MoonchesterException("[!] Please select a valid S/N");
                        }
                        break;
                    default:
                        throw new MoonchesterException("[!] Unknown Command. Permitted Commands: todo (t), deadline (d), event (e), list, mark(m), unmark(um), delete (del), find (f), date(da),exit");
                }

            } catch (MoonchesterException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Prints the Moonchester greeting message along with the full
     * list of available commands for the user. This method displays prompts the user * for input.
     */
    private void userGreeting() {
        String greetingMessage = """
        ____________________________________________________________
        __  __                        _               _            
        |  \\/  | ___   ___  _ __   ___| |__   ___  ___| |_ ___ _ __ 
        | |\\/| |/ _ \\ / _ \\| '_ \\ / __| '_ \\ / _ \\/ __| __/ _ \\ '__|
        | |  | | (_) | (_) | | | | (__| | | |  __/\\__ \\ ||  __/ |   
        |_|  |_|\\___/ \\___/|_| |_|\\___|_| |_|\\___||___/\\__\\___|_|   
                                                                    
        Hello! I am Moonchester, your personal chatbot!
        List of commands:
        Command/Shortcut
        1. list
        2. todo/t [description]
        3. deadline/d [description] /by [date - dd/MM/yyyy HHmm or ddd]
        4. event/e [description] /from [date - dd/MM/yyyy HHmm or dd/MM/yyyy or ddd] /to [date - dd/MM/yyyy HHmm or dd/MM/yyyy or ddd]
        5. find/f [keyword]
        6. date/da [date - dd/MM/yyyy HHmm or dd/MM/yyyy]
        7. delete/del [S/N of list]
        8. mark/m [S/N of list]
        9. unmark/um [S/N of list]
        10. exit
        What can I do for you?
        """;
        System.out.print(greetingMessage);
    }


    /**
     * Handles the Moonchester's exit. This method updates and saves
     * the current task list to storage, prints the exit banner message,
     * and closes the input scanner. If there is an error while saving tasks,
     * an error message is displayed.
     */
    private void userExit() {
        String exitMessage = """
        Hope to see you again soon, goodbye!
        ____________________________________________________________
        """;
        try {
            // Only updates the task list AFTER the user exits the program
            storage.updateActiveTasks(userList);
        } catch (java.io.IOException e) {
            System.err.println("[!] Error saving tasks: " + e.getMessage());
        }
        System.out.print(exitMessage);
        scanner.close();
    }

    /**
     * Splits the given user input string using the specified delimiter.
     * This helper method returns the resulting array of the string.
     *
     * @param userItem  The input string to be split
     * @param delimiter The delimiter used to split the input string
     * @return An array of the splitted segments of the input string
     */
    private static String[] stringSplitter(String userItem, String delimiter) {
        String[] userItemSplit = userItem.split(delimiter);
        return userItemSplit;
    }

    /**
     * Combines the elements of the given string array starting from index 1
     * into a single space separated string. If the array contains one or
     * zero elements, an empty string is returned.
     *
     * @param taskDescription The array containing task description parts
     * @return A space-joined string of all elements from index 1 onwards,
     *         or an empty string if insufficient elements exist
     */
    private static String joinFromSecond(String[] taskDescription) {
        if (taskDescription.length <= 1) {
            return "";
        }
        String[] result = Arrays.copyOfRange(taskDescription, 1, taskDescription.length);
        return String.join(" ", result);
    }


    /**
     * Prints the user's current  userList in a numbered and formatted layout.
     * Each task is displayed with its corresponding index (via a counter, not the
     * index of the array itself) and string representation. A separator line is 
     * printed after the list.
     * 
     */
    private void printList() {
        System.out.println("[+] User's List");
        int counter = 1;
        for (Task item : userList.getList()) {
            // %-4s%s%n -> Left align the number string with width of 4 characters, %s%s is for the string and %n is newline
            System.out.printf("%-4s%s%n", counter + ".", item.printString());
            counter++;
        }
        System.out.println("____________________________________________________________");
    }


    /**
     * Prints a filtered list of tasks happening on the specified date.
     * The method displays each matching task in a numbered format, prompts
     * the user to press ENTER, and then prints the full master task list, this is to
     * ensure that should the user want to delete that task, it will refer to the
     * master list and the original number.
     *
     * @param queriedList The list of tasks that match the queried date
     * @param dateString The formatted date string used for display
     */
    private void printListDate(ArrayList<Task> queriedList, String dateString) {
        System.out.println("[+] Events/Deadlines occuring on " + dateString);
        int counter = 1;
        for (Task item : queriedList) {
            System.out.println(counter + ". " + item.printString());
            counter++;
        }
        System.out.println("____________________________________________________________");
        System.out.println("Press \"ENTER\" to view your master list - You will need to view your master list to mark/unmark them :)");
        System.out.print("____________________________________________________________");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        printList();
    }

    /**
     * Prints a filtered list of tasks that contain the specified keyword.
     * The method displays each matching task in a numbered format, prompts
     * the user to press ENTER, and then prints the full master task list, this is to
     * ensure that should the user want to delete that task, it will refer to the
     * master list and the original number (same as printListDate)
     *
     * @param queriedList The list of tasks that match the given keyword
     * @param keyword The keyword used to filter tasks
     */
    private void printListKeyword(ArrayList<Task> queriedList, String keyword) {
        System.out.println("[+] Keyword found in the following tasks: [" + keyword + "]");
        int counter = 1;
        for (Task item : queriedList) {
            System.out.println(counter + ". " + item.printString());
            counter++;
        }
        System.out.println("____________________________________________________________");
        System.out.println("Press \"ENTER\" to view your master list - You will need to view your master list to mark/unmark them :)");
        System.out.print("____________________________________________________________");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        printList();
    }

    /**
     * Handles the marking or unmarking of a task based on user input.
     * This method parses the task index, validates it, updates the task's
     * completion status, and prints a confirmation message. It also handles
     * invalid input formats and out-of-range task numbers.
     *
     * @param userItemSplit The user command after being splitted
     * @param status The status to set for the task (true = mark, false = unmark)
     */
    private void handleMarking(String[] userItemSplit, boolean status) {
        try {
            int index = Integer.parseInt(userItemSplit[1]);
            if (index <= 0 || index > userList.getSize()) {
                throw new MoonchesterException("[!] Please select a valid S/N");
            }

            userList.getSpecificTask(index).setStatus(status);
            String statusDescription;

            if (status == true) {
                statusDescription = "completed";
            } else {
                statusDescription = "not completed";
            }
            System.out.println("[+] Marked as " + statusDescription + " : " + userList.getSpecificTask(index).getDescription());
            System.out.println("____________________________________________________________");

        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid task number.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("[!] Please specify which task to mark or unmark.");
        }
    }


    /**
     * Handles the deletion of a task from the user's list. The method attempts
     * to delete the task at the specified index (had to -1 to cater for the array 
     * index) and displays an error message if the index is invalid.
     *
     * @param arrayIndex The 1-based index of the task to delete
     */
    private void handleDelete(int arrayIndex) {
        try {
            userList.deleteItem(arrayIndex - 1, userList.getSpecificTask(arrayIndex));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("[!] Ensure that the S/N is valid.");
        }
        
    }

    /**
     * Extracts the start and end dates from an event input string based on
     * the "/from" and "/to" delimiters. Returns a string array where the
     * first element is the start date and the second element is the end date.
     *
     * @param eventArray The event input string containing "/from" and "/to" date 
     * @return A string array with two elements: [startDate, endDate]
     */
    private String[] eventExtractor(String eventArray) {
        String[] results = new String[2];
        String[] eventArrayParts = eventArray.split("/to");
        results[0] = eventArrayParts[0].replace("/from", "").trim();
        results[1] = eventArrayParts[1].trim();
        return results;
    }


    /**
     * Handles the creation and addition of a new Todo task to the userList.
     * This method combines the input string parts into a task description,
     * creates a Todo object, and adds it to the user list.
     *
     * @param splittedString The splitted user input, where index 1 onward contains the task description
     */
    private void addTodo(String[] splittedString) {
        String[] taskDescriptionArray = Arrays.copyOfRange(splittedString, 1, splittedString.length);
        String taskDescription = String.join(" ", taskDescriptionArray);
        Todo new_todo = new Todo(taskDescription);
        userList.addItem(new_todo);
    }


    /**
     * Handles the creation and addition of a new Deadline task to the userList.
     * The method splits the input string into description and date parts, converts
     * the date string to a LocalDateTime object, and creates a Deadline task. If
     * required parameters are missing or the date conversion fails, an error message
     * is displayed.
     *
     * @param taskDescription The user input containing the task description and "/by" date
     */
    private void addDeadline(String taskDescription) {
        try {
            String[] parts = stringSplitter(taskDescription, "/by");
            LocalDateTime date = MoonchesterDate.convertToDateTime(parts[1].trim(), 0);
            if (date == null) {
                return;
            }
            Deadline newDeadline = new Deadline(parts[0], date);
            userList.addItem(newDeadline);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("[!] Deadline appears to have missing parameters, please follow this format: deadline [description] /by [dd/MM/yyyy HHmm]");
        }
    }


    /**
     * Handles the creation and addition of a new Event task to the userList.
     * The method splits the input string into description, start date, and end date,
     * converts the date strings to LocalDateTime objects, and validates that the start
     * date is before the end date. If validation fails or parameters are missing, an
     * error message is displayed.
     *
     * @param taskDescription The user input containing the event description, "/from" start date, and "/to" end date
     */
    private void addEvent(String taskDescription) {
        try {
            String[] description_array = stringSplitter(taskDescription, "/from");
            String description = description_array[0];
            String[] results = eventExtractor(description_array[1]);
            // Compare fromDate and toDate
            LocalDateTime fromDate = MoonchesterDate.convertToDateTime(results[0], 0);
            LocalDateTime toDate = MoonchesterDate.convertToDateTime(results[1], 0);
            if (fromDate == null || toDate == null) {
                return;
            }

            if (!MoonchesterDate.compareDateTime(fromDate, toDate)) {
                System.err.println("[!] From date is AFTER to date - Please ensure that /from is earlier than /to");
                return;
            }

            Event newEvent = new Event(description, fromDate, toDate);
            userList.addItem(newEvent);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("[!] Event appears to have missing parameters, , please follow this format : event [description] /from [dd/MM/yyyy HHmm] /to [dd/MM/yyyy HHmm]");
        }

    }


    /**
     * Retrieves the tasks based on a date found in the description and prints the list
     * 
     * @param dateString Date specified by the user
     */
    private void queryDate(String dateString) {
        // This function returns the list of tasks on a given date
        // Format - dd/MM/yyyy
        try {
            LocalDateTime convertedDate = MoonchesterDate.convertToDateTime(dateString, 1);
            if(convertedDate == null) {
                return;
            }
            ArrayList<Task> queriedList = userList.getList(convertedDate);
            printListDate(queriedList, dateString);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("[!] Missing paramaters - your date query command should be: date/da [date dd/MM/yyyy]");
        }

    }

    /**
     * Retrieves the tasks based on a keyword found in the description and prints the list
     * 
     * @param keyword Keyword specified by the user
     */
    private void queryKeyword(String keyword) {
        try {
            ArrayList<Task> queriedList = userList.getList(keyword);
            printListKeyword(queriedList,keyword);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("[!] Missing paramaters - your keyword query command should be: find/f [keyword]");
        }

    }

}
