package com.golden.exceptions;

public class ValidationException extends BotException {
    public ValidationException(String msg) {
        super(ErrorCode.VALIDATION_ERROR, msg);
    }
}