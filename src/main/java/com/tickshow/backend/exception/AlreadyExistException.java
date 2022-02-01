package com.tickshow.backend.exception;

public class AlreadyExistException extends Throwable{
    public AlreadyExistException(String message) {
        super(message);
    }
}