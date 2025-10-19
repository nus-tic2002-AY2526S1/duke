package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void testCompleteTaskLifecycle() throws TaskException, CommandException {
        // 1. Add task
        taskList.addTodo("Integration test task");
        assertEquals(1, taskList.getTaskCount());
        assertFalse(taskList.getTask(1).isDone());

        // 2. Mark as done
        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone());

        // 3. Find the task
        ArrayList<Task> results = taskList.findTasks("Integration");
        assertEquals(1, results.size());

        // 4. Delete the task
        taskList.deleteTask(1);
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    void testMultipleTaskOperations() throws TaskException, CommandException {
        // Add multiple tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");
        assertEquals(3, taskList.getTaskCount());

        // Mark one as done
        taskList.markTask(2);
        assertTrue(taskList.getTask(2).isDone());

        // Find tasks
        ArrayList<Task> results = taskList.findTasks("Task");
        assertEquals(3, results.size());

        // Verify task order and content
        assertEquals("Task 1", taskList.getTask(1).getDescription());
        assertEquals("Task 2", taskList.getTask(2).getDescription());
        assertEquals("Task 3", taskList.getTask(3).getDescription());
    }
}