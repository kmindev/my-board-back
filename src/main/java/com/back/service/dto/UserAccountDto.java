package com.back.service.dto;

import com.back.domain.UserAccount;
import com.back.domain.UserRoleType;

import java.time.LocalDateTime;

public record UserAccountDto(
        String userId, // 유저 id
        String userPassword, // 패스워드
        String email, // 이메일
        String nickname, // 닉네임
        String memo, // 메모
        String socialProvider, // 소셜 로그인 제공처
        String socialId, // 소셜 로그인 고유 id
        UserRoleType role, // 권한
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static UserAccountDto of(String userId, String userPassword, String email, String nickname, String memo, String socialProvider, String socialId) {
        return new UserAccountDto(
                userId, userPassword, email, nickname, memo, socialProvider, socialId,
                null, null, null, null, null
        );
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return new UserAccountDto(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getMemo(),
                userAccount.getSocialProvider(),
                userAccount.getSocialId(),
                userAccount.getRole(),
                userAccount.getCreatedAt(),
                userAccount.getCreatedBy(),
                userAccount.getModifiedAt(),
                userAccount.getModifiedBy()
        );
    }
}
