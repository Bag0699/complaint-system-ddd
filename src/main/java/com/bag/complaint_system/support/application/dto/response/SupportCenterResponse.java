package com.bag.complaint_system.support.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCenterResponse {
  @Schema(description = "Unique identifier of the support center", example = "1")
  private Long id;

  @Schema(description = "Official name of the center", example = "Casa Refugio Miraflores")
  private String name;

  @Schema(description = "Specific street name and number", example = "Calle Granda 456")
  private String street;

  @Schema(
      description = "Administrative district where the center is located",
      example = "MIRAFLORES")
  private String district;

  @Schema(
      description = "Full formatted address for display purposes",
      example = "Calle Granda 456, Miraflores")
  private String fullAddress;

  @Schema(description = "Contact phone number for the center", example = "+51988456456")
  private String phone;

  @Schema(description = "Official email for inquiries", example = "refugio.miraflores@correo.org")
  private String email;

  @Schema(description = "Operating hours and days of availability", example = "atención 24 horas")
  private String schedule;

  @Schema(description = "Indicates if the center is currently operational", example = "true")
  private boolean isActive;

  @Schema(
      description = "Timestamp when the center was registered in the system",
      example = "2026-01-03T12:32:59")
  private LocalDateTime createdAt;
}
