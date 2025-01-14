package com.back.secuirty;

import com.back.domain.UserAccount;
import com.back.service.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record BoardUserDetails(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        String socialProvider,
        String socialId,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                userId, userPassword, email, nickname, memo, socialProvider, socialId
        );
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
                List.of(new SimpleGrantedAuthority(userAccount.getRole().getName()))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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


}
