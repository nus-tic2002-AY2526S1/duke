package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.validationErrors.IllegalArgumentException;

public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws IllegalArgumentException {
        String taskToBeDeleted = actions.getTaskString(taskNumber);
        actions.delete(taskNumber);
        ui.printBotReply(actions.constructDeletedTaskMsg(taskToBeDeleted));
    }
}
