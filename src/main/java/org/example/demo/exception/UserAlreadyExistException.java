package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class UserAlreadyExistException extends ClientException {

    public UserAlreadyExistException() {}

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return CodeConstants.USER_ALREADY_EXIST;
    }
}
