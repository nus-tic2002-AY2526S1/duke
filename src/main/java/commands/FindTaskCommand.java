package commands;

import static common.Messages.ERROR_NO_MATCHING_TASKS;
import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Finds tasks that match a given keyword in their description.
 */
public class FindTaskCommand extends Command {

    private String targetString;
    private TaskList foundTasks = new TaskList();

    public FindTaskCommand(String desc) {
        targetString = desc;
    }

    /**
     * Searches for tasks that contain the given keyword in their description
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().contains(targetString)) {
                foundTasks.add(tasks.get(i));
            }
        }
        if (foundTasks.isEmpty()) {
            return ui.showErrorMessage(ERROR_NO_MATCHING_TASKS);
        } else {
            String foundTasksString = "";

            for (int i = 0; i < foundTasks.size(); i += 1) {
                foundTasksString += foundTasks.get(i).getTaskStringWithIndex() + "\n";
            }
            foundTasksString = foundTasksString.substring(0, foundTasksString.length() - 1);
            return ui.showFoundTasksMessage(foundTasksString);
        }
    }
}
