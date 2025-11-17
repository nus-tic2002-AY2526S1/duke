package nerunerune.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.stream.Collectors;

import nerunerune.exception.NeruneruneException;
import nerunerune.parser.DateTimeParser;
import nerunerune.task.Deadline;
import nerunerune.task.Event;
import nerunerune.task.Task;
import nerunerune.tasklist.TaskList;

/**
 * Handles display logic for task schedules.
 * <p>
 * Separates schedule-related display methods from the main Ui class.
 * Supports displaying schedules for single dates and date ranges (next week, next month).
 * Tasks are grouped by date and categorized by type (Deadlines and Events).
 */
public class ScheduleDisplay {
    private final Ui ui;

    /**
     * Constructs a ScheduleDisplay with the specified Ui instance.
     *
     * @param ui the user interface to use for displaying messages
     */
    public ScheduleDisplay(Ui ui) {
        this.ui = ui;
    }

    /**
     * Displays the schedule for a date string.
     * <p>
     * Automatically detects if the date string represents a range (e.g., "next week", "next month")
     * or a single date (e.g., "today", "25-10-2025") and displays accordingly.
     *
     * @param tasks      the TaskList containing all tasks
     * @param dateString the date string (e.g., "today", "next week", "25-10-2025")
     * @throws NeruneruneException if the date format is invalid or cannot be parsed
     */
    public void showSchedule(TaskList tasks, String dateString) throws NeruneruneException {
        if (dateString.equalsIgnoreCase("next week") || dateString.equalsIgnoreCase("next month")) {
            showScheduleRange(tasks, dateString);
        } else {
            LocalDate date = DateTimeParser.parseDateWithKeywords(dateString).toLocalDate();
            showScheduleSingleDay(tasks, date);
        }
    }

    /**
     * Displays the schedule for a single date.
     * <p>
     * Shows tasks grouped by type (Deadlines and Events) for the specified date.
     *
     * @param tasks the TaskList containing all tasks
     * @param date  the date to display the schedule for
     */
    private void showScheduleSingleDay(TaskList tasks, LocalDate date) {
        ui.printMessage("Schedule for " + DateTimeParser.formatForSchedule(date) + ":");
        ArrayList<Task> deadlines = new ArrayList<>();
        ArrayList<Task> events = new ArrayList<>();
        filterAndGroupTasks(tasks, date, deadlines, events);
        displayScheduleContent(deadlines, events);
    }

    /**
     * Capitalizes the first letter of a month name.
     * <p>
     * Converts uppercase month enum names to proper case (e.g., "DECEMBER" becomes "December").
     *
     * @param month the Month enum to capitalize
     * @return the capitalized month name
     */
    private String capitalizeMonth(java.time.Month month) {
        String monthStr = month.toString().toLowerCase();
        return monthStr.substring(0, 1).toUpperCase() + monthStr.substring(1);
    }

    /**
     * Displays the schedule for a date range (next week or next month).
     * <p>
     * For "next week": displays Monday to Sunday of next week.
     * For "next month": displays all days in next month.
     * Tasks are grouped by date and then by type within each date.
     *
     * @param tasks        the TaskList containing all tasks
     * @param rangeKeyword the range keyword ("next week" or "next month")
     */
    private void showScheduleRange(TaskList tasks, String rangeKeyword) {
        LocalDate startDate;
        LocalDate endDate;

        if (rangeKeyword.equalsIgnoreCase("next week")) {
            startDate = LocalDate.now().plusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endDate = startDate.plusDays(6);

            String startFormatted = startDate.getDayOfMonth() + " " + capitalizeMonth(startDate.getMonth());
            String endFormatted = endDate.getDayOfMonth() + " " + capitalizeMonth(endDate.getMonth()) + " " + endDate.getYear();

            ui.printMessage("Schedule from " + startFormatted + " to " + endFormatted + ":");
        } else {
            startDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
            endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

            ui.printMessage("Schedule for " + capitalizeMonth(startDate.getMonth()) + ":");
        }

        ArrayList<Task> allTasksInRange = collectTasksInRange(tasks, startDate, endDate);

        if (allTasksInRange.isEmpty()) {
            ui.printMessage("\nNo tasks scheduled in this period.");
            return;
        }

        displayTasksByDate(allTasksInRange, startDate, endDate);
    }

