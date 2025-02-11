package com.back.domain;

import com.back.domain.converter.UserRoleTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends BaseEntity {

    @Id
    @Column(nullable = false, length = 50)
    private String userId; // 유저 id

    @Column(nullable = false) private String userPassword; // 패스워드
    @Column(length = 50) private String email; // 이메일
    @Column(length = 50) private String nickname; // 닉네임
    @Column private String memo; // 메모
    @Column private String socialProvider; // 소셜 로그인 제공처
    @Column private String socialId; // 유저 소셜 고유 id

    @Column(length = 10)
    @Convert(converter = UserRoleTypeConverter.class)
    private UserRoleType role; // 권한

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo, UserRoleType role) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.role = role;
    }

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo,
                        String socialProvider, String socialId, UserRoleType role, String createdBy, String modifiedBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.role = role;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, UserRoleType role) {
        return new UserAccount(userId, userPassword, email, nickname, memo, role);
    }

    public static UserAccount createOAuth2UserAccount(
            String userId, String userPassword, String email, String nickname, String memo,
            String socialProvider, String socialId, UserRoleType role
    ) {
        return new UserAccount(
                userId,
                userPassword,
                email,
                nickname,
                memo,
                socialProvider,
                socialId,
                role,
                userId,
                userId
        );
    }

}
