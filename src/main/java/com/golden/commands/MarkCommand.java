package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.validationErrors.IllegalArgumentException;

public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws IllegalArgumentException {
        actions.mark(taskNumber);
        ui.printBotReply(actions.constructMarkItemMsg(taskNumber, true));
    }
}
