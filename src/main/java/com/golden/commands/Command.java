package com.golden.commands;

import com.golden.core.BotActions;
import com.golden.core.Ui;
import com.golden.exceptions.BotException;
import com.golden.exceptions.validationErrors.IllegalArgumentException;

public abstract class Command {
    private final boolean isGoodbye;

    protected Command(){
        this.isGoodbye = false;
    }
    protected Command(boolean isGoodbye){
        this.isGoodbye = isGoodbye;
    }

    public boolean isGoodbye(){
        return this.isGoodbye;
    }

    public abstract void execute(BotActions actions, Ui ui) throws BotException;

    /**
     * Checks that the prefix is contained within the String input.
     * @param string The input to check against.
     * @param prefix The prefix that is part of the required structure.
     * @return True if prefix is found at the head of the input.
     * @throws IllegalArgumentException
     */
    protected boolean validatePrefixArgs(String string, String prefix) throws IllegalArgumentException{
        if (!string.startsWith(prefix)){
            throw new IllegalArgumentException(String.format("'%s'. \n Please enter: '%s...'", string, prefix));
        }
        return true;
    }
    protected String extractArgFromPrefix(String string, String prefix) {
        return string.trim().substring(prefix.length()).trim();
    }
}
