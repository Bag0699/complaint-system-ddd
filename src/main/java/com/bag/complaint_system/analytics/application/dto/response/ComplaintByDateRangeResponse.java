package com.bag.complaint_system.analytics.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintByDateRangeResponse {
  @Schema(
      description = "Map containing the count of complaints per date.",
      example = "{\"2025-10-01\": 5, \"2025-10-02\": 3}")
  private Map<LocalDate, Long> data;

  @Schema(description = "Total number of complaints within the range.", example = "8")
  private Long totalComplaints;

  @Schema(description = "The start date of the requested range.", example = "2025-10-01")
  private LocalDate startDate;

  @Schema(description = "The end date of the requested range.", example = "2025-10-31")
  private LocalDate endDate;
}
