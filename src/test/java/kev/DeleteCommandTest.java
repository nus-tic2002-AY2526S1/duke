package kev.command;

import kev.task.*;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCommandTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    void setUp() throws KevException {
        tasks = new TaskList();
        tasks.addTask(new Todo("Read book"));
        tasks.addTask(new Todo("Write code"));
        ui = new Ui();
        storage = new Storage("data/test_delete.txt");

        File f = new File("data/test_delete.txt");
        f.getParentFile().mkdirs();
    }

    @AfterEach
    void tearDown() {
        File f = new File("data/test_delete.txt");
        if (f.exists()) f.delete();
    }

    @Test
    void execute_deleteTask_taskRemoved() throws KevException {
        DeleteCommand command = new DeleteCommand(0); // delete first task
        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals(" Write code", tasks.get(0).toString().substring(6)); // skip "[T][ ] "
    }
}
