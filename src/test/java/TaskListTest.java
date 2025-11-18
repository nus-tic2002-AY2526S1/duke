import chatbot.DateTimeParser;
import chatbot.QianException;
import chatbot.tasks.Todo;
import chatbot.TaskList;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    void testAddTodo() {
        TaskList list = new TaskList();
        list.addTask(new Todo("read book"));
        assertEquals(1, list.size());
    }

    @Test
    void testMarkTask() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new Todo("read book"));
        list.markTask(0);
        assertTrue(list.getTasks().get(0).isDone);
    }

    @Test
    void testPriorityUpdate() throws Exception {
        TaskList list = new TaskList();
        list.addTask(new Todo("read book"));
        list.updatePriority(0, 3);
        assertEquals(3, list.getTasks().get(0).getPriority());
    }

    @Test
    public void testParseDateTime() throws QianException {
        LocalDateTime t = DateTimeParser.parse("2025-11-16 0900");
        assertEquals(2025, t.getYear());
    }
}
