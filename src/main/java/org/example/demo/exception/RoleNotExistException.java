package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class RoleNotExistException extends ClientException {
    public RoleNotExistException() {}

    public RoleNotExistException(Throwable cause) {
        super(cause);
    }

    public RoleNotExistException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CodeConstants.ROLE_NOT_EXIST;
    }
}
