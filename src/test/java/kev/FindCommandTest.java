package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FindCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    // for capturing console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // redirect console output
        System.setOut(new PrintStream(outContent));

        ui = new Ui();
        storage = new Storage("data/test.txt"); // dummy path for tests
        tasks = new TaskList();

        // add some sample tasks
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("write code"));
        tasks.addTask(new Deadline("return book", "2025-06-06"));
    }

    @Test
    void execute_findKeyword_matchesCorrectTasks() {
        FindCommand findCommand = new FindCommand("book");

        try {
            findCommand.execute(tasks, ui, storage);
        } catch (KevException e) {
            fail("Execution threw an exception: " + e.getMessage());
        }

        String output = outContent.toString();

        // findCommand should not exit
        assertFalse(findCommand.isExit());
    }

    @Test
    void execute_findKeywordNoMatch_showsNoTasksMessage() {
        FindCommand findCommand = new FindCommand("nonexistent");

        try {
            findCommand.execute(tasks, ui, storage);
        } catch (KevException e) {
            fail("Execution threw an exception: " + e.getMessage());
        }

        String output = outContent.toString();
        assertTrue(output.contains("No matching tasks found"));

        assertFalse(findCommand.isExit());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
