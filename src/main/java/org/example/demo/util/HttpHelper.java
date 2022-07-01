package org.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.demo.constant.CodeConstants;
import org.example.demo.exception.ClientException;
import org.example.demo.modal.dto.response.ErrorResponseDto;
import org.example.demo.servlet.ControllerLogic;

/**
 * Handles some HTTP related parameter parsing and standard process flow
 */
public class HttpHelper {

    private static final Logger logger = Logger.getGlobal();

    private HttpHelper() {}

    /**
     * Encapsulate a common HTTP processing flow
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param myFunction special processing flow for each API
     * @throws JsonProcessingException
     */
    public static void commonServletProcess(
            HttpServletRequest request, HttpServletResponse response, ControllerLogic myFunction)
            throws JsonProcessingException {
        response.setContentType("text/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = null;
        String res = null;
        try {
            out = response.getWriter();
            Object responseDto = myFunction.run();
            res = objectMapper.writeValueAsString(responseDto);
        } catch (ClientException e) {
            logger.warning(e.getMessage());
            response.setStatus(400);
            ErrorResponseDto responseDto = new ErrorResponseDto(e.getCode(), e.getMessage());
            res = objectMapper.writeValueAsString(responseDto);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            response.setStatus(500);
            ErrorResponseDto responseDto =
                    new ErrorResponseDto(CodeConstants.INTERNAL_SERVER_ERROR, e.getMessage());
            res = objectMapper.writeValueAsString(responseDto);
        } finally {
            if (out != null) {
                out.println(res);
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Extract body from http request
     *
     * @param request HttpServletRequest
     * @return body in json string format
     * @throws IOException
     */
    public static String getBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str;
        StringBuilder wholeStr = new StringBuilder();
        while ((str = br.readLine()) != null) {
            wholeStr.append(str);
        }
        return wholeStr.toString();
    }

    /**
     * Get the last part of the url
     *
     * @param request HttpServletRequest
     * @return last part
     */
    public static String getLastPathPart(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null || uri.length() == 0) {
            return null;
        }
        String[] split = uri.split("/");
        return split[split.length - 1];
    }
}
