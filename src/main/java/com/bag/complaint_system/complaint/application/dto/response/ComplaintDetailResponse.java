package com.bag.complaint_system.complaint.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDetailResponse {
  @Schema(description = "Unique identifier of the complaint", example = "1")
  private Long id;

  @Schema(description = "ID of the victim associated with the complaint", example = "1")
  private Long victimId;

  @Schema(description = "Full name of the victim", example = "Maria Rojas")
  private String victimName;

  @Schema(description = "Contact email of the victim", example = "maria.rojas@victim.com")
  private String victimEmail;

  @Schema(
      description = "Detailed narrative of the reported incident",
      example = "Physical aggression in public spaces by an ex-partner.")
  private String description;

  @Schema(description = "Current processing state of the complaint", example = "RECEIVED")
  private String status;

  @Schema(description = "Category of violence reported", example = "PHYSICAL")
  private String violenceType;

  @Schema(description = "Date when the incident occurred", example = "2025-09-20")
  private LocalDate incidentDate;

  @Schema(
      description = "Geographic or descriptive location of the incident",
      example = "Cercado de Lima, near the main plaza")
  private String incidentLocation;

  @Schema(description = "Detailed information about the alleged aggressor")
  private AggressorResponse aggressor;

  @Schema(description = "List of attached evidence files or documents linked to the complaint")
  private List<EvidenceResponse> evidences;

  @Schema(description = "Timestamp when the record was created", example = "2025-12-12")
  private LocalDateTime createdAt;

  @Schema(description = "Timestamp of the last update to the record", example = "2025-12-12")
  private LocalDateTime updatedAt;
}
