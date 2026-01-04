package bongocat.commands;

import bongocat.tasks.Task;
import bongocat.tasks.TaskList;
import bongocat.tasks.Deadline;
import bongocat.ui.Ui;
import bongocat.storage.Storage;
import bongocat.BongoException;

import java.io.IOException;

public class AddDeadlineCommand extends Command {

    private String name;
    private String by;

    public AddDeadlineCommand(String name, String by) {
        this.name = name;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {

        Task newTask = new Deadline(name, by);

        // duplicate check
        if (tasks.isDuplicate(newTask)) {
            ui.showDuplicate(newTask);
            return;
        }

        // add task
        tasks.addTask(newTask);

        // show UI
        ui.showAdd(newTask, tasks.size());

        // save
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }
}
