package com.bag.complaint_system.complaint.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceResponse {

  @Schema(description = "Unique identifier of the evidence record", example = "101")
  private Long id;

  @Schema(description = "Original name of the uploaded file", example = "incident_photo.jpg")
  private String fileName;

  @Schema(description = "MIME type of the file", example = "image/jpeg")
  private String fileType;

  @Schema(description = "Size of the file in bytes", example = "2048576")
  private Long fileSize;

  @Schema(
      description = "Timestamp when the evidence was successfully uploaded to the system",
      example = "2025-12-12T22:40:00")
  private LocalDateTime uploadedAt;
}
