package org.example.demo.modal.entity;

import java.util.Objects;

public class Token {
    String name;
    String username;
    Long expiredAt;

    public Token(String name, String username, Long expiredAt) {
        this.name = name;
        this.username = username;
        this.expiredAt = expiredAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(name, token.name)
                && Objects.equals(username, token.username)
                && Objects.equals(expiredAt, token.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, expiredAt);
    }
}
