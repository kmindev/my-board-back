package com.back.service;

import com.back.domain.UserAccount;
import com.back.domain.UserRoleType;
import com.back.exception.UserNotFoundException;
import com.back.repository.UserAccountRepository;
import com.back.service.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public UserAccount getUserAccount(String userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserAccountDto saveUser(
            String userId, String password, String email, String nickname,
            String memo, String socialProvider, String socialId, UserRoleType role
    ) {
        UserAccount userAccount = UserAccount.createOAuth2UserAccount(userId, password, email, nickname, memo, socialProvider, socialId, role);
        return UserAccountDto.from(userAccountRepository.save(userAccount));
    }
}
