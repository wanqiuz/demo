package org.example.demo.modal.dto.response;

import org.example.demo.constant.CodeConstants;

public class SuccessResponseDto {
    String code;
    String message;

    public SuccessResponseDto() {
        this(CodeConstants.SUCCESS);
    }

    public SuccessResponseDto(String message) {
        this.code = CodeConstants.SUCCESS;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
