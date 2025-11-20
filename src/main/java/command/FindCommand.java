package command;

import storage.Storage;
import task.Task;
import tasklist.TaskList;
import ui.Ui;
import util.DukeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds tasks whose descriptions contains the given keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        if (keyword == null) {
            this.keyword = "";
        } else {
            this.keyword = keyword.trim();
        }
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        if (keyword.isEmpty()) {
            throw new DukeException("Find what?");
        }

        List<Integer> matches = new ArrayList<Integer>();

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            String desc = t.getDescription().toLowerCase();
            if (desc.contains(keyword.toLowerCase())) {
                matches.add(i);
            }
        }

        ui.showLine();
        if (matches.isEmpty()) {
            System.out.println("     No matching tasks found.");
        } else {
            System.out.println("     Here are the matching tasks in your list:");
            for (int i : matches) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
            }
        }
        ui.showLine();
    }
}
