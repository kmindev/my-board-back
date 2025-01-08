package com.back.service;

import com.back.domain.UserAccount;
import com.back.exception.UserNotFoundException;
import com.back.repository.UserAccountRepository;
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

}
