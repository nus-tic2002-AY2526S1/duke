package kev.command;

import kev.task.Task;
import kev.task.TaskList;
import kev.task.Deadline;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

import java.util.List;

/**
 * command to remind the user about upcoming deadlines.
 */
public class RemindCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        List<Task> upcomingTasks = tasks.getUpcomingTasks();

        ui.showLine();
        if (upcomingTasks.isEmpty()) {
            ui.showMessage("You're good! There are no tasks for today or tomorrow!");
        } else {
            ui.showMessage("Reminder! Upcoming deadlines:");
            for (int i = 0; i < upcomingTasks.size(); i++) {
                ui.showMessage((i + 1) + ". " + upcomingTasks.get(i));
            }
        }
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}

