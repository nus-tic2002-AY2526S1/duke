package commands;

import java.time.LocalDateTime;

import storage.Storage;
import task.Deadline;
import task.TaskList;
import ui.Ui;

/**
 * Adds a deadline task into the TaskList.
 */
public class AddDeadlineCommand extends Command {

    private String description;
    private LocalDateTime deadline;

    /**
     * Constructor for AddDeadlineCommand.
     *
     * @param description of deadline task
     * @param deadline of the task
     */
    public AddDeadlineCommand(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    /**
     * Adds a deadline task into the TaskList. Displays an AddMessage. Saves new
     * list to storage.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Deadline t = new Deadline(description, tasks.size() + 1, deadline);
        tasks.addTask(t);
        saveToStorage(tasks, ui, storage);
        return ui.showAddMessage(t.getTaskString(), tasks.size());
    }
}
