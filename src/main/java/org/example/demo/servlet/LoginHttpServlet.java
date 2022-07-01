package org.example.demo.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.demo.exception.IllegalArgumentException;
import org.example.demo.modal.dto.request.UserLoginRequestDto;
import org.example.demo.modal.dto.response.RichSuccessResponseDto;
import org.example.demo.modal.dto.response.UserLoginResponseDto;
import org.example.demo.service.UserService;
import org.example.demo.util.HttpHelper;
import org.example.demo.util.ObjectMapperHolder;

public class LoginHttpServlet extends HttpServlet {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public LoginHttpServlet() {
        super();
        userService = UserService.getInstance();
        objectMapper = ObjectMapperHolder.getInstance();
    }

    /**
     * Authenticate user
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
                    UserLoginRequestDto dto;
                    try {
                        dto = objectMapper.readValue(body, UserLoginRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("Request params is invalid");
                    }

                    String token = userService.authenticate(dto.getUsername(), dto.getPassword());
                    UserLoginResponseDto tokenDto = new UserLoginResponseDto(token);
                    return new RichSuccessResponseDto("User login", tokenDto);
                });
    }
}
