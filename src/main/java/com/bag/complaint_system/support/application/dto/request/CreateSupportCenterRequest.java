package com.bag.complaint_system.support.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateSupportCenterRequest {
  @Schema(
      description = "Official and unique name of the support center",
      example = "Casa Refugio Miraflores",
      minLength = 3,
      maxLength = 200)
  @NotBlank(message = "The field Name cannot be blank or null.")
  @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
  private String name;

  @Schema(
      description = "Physical street address where the center is located",
      example = "Calle Granda 456",
      maxLength = 300)
  @NotBlank(message = "Street address is required")
  @Size(max = 300, message = "Street address cannot exceed 300 characters")
  private String street;

  @Schema(description = "Administrative district for categorization", example = "MIRAFLORES")
  @NotNull(message = "The field District cannot be null.")
  private String district;

  @Schema(
      description = "Primary contact phone number (including country code recommended)",
      example = "+51988456456",
      minLength = 9,
      maxLength = 15)
  @NotBlank(message = "The field Phone cannot be blank or null.")
  @Size(min = 9, max = 15, message = "Phone must be between 9 and 15 characters")
  private String phone;

  @Schema(
      description = "Official email address for administrative or public contact",
      example = "refugio.miraflores@correo.org",
      maxLength = 150)
  @NotBlank(message = "The field Email cannot be blank or null.")
  @Email(message = "Email must be valid")
  @Size(max = 150, message = "Email cannot exceed 150 characters")
  private String email;

  @Schema(
      description = "General availability or public opening hours",
      example = "Monday to Friday, 24 hours",
      maxLength = 200)
  @NotBlank(message = "The field Schedule cannot be blank or null.")
  @Size(max = 200, message = "Schedule cannot exceed 200 characters")
  private String schedule;
}
