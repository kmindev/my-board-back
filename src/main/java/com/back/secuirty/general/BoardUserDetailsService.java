package com.back.secuirty.general;

import com.back.repository.UserAccountRepository;
import com.back.secuirty.BoardUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findById(username)
                .map(BoardUserDetails::from)
                .orElseThrow(() -> new UsernameNotFoundException("ID 가 존재하지 않습니다."));
    }

}
