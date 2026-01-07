package com.bag.complaint_system.complaint.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComplaintStatusRequest {

  @Schema(
      description =
          "The new status to be assigned to the complaint. Must be a valid state within the system workflow.",
      example = "IN_PROGRESS",
      allowableValues = {
        "RECEIVED",
        "IN_REVIEW",
        "ACTION_TAKEN",
        "CLOSED",
      })
  @NotBlank(message = "The field New status cannot be blank or null.")
  private String newStatus;
}
