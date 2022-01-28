package com.tickshow.backend.exception;

public class EntityNotFoundException extends Throwable{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
