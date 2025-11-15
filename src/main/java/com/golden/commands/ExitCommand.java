package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.storageErrors.StorageFileParseException;

public class ExitCommand extends Command {
    public ExitCommand(){
        super(true);
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws StorageFileParseException {
        actions.saveTaskList();
        ui.printBotReply("Goodbye! See you soon!");
    }
}
