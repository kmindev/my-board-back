package com.back.secuirty.general.handler;

import com.back.controler.dto.reponse.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.util.ResponseUtils.sendResponseWithBody;

/**
 * 인증은 되었지만, 요청한 리소스에 대한 권한이 없을 때 호출되는 핸들러
 */
@Slf4j
@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[Access Denied] : {} : {}", request.getAttribute("username"), accessDeniedException.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.errorWithMessage(HttpStatus.FORBIDDEN, "해당 리소스에 접근 권한이 없습니다.");
        sendResponseWithBody(response, apiResponse);
    }
}
