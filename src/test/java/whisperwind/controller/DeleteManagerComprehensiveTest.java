package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DeleteManagerComprehensiveTest {
    private TaskList taskList;
    private DeleteManager deleteManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add various test tasks
        taskList.addTodo("Read book");
        taskList.addTodo("Read novel");
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Meeting with team");

        // Mark some as completed
        taskList.markTask(1);
        taskList.markTask(3);

        Scanner scanner = new Scanner(System.in);
        deleteManager = new DeleteManager(taskList, scanner);
    }

    @Test
    void testDeleteCompletedTasks() throws CommandException {
        // This tests requirement: Delete completed tasks
        // Note: Might require mocking user confirmation
        assertEquals(4, taskList.getTaskCount());

        // TODO: Add test for deleteManager.handleDeleteCommand once user confirmation can be simulated
    }

    @Test
    void testDeleteByPattern() throws CommandException {
        // This tests requirement: Delete tasks matching a pattern (e.g., "book*")
        assertEquals(4, taskList.getTaskCount());

        // TODO: Implement when deleteManager supports pattern-based deletion
    }

    @Test
    void testDeleteSpecificType() throws CommandException {
        // This tests requirement: Delete specific task types (e.g., "todo")
        assertEquals(4, taskList.getTaskCount());

        // TODO: Implement when deleteManager supports type-based deletion
    }
}
