package com.bag.complaint_system.identity.infrastructure.adapters.input.rest;

import com.bag.complaint_system.identity.application.dto.request.LoginRequest;
import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.ports.input.LoginUseCase;
import com.bag.complaint_system.identity.application.ports.input.RegisterUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User registration and login operations")
public class AuthController {

  private final RegisterUserUseCase registerUserUseCase;
  private final LoginUseCase loginUserUseCase;

  @PostMapping("/register")
  @Operation(
      summary = "Register a new user",
      description = "Register a new user and return a token")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserRequest request) {
    AuthResponse response = registerUserUseCase.execute(request);

    return ResponseEntity.created(URI.create("/api/v1/auth/register" + response.getId()))
        .body(response);
  }

  @PostMapping("/login")
  @Operation(
      summary = "Login",
      description =
          "Authenticate the user with credentials and return a JWT token for future access.")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    AuthResponse response = loginUserUseCase.execute(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
