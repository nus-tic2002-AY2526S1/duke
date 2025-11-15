package com.golden.exceptions.validationErrors;

import com.golden.exceptions.ValidationException;

public class IllegalArgumentException extends ValidationException {

    /**
     * Thrown when there is an error with the input arguments, which are found to be invalid.
     *
     * @param what 'what' describes the illegal argument.
     *
     */
    public IllegalArgumentException(String what) {
        super("Illegal argument: " + what);
    }
}