package com.back.secuirty.oauth2.handler;

import com.back.controler.dto.reponse.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.secuirty.handler.HandlerUtils.sendResponseWithBody;

/**
 * 인증 실패 시 호출되는 핸들러
 */
@Slf4j
@Component
public class OAuth2AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        log.info("[OAuth2 Auth Failed] : {} : {}", request.getAttribute("username"), exception.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, "OAuth2 로그인 실패.");
        sendResponseWithBody(response, apiResponse);
    }

}
