package com.example.service;

import com.example.dto.CreateUserRequest;
import org.springframework.http.ResponseEntity;

public interface PublicService {

  ResponseEntity<String> createUser(CreateUserRequest request);

}