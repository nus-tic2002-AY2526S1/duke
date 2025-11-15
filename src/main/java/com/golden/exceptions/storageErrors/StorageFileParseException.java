package com.golden.exceptions.storageErrors;

import com.golden.exceptions.StorageException;

public class StorageFileParseException extends StorageException {
    public StorageFileParseException(String msg){
        super(msg);
    }
}
