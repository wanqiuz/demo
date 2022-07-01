package org.example.demo.modal.dto.response;

import org.example.demo.constant.CodeConstants;

public class RichSuccessResponseDto {
    String code;
    String message;
    Object data;

    public RichSuccessResponseDto() {
        this(null);
    }

    public RichSuccessResponseDto(Object data) {
        this(CodeConstants.SUCCESS, data);
    }

    public RichSuccessResponseDto(String message, Object data) {
        this.code = CodeConstants.SUCCESS;
        this.message = message;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
