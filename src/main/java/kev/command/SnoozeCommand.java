package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * command to snooze/postpone a task to a new date.
 * only applicable for Deadline and Event tasks.
 */
public class SnoozeCommand extends Command {
    private int index;
    private String newDateStr;

    /**
     * creates a SnoozeCommand for a specific task through user input/index and new date.
     *
     * @param index The zero-based index of the task to snooze.
     * @param newDateStr The new date to set for the task in YYYY-MM-DD format.
     */
    public SnoozeCommand(int index, String newDateStr) {
        this.index = index;
        this.newDateStr = newDateStr;
    }

    /**
     * executes the snooze command, updating the date of the task.
     *
     * @param tasks   The task list containing all tasks.
     * @param ui      The UI object for displaying messages.
     * @param storage The storage object for saving updated tasks.
     * @throws KevException If the index is invalid, the task is not snoozable,
     *                      or the date format is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        if (index < 0 || index >= tasks.size()) {
            throw new KevException("Please review tasklist and provide a valid task index.");
        }

        Task task = tasks.get(index);

        if (task instanceof Deadline) {
            try {
                LocalDate newDate = LocalDate.parse(newDateStr);
                ((Deadline) task).setBy(newDate);
                ui.showMessage("Task snoozed: " + task);
            } catch (DateTimeParseException e) {
                throw new KevException("Invalid date format! Use YYYY-MM-DD");
            }
        } else if (task instanceof Event) {
            try {
                // Expected format: "startDate startTime endDate endTime"
                String[] parts = newDateStr.split(" ");
                if (parts.length != 4) throw new KevException(
                        "Invalid event snooze format. Use: YYYY-MM-DD HH:mm YYYY-MM-DD HH:mm"
                );

                LocalDate newStartDate = LocalDate.parse(parts[0]);
                LocalTime newStartTime = LocalTime.parse(parts[1]);
                LocalDate newEndDate = LocalDate.parse(parts[2]);
                LocalTime newEndTime = LocalTime.parse(parts[3]);

                ((Event) task).reschedule(newStartDate, newStartTime, newEndDate, newEndTime);
                ui.showMessage("Event snoozed: " + task);

            } catch (DateTimeParseException e) {
                throw new KevException("Invalid date format! Use YYYY-MM-DD");
            }
        } else {
            throw new KevException("Only deadlines or events can be snoozed.");
        }

        try {
            storage.saveTasks(tasks.getAll());
        } catch (IOException e) {
            throw new KevException("Error saving tasks: " + e.getMessage());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}

