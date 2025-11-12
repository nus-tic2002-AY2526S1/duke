package commands;

import storage.Storage;
import task.TaskList;
import task.Todo;
import ui.Ui;

/**
 * Adds an To-do task into the TaskList.
 */
public class AddTodoCommand extends Command {

    private String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Adds a To-do task into the TaskList. Displays an AddMessage. Saves new
     * list to storage.
     */
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Todo t = new Todo(description, tasks.size() + 1);
        tasks.addTask(t);
        saveToStorage(tasks, ui, storage);
        return ui.showAddMessage(t.getTaskString(), tasks.size());
    }
}
