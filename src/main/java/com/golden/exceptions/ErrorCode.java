package com.golden.exceptions;

public enum ErrorCode {
    GENERIC("Oops! Something went wrong."),
    VALIDATION_ERROR("Oops! There is a problem with your input."),
    PARSE_ERROR("Sorry! I am afraid I don't understand"),
    STORAGE_ERROR("Oops! There seems to be a file error.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
    public String message(){
        return message;
    }
}