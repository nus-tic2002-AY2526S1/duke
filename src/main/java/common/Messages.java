package common;

/**
 * Container for messages to be displayed
 */
public class Messages {

    public static final String MESSAGE_WELCOME = """
                                                  Yo! I'm DefinitelyRealRon. Definitely Real. Definitely Ron.
                                                  How may I help you today?""";
    public static final String MESSAGE_BYE = " Seeya! Hope I was helpful. I'm DefinitelyRealRon!";
    public static final String MESSAGE_SHOW_TASKLIST = " Here are the tasks in your list:";
    public static final String MESSAGE_MARKED = " You're so productive! I've marked this task as done:";
    public static final String MESSAGE_UNMARKED = " L! I've marked this task as not done yet:";
    public static final String MESSAGE_ERROR = " Beep Boop. An error has occurred.";
    public static final String MESSAGE_ADDED = """
                                                Got it. I've added this task:
                                               %1$s
                                                Now you have %2$d tasks in your list.""";
    public static final String MESSAGE_DELETED = """
                                                  Ight bet. I've deleted this task:
                                                 %1$s
                                                  Now you have %2$d tasks in your list.""";
    public static final String MESSAGE_FOUND_TASKS = " Here are the matching tasks in your list:";
    public static final String MESSAGE_PRIORITY_SET = " Gotchu bro. I've set the priority of this task:\n%1$s";

    public static final String ERROR_IO_INITIALISATION = " Initialisation Error. File may be open elsewhere :(";
    public static final String ERROR_INVALID_STORAGE = """
                                                        Invalid path detected!
                                                        Please ensure extension ends with .txt :(""";
    public static final String ERROR_STORE_FAILED = " Unable to store data!\n Please check that the file is closed :(";

    public static final String ERROR_EMPTY_DESC = " Description for task cannot be empty :(";
    public static final String ERROR_UNKNOWN_COMMAND = " Sorry bruv. I don't understand %1$s :(";
    public static final String ERROR_EMPTY_LIST = " Your list is empty :(";
    public static final String ERROR_INVALID_BY = " Please input a valid /by date :(";
    public static final String ERROR_INVALID_FROM_TO = " Please input a valid /from /to date :(";
    public static final String ERROR_INVALID_INTEGER = " Please input a valid integer :(";
    public static final String ERROR_ALREADY_MARKED = " Task %1$s was already marked :(";
    public static final String ERROR_ALREADY_UNMARKED = " Task %1$s was already unmarked :(";
    public static final String ERROR_OUT_OF_BOUNDS = " Bruh. You can't do that. You only have %1$d tasks :(";
    public static final String ERROR_WRONG_DATE_FORMAT = " Man. Please input the date format as: dd/MM/yyyy HHmm :(";
    public static final String ERROR_NO_MATCHING_TASKS = " No matching tasks found :(";
    public static final String ERROR_INVALID_PRIORITY = " Invalid priority level! Use: LOW, MEDIUM, HIGH :(";
    public static final String ERROR_TO_BEFORE_FROM = " Start date/time is after End date/time :(";
}
