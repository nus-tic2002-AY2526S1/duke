package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.BotException;
import com.golden.task.Priority;
import com.golden.util.ParseHelper;

public class TodoCommand extends Command {
    private final String priorityPrefix = "/priority";

    private final String taskString;
    private final String priorityString;
    private Priority priority;

    public TodoCommand(String[] args) throws BotException {
        // Assumes that the existence of all args are checked/caught before reaching this constructor
        assert args != null && args.length == 2;
        this.taskString = args[0];
        this.priorityString = args[1];

        validatePrefixArgs(priorityString, priorityPrefix);
        String validPriorityString = extractArgFromPrefix(priorityString, priorityPrefix);
        this.priority = ParseHelper.convertStringToPriority(validPriorityString);
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws BotException {
        assert taskString != null
                && !taskString.isBlank()
                && priority != null;
        if (actions.addTodo(taskString, priority)){
            ui.printBotReply(actions.constructAddTaskMsg());
        } else {
            ui.printBotReply("Sorry, list is full!");
        }
    }
}
