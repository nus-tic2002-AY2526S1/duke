package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;

/**
 * Represents a command to view the schedule for a specific date or date range.
 * When executed, displays all tasks (deadlines and events) scheduled for the given date or range.
 * Supports single dates (e.g., "today", "tomorrow", "25-10-2025") and date ranges (e.g., "next week", "next month").
 */
public class ScheduleCommand extends Command {
    private final String dateString;

    /**
     * Constructs a ScheduleCommand with the specified date string.
     *
     * @param dateString the date string (e.g., "today", "next week", "25-10-2025")
     */
    public ScheduleCommand(String dateString) {
        this.dateString = dateString;
    }

    /**
     * Executes the schedule command by displaying tasks scheduled for the specified date or range.
     * Delegates to the UI to format and show the schedule view with tasks grouped by date and type.
     *
     * @param tasks   the task list containing all tasks
     * @param ui      the user interface to display the schedule
     * @param storage the storage handler
     * @throws NeruneruneException if an error occurs during execution or date parsing
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        ui.showSchedule(tasks, dateString);
    }
}