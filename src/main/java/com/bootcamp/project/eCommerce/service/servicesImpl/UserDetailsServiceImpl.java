package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userList = userRepository.findByEmail(email);
        if (userList == null) {
            throw new RuntimeException("User Not Found!");
        }
        return userList;
    }
}
