package com.smartavaas.security;

import com.smartavaas.model.User;
import com.smartavaas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByMobileOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile/email: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail() != null ? user.getEmail() : user.getMobile())
                .password(user.getPassword())
                .authorities("ROLE_USER") // or get from user.getRoles()
                .build();
    }
}
