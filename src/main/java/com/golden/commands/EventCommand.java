package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.BotException;
import com.golden.task.Priority;
import com.golden.util.ParseHelper;
import com.golden.util.ValidationHelper;

import java.time.LocalDate;

public class EventCommand extends Command {
    private final String fromPrefix = "/from";
    private final String toPrefix = "/to";
    private final String priorityPrefix = "/priority";

    private final String taskString;
    private final String fromString;
    private final String toString;
    private final String priorityString;

    private LocalDate fromDate;
    private LocalDate toDate;
    private Priority priority;

    public EventCommand(String[] args) throws BotException {
        // Assumes that the existence of all args are checked/caught before reaching this constructor
        assert args != null && args.length == 4;
        this.taskString = args[0].trim();
        this.fromString = args[1].trim();
        this.toString = args[2].trim();
        this.priorityString = args[3].trim();

        validatePrefixArgs(fromString, fromPrefix);
        validatePrefixArgs(toString, toPrefix);
        validatePrefixArgs(priorityString, priorityPrefix);

        String validFromString = extractArgFromPrefix(fromString, fromPrefix);
        String validToString = extractArgFromPrefix(toString, toPrefix);
        String validPriorityString = extractArgFromPrefix(priorityString, priorityPrefix);

        this.fromDate = ParseHelper.convertStringToDate(validFromString);
        this.toDate = ParseHelper.convertStringToDate(validToString);
        this.priority = ParseHelper.convertStringToPriority(validPriorityString);
        ValidationHelper.validateDateOrder(fromDate, toDate);
    }

    @Override
    public void execute(BotActions actions, Ui ui) throws BotException {
        assert taskString != null
                && !taskString.isBlank()
                && fromDate != null
                && toDate != null
                && priority != null;
        ;
        if (actions.addEvent(taskString, fromDate, toDate, priority)){
            ui.printBotReply(actions.constructAddTaskMsg());
        } else {
            ui.printBotReply("Sorry, list is full!");
        }
    }
}
