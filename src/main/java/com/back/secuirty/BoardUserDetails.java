package com.back.secuirty;

import com.back.domain.UserAccount;
import com.back.domain.UserRoleType;
import com.back.service.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record BoardUserDetails(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        String socialProvider,
        String socialId,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {

    public UserAccountDto toDto() {
        return UserAccountDto.of(userId, userPassword, email, nickname, memo, socialProvider, socialId);
    }

    public static BoardUserDetails from(UserAccount userAccount) {
        return new BoardUserDetails(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getMemo(),
                userAccount.getSocialProvider(),
                userAccount.getSocialId(),
                List.of(new SimpleGrantedAuthority(userAccount.getRole().getName())),
                Map.of()
        );
    }

    public static BoardUserDetails from(UserAccountDto userAccountDto) {
        return new BoardUserDetails(
                userAccountDto.userId(),
                userAccountDto.userPassword(),
                userAccountDto.email(),
                userAccountDto.nickname(),
                userAccountDto.memo(),
                userAccountDto.socialProvider(),
                userAccountDto.socialId(),
                List.of(new SimpleGrantedAuthority(userAccountDto.role().getName())),
                Map.of()
        );
    }

    public static BoardUserDetails of(UserAccountDto userAccountDto, Map<String, Object> oAuth2Attributes) {
        return new BoardUserDetails(
                userAccountDto.userId(),
                userAccountDto.userPassword(),
                userAccountDto.email(),
                userAccountDto.nickname(),
                userAccountDto.memo(),
                userAccountDto.socialProvider(),
                userAccountDto.socialId(),
                List.of(new SimpleGrantedAuthority(userAccountDto.role().getName())),
                oAuth2Attributes
        );
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return userId;
    }

}
