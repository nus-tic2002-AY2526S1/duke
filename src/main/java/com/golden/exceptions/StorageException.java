package com.golden.exceptions;

public class StorageException extends BotException{
    public StorageException(String msg) {
        super(ErrorCode.STORAGE_ERROR, msg);
    }
}