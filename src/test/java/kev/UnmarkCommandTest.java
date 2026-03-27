package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class UnmarkCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    void setUp() throws KevException {
        tasks = new TaskList();
        Todo task = new Todo("Read book");
        task.markAsDone(); // initially done
        tasks.addTask(task);
        ui = new Ui();
        storage = new Storage("data/test_unmark.txt");

        File f = new File("data/test_unmark.txt");
        f.getParentFile().mkdirs();
    }

    @AfterEach
    void tearDown() {
        File f = new File("data/test_unmark.txt");
        if (f.exists()) f.delete();
    }

    @Test
    void execute_unmarkTask_setsDoneFalse() throws KevException {
        UnmarkCommand command = new UnmarkCommand(0); // unmark first task
        command.execute(tasks, ui, storage);

        assertEquals(" ", tasks.get(0).getStatusIcon());
    }
}
