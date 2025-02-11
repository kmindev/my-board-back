package com.back.secuirty.general.handler;

import com.back.controler.dto.reponse.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.util.ResponseUtils.sendResponseWithBody;

/**
 * 인증 성공 시 호출되는 핸들러
 */
@Slf4j
@Component
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        log.info("[Authentication Succeed] : {}", request.getAttribute("username"));
        ApiResponse<Void> apiResponse = ApiResponse.okWithMessage("로그인 성공.");
        sendResponseWithBody(response, apiResponse);
    }

}
