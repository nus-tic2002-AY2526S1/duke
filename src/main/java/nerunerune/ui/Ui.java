package nerunerune.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import nerunerune.command.Command;
import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.task.Task;
import nerunerune.tasklist.TaskList;

/**
 * Handles user interface display functions for the Nerunerune task manager.
 * <p>
 * Responsible for displaying messages, task lists, schedules, start/end greetings,
 * and command guides. Coordinates with ScheduleDisplay for schedule-related operations.
 * Also captures command output for GUI display while maintaining console visibility.
 */
public class Ui {
    private static final String BOT_NAME = "Nerunerune";
    private static final String USER_GUIDE_MSG = """
            Nerunerune Commands
            
            list - Show all tasks
            find <keyword> - Search tasks
            schedule <date> - View tasks for date
              • Dates: DD-MM-YYYY, today, tomorrow, yesterday, next week, next month
            
            todo <task> - Add simple task
            deadline <task> /by <date> - Add deadline
              • Dates: DD-MM-YYYY HHmm or today, tomorrow, next week, next month
              • Example: deadline report /by tomorrow
            event <task> /from <date> /to <date> - Add event
              • Format: DD-MM-YYYY HHmm
              • Example: event meeting /from 01-01-2025 1400 /to 01-01-2025 1600
            
            mark <task> - Mark complete (task name only)
            unmark <task> - Mark incomplete (task name only)
            delete <task> - Delete task (task name only)
            
            mark backdated - Mark all overdue as complete
            delete all done - Delete all completed
            
            command - Show help
            bye - Exit
            """;


    private final ScheduleDisplay scheduleDisplay;

    /**
     * Constructs an Ui instance and initializes the ScheduleDisplay helper.
     */
    public Ui() {
        this.scheduleDisplay = new ScheduleDisplay(this);
    }

    /**
     * Gets the starting greeting message for GUI display.
     * <p>
     * This message is shown when the application starts, welcoming the user
     * and prompting them to view available commands.
     *
     * @return the greeting message as a String
     */
    public static String getStartMsg() {
        return String.format("Hello! I'm %s, here to help you manage your tasks.", BOT_NAME)
                + "\n\nType \"command\" to see all available command";
    }

    /**
     * Displays the schedule for a given date or date range.
     * <p>
     * Delegates to ScheduleDisplay to handle both single dates and date ranges.
     *
     * @param tasks      the TaskList containing all tasks
     * @param dateString the date string (e.g., "today", "next week", "25-10-2025")
     * @throws NeruneruneException if the date format is invalid or cannot be parsed
     */
    public void showSchedule(TaskList tasks, String dateString) throws NeruneruneException {
        scheduleDisplay.showSchedule(tasks, dateString);
    }

    /**
     * Prints the farewell message when the program ends.
     */
    public void endMsg() {
        printMessage(("Until next time!").indent(4));
    }

    /**
     * Prints all tasks in the given task list, formatted with index and indentation.
     * <p>
     * If the list is empty, displays an appropriate message instead.
     *
     * @param taskList the list of tasks to display
     */
    public void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            printMessage(("Your task list is empty!").indent(4));
        } else {
            printMessage(
                    ("Here's what's on your task list so far: " + "(" + taskList.size() + " in total)").indent(4));
            for (int i = 0; i < taskList.size(); i++) {
                printMessage(((i + 1) + ". " + taskList.get(i)).indent(8));
            }
        }
    }

    /**
     * Returns the user guide message listing all available commands.
     * <p>
     * This message includes descriptions and usage examples for all supported commands
     * such as add, delete, mark, schedule, find, etc.
     *
     * @return the user guide message string
     */
    public String getUserGuideMsg() {
        return USER_GUIDE_MSG;
    }

    /**
     * Prints a specified message to the standard output.
     *
     * @param message the message string to print
     */
    public void printMessage(String message) {
        System.out.println(message);
    }


    /**
     * Displays the list of tasks that match the search criteria.
     * <p>
     * If the list is empty, displays a "no matching tasks found" message.
     * Otherwise, displays each matching task with its index number starting from 1.
     *
     * @param matchingTasks the list of tasks to display; can be empty
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            printMessage(("No matching tasks found!").indent(4));
        } else {
            printMessage(("Here are all the matching tasks I can find:\n").indent(4));
            for (int i = 0; i < matchingTasks.size(); i++) {
                printMessage(((i + 1) + ". " + matchingTasks.get(i)).indent(8));
            }
        }
    }

    /**
     * Executes a command and captures its output as a String for GUI display.
     * <p>
     * The output is simultaneously written to both the console (for CLI visibility)
     * and captured for GUI display, ensuring both interfaces show the same information.
     * This method temporarily redirects {@code System.out} to a "tee" stream that
     * duplicates all output to both the original console and an internal capture buffer.
     *
     * @param command  the command to execute
     * @param taskList the task list to operate on
     * @param storage  the storage handler for persistence
     * @return the captured output as a String, trimmed of leading/trailing whitespace
     * @throws NeruneruneException if command execution fails
     */
    // implemented with help of AI
    public String executeAndCapture(Command command, TaskList taskList, Storage storage)
            throws NeruneruneException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        // create a "tee" stream that writes to both original output and capture
        PrintStream teeStream = new PrintStream(baos) {
            @Override
            public void write(int b) {
                super.write(b);
                originalOut.write(b);  // write to original System.out
            }

            @Override
            public void write(byte[] buf, int off, int len) {
                super.write(buf, off, len);
                originalOut.write(buf, off, len);  // write to original System.out
            }
        };

        System.setOut(teeStream);

        try {
            command.execute(taskList, this, storage);
            teeStream.flush();
            originalOut.flush();
            return baos.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }

}
