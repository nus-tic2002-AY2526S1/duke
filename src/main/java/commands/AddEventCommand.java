package commands;

import java.time.LocalDateTime;

import storage.Storage;
import task.Event;
import task.TaskList;
import ui.Ui;

/**
 * Adds an event task to the TaskList.
 */
public class AddEventCommand extends Command {

    private String description;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    /**
     * Constructor for AddEventCommand.
     *
     * @param description of event task
     * @param fromDate start date and time of the event
     * @param toDate end date and time of the event
     */
    public AddEventCommand(String description, LocalDateTime fromDate, LocalDateTime toDate) {
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Adds an event task into the TaskList. Displays an AddMessage. Saves new
     * list to storage.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Event t = new Event(description, tasks.size() + 1, fromDate, toDate);
        tasks.addTask(t);
        saveToStorage(tasks, ui, storage);
        return ui.showAddMessage(t.getTaskString(), tasks.size());
    }
}
