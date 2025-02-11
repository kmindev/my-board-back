package com.back.util;

import com.back.controler.dto.reponse.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendResponseWithBody(HttpServletResponse response, ApiResponse<Void> apiResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.setStatus(apiResponse.code());
        PrintWriter out = response.getWriter();
        String body = objectMapper.writeValueAsString(apiResponse);
        out.print(body);
        out.flush();
    }

}
