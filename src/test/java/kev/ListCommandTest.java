package kev.command;

import kev.task.TaskList;
import kev.task.Todo;
import kev.ui.Ui;
import kev.storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListCommandTest {

    @Test
    public void execute_listCommand_noError() {
        TaskList list = new TaskList();
        list.addTask(new Todo("X"));

        ListCommand cmd = new ListCommand();

        assertDoesNotThrow(() ->
                cmd.execute(list, new Ui(), new Storage("test.txt")));
    }
}
