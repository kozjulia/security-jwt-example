package com.example.controller;

import com.example.dto.TokenRequest;
import com.example.dto.RefreshTokenRequest;
import com.example.dto.TokenResponse;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
@Tag(name = "Логирование пользователей", description = "Апи для логирования и обновления jwt-токена пользователей")
public class AuthController {

  private final AuthService authService;

  @PostMapping("login")
  @Operation(
      summary = "Залогиниться",
      description = "Позволяет залогиниться пользователю"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = TokenResponse.class))})})
  /**
   * Логирование пользователя.
   */
  public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest authRequest) {
    final TokenResponse token = authService.login(authRequest);
    return ResponseEntity.ok(token);
  }

  @PostMapping("refresh")
  @Operation(
      summary = "Обновить токен",
      description = "Позволяет обновить токен пользователю"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = TokenResponse.class))})})
  /**
   * Обновление токена пользователя.
   */
  public ResponseEntity<TokenResponse> getNewRefreshToken(@RequestBody RefreshTokenRequest request) {
    final TokenResponse token = authService.refresh(request.getRefreshToken());
    return ResponseEntity.ok(token);
  }

}