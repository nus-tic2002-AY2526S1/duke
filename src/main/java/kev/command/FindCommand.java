package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * command to find tasks that contain a specific keyword in their description.
 */
public class FindCommand extends Command {

    private static final String NO_MATCH_MESSAGE = "No matching tasks found";
    private static final String MATCH_HEADER = "Here are the matching tasks in your list:";

    private String keyword;

    /**
     * creates a FindCommand for a specific keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * executes the find command, displaying all tasks that contains the keyword.
     *
     * @param tasks   The task list containing all tasks.
     * @param ui      The UI object for displaying messages.
     * @param storage The storage object (not used here, but required by interface).
     * @throws KevException Never thrown in this implementation.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KevException {
        List<Task> allTasks = tasks.getAllTasks();

        // Filter tasks that contain the keyword (case-insensitive)
        List<Task> matchedTasks = allTasks.stream()
                .filter(t -> t.toString().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        ui.showLine();
        if (matchedTasks.isEmpty()) {
            ui.showMessage(NO_MATCH_MESSAGE);
        } else {
            ui.showMessage(MATCH_HEADER);
            for (int i = 0; i < matchedTasks.size(); i++) {
                ui.showMessage((i + 1) + "." + matchedTasks.get(i));
            }
        }
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}

