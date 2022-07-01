package org.example.demo.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.demo.exception.IllegalArgumentException;
import org.example.demo.modal.dto.request.UserCreateRequestDto;
import org.example.demo.modal.dto.response.RichSuccessResponseDto;
import org.example.demo.modal.dto.response.RoleAssociateRequestDto;
import org.example.demo.modal.dto.response.RoleCheckResponseDto;
import org.example.demo.modal.dto.response.SuccessResponseDto;
import org.example.demo.service.UserService;
import org.example.demo.util.HttpHelper;
import org.example.demo.util.ObjectMapperHolder;

public class UserHttpServlet extends HttpServlet {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public UserHttpServlet() {
        super();
        userService = UserService.getInstance();
        objectMapper = ObjectMapperHolder.getInstance();
    }

    /**
     * Create user
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
                    UserCreateRequestDto dto;
                    try {
                        dto = objectMapper.readValue(body, UserCreateRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("Request params is invalid");
                    }

                    userService.createUser(dto.getUsername(), dto.getPassword());
                    return new SuccessResponseDto("User created");
                });
    }

    /**
     * Delete user
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
                    String username = HttpHelper.getLastPathPart(request);
                    if (username == null || username.length() <= 4) {
                        throw new IllegalArgumentException(
                                "Length of username should be more than 4");
                    }
                    userService.deleteUser(username);
                    return new SuccessResponseDto("User deleted");
                });
    }

    /**
     * Add role to user
     *
     * @param request
     * @param response
     * @throws JsonProcessingException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException {
        HttpHelper.commonServletProcess(
                request,
                response,
                () -> {
                    String body = HttpHelper.getBody(request);
                    RoleAssociateRequestDto dto;
                    try {
                        dto = objectMapper.readValue(body, RoleAssociateRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException(e);
                    }
                    userService.associateRole(dto.getUsername(), dto.getRoleName());
                    return new SuccessResponseDto("Role associate to user");
                });
    }

    /**
     * Check role
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
                    URLDecoder.decode(token, StandardCharsets.UTF_8.name());
                    String roleName = request.getParameter("roleName");
                    if (token == null || token.length() <= 12) {
                        throw new IllegalArgumentException("Length of token is invalid");
                    }
                    if (roleName == null || roleName.length() <= 4) {
                        throw new IllegalArgumentException("Length of role should be more than 4");
                    }
                    boolean hasRole = userService.checkRole(token, roleName);
                    return new RichSuccessResponseDto(
                            "Role checked", new RoleCheckResponseDto(hasRole));
                });
    }
}
