package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class AuthenticationFailedException extends ClientException {
    public AuthenticationFailedException() {}

    public AuthenticationFailedException(Throwable cause) {
        super(cause);
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CodeConstants.AUTHENTICATION_FAILED;
    }
}
