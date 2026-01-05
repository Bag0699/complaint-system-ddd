package com.bag.complaint_system.identity.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  @Schema(
      description = "JWT (JSON Web Token) used to authorize subsequent API requests",
      example = "eyJhbGciOiJIUzI1NiJ9...")
  private String token;

  @Schema(description = "The authentication scheme used", example = "Bearer")
  private String type = "Bearer";

  @Schema(description = "Unique identifier of the authenticated user", example = "3")
  private Long id;

  @Schema(
      description = "Electronic mail address associated with the user account",
      example = "admin@admin.com")
  private String email;

  @Schema(description = "User's legal first name", example = "admin")
  private String firstName;

  @Schema(description = "User's legal last name", example = "admin")
  private String lastName;

  @Schema(
      description = "The security role assigned to the user which defines their permissions",
      example = "ADMIN")
  private String role;
}
