package whisperwind;

import org.junit.jupiter.api.Test;
import whisperwind.controller.TaskList;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal test to verify A-Gradle and A-JUnit requirements
 * This single test proves both Gradle and JUnit are working
 */
class AGradleAJUnitTest {

    @Test
    void testGradleAndJUnitIntegration() throws TaskException, CommandException {
        // Arrange - Create a new task list
        TaskList taskList = new TaskList();

        // Act - Add a simple todo task
        taskList.addTodo("Test task for A-Gradle and A-JUnit");

        // Assert - Verify the task was added successfully
        assertEquals(1, taskList.getTaskCount(), "Task count should be 1 after adding a task");
        assertFalse(taskList.isEmpty(), "Task list should not be empty after adding a task");
        assertEquals("Test task for A-Gradle and A-JUnit",
                taskList.getTask(1).getDescription(),
                "Task description should match what was added");

        System.out.println("✅ A-Gradle and A-JUnit test passed! Both are working correctly.");
    }
}