package org.example.demo.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.demo.exception.IllegalArgumentException;
import org.example.demo.modal.dto.request.RoleCreateRequestDto;
import org.example.demo.modal.dto.response.*;
import org.example.demo.modal.entity.Role;
import org.example.demo.service.RoleService;
import org.example.demo.util.HttpHelper;
import org.example.demo.util.ObjectMapperHolder;

public class RoleHttpServlet extends HttpServlet {
    private final RoleService roleService;

    private final ObjectMapper objectMapper;

    public RoleHttpServlet() {
        super();
        roleService = RoleService.getInstance();
        objectMapper = ObjectMapperHolder.getInstance();
    }

    /**
     * Create role
     *
     * @param request
     * @param response
     * @throws JsonProcessingException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException {
        HttpHelper.commonServletProcess(
                request,
                response,
                () -> {
                    String body = HttpHelper.getBody(request);
                    RoleCreateRequestDto dto;
                    try {
                        dto = objectMapper.readValue(body, RoleCreateRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException(e);
                    }
                    roleService.createRole(dto.getRoleName());
                    return new SuccessResponseDto("Role created");
                });
    }

    /**
     * Delete role
     *
     * @param request
     * @param response
     * @throws JsonProcessingException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException {
        HttpHelper.commonServletProcess(
                request,
                response,
                () -> {
                    String roleName = HttpHelper.getLastPathPart(request);
                    if (roleName == null || roleName.length() <= 4) {
                        throw new IllegalArgumentException("Length of role should be more than 4");
                    }
                    roleService.deleteRole(roleName);
                    return new SuccessResponseDto("Role deleted");
                });
    }

    /**
     * list roles
     *
     * @param request
     * @param response
     * @throws JsonProcessingException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException {
        HttpHelper.commonServletProcess(
                request,
                response,
                () -> {
                    String token = request.getParameter("token");
                    if (token == null || token.length() <= 12) {
                        throw new IllegalArgumentException("Length of token is invalid");
                    }
                    List<Role> roles = roleService.allRoles(token);
                    return new RichSuccessResponseDto(
                            "Role list",
                            new RoleListResponseDto(
                                    roles.stream()
                                            .map(RoleResponseDto::new)
                                            .collect(Collectors.toUnmodifiableList())));
                });
    }
}
