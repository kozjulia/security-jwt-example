package com.example;

import com.example.dto.TokenRequest;
import com.example.dto.TokenResponse;
import com.example.exception.AuthException;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.security.TokenProvider;
import com.example.service.UserService;
import com.example.service.impl.AuthServiceImpl;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock
  private UserService userService;
  @Mock
  private RefreshTokenRepository tokenRepository;
  @Mock
  private TokenProvider tokenProvider;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthServiceImpl authService;

  @Test
  @DisplayName("залогинен, когда пользователь валиден, тогда возвращаются токены")
  void login_whenUserValid_thenReturnedTokens() {
    User user = new User();
    String accessToken = "1";
    String refreshToken = "2";
    when(userService.findByUsername(any())).thenReturn(user);
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(tokenProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
    when(tokenProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

    TokenResponse actualToken = authService.login(new TokenRequest());

    assertThat(new TokenResponse(accessToken, refreshToken), equalTo(actualToken));
    InOrder inOrder = inOrder(userService, passwordEncoder, tokenProvider);
    inOrder.verify(userService, times(1)).findByUsername(any());
    inOrder.verify(passwordEncoder, times(1)).matches(any(), any());
    inOrder.verify(tokenProvider, times(1)).generateAccessToken(any(User.class));
    inOrder.verify(tokenProvider, times(1)).generateRefreshToken(any(User.class));
  }

  @Test
  @DisplayName("залогинен, когда пользователь не валиден, тогда выбрасывается исключение")
  void login_whenUserNotValid_thenExceptionThrown() {
    when(userService.findByUsername(any())).thenReturn(new User());
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    final AuthException exception = assertThrows(AuthException.class,
        () -> authService.login(new TokenRequest()));

    assertThat("Неправильный пароль", equalTo(exception.getMessage()));
    InOrder inOrder = inOrder(userService, passwordEncoder, tokenProvider);
    inOrder.verify(userService, times(1)).findByUsername(any());
    inOrder.verify(passwordEncoder, times(1)).matches(any(), any());
    inOrder.verify(tokenProvider, never()).generateAccessToken(any(User.class));
    inOrder.verify(tokenProvider, never()).generateRefreshToken(any(User.class));
  }

  @Test
  @DisplayName("обновлен токен, когда токен валиден, тогда возвращаются токены")
  void refresh_whenTokenValid_thenReturnedTokens() {
    String oldToken = "old";
    User user = new User();
    String accessToken = "1";
    String refreshToken = "2";

    when(tokenProvider.validateRefreshToken(anyString())).thenReturn(true);
    when(tokenProvider.getRefreshClaims(anyString())).thenReturn(new DefaultClaims());
    when(tokenRepository.findTokenValueByUserUsername(any())).thenReturn(oldToken);
    when(userService.findByUsername(any())).thenReturn(user);
    when(tokenProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
    when(tokenProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

    TokenResponse actualToken = authService.refresh(oldToken);

    assertThat(new TokenResponse(accessToken, refreshToken), equalTo(actualToken));
    InOrder inOrder = inOrder(tokenProvider, tokenRepository, userService, tokenProvider);
    inOrder.verify(tokenProvider, times(1)).validateRefreshToken(anyString());
    inOrder.verify(tokenProvider, times(1)).getRefreshClaims(anyString());
    inOrder.verify(tokenRepository, times(1)).findTokenValueByUserUsername(any());
    inOrder.verify(userService, times(1)).findByUsername(any());
    inOrder.verify(tokenProvider, times(1)).generateAccessToken(any(User.class));
    inOrder.verify(tokenProvider, times(1)).generateRefreshToken(any(User.class));
  }

  @Test
  @DisplayName("обновлен токен, когда токен не валиден, тогда выбрасывается исключение")
  void refresh_whenTokenNotValid_thenExceptionThrown() {
    String oldToken = "old";

    when(tokenProvider.validateRefreshToken(anyString())).thenReturn(false);

    final AuthException exception = assertThrows(AuthException.class,
        () -> authService.refresh(oldToken));

    assertThat("Невалидный JWT токен", equalTo(exception.getMessage()));
    InOrder inOrder = inOrder(tokenProvider, tokenRepository, userService, tokenProvider);
    inOrder.verify(tokenProvider, times(1)).validateRefreshToken(anyString());
    inOrder.verify(tokenProvider, never()).getRefreshClaims(anyString());
    inOrder.verify(tokenRepository, never()).findTokenValueByUserUsername(any());
    inOrder.verify(userService, never()).findByUsername(any());
    inOrder.verify(tokenProvider, never()).generateAccessToken(any(User.class));
    inOrder.verify(tokenProvider, never()).generateRefreshToken(any(User.class));
  }

}