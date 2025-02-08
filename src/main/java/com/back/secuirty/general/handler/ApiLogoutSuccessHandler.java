package com.back.secuirty.general.handler;

import com.back.controler.dto.reponse.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.back.util.ResponseUtils.sendResponseWithBody;

@Slf4j
@Component
public class ApiLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[Logout Succeed] : {}", authentication.getName());
        ApiResponse<Void> apiResponse = ApiResponse.okWithMessage("로그아웃 성공");
        sendResponseWithBody(response, apiResponse);
    }

}
