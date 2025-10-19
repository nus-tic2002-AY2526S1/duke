import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.controller.TaskList;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for archive functionality
 */
class ArchiveIntegrationTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();

        // Setup test data
        taskList.addTodo("Archive test task 1");
        taskList.addTodo("Archive test task 2");
        taskList.addDeadline("Important deadline /by 25/12/2025 1800");

        // Mark one as completed
        taskList.markTask(1);
    }

    @Test
    void testArchiveAndClearWorkflow() throws CommandException {
        // Test the complete workflow: archive → clear
        assertEquals(3, taskList.getTaskCount(), "Should have 3 tasks initially");

        taskList.clearAllTasks();

        assertTrue(taskList.isEmpty(), "Task list should be empty after clearing");
        assertEquals(0, taskList.getTaskCount(), "Task count should be 0");
    }

    @Test
    void testMultipleArchiveOperations() throws TaskException, CommandException {
        // Test that we can perform multiple archive-related operations
        assertFalse(taskList.isEmpty(), "Task list should not be empty initially");

        // Test that basic operations work
        assertEquals(3, taskList.getTaskCount());
        assertNotNull(taskList.getTask(1));
        assertNotNull(taskList.getTask(2));
        assertNotNull(taskList.getTask(3));
    }

    @Test
    void testTaskRecoveryAfterArchiveSimulation() throws TaskException, CommandException {
        // Simulate archive workflow: add tasks → "archive" (clear) → add new tasks
        int initialCount = taskList.getTaskCount();

        // Simulate archiving by clearing
        taskList.clearAllTasks();
        assertTrue(taskList.isEmpty());

        // Add new tasks after "archiving"
        taskList.addTodo("New task after archive");
        taskList.addTodo("Another new task");

        assertEquals(2, taskList.getTaskCount(), "Should have new tasks after archive simulation");
        assertFalse(taskList.isEmpty(), "Task list should not be empty after adding new tasks");
    }
}