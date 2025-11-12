package commands;

import storage.Storage;
import task.TaskList;
import ui.Ui;

/**
 * Displays an indexed TaskList when executed.
 */
public class ListCommand extends Command {

    /**
     * Displays an indexed TaskList when executed.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        String taskListString = "";

        for (int i = 0; i < tasks.size(); i += 1) {
            taskListString += tasks.get(i).getTaskStringWithIndex() + "\n";
        }
        taskListString = taskListString.substring(0, taskListString.length() - 1);
        return ui.showTaskListMessage(taskListString);
    }
}
