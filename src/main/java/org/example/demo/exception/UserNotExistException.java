package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class UserNotExistException extends ClientException {
    public UserNotExistException() {}

    public UserNotExistException(Throwable cause) {
        super(cause);
    }

    public UserNotExistException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CodeConstants.USER_NOT_EXIST;
    }
}
