package com.back.secuirty.oauth2;

import com.back.domain.UserAccount;
import com.back.domain.UserRoleType;
import com.back.exception.UserNotFoundException;
import com.back.secuirty.BoardUserDetails;
import com.back.secuirty.oauth2.response.KakaoOAuth2Response;
import com.back.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserAccountService userAccountService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        KakaoOAuth2Response kakaoOAuth2Response = KakaoOAuth2Response.from(oAuth2User.getAttributes());

        // OAuth2 사용자 데이터 생성
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao
        String nickname = kakaoOAuth2Response.nickname(); // kakao 사용자 닉네임
        String providerId = String.valueOf(kakaoOAuth2Response.id()); // kakao 사용자 고유 id
        String userId = registrationId + "_" + providerId; // kakao_{사용자}
        String password = UUID.randomUUID().toString(); // 패스워드 랜덤값

        try {
            UserAccount userAccount = userAccountService.getUserAccount(userId);
            return BoardUserDetails.from(userAccount);
        } catch (UserNotFoundException e) {
            return BoardUserDetails.of(
                    userAccountService.saveUser(
                            userId,
                            password,
                            null,
                            nickname,
                            null,
                            registrationId,
                            providerId,
                            UserRoleType.USER
                    ),
                    kakaoOAuth2Response.properties()
            );
        }
    }

}
