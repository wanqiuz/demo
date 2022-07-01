package org.example.demo.modal.dto.request;

import org.example.demo.exception.IllegalArgumentException;

public class UserCreateRequestDto {
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.length() <= 4) {
            throw new IllegalArgumentException("Length of username should be more than 4");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() <= 5) {
            throw new IllegalArgumentException("Length of password should be more than 5");
        }
        this.password = password;
    }
}
