package com.back.secuirty.oauth2.handler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.secuirty.BoardUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.secuirty.handler.HandlerUtils.sendResponseWithBody;

/**
 * OAuth2 인증 성공 시 호출되는 핸들러
 */
@Slf4j
@Component
public class Oauth2AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        if (authentication.getPrincipal() instanceof BoardUserDetails userDetails) {
            String username = userDetails.getUsername();
            log.info("[Authentication Succeed] Username: {}", username);
        } else {
            log.warn("[Authentication Succeed] 사용자 정보를 가져올 수 없습니다.");
        }
        ApiResponse<Void> apiResponse = ApiResponse.okWithMessage("로그인 성공.");
        sendResponseWithBody(response, apiResponse);
    }
}
