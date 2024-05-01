package com.example.security;

import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import java.security.Key;
import java.util.Date;
import java.time.Duration;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final RefreshTokenRepository tokenRepository;
  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.token.expiration}")
  private Duration tokenExpiration;
  @Value("${jwt.refresh.token.expiration}")
  private Duration refreshTokenExpiration;

  public String generateAccessToken(@NonNull User user) {
    return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date())
        .setExpiration(new Date((System.currentTimeMillis() + tokenExpiration.toMillis())))
        .claim("roles", user.getRoles())
        .claim("id", user.getId())
        .signWith(getSigningKey())
        .compact();
  }

  public String generateRefreshToken(@NonNull User user) {
    Date expirationDate = new Date((System.currentTimeMillis() + refreshTokenExpiration.toMillis()));

    String token = Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(expirationDate)
        .signWith(getSigningKey())
        .compact();

    RefreshToken refreshToken = RefreshToken.builder()
        .user(user)
        .value(token)
        .expiryDate(expirationDate.toInstant())
        .build();
    tokenRepository.save(refreshToken);

    return token;
  }

  public boolean validateAccessToken(@NonNull String accessToken) {
    return validateToken(accessToken, getSigningKey());
  }

  public boolean validateRefreshToken(@NonNull String refreshToken) {
    return validateToken(refreshToken, getSigningKey());
  }

  private boolean validateToken(@NonNull String token, @NonNull Key secret) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException expEx) {
      log.error("Token expired", expEx);
    } catch (UnsupportedJwtException unsEx) {
      log.error("Unsupported jwt", unsEx);
    } catch (MalformedJwtException mjEx) {
      log.error("Malformed jwt", mjEx);
    } catch (SignatureException sEx) {
      log.error("Invalid signature", sEx);
    } catch (Exception e) {
      log.error("invalid token", e);
    }
    return false;
  }

  public Claims getAccessClaims(@NonNull String token) {
    return getClaims(token, getSigningKey());
  }

  public Claims getRefreshClaims(@NonNull String token) {
    return getClaims(token, getSigningKey());
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims getClaims(@NonNull String token, @NonNull Key secret) {
    return Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

}