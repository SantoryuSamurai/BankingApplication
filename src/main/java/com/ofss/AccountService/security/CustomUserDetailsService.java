package com.ofss.AccountService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder().encode("adminpass"))
                    .roles("ADMIN")
                    .build();
        }
        if ("user".equals(username)) {
            return User.withUsername("user")
                    .password(passwordEncoder().encode("userpass"))
                    .roles("USER")
                    .build();
        }
        throw new RuntimeException("User not found");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
