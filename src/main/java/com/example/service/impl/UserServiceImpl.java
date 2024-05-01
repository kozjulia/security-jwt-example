package com.example.service.impl;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import java.util.Set;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  private void postConstruct() {
    User user = User.builder()
        .username("admin")
        .email("admin@mail.com")
        .password(passwordEncoder.encode("password"))
        .roles(Set.of(Role.ADMIN))
        .build();
    if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
      userRepository.save(user);
    }
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
  }

}