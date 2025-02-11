package com.back.secuirty.general;

import com.back.controler.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiAuthenticationFilter(AntPathRequestMatcher antPathRequestMatcher, AuthenticationManager authenticationManager) {
        super(antPathRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isValidRequest(request)) {
            throw new AuthenticationServiceException("HTTP Methon와 ContentType을 확인하세요.");
        }
        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        request.setAttribute("username", loginRequest.username()); // username 세팅(로깅 확인을 위해)

        if (!StringUtils.hasLength(loginRequest.username()) || !StringUtils.hasLength(loginRequest.password())) {
            throw new BadCredentialsException("id, pw를 입력하세요.");
        }

        return getAuthenticationManager().authenticate(
                new ApiAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
    }

    private boolean isValidRequest(HttpServletRequest request) {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            return false;
        }

        return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE);
    }

}
