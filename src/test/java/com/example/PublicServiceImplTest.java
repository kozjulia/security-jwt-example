package com.example;

import com.example.dto.CreateUserRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.impl.PublicServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private PublicServiceImpl publicService;

  @Test
  @DisplayName("сохранен пользователь, когда пользователь не найден, тогда он сохраняется")
  void createUser_whenUserNotFound_thenSavedUser() {
    when(passwordEncoder.encode(any())).thenReturn("");
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(new User());

    ResponseEntity<String> actual = publicService.createUser(new CreateUserRequest());

    assertThat("Пользователь успешно создан.", equalTo(actual.getBody()));
    InOrder inOrder = inOrder(passwordEncoder, userRepository);
    inOrder.verify(passwordEncoder, times(1)).encode(any());
    inOrder.verify(userRepository, times(1)).findByUsername(any());
    inOrder.verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("сохранен пользователь, когда пользователь найден, тогда он не сохраняется")
  void createUser_whenUserFound_thenDidntSaveUser() {
    when(passwordEncoder.encode(any())).thenReturn("");
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

    ResponseEntity<String> actual = publicService.createUser(new CreateUserRequest());

    assertThat("Пользователь с таким именем уже существует.", equalTo(actual.getBody()));
    InOrder inOrder = inOrder(passwordEncoder, userRepository);
    inOrder.verify(passwordEncoder, times(1)).encode(any());
    inOrder.verify(userRepository, times(1)).findByUsername(any());
    inOrder.verify(userRepository, never()).save(any(User.class));
  }

}