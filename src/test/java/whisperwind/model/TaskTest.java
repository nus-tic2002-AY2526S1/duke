package whisperwind.model;

import org.junit.jupiter.api.Test;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

// Test the abstract Task class using Todo as concrete implementation
class TaskTest {

    @Test
    void testTaskCreation_validDescription_success() throws TaskException {
        // Arrange & Act
        Task task = new Todo("Test task");

        // Assert
        assertEquals("Test task", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void testSetDescription_validDescription_success() throws TaskException {
        // Arrange
        Task task = new Todo("Original description");

        // Act
        task.setDescription("New description");

        // Assert
        assertEquals("New description", task.getDescription());
    }

    @Test
    void testSetDescription_emptyDescription_throwsException() throws TaskException {
        // Arrange
        Task task = new Todo("Original description");

        // Act & Assert
        assertThrows(TaskException.class, () -> task.setDescription(""));
    }

    @Test
    void testGetStatusIcon_notDone_returnsEmptyBrackets() throws TaskException {
        // Arrange
        Task task = new Todo("Test task");

        // Act & Assert
        assertEquals("[ ]", task.getStatusIcon());
    }

    @Test
    void testGetStatusIcon_done_returnsXBrackets() throws TaskException {
        // Arrange
        Task task = new Todo("Test task");
        task.markAsDone();

        // Act & Assert
        assertEquals("[X]", task.getStatusIcon());
    }
}