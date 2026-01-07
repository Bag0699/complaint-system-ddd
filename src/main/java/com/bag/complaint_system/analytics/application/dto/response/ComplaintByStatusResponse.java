package com.bag.complaint_system.analytics.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintByStatusResponse {
  @Schema(
      description =
          "A map where keys are complaint statuses and values are the count of complaints in that status",
      example = "{\"RECEIVED\": 1, \"IN_PROGRESS\": 3, \"CLOSED\": 5}")
  private Map<String, Long> data;

  @Schema(description = "The total sum of all complaints across all statuses", example = "9")
  private Long totalComplaints;
}
