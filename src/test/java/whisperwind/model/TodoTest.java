package whisperwind.model;

import org.junit.jupiter.api.Test;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testCreateTodo_validDescription_success() throws TaskException {
        // Arrange
        String description = "Read book";

        // Act
        Todo todo = new Todo(description);

        // Assert
        assertNotNull(todo);
        assertEquals(description, todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    void testCreateTodo_emptyDescription_throwsException() {
        // Arrange
        String emptyDescription = "";

        // Act & Assert
        assertThrows(TaskException.class, () -> new Todo(emptyDescription));
    }

    @Test
    void testMarkAsDone_success() throws TaskException {
        // Arrange
        Todo todo = new Todo("Test task");

        // Act
        todo.markAsDone();

        // Assert
        assertTrue(todo.isDone());
    }

    @Test
    void testMarkAsDone_alreadyDone_throwsException() throws TaskException {
        // Arrange
        Todo todo = new Todo("Test task");
        todo.markAsDone();

        // Act & Assert
        assertThrows(TaskException.class, todo::markAsDone);
    }

    @Test
    void testToString_incompleteTask_containsBrackets() throws TaskException {
        // Arrange
        Todo todo = new Todo("Test task");

        // Act
        String result = todo.toString();

        // Assert
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("Test task"));
    }

    @Test
    void testToString_completeTask_containsX() throws TaskException {
        // Arrange
        Todo todo = new Todo("Test task");
        todo.markAsDone();

        // Act
        String result = todo.toString();

        // Assert
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("Test task"));
    }
}