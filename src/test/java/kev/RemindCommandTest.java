package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RemindCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));

        ui = new Ui();
        storage = new Storage("data/test.txt"); // dummy path
        tasks = new TaskList();

        // Add tasks for testing
        tasks.addTask(new Todo("Write essay")); // no deadline
        tasks.addTask(new Deadline("Submit report", LocalDate.now().toString())); // due today
        tasks.addTask(new Deadline("Prepare slides", LocalDate.now().plusDays(1).toString())); // due tomorrow
        tasks.addTask(new Deadline("Future task", LocalDate.now().plusDays(5).toString())); // not due soon
    }

    @Test
    void execute_showsUpcomingTasksCorrectly() {
        RemindCommand remindCommand = new RemindCommand();

        try {
            remindCommand.execute(tasks, ui, storage);
        } catch (KevException e) {
            fail("Execution threw an exception: " + e.getMessage());
        }

        String output = outContent.toString();

        // check output contains tasks due today and tomorrow
        assertTrue(output.contains("Submit report"), "Output should include task due today");
        assertTrue(output.contains("Prepare slides"), "Output should include task due tomorrow");

        // check output does not include task not due soon
        assertFalse(output.contains("Future task"), "Output should not include task not due soon");

        // remindCommand should not exit
        assertFalse(remindCommand.isExit());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
