package com.golden.exceptions.parseErrors;

import com.golden.exceptions.ParseException;

public class MissingArgumentException extends ParseException {

    /**
     * Describes a parse error when a certain string is expected but not found.
     *
     * @param what describes "what" is missing?
     *
     */
    public MissingArgumentException(String what) {
        super(what);
    }
}
