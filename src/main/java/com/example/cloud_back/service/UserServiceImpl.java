package com.example.cloud_back.service;

import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.model.Authority;
import com.example.cloud_back.model.User;
import com.example.cloud_back.repository.AuthorityRepository;
import com.example.cloud_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(IncorrectDataException::new);
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), user.getAuthorities());
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void addAuthorityToUser(User user, String authorityName) {
        Authority authority = authorityRepository.findByName(authorityName).get();
        user.getAuthorities().add(authority);
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        log.info("Saving new authority {} to the database", authority.getName());
        return authorityRepository.save(authority);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByLogin(username).orElseThrow(IncorrectDataException::new);
    }
}
