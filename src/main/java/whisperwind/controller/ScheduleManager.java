package whisperwind.controller;

import whisperwind.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The {@code ScheduleManager} class handles viewing tasks in schedule format
 * for specific dates, including daily schedules, today/tomorrow views, and date ranges.
 */
public class ScheduleManager {
    private TaskList taskList;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Constructs a ScheduleManager instance.
     *
     * @param taskList The list of tasks to manage schedules for.
     */
    public ScheduleManager(TaskList taskList) {
        assert taskList != null : "TaskList should not be null";
        this.taskList = taskList;

        assert DATE_FORMATTER != null : "Date formatter should be initialized";
        assert DISPLAY_FORMATTER != null : "Display formatter should be initialized";
    }

    /**
     * Shows the schedule for a specific date.
     *
     * @param dateString The date in YYYY-MM-DD format.
     */
    public void showScheduleForDate(String dateString) {
        try {
            LocalDate targetDate = LocalDate.parse(dateString, DATE_FORMATTER);
            List<Task> tasksOnDate = getTasksForDate(targetDate);

            displayDailySchedule(targetDate, tasksOnDate);

        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format (e.g., 2024-12-25)");
        }
    }

    /**
     * Shows schedule for a date range.
     *
     * @param startDateString Start date in YYYY-MM-DD format.
     * @param endDateString   End date in YYYY-MM-DD format.
     */
    public void showScheduleForDateRange(String startDateString, String endDateString) {
        try {
            LocalDate startDate = LocalDate.parse(startDateString, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(endDateString, DATE_FORMATTER);

            if (endDate.isBefore(startDate)) {
                System.out.println("❌ End date cannot be before start date!");
                return;
            }

            displayDateRangeSchedule(startDate, endDate);

        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format (e.g., 2024-12-25)");
        }
    }

    /**
     * Shows schedule for today.
     */
    public void showScheduleForToday() {
        LocalDate today = LocalDate.now();
        List<Task> tasksToday = getTasksForDate(today);

        System.out.println("📅 TODAY'S SCHEDULE - " + today.format(DISPLAY_FORMATTER));
        displayDailySchedule(today, tasksToday);
    }

    /**
     * Shows schedule for tomorrow.
     */
    public void showScheduleForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Task> tasksTomorrow = getTasksForDate(tomorrow);

        System.out.println("📅 TOMORROW'S SCHEDULE - " + tomorrow.format(DISPLAY_FORMATTER));
        displayDailySchedule(tomorrow, tasksTomorrow);
    }

    /**
     * Shows upcoming deadlines and events for the next 7 days.
     */
    public void showUpcomingSchedule() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        System.out.println("📅 UPCOMING SCHEDULE (Next 7 Days)");
        System.out.println("====================================");

        displayDateRangeSchedule(today, nextWeek);
    }
    /**
     * Gets all tasks occurring on a specific date.
     */
    private List<Task> getTasksForDate(LocalDate date) {
        List<Task> tasksOnDate = new ArrayList<>();

        for (int i = 0; i < taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i + 1);
            if (taskOccursOnDate(task, date)) {
                tasksOnDate.add(task);
            }
        }

        // Sort tasks by time
        tasksOnDate.sort(new TaskTimeComparator());
        return tasksOnDate;
    }

    /**
     * Checks if a task occurs on a specific date.
     */
    private boolean taskOccursOnDate(Task task, LocalDate date) {
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return deadline.getBy().toLocalDate().equals(date);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            LocalDate eventStartDate = event.getFrom().toLocalDate();
            LocalDate eventEndDate = event.getTo().toLocalDate();

            // Check if the date falls within the event range
            return !date.isBefore(eventStartDate) && !date.isAfter(eventEndDate);
        }
        // Todo tasks don't have specific dates, so they're not included in schedules
        return false;
    }

    /**
     * Displays the daily schedule in a formatted way.
     */
    private void displayDailySchedule(LocalDate date, List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("   No scheduled tasks for " + date.format(DISPLAY_FORMATTER) + " 🎉");
            System.out.println("   💡 It's a free day! Time to relax or add new tasks.");
            return;
        }

        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-55s │%n", "Schedule for " + date.format(DISPLAY_FORMATTER));
        System.out.println("├─────────────────────────────────────────────────────────┤");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String timeInfo = getTaskTimeInfo(task);
            String status = task.isDone() ? "✅" : "⏳";

            System.out.printf("│ %-2d. %-10s %-40s │%n",
                    i + 1, timeInfo, truncateDescription(task.getDescription(), 35) + " " + status);
        }

        System.out.println("└─────────────────────────────────────────────────────────┘");
        showScheduleStatistics(tasks);
    }

    /**
     * Displays schedule for a date range.
     */
    private void displayDateRangeSchedule(LocalDate startDate, LocalDate endDate) {
        System.out.println("📅 SCHEDULE FROM " + startDate.format(DISPLAY_FORMATTER) +
                " TO " + endDate.format(DISPLAY_FORMATTER));

        LocalDate currentDate = startDate;
        boolean hasTasksInRange = false;

        while (!currentDate.isAfter(endDate)) {
            List<Task> tasksOnDate = getTasksForDate(currentDate);
            if (!tasksOnDate.isEmpty()) {
                hasTasksInRange = true;
                displayDailySchedule(currentDate, tasksOnDate);
                System.out.println();
            }
            currentDate = currentDate.plusDays(1);
        }

        if (!hasTasksInRange) {
            System.out.println("   No scheduled tasks in this date range 🎉");
            System.out.println("   💡 It's a free period! Time to plan some activities.");
        }
    }

    /**
     * Gets time information for a task.
     */
    private String getTaskTimeInfo(Task task) {
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return formatTime(deadline.getBy());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return formatTime(event.getFrom()) + "-" + formatTime(event.getTo());
        }
        return "All Day";
    }

    /**
     * Formats time for display.
     */
    private String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Truncates long descriptions for display.
     */
    private String truncateDescription(String description, int maxLength) {
        if (description.length() <= maxLength) {
            return description;
        }
        return description.substring(0, maxLength - 3) + "...";
    }

    /**
     * Shows statistics for the schedule.
     */
    private void showScheduleStatistics(List<Task> tasks) {
        int deadlines = 0;
        int events = 0;
        int completed = 0;

        for (Task task : tasks) {
            if (task instanceof Deadline) deadlines++;
            if (task instanceof Event) events++;
            if (task.isDone()) completed++;
        }

        System.out.printf("📊 Summary: %d deadlines, %d events, %d completed%n",
                deadlines, events, completed);

        if (completed == tasks.size() && !tasks.isEmpty()) {
            System.out.println("🎉 All tasks completed for this day! Well done! 👏");
        }
    }

    /**
     * Comparator to sort tasks by time.
     */
    private static class TaskTimeComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            LocalDateTime time1 = getTaskDateTime(t1);
            LocalDateTime time2 = getTaskDateTime(t2);
            return time1.compareTo(time2);
        }

        private LocalDateTime getTaskDateTime(Task task) {
            if (task instanceof Deadline) {
                return ((Deadline) task).getBy();
            } else if (task instanceof Event) {
                return ((Event) task).getFrom();
            }
            return LocalDateTime.MAX; // Todo tasks go to the end
        }
    }
}