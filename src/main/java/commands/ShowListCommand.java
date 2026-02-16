package commands;

import enums.CommandType;
import exceptions.GrootException;
import exceptions.ViewException;
import parser.DateTimeParser;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import ui.UserInteraction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Used to display the tasks in task list
 */
public class ShowListCommand extends Command {
    /**
     * list: list to display
     * commandType: list or view
     */
    private final ArrayList<Task> list;
    private final CommandType commandType;

    /**
     * Populate required parameters
     *
     * @param commandLine parsed user's input
     * @param tasklist    tasklist from task manager
     * @throws GrootException if there are any errors in populating
     */
    protected ShowListCommand(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine,
                              ArrayList<Task> tasklist) throws GrootException {
        super(tasklist);

        assert commandLine != null;
        assert commandLine.getKey().equals(CommandType.LIST) || commandLine.getKey().equals(CommandType.VIEW);
        assert commandLine.getValue() != null;

        this.commandType = commandLine.getKey();
        if (tasklist.isEmpty()) {
            throw new GrootException.EmptyListException();
        }

        if (commandType == CommandType.VIEW) {
            assert !commandLine.getValue().isEmpty();
            assert commandLine.getValue().get(0) != null;

            LocalDate date = DateTimeParser.parseDate(commandLine.getValue().get(0));
            list = viewTasksOnDate(date);
        } else {
            list = tasklist;
        }
    }

    /**
     * Display entire tasklist
     */
    public void displayTasks() {
        ArrayList<String> showTasks = new ArrayList<>();
        int taskNumber = 1;
        for (Task task : list) {
            showTasks.add(taskNumber + ": " + task);
            taskNumber++;
        }
        UserInteraction.printMessage(showTasks.toArray(new String[0]));
    }

    /**
     * Display filtered tasks for viewing
     */
    public void viewTasks() {
        ArrayList<String> viewTasks = new ArrayList<>();
        for (Task task : list) {
            viewTasks.add(task.toString());
        }
        UserInteraction.printMessage(viewTasks.toArray(new String[0]));
    }

    /*
     * Add tasks with dates, i.e. deadline or event tasks
     * Only add deadline tasks that have the date as deadline and
     * event tasks where date is between from and to of event to list
     */
    private ArrayList<Task> viewTasksOnDate(LocalDate date) throws ViewException.NoTaskForViewException {
        ArrayList<Task> viewList = new ArrayList<>();

        for (Task task : tasklist) {
            boolean isDeadline = task instanceof Deadline;
            boolean isEvent = task instanceof Event;

            if (isDeadline) { // if task is a deadline task
                viewDeadline(task, date, viewList);
            } else if (isEvent) { //if task is an event task
                viewEvent(task, date, viewList);
            }
        }

        if (viewList.isEmpty()) {
            throw new ViewException.NoTaskForViewException();
        }
        return viewList;
    }

    /*
     * Used by viewTasksOnDate function.
     * Add deadline task if it has the date given as deadline
     */
    private void viewDeadline(Task task, LocalDate date, ArrayList<Task> viewList) {
        assert task != null;
        assert task instanceof Deadline;
        assert date != null;
        assert viewList != null;

        LocalDateTime deadlineDateTime = ((Deadline) task).getBy();

        LocalDate deadlineDate = deadlineDateTime.toLocalDate();

        boolean isDeadlineOnDate = deadlineDate.equals(date);

        if (isDeadlineOnDate) {
            viewList.add(task);
        }
    }

    /*
     * Used by viewTasksOnDate function
     * Add event task if the date given is between the from and to of event
     */
    private void viewEvent(Task task, LocalDate date, ArrayList<Task> viewList) {
        assert task != null;
        assert task instanceof Event;
        assert date != null;
        assert viewList != null;

        LocalDateTime from = ((Event) task).getStartDateTime();
        LocalDateTime to = ((Event) task).getEndDateTime();

        assert from != null;
        assert to != null;

        LocalDate fromDate = from.toLocalDate();
        LocalDate toDate = to.toLocalDate();

        boolean isOnDate = fromDate.equals(date) || toDate.equals(date);
        boolean isWithinPeriod = fromDate.isBefore(date) && toDate.isAfter(date);

        if (isWithinPeriod || isOnDate) { //given date is from <= date <= to
            viewList.add(task);
        }
    }

    /**
     * Execution of command
     * Display entire list or display tasks that fit the date given
     */
    @Override
    public void execute() {
        switch (commandType) {
        case VIEW -> viewTasks();
        case LIST -> displayTasks();
        }
    }
}
