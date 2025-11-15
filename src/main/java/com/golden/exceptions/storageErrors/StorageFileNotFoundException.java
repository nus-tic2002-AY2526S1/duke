package com.golden.exceptions.storageErrors;

import com.golden.exceptions.StorageException;

public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String msg) {
        super(msg);
    }
}