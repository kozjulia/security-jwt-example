package com.example;

import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @SneakyThrows
  @Test
  @DisplayName("получено сообщение и статус OK, если роль ADMIN")
  @WithMockUser(authorities = {"ADMIN"})
  void getMessageAndStatus200_whenRoleIsAdmin() {
    String result = mockMvc.perform(get("/admin")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    assertThat("Ты являешься пользователем с ролью ADMIN", equalTo(result));
  }

  @SneakyThrows
  @Test
  @DisplayName("получено сообщение и статус 403, если роль не ADMIN")
  @WithAnonymousUser
  void getEmptyMessageAndStatus403_whenRoleIsNotAdmin() {
    String result = mockMvc.perform(get("/admin")
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    assertThat("", equalTo(result));
  }

}