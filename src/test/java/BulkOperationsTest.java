package whisperwind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.controller.TaskList;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for bulk operations and advanced functionality
 */
class BulkOperationsTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Setup multiple tasks for bulk operations
        for (int i = 1; i <= 10; i++) {
            taskList.addTodo("Task " + i);
        }
    }

    @Test
    void testBulkMarkSimulation() throws TaskException, CommandException {
        // Simulate bulk mark operations by marking multiple tasks individually
        // Requirement: Bulk mark operations
        taskList.markTask(1);
        taskList.markTask(3);
        taskList.markTask(5);

        assertTrue(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(2).isDone());
        assertTrue(taskList.getTask(3).isDone());
        assertFalse(taskList.getTask(4).isDone());
        assertTrue(taskList.getTask(5).isDone());
    }

    @Test
    void testMultipleDeleteOperations() throws TaskException, CommandException {
        // Test multiple delete operations
        // Requirement: Delete multiple tasks
        assertEquals(10, taskList.getTaskCount());

        // Delete some tasks
        taskList.deleteTask(3);
        taskList.deleteTask(5);
        taskList.deleteTask(7);

        assertEquals(7, taskList.getTaskCount());

        // Verify remaining tasks are renumbered correctly
        assertEquals("Task 1", taskList.getTask(1).getDescription());
        assertEquals("Task 2", taskList.getTask(2).getDescription());
        assertEquals("Task 4", taskList.getTask(3).getDescription()); // Original task 4 is now at position 3
    }

    @Test
    void testFindWithMultipleMatches() throws TaskException, CommandException {
        // Test finding tasks with multiple matches
        taskList.addTodo("Read Java book");
        taskList.addTodo("Java programming");
        taskList.addTodo("Learn Java streams");
        taskList.addTodo("Buy groceries");

        var results = taskList.findTasks("Java");
        assertEquals(3, results.size());
    }

    @Test
    void testEmptySearchResults() throws TaskException, CommandException {
        // Test search with no results
        taskList.addTodo("Read book");
        taskList.addTodo("Write code");

        var results = taskList.findTasks("nonexistent");
        assertEquals(0, results.size());
    }
}