package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;

public class ListCommand extends Command{
    public ListCommand(){
    }

    @Override
    public void execute(BotActions actions, Ui ui) {
        ui.printBotReply(actions.listTasks());
    }
}
