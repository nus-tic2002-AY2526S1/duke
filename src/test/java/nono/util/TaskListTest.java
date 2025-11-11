package nono.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import nono.exception.UserInputException;
import nono.task.Deadline;
import nono.task.Event;
import nono.task.ToDo;

public class TaskListTest {

    @Test
    public void addTask_success() throws UserInputException {
        TaskList list = new TaskList(new ArrayList<>());
        list.addTask(new ToDo("sleep well"));
        assertEquals(1, list.size());
    }

    @Test
    public void deleteTask_success() throws UserInputException {
        TaskList list = new TaskList(new ArrayList<>());
        list.addTask(new Deadline("return book", "2025-10-30 23:59"));
        list.deleteTask(0);
        assertEquals(0, list.size());
    }

    @Test
    public void markTask_success() throws UserInputException {
        TaskList list = new TaskList(new ArrayList<>());
        list.addTask(new Event("exam", "2025-11-25 00:00", "2025-12-06 00:00"));
        list.markTask(0, true);
        assertEquals("X", list.getTask(0).getStatusIcon());
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() {
        TaskList list = new TaskList(new ArrayList<>());
        assertThrows(UserInputException.class, () -> list.deleteTask(3));
    }
}
