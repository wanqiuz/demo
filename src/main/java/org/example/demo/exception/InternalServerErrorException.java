package org.example.demo.exception;

import org.example.demo.constant.CodeConstants;

public class InternalServerErrorException extends ServerException {

    public InternalServerErrorException() {}

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return CodeConstants.INTERNAL_SERVER_ERROR;
    }
}
