package com.example.service.impl;

import com.example.dto.CreateUserRequest;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.PublicService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ResponseEntity<String> createUser(CreateUserRequest request) {
    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(Set.of(Role.USER))
        .build();
    return saveUser(user);
  }

  private ResponseEntity<String> saveUser(User user) {
    if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
      userRepository.save(user);
      return ResponseEntity.ok("Пользователь успешно создан.");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким именем уже существует.");
    }
  }

}