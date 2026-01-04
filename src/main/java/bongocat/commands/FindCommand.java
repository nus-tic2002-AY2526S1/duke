package bongocat.commands;

import bongocat.tasks.TaskList;
import bongocat.tasks.Task;
import bongocat.ui.Ui;
import bongocat.storage.Storage;
import java.util.ArrayList;

public class FindCommand extends Command {

    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword.toLowerCase().trim();
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> results = tasks.findTasks(keyword);
        ui.showFindResults(results);
    }
}
