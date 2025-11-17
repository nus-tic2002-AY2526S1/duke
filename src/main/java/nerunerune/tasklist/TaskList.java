package nerunerune.tasklist;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import nerunerune.exception.NeruneruneException;
import nerunerune.parser.Parser;
import nerunerune.storage.Storage;
import nerunerune.task.Deadline;
import nerunerune.task.Event;
import nerunerune.task.Task;
import nerunerune.task.Todo;
import nerunerune.ui.Ui;

/**
 * Manages a list of tasks and coordinates between UI and storage.
 * Supports loading, saving, adding, deleting, marking, and listing tasks.
 */
public class TaskList {
    private final ArrayList<Task> taskList;
    private final Storage storage;
    private final Ui ui;

    /**
     * Constructs a TaskList with given Storage and UI components.
     *
     * @param storage the storage handler for persistence storage
     * @param ui      the user interface for displaying messages
     */
    public TaskList(Storage storage, Ui ui) {
        this.storage = storage;
        this.ui = ui;
        this.taskList = new ArrayList<>();
    }

    /**
     * Loads tasks from storage into the task list.
     *
     * @throws NeruneruneException if storage loading fails
     * @throws IOException         if an IO error occurs during reading
     */
    public void loadTasks() throws NeruneruneException, IOException {
        storage.handleStorage(taskList);
    }

    /**
     * Prints the current list of tasks using the UI component.
     */
    public void listTasks() {
        ui.printTaskList(taskList);
    }

    /**
     * Returns the list of tasks.
     *
     * @return the ArrayList of Task objects
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * Adds a task to the list and displays a confirmation message.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        assert task != null : "task to add should not be null";

        int sizeBeforeAdd = taskList.size();
        taskList.add(task);

        assert taskList.size() == sizeBeforeAdd + 1 : "Task list size should increase by 1";
        assert taskList.contains(task) : "Task should be in the list after adding";

        ui.printMessage(("Got it, task added to your task list.").indent(4));
        ui.printMessage((task + "\n").indent(8));
        ui.printMessage(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));
    }

    /**
     * Marks a task or multiple tasks as done based on the task string provided.
     * If the task string is "backdated", marks all tasks with deadlines/events that have passed.
     * Otherwise, finds and marks a single task by description or numeric index.
     * Displays confirmation message after marking.
     *
     * @param taskString the task description, index, or "backdated" to mark all backdated tasks
     */
    public void markTask(String taskString) {
        try {
            // mark all backdated tasks
            if (taskString.equalsIgnoreCase("backdated")) {
                int taskCount = 0;

                for (Task task : taskList) {
                    if (!task.getIsDone() && task.isBackdated()) {
                        task.markAsDone();
                        taskCount++;
                    }
                }

                ui.printMessage(("Marked " + taskCount + " backdated task(s) as done.\n").indent(4));
            } else {
                Task task = findTaskByDescription(taskString, false, false);
                task.markAsDone();
                ui.printMessage(("Alright! \"" + taskString + "\" mark as done!").indent(4));
                ui.printMessage((task + "\n").indent(8));
            }
        } catch (NeruneruneException e) {
            ui.printMessage((e.getMessage() + "\n").indent(4));
        }
    }

    /**
     * Unmarks a task as undone, found by description or index.
     *
     * @param taskString the task description or index
     */
    public void unmarkTask(String taskString) {
        try {
            Task task = findTaskByDescription(taskString, true, true);
            task.markAsUndone();
            ui.printMessage(("Alright! \"" + taskString + "\" unmark.").indent(4));
            ui.printMessage((task + "\n").indent(8));
        } catch (NeruneruneException e) {
            ui.printMessage((e.getMessage() + "\n").indent(4));
        }
    }

