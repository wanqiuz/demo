package org.example.demo.exception.internal;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException() {}

    public EntityAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
