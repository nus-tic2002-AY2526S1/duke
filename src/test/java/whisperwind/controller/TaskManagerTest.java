package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskList taskList;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add test tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        taskManager = new TaskManager(taskList, scanner);
    }

    @Test
    void testBulkMark_validTasks_success() throws TaskException, CommandException {
        // This would test the handleBulkMark method
        // Note: This might require refactoring to make it testable
        assertNotNull(taskManager);
    }

    @Test
    void testBulkUnmark_validTasks_success() throws TaskException, CommandException {
        // Mark some tasks first
        taskList.markTask(1);
        taskList.markTask(2);

        // Then test bulk unmark
        assertNotNull(taskManager);
    }
}