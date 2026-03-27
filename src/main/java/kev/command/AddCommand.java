package kev.command;

import kev.task.*;
import kev.storage.Storage;
import kev.ui.Ui;
import kev.exception.KevException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * command to handle tasks added (todo, deadline, event) to the task list.
 */
public class AddCommand extends Command {
    private String input;

    /**
     * creates an AddCommand with the user input.
     * @param input the raw input string for the task command
     */
    public AddCommand(String input) {
        this.input = input;
    }

    /**
     * executes the add command: parses the input, creates the task,
     * adds it to the task list, displays confirmation, and saves to storage.
     *
     * @param tasks Task list to add the task to
     * @param ui UI for user interaction
     * @param storage Storage to save tasks
     * @throws KevException if input is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) throw new KevException("The description of a task cannot be empty.");

        String command = parts[0];
        String description = parts[1];
        Task task;

        switch (command) {
            case "todo":
                task = new Todo(description);
                break;

            case "deadline":
                String[] deadlineParts = description.split(" /by ");
                if (deadlineParts.length != 2)
                    throw new KevException("Deadline must have a date: /by YYYY-MM-DD");
                try {
                    LocalDate by = LocalDate.parse(deadlineParts[1].trim());

                    // to check deadline input to ensure date is valid
                    if (by.isBefore(LocalDate.now())) {
                        throw new KevException("Deadline cannot be in the past!");
                    }

                    task = new Deadline(deadlineParts[0].trim(), by.toString());
                } catch (DateTimeParseException e) {
                    throw new KevException("Invalid date format! Use YYYY-MM-DD");
                }
                break;

            case "event":
                String[] eventParts = description.split(" /at ");
                if (eventParts.length != 2)
                    throw new KevException("Event must have a start and end: /at YYYY-MM-DD HH:mm to YYYY-MM-DD HH:mm");
                try {
                    String[] range = eventParts[1].split(" to ");
                    if (range.length == 1) {
                        String[] timeParts = range[0].split(" ");
                        task = new Event(eventParts[0].trim(), timeParts[0], timeParts[1].split("-")[0],
                                timeParts[0], timeParts[1].split("-")[1]);
                    } else {
                        String[] startParts = range[0].split(" ");
                        String[] endParts = range[1].split(" ");
                        task = new Event(eventParts[0].trim(), startParts[0], startParts[1],
                                endParts[0], endParts[1]);
                    }
                } catch (KevException e) {
                    // preserve the specific error
                    throw e;
                }catch (Exception e) {
                    throw new KevException("Invalid event format! Use YYYY-MM-DD HH:mm[-HH:mm] or YYYY-MM-DD HH:mm to YYYY-MM-DD HH:mm");
                }
                break;

            default:
                throw new KevException("Unknown add command.");
        }

        tasks.addTask(task);
        ui.showTaskAdded(task, tasks.size());

        try {
            storage.saveTasks(tasks.getAllTasks());
        } catch (IOException e) {
            throw new KevException("Error saving tasks: " + e.getMessage());
        }
    }
}
