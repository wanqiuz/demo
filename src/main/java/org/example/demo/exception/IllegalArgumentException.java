package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class IllegalArgumentException extends ClientException {
    public IllegalArgumentException() {}

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public IllegalArgumentException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CodeConstants.ILLEGAL_ARGUMENT;
    }
}
