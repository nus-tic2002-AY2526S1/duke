package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TaskList operations across full lifecycle.
 */
class IntegrationTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void shouldCompleteFullTaskLifecycle() throws TaskException, CommandException {
        // 1. Add a new task
        taskList.addTodo("Integration test task");
        assertEquals(1, taskList.getTaskCount(), "Task count should be 1 after adding a task");
        assertFalse(taskList.getTask(1).isDone(), "Newly added task should not be marked as done");

        // 2. Mark it as done
        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone(), "Task should be marked as done after markTask()");

        // 3. Find the task
        ArrayList<Task> results = taskList.findTasks("Integration");
        assertEquals(1, results.size(), "Should find one matching task when searching 'Integration'");

        // 4. Delete the task
        taskList.deleteTask(1);
        assertEquals(0, taskList.getTaskCount(), "Task count should be 0 after deletion");
    }

    @Test
    void shouldHandleMultipleTaskOperationsCorrectly() throws TaskException, CommandException {
        // Add multiple tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");
        assertEquals(3, taskList.getTaskCount(), "Should have 3 tasks after adding them");

        // Mark one as done
        taskList.markTask(2);
        assertTrue(taskList.getTask(2).isDone(), "Task 2 should be marked as done");

        // Find tasks by keyword
        ArrayList<Task> results = taskList.findTasks("Task");
        assertEquals(3, results.size(), "Should find all 3 tasks when searching 'Task'");

        // Verify order and content
        assertEquals("Task 1", taskList.getTask(1).getDescription(), "First task description should match");
        assertEquals("Task 2", taskList.getTask(2).getDescription(), "Second task description should match");
        assertEquals("Task 3", taskList.getTask(3).getDescription(), "Third task description should match");
    }
}
