package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Апи для обычных пользователей", description = "Апи для работы пользователей с ролью USER")
public class UserController {

  @GetMapping
  @Operation(
      summary = "Получить сообщение для пользователя",
      description = "Позволяет получить сообщение для пользователей с ролью USER",
      security = @SecurityRequirement(name = "Bearer Authenticationh")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class))})})
  /**
   * Возвращение сообщения для пользователя.
   */
  public ResponseEntity<String> getMessage() {
    return ResponseEntity.ok().body("Ты являешься пользователем с ролью USER");
  }

}