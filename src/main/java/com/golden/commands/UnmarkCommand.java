package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.validationErrors.IllegalArgumentException;

public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) throws IllegalArgumentException {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws IllegalArgumentException {
        actions.unmark(taskNumber);
        ui.printBotReply(actions.constructMarkItemMsg(taskNumber, false));
    }
}
