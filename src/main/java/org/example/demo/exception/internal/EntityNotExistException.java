package org.example.demo.exception.internal;

public class EntityNotExistException extends RuntimeException {
    public EntityNotExistException() {}

    public EntityNotExistException(Throwable cause) {
        super(cause);
    }
}
