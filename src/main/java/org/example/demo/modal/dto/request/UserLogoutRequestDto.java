package org.example.demo.modal.dto.request;

import org.example.demo.exception.IllegalArgumentException;

public class UserLogoutRequestDto {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (token == null || token.length() <= 4) {
            throw new IllegalArgumentException("Length of tokenis invalid");
        }
        this.token = token;
    }

    private String token;
}
