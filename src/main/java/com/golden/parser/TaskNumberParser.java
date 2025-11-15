package com.golden.parser;

import com.golden.exceptions.BotException;
import com.golden.exceptions.validationErrors.IllegalArgumentException;
import com.golden.util.ParseHelper;

public class TaskNumberParser {
    private TaskNumberParser(){}

    /**
     *  Checks that the string input contains exactly 1 valid number of Int type.
     *
     *  @param s String that contains the number.
     *
     *  @return the int-type number parsed from this string input or {@code Throwable} Exception.
     *  */
    public static int parseNumber(String s) throws BotException {
        String arg = s.trim();
        String[] markArgParts = ParseHelper.splitOnWhitespaces(arg);
        ParseHelper.requireArgs(markArgParts, 1, "\nPlease provide one task number.");

        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("'%s'.\nPlease provide a valid task number.", arg));
        }
    }
}