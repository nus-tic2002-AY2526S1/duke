package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * command to list tasks occuring on a specific date.
 * Deadline (by) and Event (at) tasks.
 */
public class OnCommand extends Command {
    private String dateStr;

    /**
     * creates OnCommand with the user-provided date string.
     *
     * @param dateStr A date in YYYY-MM-DD format.
     */
    public OnCommand(String dateStr) {
        this.dateStr = dateStr;
    }

    /**
     * parses the date, filters for matching tasks within tasklist, and prints them.
     *
     * @throws KevException If the date format is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<Task> all = tasks.getAllTasks();
            boolean found = false;

            for (Task t : all) {
                if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    if (d.getBy().equals(date)) {
                        ui.showMessage(d.toString());
                        found = true;
                    }
                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    // Check if date falls within event start-end range
                    if (!date.isBefore(e.getStartDate()) && !date.isAfter(e.getEndDate())) {
                        ui.showMessage(e.toString());
                        found = true;
                    }
                }
            }

            if (!found) ui.showMessage("No tasks found on " + date);
        } catch (DateTimeParseException e) {
            throw new KevException("Invalid date format! Use YYYY-MM-DD");
        }
    }
}