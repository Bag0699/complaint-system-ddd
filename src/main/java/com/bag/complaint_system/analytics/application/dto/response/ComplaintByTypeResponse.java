package com.bag.complaint_system.analytics.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintByTypeResponse {
  @Schema(
      description =
          "A map containing complaint types as keys and their respective counts as values",
      example = "{\"PHYSICAL\": 10, \"SEXUAL\": 5}")
  private Map<String, Long> data;

  @Schema(
      description = "The cumulative count of all complaints regardless of their type",
      example = "15")
  private Long totalComplaints;
}
