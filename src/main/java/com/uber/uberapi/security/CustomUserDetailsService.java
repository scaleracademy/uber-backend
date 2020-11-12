package com.uber.uberapi.security;

import com.uber.uberapi.models.Account;
import com.uber.uberapi.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findFirstByUsername(username);
        if(account.isEmpty()) {
            throw  new UsernameNotFoundException("No such user " + username);
        }
        return new CustomUserDetails(account.get());
    }
}
