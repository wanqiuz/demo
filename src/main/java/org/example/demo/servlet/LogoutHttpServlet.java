package org.example.demo.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.demo.exception.IllegalArgumentException;
import org.example.demo.modal.dto.request.UserLogoutRequestDto;
import org.example.demo.modal.dto.response.SuccessResponseDto;
import org.example.demo.service.UserService;
import org.example.demo.util.HttpHelper;
import org.example.demo.util.ObjectMapperHolder;

public class LogoutHttpServlet extends HttpServlet {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public LogoutHttpServlet() {
        super();
        userService = UserService.getInstance();
        objectMapper = ObjectMapperHolder.getInstance();
    }

    /**
     * user logout
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
                    UserLogoutRequestDto dto;
                    try {
                        dto = objectMapper.readValue(body, UserLogoutRequestDto.class);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException(e);
                    }

                    userService.invalidateToken(dto.getToken());
                    return new SuccessResponseDto("User logout");
                });
    }
}
