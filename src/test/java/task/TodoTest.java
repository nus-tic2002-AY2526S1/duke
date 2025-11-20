package task; //same package as the class being tested

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Todo} class.
 */
public class TodoTest {
    @Test
    public void testTodoCreation_storesDescriptionCorrectly() {
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getDescription(),
                "Todo should store the description passed into its constructor");
    }

    @Test
    public void testToString_returnsCorrectFormat() {
        Todo todo = new Todo("read book");
        assertTrue(todo.toString().startsWith("[T]"),
                "Todo string representation should start with [T]");
    }

    @Test
    public void testTodoIsInstanceOfTask() {
        Todo todo = new Todo("read book");
        assertTrue(todo instanceof Task, "Todo should be a subclass of Task");
    }
}
