package whisperwind.model;

import org.junit.jupiter.api.Test;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testCreateTodo_validDescription_success() throws TaskException {
        String description = "Read book";
        Todo todo = new Todo(description);
        assertNotNull(todo);
        assertEquals(description, todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testCreateTodo_emptyDescription_throwsException() {
        String emptyDescription = "";
        assertThrows(TaskException.class, () -> new Todo(emptyDescription));
    }

    @Test
    void testMarkAsDone_success() throws TaskException {
        Todo todo = new Todo("Test task");
        todo.markAsDone();
        assertTrue(todo.isDone());
    }

    @Test
    void testMarkAsDone_alreadyDone_throwsException() throws TaskException {
        Todo todo = new Todo("Test task");
        todo.markAsDone();
        assertThrows(TaskException.class, todo::markAsDone);
    }

    @Test
    void testToString_incompleteTask_containsBrackets() throws TaskException {
        Todo todo = new Todo("Test task");
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("Test task"));
    }

    @Test
    void testToString_completeTask_containsX() throws TaskException {
        Todo todo = new Todo("Test task");
        todo.markAsDone();
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("Test task"));
    }
}