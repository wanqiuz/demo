package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class RoleAlreadyExistException extends ClientException {
    public RoleAlreadyExistException() {}

    public RoleAlreadyExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return CodeConstants.ROLE_ALREADY_EXIST;
    }
}
