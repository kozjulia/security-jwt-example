package com.example.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "users_id")
  private UUID id;

  @Column(name = "users_username", nullable = false, unique = true)
  private String username;

  @Column(name = "users_email", nullable = false)
  private String email;

  @Column(name = "users_password", nullable = false)
  private String password;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "users_id"))
  private Set<Role> roles;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(username, user.username) && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email);
  }

}