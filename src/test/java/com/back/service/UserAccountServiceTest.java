package com.back.service;

import com.back.domain.UserAccount;
import com.back.exception.UserNotFoundException;
import com.back.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.back.domain.UserAccountMockDataFactory.createDBUserAccountFromUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService sut;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("존재하는 유저 id를 전달하면, UserAccount 엔티티를 반환한다.")
    @Test
    void givenExitingUserId_whenGetUserAccount_thenReturnsUserAccount() {
        // Given
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        given(userAccountRepository.findById(userId)).willReturn(Optional.of(userAccount));

        // When
        UserAccount result = sut.getUserAccount(userId);

        // Then
        assertThat(result.getUserId()).isEqualTo(userAccount.getUserId());
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("존재하지 않는 유저 id를 전달하면, UserNotFoundException 예외를 던진다.")
    @Test
    void givenNonExitingUserId_whenGetUserAccount_thenThrowsUserNotFoundException() {
        // Given
        String nonExistentUserId = "user2";
        given(userAccountRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

        // When
        UserNotFoundException result = assertThrows(UserNotFoundException.class,
                () -> sut.getUserAccount(nonExistentUserId)
        );

        // Then
        assertThat(result).isInstanceOf(UserNotFoundException.class);
        then(userAccountRepository).should().findById(nonExistentUserId);
    }

}