package kev.command;

import kev.task.TaskList;
import kev.task.Todo;
import kev.ui.Ui;
import kev.storage.Storage;
import kev.exception.KevException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddCommandTest {

    @Test
    void execute_addTodoTask_taskAdded() throws KevException {
        TaskList tasks = new TaskList();
        Ui ui = new Ui();
        Storage storage = new Storage("data/test.txt");

        AddCommand cmd = new AddCommand("todo Read book");
        cmd.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(tasks.getTask(0) instanceof Todo);
        assertEquals(" Read book", tasks.getTask(0).toString().substring(6)); // skip [T][ ] prefix
    }
}
