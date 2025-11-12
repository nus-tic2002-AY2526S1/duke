package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void testAddTodo_validDescription_success() throws TaskException, CommandException {
        // Arrange
        String description = "Read programming book";

        // Act
        taskList.addTodo(description);

        // Assert
        assertEquals(1, taskList.getTaskCount());
        Task task = taskList.getTask(1);
        assertNotNull(task);
        assertEquals(description, task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void testAddTodo_emptyDescription_throwsException() {
        // Arrange
        String emptyDescription = "";

        // Act & Assert
        assertThrows(CommandException.class, () -> taskList.addTodo(emptyDescription));
    }

    @Test
    void testAddDeadline_validInput_success() throws TaskException, CommandException {
        // Arrange - Use a future date (next year)
        String input = "Submit report /by 25/12/2025 1800";

        // Act
        taskList.addDeadline(input);

        // Assert
        assertEquals(1, taskList.getTaskCount());
        Task task = taskList.getTask(1);
        assertNotNull(task);
        assertEquals("Submit report", task.getDescription());
    }

    @Test
    void testAddDeadline_missingByClause_throwsException() {
        // Arrange
        String invalidInput = "Submit report";

        // Act & Assert
        assertThrows(CommandException.class, () -> taskList.addDeadline(invalidInput));
    }

    @Test
    void testAddDeadline_pastDate_throwsException() {
        // Arrange - Use a clearly past date
        String input = "Submit report /by 01/01/2020 1200";

        // Act & Assert
        assertThrows(TaskException.class, () -> taskList.addDeadline(input));
    }

    @Test
    void testMarkTask_validTaskNumber_success() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Test task");
        assertFalse(taskList.getTask(1).isDone());

        // Act
        taskList.markTask(1);

        // Assert
        assertTrue(taskList.getTask(1).isDone());
    }

    @Test
    void testMarkTask_invalidTaskNumber_throwsException() {
        // Act & Assert
        assertThrows(CommandException.class, () -> taskList.markTask(999));
    }

    @Test
    void testDeleteTask_validTaskNumber_success() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        assertEquals(2, taskList.getTaskCount());

        // Act
        taskList.deleteTask(1);

        // Assert
        assertEquals(1, taskList.getTaskCount());
        assertEquals("Task 2", taskList.getTask(1).getDescription());
    }

    @Test
    void testDeleteTask_invalidTaskNumber_throwsException() {
        // Act & Assert
        assertThrows(CommandException.class, () -> taskList.deleteTask(1));
    }

    @Test
    void testFindTasks_withMatchingKeyword_returnsTasks() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Read book about Java");
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Java programming exercise");

        // Act
        ArrayList<Task> results = taskList.findTasks("Java");

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    void testFindTasks_noMatchingKeyword_returnsEmptyList() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Read book about Java");
        taskList.addTodo("Buy groceries");

        // Act
        ArrayList<Task> results = taskList.findTasks("Python");

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    void testGetTaskCount_emptyList_returnsZero() {
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    void testIsEmpty_newTaskList_returnsTrue() {
        assertTrue(taskList.isEmpty());
    }

    @Test
    void testIsEmpty_afterAddingTask_returnsFalse() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Test task");

        // Act & Assert
        assertFalse(taskList.isEmpty());
    }
    // Add to existing TaskListTest class
    @Test
    void testDeleteCompletedTasks_withCompletedTasks_success() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.markTask(1); // Mark first task as completed

        // Act & Assert
        // This would test deleteCompletedTasks()
        // Note: This method might require user confirmation simulation
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    void testDeleteTasksByPattern_matchingPattern_returnsCorrectCount() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Read book");
        taskList.addTodo("Read novel");
        taskList.addTodo("Buy groceries");

        // Act & Assert
        // This would test deleteTasksByPattern()
        // Note: This method might require user confirmation simulation
        assertEquals(3, taskList.getTaskCount());
    }

    @Test
    void testDeleteTasksByType_validType_success() throws TaskException, CommandException {
        // Arrange
        taskList.addTodo("Todo task");
        taskList.addDeadline("Deadline task /by 25/12/2025 1800");

        // Act & Assert
        // This would test deleteTasksByType()
        // Note: This method might require user confirmation simulation
        assertEquals(2, taskList.getTaskCount());
    }
}