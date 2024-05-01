package com.example.repository;

import com.example.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @Query("""
      select rt.value from RefreshToken rt
      where rt.user.username = :username
      """)
  String findTokenValueByUserUsername(@Param("username") String username);

}