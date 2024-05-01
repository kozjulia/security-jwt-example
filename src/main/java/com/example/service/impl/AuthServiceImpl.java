package com.example.service.impl;

import com.example.dto.TokenRequest;
import com.example.dto.TokenResponse;
import com.example.exception.AuthException;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.security.TokenProvider;
import com.example.service.AuthService;
import com.example.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final RefreshTokenRepository tokenRepository;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Override
  public TokenResponse login(@NonNull TokenRequest request) {
    final User user = userService.findByUsername(request.getUsername());
    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      final String accessToken = tokenProvider.generateAccessToken(user);
      final String refreshToken = tokenProvider.generateRefreshToken(user);
      return new TokenResponse(accessToken, refreshToken);
    } else {
      throw new AuthException("Неправильный пароль");
    }
  }

  @Override
  public TokenResponse refresh(@NonNull String refreshToken) {
    if (tokenProvider.validateRefreshToken(refreshToken)) {
      final Claims claims = tokenProvider.getRefreshClaims(refreshToken);
      final String username = claims.getSubject();
      final String saveRefreshToken = tokenRepository.findTokenValueByUserUsername(username);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        final User user = userService.findByUsername(username);
        final String accessToken = tokenProvider.generateAccessToken(user);
        final String newRefreshToken = tokenProvider.generateRefreshToken(user);
        return new TokenResponse(accessToken, newRefreshToken);
      }
    }
    throw new AuthException("Невалидный JWT токен");
  }

}