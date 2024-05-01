package com.example;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  @DisplayName("найден пользователь по username, когда пользователь найден, тогда он возвращается")
  void findByUsername_whenUserFound_thenReturnedUser() {
    String username = "1";
    User expectedUser = new User();
    expectedUser.setUsername(username);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));

    User actualUser = userService.findByUsername(username);

    assertThat(expectedUser, equalTo(actualUser));
    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Test
  @DisplayName("найден пользователь по username, когда пользователь не найден, тогда выбрасывается исключение")
  void findByUsername_whenUserNotFound_thenExceptionThrown() {
    String username = "1";
    User expectedUser = new User();
    expectedUser.setUsername(username);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    final UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
        () -> userService.findByUsername(username));

    assertThat(username, equalTo(exception.getMessage()));
    verify(userRepository, times(1)).findByUsername(anyString());
  }

}