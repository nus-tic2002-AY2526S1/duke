package meebot;

import command.Command;
import common.ErrorMessage;
import manager.TaskManager;
import message.Message;
import parser.commandargs.CommandProcessor;
import storage.Storage;

/**
 * Provide a unified backend API for both CLI and GUI.
 */
public class MeeBot {
    private final CommandProcessor processor;
    private final Storage storage;

    public MeeBot(TaskManager tm, Storage storage) {
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

    public record ExecutionResult(Message output, boolean exit) {
    }
}
