package com.example.controller;

import com.example.dto.CreateUserRequest;
import com.example.service.PublicService;
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
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(name = "Регистрация пользователей", description = "Апи для регистрации пользователей с ролью USER")
public class PublicController {

  private final PublicService publicService;

  @PostMapping("/user")
  @Operation(
      summary = "Зарегистрировать пользователя",
      description = "Позволяет зарегистрировать пользователя с ролью USER"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class))})})
  /**
   * Регистрация пользователя.
   */
  public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
    return publicService.createUser(request);
  }

}