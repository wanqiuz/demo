package org.example.demo.modal.dto.response;

public class RoleCheckResponseDto {

    boolean hasRole;

    public RoleCheckResponseDto(boolean hasRole) {
        this.hasRole = hasRole;
    }

    public boolean isHasRole() {
        return hasRole;
    }

    public void setHasRole(boolean hasRole) {
        this.hasRole = hasRole;
    }
}
