package com.example.security;

import com.example.model.TokenAuthentication;
import com.example.model.Role;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public final class TokenUtils {

  public static TokenAuthentication generate(Claims claims) {
    final TokenAuthentication jwtInfoToken = new TokenAuthentication();
    jwtInfoToken.setRoles(getRoles(claims));
    jwtInfoToken.setFirstName(claims.get("firstName", String.class));
    jwtInfoToken.setUsername(claims.getSubject());
    return jwtInfoToken;
  }

  private static Set<Role> getRoles(Claims claims) {
    final List<String> roles = claims.get("roles", List.class);
    return roles.stream()
        .map(Role::valueOf)
        .collect(Collectors.toSet());
  }

}