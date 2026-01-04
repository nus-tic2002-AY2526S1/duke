package bongocat.tasks;

import bongocat.BongoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void addTask_validTask_taskIsAdded() throws Exception {
        TaskList list = new TaskList();
        ToDo todo = new ToDo("eat rice");

        list.addTask(todo);

        assertEquals(1, list.size());
        assertEquals(todo, list.getAllTasks().get(0));
    }

    @Test
    public void addTask_multipleTasks_correctOrder() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new ToDo("eat"));
        list.addTask(new ToDo("sleep"));
        list.addTask(new ToDo("code"));

        assertEquals("code", list.getAllTasks().get(2).getDescription());
    }
}
