package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.BotException;
import com.golden.task.Priority;
import com.golden.util.ParseHelper;

import java.time.LocalDate;

public class DeadlineCommand extends Command {
    private final String byPrefix = "/by";
    private final String priorityPrefix = "/priority";

    private final String taskString;
    private final String byString;
    private final String priorityString;

    private LocalDate byDate;
    private Priority priority;

    public DeadlineCommand(String[] args) throws BotException {
        // Assumes that the existence of all args are checked/caught before reaching this constructor
        assert args != null && args.length == 3;
        assert args[1] != null;
        this.taskString = args[0];
        this.byString = args[1];
        this.priorityString = args[2];

        validatePrefixArgs(byString, byPrefix);
        validatePrefixArgs(priorityString, priorityPrefix);
        String validByString = extractArgFromPrefix(byString, byPrefix);
        String validPriorityString = extractArgFromPrefix(priorityString, priorityPrefix);
        this.byDate = ParseHelper.convertStringToDate(validByString);
        this.priority = ParseHelper.convertStringToPriority(validPriorityString);
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws BotException {
        assert taskString != null
                && !taskString.isBlank()
                && byDate != null
                && priority != null;
        if (actions.addDeadline(taskString, byDate, priority)){
            ui.printBotReply(actions.constructAddTaskMsg());
        } else {
            ui.printBotReply("Sorry, list is full!");
        }
    }
}
