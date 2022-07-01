package org.example.demo.modal.dto.response;

import java.util.List;

public class RoleListResponseDto {

    private List<RoleResponseDto> roles;

    public RoleListResponseDto(List<RoleResponseDto> roles) {
        this.roles = roles;
    }

    public List<RoleResponseDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponseDto> roles) {
        this.roles = roles;
    }
}
