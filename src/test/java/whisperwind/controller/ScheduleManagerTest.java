package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleManagerTest {
    private TaskList taskList;
    private ScheduleManager scheduleManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add tasks with specific dates for testing
        taskList.addDeadline("Submit report /by 25/12/2025 1800");
        taskList.addEvent("Team meeting /from 25/12/2025 1400 /to 25/12/2025 1500");

        scheduleManager = new ScheduleManager(taskList);
    }

    @Test
    void testShowScheduleForDate() {
        // Tests requirement: Find tasks on specific date
        assertDoesNotThrow(() -> scheduleManager.showScheduleForDate("2025-12-25"));
    }

    @Test
    void testShowScheduleForToday() {
        // Tests requirement: Schedule views (today)
        assertDoesNotThrow(() -> scheduleManager.showScheduleForToday());
    }

    @Test
    void testShowUpcomingSchedule() {
        // Tests requirement: Schedule views (upcoming)
        assertDoesNotThrow(() -> scheduleManager.showUpcomingSchedule());
    }
}