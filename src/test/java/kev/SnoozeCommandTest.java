package kev.command;

import kev.task.*;
import kev.storage.Storage;
import kev.ui.Ui;
import kev.exception.KevException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests for the SnoozeCommand class.
 * ensures that tasks (Deadline/Event) can be postponed correctly.
 */
public class SnoozeCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    /**
     * sets up a fresh TaskList, Ui, and Storage before each test.
     */
    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage("data/test_snooze.txt");
    }

    /**
     * to test that snoozing a Deadline task updates its date correctly.
     */
    @Test
    public void execute_snoozeDeadline_taskDateUpdated() throws KevException, IOException {
        Deadline deadline = new Deadline("Finish project", "2025-11-20");
        tasks.addTask(deadline);

        SnoozeCommand snooze = new SnoozeCommand(0, "2025-11-25");
        snooze.execute(tasks, ui, storage);

        assertEquals(LocalDate.parse("2025-11-25"), ((Deadline) tasks.get(0)).getBy());
    }

    /**
     * to test that snoozing an Event task updates its date correctly.
     */
    @Test
    public void execute_snoozeEvent_taskDateUpdated() throws KevException, IOException {
        Event event = new Event(
                "Team meeting",
                "2025-11-22", "14:00",
                "2025-11-22", "16:00"
        );
        tasks.addTask(event);

        SnoozeCommand snooze = new SnoozeCommand(0, "2025-11-28 14:00 2025-11-28 16:00");
        snooze.execute(tasks, ui, storage);

        assertEquals(LocalDate.parse("2025-11-28"), ((Event) tasks.get(0)).getStartDate());
        assertEquals(LocalDate.parse("2025-11-28"), ((Event) tasks.get(0)).getEndDate());
    }

    /**
     * to test that attempting to snooze a Todo task throws a KevException.
     */
    @Test
    public void execute_snoozeTodo_throwsException() {
        Todo todo = new Todo("Read book");
        tasks.addTask(todo);

        SnoozeCommand snooze = new SnoozeCommand(0, "2025-11-30");
        assertThrows(KevException.class, () -> snooze.execute(tasks, ui, storage));
    }

    /**
     * to tests for an invalid task index - should throw a KevException.
     */
    @Test
    public void execute_invalidIndex_throwsException() {
        Deadline deadline = new Deadline("Submit report", "2025-11-21");
        tasks.addTask(deadline);

        SnoozeCommand snooze = new SnoozeCommand(1, "2025-11-25"); // invalid index
        assertThrows(KevException.class, () -> snooze.execute(tasks, ui, storage));
    }
}
