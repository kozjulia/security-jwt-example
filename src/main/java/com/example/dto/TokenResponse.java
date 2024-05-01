package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ токена")
public class TokenResponse {

  @Schema(description = "Токен", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzE0NTE4ODcwLCJleHAiOjE3MTQ1MTk0NzAsInJvbGVzIjpbIlVTRVIiXSwiaWQiOiI2ZGE4Mjk2Yy1kNGVhLTQwMTktYmM1Mi03YTBjZjlkODgxMTQifQ.IVOJ9g03TUUmODjvmJAZp2YZ9A8ZsAMBhgD_8bIBG3cn0wq9lupOpsma4usqiN_Z")
  private String token;

  @Schema(description = "Обновленный токен", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzE0NTE4ODcwLCJleHAiOjE3MTQ1MTk0NzAsInJvbGVzIjpbIlVTRVIiXSwiaWQiOiI2ZGE4Mjk2Yy1kNGVhLTQwMTktYmM1Mi03YTBjZjlkODgxMTQifQ.IVOJ9g03TUUmODjvmJAZp2YZ9A8ZsAMBhgD_8bIBG3cn0wq9lupOpsma4usqiN_Z")
  private String refreshToken;

}