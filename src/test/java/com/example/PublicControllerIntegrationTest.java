package com.example;

import com.example.dto.CreateUserRequest;
import com.example.service.PublicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class PublicControllerIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PublicService publicService;

  @SneakyThrows
  @Test
  @DisplayName("создан пользователь с ролью USER, когда не найден с таким же username, "
      + "то ответ статус ок, и возвращается сообщение")
  @WithMockUser
  void createUser_whenUserNotFound_thenReturnedMessage() {
    when(publicService.createUser(any(CreateUserRequest.class))).thenReturn(
        ResponseEntity.ok("Пользователь успешно создан."));

    String result = mockMvc.perform(post("/register/user")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new CreateUserRequest())))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    assertThat("Пользователь успешно создан.", equalTo(result));
    verify(publicService, times(1)).createUser(any(CreateUserRequest.class));
  }

  @SneakyThrows
  @Test
  @DisplayName("создан пользователь с ролью USER, когда найден с таким же username, "
      + "то ответ статус bad request, и возвращается сообщение")
  @WithMockUser
  void createUser_whenUserFound_thenReturnedMessage() {
    when(publicService.createUser(any(CreateUserRequest.class)))
        .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким именем уже существует."));

    String result = mockMvc.perform(post("/register/user")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new CreateUserRequest())))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    assertThat("Пользователь с таким именем уже существует.", equalTo(result));
    verify(publicService, times(1)).createUser(any(CreateUserRequest.class));
  }

}