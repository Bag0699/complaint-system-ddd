package com.bag.complaint_system.complaint.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateComplaintRequest {

  @Schema(
      description =
          "Detailed description of the incident. Must be descriptive enough for initial assessment.",
      example = "I was physically assaulted by my former partner during a discussion at his home.",
      minLength = 20,
      maxLength = 2000)
  @NotBlank(message = "Description is required")
  @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
  private String description;

  @Schema(
      description = "The category of violence experienced",
      example = "PHYSICAL",
      allowableValues = {
        "PHYSICAL",
        "PSYCHOLOGICAL",
        "EMOTIONAL",
        "SOCIAL",
        "HARASSMENT",
        "ECONOMIC",
        "SEXUAL",
        "OTHER"
      })
  @NotNull(message = "Violence type is required")
  private String violenceType;

  @Schema(
      description = "The date when the event occurred. Cannot be a future date.",
      example = "2025-09-20")
  @PastOrPresent(message = "Incident date cannot be in the future")
  private LocalDate incidentDate;

  @Schema(
      description = "Specific address or landmark where the incident took place",
      example = "Cercado de Lima, near the main plaza",
      maxLength = 255)
  @Size(max = 255, message = "Incident location cannot exceed 255 characters")
  private String incidentLocation;

  // Aggressor information
  @Schema(
      description = "Full legal name of the person being reported",
      example = "Carlos Valdivia Ríos",
      minLength = 2,
      maxLength = 200)
  @NotBlank(message = "Aggressor full name is required")
  @Size(min = 2, max = 200, message = "Aggressor full name must be between 2 and 200 characters")
  private String aggressorFullName;

  @Schema(
      description = "The connection between the victim and the reported individual",
      example = "EX_PARTNER",
      allowableValues = {
        "FRIEND",
        "FAMILY",
        "NEIGHBOUR",
        "EX_PARTNER",
        "PARTNER",
        "STRANGE",
        "OTHER"
      })
  @NotNull(message = "Relationship with aggressor is required")
  private String aggressorRelationship;

  @Schema(
      description = "Any extra context about the aggressor, such as physical traits or history",
      example = "The individual has a history of similar behavior in previous relationships.",
      maxLength = 1000)
  @Size(max = 1000, message = "Additional details cannot exceed 1000 characters")
  private String aggressorAdditionalDetails;
}
