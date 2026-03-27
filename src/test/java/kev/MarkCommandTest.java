package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MarkCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    void setUp() throws KevException {
        tasks = new TaskList();
        tasks.addTask(new Todo("Read book"));
        ui = new Ui();
        storage = new Storage("data/test_mark.txt");

        File f = new File("data/test_mark.txt");
        f.getParentFile().mkdirs();
    }

    @AfterEach
    void tearDown() {
        File f = new File("data/test_mark.txt");
        if (f.exists()) f.delete();
    }

    @Test
    void execute_markTask_setsDoneTrue() throws KevException {
        MarkCommand command = new MarkCommand(0); // mark first task
        command.execute(tasks, ui, storage);

        assertEquals("X", tasks.get(0).getStatusIcon());
    }
}
