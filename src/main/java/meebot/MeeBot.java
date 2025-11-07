package meebot;

import logic.command.Command;
import common.ErrorMessage;
import model.TaskManager;
import common.message.Message;
import model.CommandProcessor;
import model.StorageManager;

/**
 * Provide a unified backend API for both CLI and GUI.
 */
public class MeeBot {
    private final CommandProcessor processor;
    private final StorageManager storage;

    public MeeBot(TaskManager tm, StorageManager storage) {
        this.processor = new CommandProcessor(tm);
        this.storage = storage;
        storage.loadTasks();
    }

    /**
     * Shared execution path so both CLI and GUI use identical command logic.
     */
    public ExecutionResult execute(String input) {
        try {
            Command cmd = processor.parseCommand(input);
            Message msg = cmd.execute();
            storage.saveTasks();
            return new ExecutionResult(msg, cmd.isExit());
        } catch (RuntimeException e) {
            return new ExecutionResult(new ErrorMessage(ErrorMessage.CATCH_ALL), false);
        }
    }

    public String getResponse(String input) {
        try {
            return execute(input).output().message();
        } catch (RuntimeException e) {
            return new ErrorMessage("Mee-stakes happen lah, try again!").message();
        }
    }

    public record ExecutionResult(Message output, boolean isExit) {
    }

    public ExecutionResult getExecutionResult(String input) {
        return execute(input);
    }
}
