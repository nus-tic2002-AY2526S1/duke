package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for DeleteManager functionality.
 */
class DeleteManagerTest {
    private TaskList taskList;
    private DeleteManager deleteManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add some test tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        Scanner scanner = new Scanner(System.in);
        deleteManager = new DeleteManager(taskList, scanner);
    }

    @Test
    void testDeleteManagerCreation() {
        assertNotNull(deleteManager, "DeleteManager should be initialized successfully");
    }

    @Test
    void testShowDeleteHelp_doesNotThrow() {
        assertDoesNotThrow(() -> deleteManager.showDeleteHelp(),
                "showDeleteHelp() should not throw any exceptions");
    }

    // TODO: Add tests for deletion behavior through public methods once user confirmation logic is implemented
}