    /**
     * Deletes a task or multiple tasks based on the description provided.
     * If the description is "all done", removes all completed tasks from the list.
     * Otherwise, finds and removes a single task by description or numeric index.
     * Displays confirmation message after deletion.
     *
     * @param description the task description, index, or "all done" to delete all completed tasks
     */
    public void deleteTask(String description) {
        try {
            // delete all completed tasks
            if (description.equalsIgnoreCase("all done")) {
                int initialSize = taskList.size();
                taskList.removeIf(Task::getIsDone);
                int deletedCount = initialSize - taskList.size();
                ui.printMessage(("Got it, " + deletedCount + " completed task(s) removed.").indent(4));
                ui.printMessage(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));
                // delete task based on description
            } else {
                Task task = findTaskByDescription(description);
                taskList.remove(task);
                ui.printMessage(("Got it, task removed from your task list.").indent(4));
                ui.printMessage((task.toString() + "\n").indent(8));
                ui.printMessage(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));
            }

        } catch (NeruneruneException e) {
            ui.printMessage(e.getMessage());
        }
    }

    /**
     * Finds a task by description and done status, optionally searching from the end.
     * Finds from end if findFromEnd is true
     * Supports finding tasks with duplicate descriptions by searching from the
     * start of the list when marking done, and from the end of the list when unmarking.
     *
     * @param description the task description (case-insensitive)
     * @param doneStatus  the done status the task must have
     * @param findFromEnd true to search from the end of the list, false from beginning
     * @return the Task found with matching criteria
     * @throws NeruneruneException if no matching task is found
     */
    private Task findTaskByDescription(String description, boolean doneStatus, boolean findFromEnd)
            throws NeruneruneException {

        int startFrom = findFromEnd ? taskList.size() - 1 : 0;
        int endAt = findFromEnd ? -1 : taskList.size();
        int step = findFromEnd ? -1 : 1;

        for (int i = startFrom; i != endAt; i += step) {
            Task task = taskList.get(i);
            assert task != null : "task in list should not be null";

            if (task.getDescription().equalsIgnoreCase(description) && task.getIsDone() == doneStatus) {
                return task;
            }
        }
        throw new NeruneruneException(
                "Task with description \"" + description + "\" not found.");

    }

    /**
     * Finds a task by description or numeric index.
     *
     * @param description the task description or index string
     * @return the Task found
     * @throws NeruneruneException if no matching task is found or index is out of range
     */
    private Task findTaskByDescription(String description) throws NeruneruneException {
        try {
            int taskIndex = Integer.parseInt(description) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new NeruneruneException("No task number: " + (taskIndex + 1));
            }
            return taskList.get(taskIndex);
        } catch (NumberFormatException e) {
            // no number provided, fall back to description search
        }

        for (Task task : taskList) {
            if (task.getDescription().equalsIgnoreCase(description)) {
                return task;
            }
        }

        throw new NeruneruneException("Task with description \"" + description + "\" not found.");
    }

    /**
     * Adds a Todo task parsed from the description.
     *
     * @param description the todo description string
     */
    public void addTodo(String description) {
        try {
            Todo todoTask = Parser.parseTodo(description);
            addTask(todoTask);
        } catch (Exception e) {
            ui.printMessage(("Please use the format: todo <tasks>").indent(4));
        }
    }

    /**
     * Adds a Deadline task parsed from the description.
     *
     * @param description the deadline description string
     */
    public void addDeadline(String description) {
        try {
            Deadline deadlineTask = Parser.parseDeadline(description);
            addTask(deadlineTask);
        } catch (Exception e) {
            ui.printMessage(("Please use the format: deadline <task> /by <date/time>").indent(4));
        }
    }

    /**
     * Adds an Event task parsed from the description.
     * Checks for overlapping events before adding. If the new event overlaps
     * with an existing event in the task list, it will not be added and a
     * warning message displaying the conflicting event will be shown.
     *
     * @param description the event description string
     */
    public void addEvent(String description) {
        try {
            Event eventTask = Parser.parseEvent(description);
            Event clashedEvent = findOverlappingEvent(
                    eventTask.getEventFromDateTime(),
                    eventTask.getEventToDateTime()
            );
            if (clashedEvent != null) {
                ui.printMessage(("Cannot add event: It clash with existing event:\n\n"
                        + clashedEvent).indent(0));
                return;
            }
            addTask(eventTask);

        } catch (Exception e) {
            ui.printMessage(("Please use the format: event <task> /from <date/time> /to <date/time>").indent(4));
        }
    }

    /**
     * Finds an overlapping event with the given time range.
     * An overlap occurs when the new event starts before an existing event ends
     * and the new event ends after the existing event starts.
     *
     * @param newFrom the start time of the new event
     * @param newTo   the end time of the new event
     * @return the first overlapping Event found, or null if no overlap exists
     */
    public Event findOverlappingEvent(LocalDateTime newFrom, LocalDateTime newTo) {
        for (Task task : taskList) {
            if (task instanceof Event event) {
                LocalDateTime existingFrom = event.getEventFromDateTime();
                LocalDateTime existingTo = event.getEventToDateTime();

                // check new event starts before existing ends && new event ends after existing starts
                if (newFrom.isBefore(existingTo) && newTo.isAfter(existingFrom)) {
                    return event;
                }
            }
        }
        return null;
    }

    /**
     * Filters the task list to find all tasks containing the
     * specified keyword (case-sensitive) in their description.
     *
     * @param keyword The keyword to search for in task descriptions.
     * @return An ArrayList containing all tasks whose descriptions contain the keyword.
     * Returns an empty ArrayList if no matches are found.
     */
    public ArrayList<Task> filterTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