    /**
     * Collects all tasks that occur within a date range.
     * <p>
     * Uses Java Streams to filter tasks whose dates fall between the specified
     * start and end dates (inclusive).
     * <p>
     * The stream pipeline:
     * <ol>
     *   <li>Creates a stream from the task list</li>
     *   <li>Filters tasks to include only those with dates in range</li>
     *   <li>Collects remaining tasks into a new ArrayList</li>
     * </ol>
     *
     * @param tasks     the TaskList containing all tasks to filter
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return a new ArrayList containing only tasks with dates between startDate and endDate
     */
    private ArrayList<Task> collectTasksInRange(TaskList tasks, LocalDate startDate, LocalDate endDate) {
        return tasks.getTaskList().stream()
                .filter(task -> {
                    LocalDate taskDate = getTaskDate(task);
                    // task has date && date is not before start && date is not after end
                    return taskDate != null && !taskDate.isBefore(startDate) && !taskDate.isAfter(endDate);
                }) // filter only tasks with dates between startDate and endDate
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Extracts the date from a task based on its type.
     * <p>
     * For Deadline tasks, returns the deadline date.
     * For Event tasks, returns the event start date.
     * For Todo tasks or unknown types, returns null since they have no associated date.
     * <p>
     * This method centralizes the instanceof checking logic, avoiding code duplication
     * throughout the ScheduleDisplay class.
     *
     * @param task the task to extract the date from
     * @return the LocalDate associated with the task, or null if the task has no date
     */
    private LocalDate getTaskDate(Task task) {
        if (task instanceof Deadline) {
            return ((Deadline) task).getDeadlineByDateTime().toLocalDate();
        } else if (task instanceof Event) {
            return ((Event) task).getEventFromDateTime().toLocalDate();
        }
        return null;
    }


    /**
     * Displays tasks grouped by date and task type within a date range.
     * <p>
     * For each date in the range, displays a header with the formatted date,
     * followed by separate sections for Deadlines and Events.
     * Shows a total count of tasks at the end.
     *
     * @param allTasks  the list of all tasks in the range
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     */
    private void displayTasksByDate(ArrayList<Task> allTasks, LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        int totalCount = 0;

        while (!currentDate.isAfter(endDate)) {
            ArrayList<Task> deadlinesForDate = new ArrayList<>();
            ArrayList<Task> eventsForDate = new ArrayList<>();

            // separate tasks by type for this date
            for (Task task : allTasks) {
                if (task instanceof Deadline && ((Deadline) task).getDeadlineByDateTime().toLocalDate().equals(currentDate)) {
                    deadlinesForDate.add(task);
                } else if (task instanceof Event && ((Event) task).getEventFromDateTime().toLocalDate().equals(currentDate)) {
                    eventsForDate.add(task);
                }
            }

            // display date if there are tasks
            if (!deadlinesForDate.isEmpty() || !eventsForDate.isEmpty()) {
                ui.printMessage("\n" + DateTimeParser.formatForSchedule(currentDate) + ":");

                displayTaskCategory("Deadlines", deadlinesForDate);

                displayTaskCategory("Events", eventsForDate);

                totalCount += deadlinesForDate.size() + eventsForDate.size();
            }

            currentDate = currentDate.plusDays(1);
        }

        ui.printMessage("\nTotal: " + totalCount + " task(s)");
    }

    /**
     * Filters tasks by the specified date and groups them by type.
     * <p>
     * Iterates through all tasks and categorizes those occurring on the given date
     * into separate lists for Deadlines and Events. Todo tasks are excluded as they
     * have no specific date.
     *
     * @param tasks     the TaskList containing all tasks
     * @param date      the date to filter tasks by
     * @param deadlines the list to populate with Deadline tasks (modified by this method)
     * @param events    the list to populate with Event tasks (modified by this method)
     */
    private void filterAndGroupTasks(TaskList tasks, LocalDate date,
                                     ArrayList<Task> deadlines, ArrayList<Task> events) {
        for (Task task : tasks.getTaskList()) {
            if (!task.occursOn(date)) {
                continue;
            }

            if (task instanceof Deadline) {
                deadlines.add(task);
            } else if (task instanceof Event) {
                events.add(task);
            }
        }
    }

    /**
     * Displays the formatted schedule content for filtered tasks.
     * <p>
     * Shows separate sections for Deadlines and Events, with a total count at the bottom.
     * If both lists are empty, displays a message indicating no tasks are scheduled.
     *
     * @param deadlines the list of Deadline tasks to display
     * @param events    the list of Event tasks to display
     */
    private void displayScheduleContent(ArrayList<Task> deadlines, ArrayList<Task> events) {
        if (deadlines.isEmpty() && events.isEmpty()) {
            ui.printMessage("\nNo tasks scheduled for this date.");
            return;
        }

        displayTaskCategory("Deadlines", deadlines);
        displayTaskCategory("Events", events);
        ui.printMessage("Total: " + (deadlines.size() + events.size()) + " task(s)");
    }

    /**
     * Displays a category of tasks with a header and numbered list.
     * <p>
     * If the task list is empty, the category is not displayed.
     * Each task is numbered starting from 1 and indented for readability.
     *
     * @param categoryName the name of the category (e.g., "Deadlines", "Events")
     * @param tasks        the list of tasks to display in this category
     */
    private void displayTaskCategory(String categoryName, ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }

        ui.printMessage((categoryName + ":").indent(4));
        for (int i = 0; i < tasks.size(); i++) {
            ui.printMessage(((i + 1) + ". " + tasks.get(i)).indent(8));
        }
    }


}
