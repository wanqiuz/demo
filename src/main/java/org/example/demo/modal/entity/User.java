package org.example.demo.modal.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class User {
    String username;
    byte[] passwordSalt;
    byte[] passwordHash;
    Set<String> roles;

    public User(String username, byte[] passwordSalt, byte[] passwordHash, Set<String> roles) {
        this.username = username;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(passwordSalt, user.passwordSalt)
                && Arrays.equals(passwordHash, user.passwordHash)
                && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, passwordSalt, passwordHash, roles);
    }
}
