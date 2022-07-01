package org.example.demo.modal.dto.response;

public class RoleAssociateRequestDto {
    String username;
    String roleName;

    public RoleAssociateRequestDto() {}

    public RoleAssociateRequestDto(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
