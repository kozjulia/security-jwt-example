package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Новый пользователь")
public class CreateUserRequest {

  @Schema(description = "Логин пользователя", example = "1")
  private String username;

  @Schema(description = "Пароль пользователя", example = "1")
  private String password;

  @Schema(description = "Электронный адрес пользователя", example = "mail@mail.ru")
  private String email;

}