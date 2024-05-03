package com.team3.weather.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.team3.weather.model.AccountHolder;
import com.team3.weather.repository.AccountHolderRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    public CustomUserDetailsService(AccountHolderRepository accountHolderRepository) {
        this.accountHolderRepository = accountHolderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountHolder user = accountHolderRepository.findByEmailAndIsDeleted(username, false);
        if (user != null) {
            ArrayList<SimpleGrantedAuthority> authority = new ArrayList<>();
            authority.add(new SimpleGrantedAuthority("ROLE_" + user.getClass().getSimpleName()));
            User authUser = new User(
                    user.getEmail(),
                    user.getPassword(),
                    authority);

            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
