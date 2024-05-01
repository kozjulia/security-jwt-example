package com.example.service;

import com.example.dto.TokenRequest;
import com.example.dto.TokenResponse;

public interface AuthService {

  TokenResponse login(TokenRequest authRequest);

  TokenResponse refresh(String refreshToken);

}