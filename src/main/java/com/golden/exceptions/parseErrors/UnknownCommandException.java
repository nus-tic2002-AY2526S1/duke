package com.golden.exceptions.parseErrors;


import com.golden.exceptions.ParseException;

/**
 * Describes a parse error when trying to read an unrecognisable command.
 *
 * @param cmd 'cmd' describes the unknown command.
 *
 */
public class UnknownCommandException extends ParseException {
    public UnknownCommandException(String cmd){
        super(String.format("unknown command '%s'.", cmd));
    }

}
