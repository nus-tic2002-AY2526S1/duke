package whisperwind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import whisperwind.controller.TaskList;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.Task;
import whisperwind.model.Todo;
import whisperwind.model.Deadline;
import whisperwind.model.Event;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Whisperwind application
 * Covers all major requirements from the specification
 */
class WhisperwindTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    // ===== BASIC TASK OPERATIONS =====

    @Test
    void testAddTodo_validDescription_success() throws TaskException, CommandException {
        // Requirement: Add todo with valid description
        taskList.addTodo("Read programming book");
        assertEquals(1, taskList.getTaskCount());
        assertEquals("Read programming book", taskList.getTask(1).getDescription());
    }

    @Test
    void testAddTodo_emptyDescription_throwsException() {
        // Requirement: Add todo with empty description (should fail)
        assertThrows(CommandException.class, () -> taskList.addTodo(""));
    }

    @Test
    void testAddDeadline_futureDate_success() throws TaskException, CommandException {
        // Requirement: Add deadline with future date
        String input = "Submit report /by 25/12/2025 1800";
        taskList.addDeadline(input);
        assertEquals(1, taskList.getTaskCount());
        assertEquals("Submit report", taskList.getTask(1).getDescription());
    }

    @Test
    void testAddDeadline_pastDate_throwsException() {
        // Requirement: Add deadline with past date (should fail)
        String input = "Submit report /by 01/01/2020 1200";
        assertThrows(TaskException.class, () -> taskList.addDeadline(input));
    }

    @Test
    void testAddEvent_validTimeRange_success() throws TaskException, CommandException {
        // Requirement: Add event with valid time range
        String input = "Team meeting /from 25/12/2025 1400 /to 25/12/2025 1500";
        taskList.addEvent(input);
        assertEquals(1, taskList.getTaskCount());
        assertEquals("Team meeting", taskList.getTask(1).getDescription());
    }

    @Test
    void testAddEvent_invalidTimeRange_throwsException() {
        // Requirement: Add event with invalid time range (end before start)
        String input = "Invalid event /from 25/12/2025 1500 /to 25/12/2025 1400";
        assertThrows(TaskException.class, () -> taskList.addEvent(input));
    }

    // ===== MARK/UNMARK OPERATIONS =====

    @Test
    void testMarkTask_validTask_success() throws TaskException, CommandException {
        // Requirement: Mark task as done
        taskList.addTodo("Test task");
        assertFalse(taskList.getTask(1).isDone());

        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone());
    }

    @Test
    void testMarkTask_alreadyDone_throwsException() throws TaskException, CommandException {
        // Requirement: Mark already done task (should fail)
        taskList.addTodo("Test task");
        taskList.markTask(1);

        assertThrows(TaskException.class, () -> taskList.markTask(1));
    }

    @Test
    void testUnmarkTask_validTask_success() throws TaskException, CommandException {
        // Requirement: Unmark task
        taskList.addTodo("Test task");
        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone());

        taskList.unmarkTask(1);
        assertFalse(taskList.getTask(1).isDone());
    }

    @Test
    void testUnmarkTask_alreadyUndone_throwsException() throws TaskException, CommandException {
        // Requirement: Unmark not-done task (should fail)
        taskList.addTodo("Test task");
        assertThrows(TaskException.class, () -> taskList.unmarkTask(1));
    }

    // ===== SEARCH AND FIND OPERATIONS =====

    @Test
    void testFindTasks_byKeyword_returnsMatchingTasks() throws TaskException, CommandException {
        // Requirement: Find tasks by keyword
        taskList.addTodo("Read Java book");
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Java programming exercise");

        ArrayList<Task> results = taskList.findTasks("Java");
        assertEquals(2, results.size());
    }

    @Test
    void testFindTasks_noMatches_returnsEmptyList() throws TaskException, CommandException {
        // Requirement: Find tasks by keyword (no matches)
        taskList.addTodo("Read Java book");
        taskList.addTodo("Buy groceries");

        ArrayList<Task> results = taskList.findTasks("Python");
        assertEquals(0, results.size());
    }

    @Test
    void testDisplayMatchingTasks_withMatches_displaysResults() throws TaskException, CommandException {
        // Requirement: Pattern matching in searches
        taskList.addTodo("Read book");
        taskList.addTodo("Buy book");
        taskList.addTodo("Write novel");

        // This should display matching tasks without throwing exceptions
        assertDoesNotThrow(() -> taskList.displayMatchingTasks("book"));
    }

    // ===== DELETE OPERATIONS (Basic) =====

    @Test
    void testDeleteTask_validTask_success() throws TaskException, CommandException {
        // Requirement: Delete single task
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        assertEquals(2, taskList.getTaskCount());

        taskList.deleteTask(1);
        assertEquals(1, taskList.getTaskCount());
        assertEquals("Task 2", taskList.getTask(1).getDescription());
    }

    @Test
    void testDeleteTask_invalidTask_throwsException() {
        // Requirement: Delete single task (invalid)
        assertThrows(CommandException.class, () -> taskList.deleteTask(1));
    }

    // ===== TASK LIST MANAGEMENT =====

    @Test
    void testTaskListInitiallyEmpty() {
        // Basic requirement: New task list should be empty
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    void testTaskListAfterAddingTasks() throws TaskException, CommandException {
        // Basic requirement: Task list should reflect added tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");

        assertFalse(taskList.isEmpty());
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    void testGetTask_validIndex_returnsTask() throws TaskException, CommandException {
        // Basic requirement: Get task by valid index
        taskList.addTodo("Test task");
        Task task = taskList.getTask(1);

        assertNotNull(task);
        assertEquals("Test task", task.getDescription());
    }

    @Test
    void testGetTask_invalidIndex_returnsNull() throws TaskException, CommandException {
        // Basic requirement: Get task by invalid index
        taskList.addTodo("Test task");
        Task task = taskList.getTask(999);

        assertNull(task);
    }

    // ===== TASK VALIDATION =====

    @Test
    void testTaskValidation_validTasks_returnsTrue() throws TaskException, CommandException {
        // Requirement: Task integrity validation
        taskList.addTodo("Valid task 1");
        taskList.addTodo("Valid task 2");

        boolean isValid = taskList.validateTaskIntegrity();
        assertTrue(isValid);
    }

    // ===== COMPREHENSIVE WORKFLOW TEST =====

    @Test
    void testCompleteTaskWorkflow() throws TaskException, CommandException {
        // Test a complete workflow: Add → Mark → Find → Delete
        // 1. Add tasks
        taskList.addTodo("Buy milk");
        taskList.addTodo("Read book");
        taskList.addDeadline("Submit report /by 25/12/2025 1800");
        assertEquals(3, taskList.getTaskCount());

        // 2. Mark one as done
        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(3).isDone());

        // 3. Find tasks
        ArrayList<Task> bookTasks = taskList.findTasks("book");
        assertEquals(1, bookTasks.size());

        // 4. Delete a task
        taskList.deleteTask(2);
        assertEquals(2, taskList.getTaskCount());

        // 5. Verify remaining tasks
        assertEquals("Buy milk", taskList.getTask(1).getDescription());
        assertEquals("Submit report", taskList.getTask(2).getDescription());
    }

    // ===== EDGE CASES =====

    @Test
    void testAddMultipleTodos() throws TaskException, CommandException {
        // Test adding multiple todos
        for (int i = 1; i <= 5; i++) {
            taskList.addTodo("Task " + i);
        }
        assertEquals(5, taskList.getTaskCount());

        // Verify all tasks are accessible
        for (int i = 1; i <= 5; i++) {
            assertEquals("Task " + i, taskList.getTask(i).getDescription());
        }
    }

    @Test
    void testTaskDescriptionsWithSpecialCharacters() throws TaskException, CommandException {
        // Test tasks with various characters
        taskList.addTodo("Task with spaces");
        taskList.addTodo("Task-with-hyphens");
        taskList.addTodo("Task_with_underscores");
        taskList.addTodo("Task with numbers 123");

        assertEquals(4, taskList.getTaskCount());

        // Verify search works with partial matches
        ArrayList<Task> spaceTasks = taskList.findTasks("spaces");
        assertEquals(1, spaceTasks.size());

        ArrayList<Task> numberTasks = taskList.findTasks("123");
        assertEquals(1, numberTasks.size());
    }

    // ===== INTEGRATION STYLE TESTS =====

    @Test
    void testMixedTaskTypes() throws TaskException, CommandException {
        // Test working with mixed task types
        taskList.addTodo("Simple todo");
        taskList.addDeadline("Urgent deadline /by 25/12/2025 2359");
        taskList.addEvent("Team meeting /from 25/12/2025 1400 /to 25/12/2025 1500");

        assertEquals(3, taskList.getTaskCount());

        // Verify all tasks are valid
        assertTrue(taskList.validateTaskIntegrity());

        // Test finding across all task types
        ArrayList<Task> urgentTasks = taskList.findTasks("Urgent");
        assertEquals(1, urgentTasks.size());

        ArrayList<Task> teamTasks = taskList.findTasks("team");
        assertEquals(1, teamTasks.size());
    }

    @Test
    void testTaskStatePersistenceInMemory() throws TaskException, CommandException {
        // Test that task states are maintained correctly
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        // Mark some tasks
        taskList.markTask(2);

        // Verify states are correct
        assertFalse(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(3).isDone());

        // Unmark a task
        taskList.unmarkTask(2);
        assertFalse(taskList.getTask(2).isDone());
    }
}

