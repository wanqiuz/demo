package org.example.demo.modal.dto.response;

import org.example.demo.modal.entity.Role;

public class RoleResponseDto {

    private String roleName;

    public RoleResponseDto(String roleName) {
        this.roleName = roleName;
    }

    public RoleResponseDto(Role role) {
        this.roleName = role.getRoleName();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
