package com.tickshow.backend.exception;

public class FileNotFoundException extends Throwable {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}