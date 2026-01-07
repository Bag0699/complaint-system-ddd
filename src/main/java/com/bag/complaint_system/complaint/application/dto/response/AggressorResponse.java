package com.bag.complaint_system.complaint.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggressorResponse {
  @Schema(description = "Unique identifier of the aggressor in the system", example = "1")
  private Long id;

  @Schema(description = "Full name of the alleged aggressor", example = "Carlos Valdivia Ríos")
  private String fullName;

  @Schema(
      description = "The nature of the link between the victim and the aggressor",
      example = "EX_PARTNER")
  private String relationship;

  @Schema(
      description = "Relevant context or background information about the aggressor",
      example = "Known history of violence and current known location.")
  private String additionalDetails;
}
