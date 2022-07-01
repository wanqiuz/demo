package org.example.demo.exception;

public abstract class ClientException extends RuntimeException implements ExternalException {
    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ClientException() {}

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
