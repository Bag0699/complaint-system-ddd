package com.bag.complaint_system.complaint.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {
  @Schema(description = "Unique identifier of the complaint", example = "1")
  private Long id;

  @Schema(description = "ID of the victim who filed the complaint", example = "1")
  private Long victimId;

  @Schema(description = "Full name of the victim", example = "Maria Rojas")
  private String victimName;

  @Schema(
      description = "Brief summary or start of the incident description",
      example = "Physical aggression in public spaces...")
  private String description;

  @Schema(description = "Current status of the complaint process", example = "RECEIVED")
  private String status;

  @Schema(description = "The specific type of violence reported", example = "PHYSICAL")
  private String violenceType;

  @Schema(description = "The date when the reported incident took place", example = "2025-09-20")
  private LocalDate incidentDate;

  @Schema(description = "Location where the incident occurred", example = "Cercado de Lima")
  private String incidentLocation;

  @Schema(description = "Total number of evidence files attached to this complaint", example = "3")
  private int evidenceCount;

  @Schema(description = "Creation timestamp of the record", example = "2025-12-12")
  private LocalDateTime createdAt;

  @Schema(description = "Last update timestamp of the record", example = "2025-12-12")
  private LocalDateTime updatedAt;
}
