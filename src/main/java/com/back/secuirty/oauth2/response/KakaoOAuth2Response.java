package com.back.secuirty.oauth2.response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SuppressWarnings("unchecked") // Map -> Object 변환 로직이 있어 제네릭 타입 캐스팅 문제를 무시
public record KakaoOAuth2Response(
        Long id,
        LocalDateTime connectedAt,
        Map<String, Object> properties,
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            Boolean profileNicknameNeedsAgreement,
            Profile profile
    ) {
        public record Profile(
                String nickname,
                Boolean isDefaultNickname
        ) {
        }
    }

    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profileMap = (Map<String, Object>) kakaoAccountMap.get("profile");

        KakaoAccount.Profile profile = new KakaoAccount.Profile(
                String.valueOf(profileMap.get("nickname")),
                Boolean.valueOf(String.valueOf(profileMap.get("is_default_nickname")))
        );
        KakaoAccount kakaoAccount = new KakaoAccount(
                Boolean.valueOf(String.valueOf(kakaoAccountMap.get("profile_nickname_needs_agreement"))),
                profile
        );

        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                kakaoAccount
        );
    }

    public String nickname() {
        return this.kakaoAccount.profile.nickname;
    }

}
