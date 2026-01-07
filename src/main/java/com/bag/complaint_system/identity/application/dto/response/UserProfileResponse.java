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
public class UserProfileResponse {
  @Schema(
      description = "Full legal name of the user (First Name and Last Name combined)",
      example = "admin admin")
  private String fullName;

  @Schema(
      description = "Registered email address used for notifications and account identity",
      example = "admin@admin.com")
  private String email;

  @Schema(
      description = "Contact phone number including the international country code",
      example = "+5199999...")
  private String phone;

  @Schema(description = "The security clearance or group assigned to the user", example = "ADMIN")
  private String role;
}
