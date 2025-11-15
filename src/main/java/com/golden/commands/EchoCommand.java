package com.golden.commands;


import com.golden.core.BotActions;
import com.golden.core.Ui;

public class EchoCommand extends Command {
    private final String echoLine;

    public EchoCommand(String commandString){
        this.echoLine = commandString;
    }

    @Override
    public void execute(BotActions actions, Ui ui) {
        ui.printBotReply(echoLine);
    }
}