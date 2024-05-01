package com.example.security;

import com.example.model.TokenAuthentication;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

  private static final String AUTHORIZATION = "Authorization";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
      throws IOException, ServletException {
    final String token = getTokenFromRequest((HttpServletRequest) request);
    if (token != null && tokenProvider.validateAccessToken(token)) {
      final Claims claims = tokenProvider.getAccessClaims(token);
      final TokenAuthentication jwtInfoToken = TokenUtils.generate(claims);
      jwtInfoToken.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
    }
    fc.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    final String bearer = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

}