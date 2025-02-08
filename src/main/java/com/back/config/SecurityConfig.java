package com.back.config;

import com.back.domain.UserRoleType;
import com.back.secuirty.general.handler.*;
import com.back.secuirty.oauth2.handler.OAuth2AuthFailureHandler;
import com.back.secuirty.oauth2.handler.Oauth2AuthSuccessHandler;
import com.back.secuirty.general.ApiAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private static final String LOGIN_URL = "/v1/auth/login";
    private static final String LOGOUT_URL = "/v1/auth/logout";
    private static final String[] ALL_PERMITTED_URLS = {LOGIN_URL, "/h2-console/**"};
    private static final String[] ADMIN_PERMITTED_URLS = {"/v1/auth/admin-test"};

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ApiAuthenticationSuccessHandler successHandler;
    private final ApiAuthenticationFailureHandler failureHandler;
    private final ApiAccessDeniedHandler deniedHandler;
    private final ApiLoginAuthenticationEntryPoint entryPoint;
    private final ApiLogoutSuccessHandler logoutSuccessHandler;
    private final Oauth2AuthSuccessHandler oauth2AuthSuccessHandler;
    private final OAuth2AuthFailureHandler oauth2AuthFailureHandler;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable()) // H2 콘솔 표시를 위한 헤더 비활성화
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ALL_PERMITTED_URLS).permitAll()
                        .requestMatchers(ADMIN_PERMITTED_URLS).hasAuthority(UserRoleType.ADMIN.getName())
                        .anyRequest().authenticated())
                .addFilterBefore(apiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(entryPoint) // 인증되지 않은 요청 처리
                        .accessDeniedHandler(deniedHandler) // 권한 부족 요청 처리
                )
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URL)
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 삭제
                        .deleteCookies("JSESSIONID")

                )
                .oauth2Login(oAuth -> oAuth.
                        userInfoEndpoint(useInfo -> useInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oauth2AuthSuccessHandler)
                        .failureHandler(oauth2AuthFailureHandler)
                );

        return http.build();
    }

    @Bean
    public ApiAuthenticationFilter apiAuthenticationFilter() throws Exception {
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter(
                new AntPathRequestMatcher(LOGIN_URL, HttpMethod.POST.name()),
                authenticationConfiguration.getAuthenticationManager()
        );
        filter.setAuthenticationSuccessHandler(successHandler); // 인증 성공 핸들러
        filter.setAuthenticationFailureHandler(failureHandler); // 인증 실패 핸드러
        filter.setSecurityContextRepository(  // SecurityContext 저장소 설정
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(), // 요청 속성에 SecurityContext를 저장
                        new HttpSessionSecurityContextRepository() // 세션에 SecurityContext를 저장
                ));

        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
