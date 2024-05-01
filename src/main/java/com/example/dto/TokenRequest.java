package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос токена")
public class TokenRequest {

    @Schema(description = "Логин пользователя", example = "1")
    private String username;

    @Schema(description = "Пароль пользователя", example = "1")
    private String password;

}