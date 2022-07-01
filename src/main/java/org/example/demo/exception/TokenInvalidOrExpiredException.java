package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class TokenInvalidOrExpiredException extends ClientException {

    public TokenInvalidOrExpiredException() {}

    public TokenInvalidOrExpiredException(Throwable cause) {
        super(cause);
    }

    public TokenInvalidOrExpiredException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CodeConstants.TOKEN_INVALID_OR_EXPIRED;
    }
}
