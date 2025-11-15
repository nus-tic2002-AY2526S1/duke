package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.BotException;
import com.golden.task.Task;
import com.golden.util.FormatHelper;

import java.util.List;

public class FindCommand extends Command {
    private final String parameters;

    public FindCommand(String searchString){
        this.parameters = searchString;
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws BotException {
        List<Task> matches = actions.searchTasks(parameters);
        String matchesString = FormatHelper.renderTaskListToString(matches);
        ui.printBotReply(matchesString);
    }
}