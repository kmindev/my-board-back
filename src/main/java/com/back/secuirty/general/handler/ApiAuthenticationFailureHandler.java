package com.back.secuirty.general.handler;

import com.back.controler.dto.reponse.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.util.ResponseUtils.sendResponseWithBody;

/**
 * 인증 실패 시 호출되는 핸들러
 */
@Slf4j
@Component
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        String failureMessage = exception.getMessage();
        log.info("[Authentication Failed] : {} : {}", request.getAttribute("username"), failureMessage);
        ApiResponse<Void> apiResponse = ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, failureMessage);
        sendResponseWithBody(response, apiResponse);
    }

}
