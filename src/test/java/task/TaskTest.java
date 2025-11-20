package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Task} class.
 */
public class TaskTest {

    @Test
    public void testTaskCreation_defaultIsNotDone() {
        Task task = new Task("read book");
        assertEquals("read book", task.getDescription(), "Description should be stored correctly");
        assertFalse(task.isDone(), "New task should not be marked as done by default");
    }

    @Test
    public void testMarkDone_changesStatusAndIcon() {
        Task task = new Task("read book");
        task.markDone();
        assertTrue(task.isDone(), "Task should be marked as done after calling markDone()");
        assertEquals("X", task.getStatusIcon(), "Status icon should be 'X' when done");
    }

    @Test
    public void testMarkUndone_changesStatusBack() {
        Task task = new Task("read book");
        task.markDone();
        task.markUndone();
        assertFalse(task.isDone(), "Task should be marked as not done after calling markUndone()");
        assertEquals(" ", task.getStatusIcon(), "Status icon should be a space when not done");
    }

    @Test
    public void testToString_reflectsStatusAndDescription() {
        Task task = new Task("read book");
        assertEquals("[ ] read book", task.toString(), "toString() should reflect undone task state");

        task.markDone();
        assertEquals("[X] read book", task.toString(), "toString() should reflect done task state");
    }
}
