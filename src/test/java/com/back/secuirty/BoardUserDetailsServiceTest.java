package com.back.secuirty;

import com.back.domain.UserAccount;
import com.back.repository.UserAccountRepository;
import com.back.secuirty.general.BoardUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.back.domain.UserAccountMockDataFactory.createDBUserAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("UserDetailService - 테스트")
@ExtendWith(MockitoExtension.class)
class BoardUserDetailsServiceTest {

    @InjectMocks
    private BoardUserDetailsService sut;
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("'username' 을 입력하면, 'UserDetails' 를 반환한다.")
    @Test
    void givenUsername_whenLoadUserByUsername_thenReturnsUserDetails() {
        // Given
        UserAccount userAccount = createDBUserAccount();
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(userAccount));

        // When
        UserDetails userDetails = sut.loadUserByUsername(userAccount.getUserId());

        // Then
        assertThat(userDetails).isInstanceOf(BoardUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo(userAccount.getUserId());
        then(userAccountRepository).should().findById(anyString());
    }

    @DisplayName("존재하지 않는 'username' 을 입력하면, 예외가 발생한다.")
    @Test
    void givenInactiveUsername_whenLoadUserByUsername_thenThrowsException() {
        // Given
        String nonExistingUsername = "NonExitingUser";
        given(userAccountRepository.findById(anyString())).willReturn(Optional.empty());

        // When
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> sut.loadUserByUsername(nonExistingUsername)
        );

        // Then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
        then(userAccountRepository).should().findById(anyString());
    }

}