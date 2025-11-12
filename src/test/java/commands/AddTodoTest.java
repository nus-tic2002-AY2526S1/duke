package commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import task.TaskList;
import task.Todo;
import testutils.DummyStorage;
import testutils.DummyUi;

public class AddTodoTest {

    @Test
    public void testAddTodo() {
        TaskList tasks = new TaskList();
        DummyUi ui = new DummyUi();
        DummyStorage storage = new DummyStorage();

        AddTodoCommand command = new AddTodoCommand("test");
        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("test", ((Todo) tasks.get(0)).getDescription());
    }
}
