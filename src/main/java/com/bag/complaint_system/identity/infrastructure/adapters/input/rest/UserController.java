package com.bag.complaint_system.identity.infrastructure.adapters.input.rest;

import com.bag.complaint_system.identity.application.dto.request.RegisterUserRequest;
import com.bag.complaint_system.identity.application.dto.request.UpdateProfileRequest;
import com.bag.complaint_system.identity.application.dto.response.AuthResponse;
import com.bag.complaint_system.identity.application.dto.response.UserProfileResponse;
import com.bag.complaint_system.identity.application.ports.input.CreateAdminUseCase;
import com.bag.complaint_system.identity.application.ports.input.GetProfileUseCase;
import com.bag.complaint_system.identity.application.ports.input.UpdateProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(
    name = "User Management",
    description = "Operations related to user profiles and administrative accounts")
public class UserController {

  private final CreateAdminUseCase createAdminUseCase;
  private final UpdateProfileUseCase updateProfileUseCase;
  private final GetProfileUseCase getProfileUseCase;

  @PostMapping("/admin")
  @Operation(
      summary = "Create an admin account",
      description = "Registers a new user with administrative privileges.")
  public ResponseEntity<AuthResponse> createAdmin(@Valid @RequestBody RegisterUserRequest request) {
    AuthResponse response = createAdminUseCase.execute(request);

    return ResponseEntity.created(URI.create("/api/v1/users/admin" + response.getId()))
        .body(response);
  }

  @GetMapping("/profile")
  @Operation(
      summary = "Get current user profile",
      description =
          "Retrieves the profile information of the currently authenticated user based on the JWT token.")
  public ResponseEntity<UserProfileResponse> getProfile() {
    UserProfileResponse response = getProfileUseCase.execute();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update user profile",
      description = "Updates the profile details for a specific user ID.")
  public ResponseEntity<AuthResponse> updateProfile(
      @Valid @RequestBody UpdateProfileRequest request, @PathVariable Long id) {
    AuthResponse response = updateProfileUseCase.execute(id, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
