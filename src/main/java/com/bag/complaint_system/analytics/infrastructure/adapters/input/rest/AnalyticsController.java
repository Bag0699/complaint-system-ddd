package com.bag.complaint_system.analytics.infrastructure.adapters.input.rest;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByDateRangeResponse;
import com.bag.complaint_system.analytics.application.dto.response.ComplaintByStatusResponse;
import com.bag.complaint_system.analytics.application.dto.response.ComplaintByTypeResponse;
import com.bag.complaint_system.analytics.application.ports.input.GetAverageResolutionTimeUseCase;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByDateUseCase;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByStatusUseCase;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByTypeUseCase;
import com.bag.complaint_system.shared.config.SecurityContextHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Analytics and metrics related to system complaints.")
public class AnalyticsController {

  private final GetComplaintsByDateUseCase getComplaintsByDateUseCase;
  private final GetComplaintsByStatusUseCase getComplaintsByStatusUseCase;
  private final GetComplaintsByTypeUseCase getComplaintsByTypeUseCase;
  private final GetAverageResolutionTimeUseCase getAverageResolutionTimeUseCase;
  private final SecurityContextHelper securityContextHelper;

  @GetMapping("/complaints-by-date")
  @Operation(
      summary = "Get complaints by date range",
      description = "Returns complaint statistics within a given date range")
  public ComplaintByDateRangeResponse getComplaintsByDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    Long authId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintsByDateUseCase.execute(authId, startDate, endDate);
  }

  @GetMapping("/complaints-by-type")
  @Operation(
      summary = "Get complaints by type",
      description = "Returns complaints grouped by type.Returns complaints grouped by type.")
  public ComplaintByTypeResponse getComplaintsByType() {
    Long authId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintsByTypeUseCase.execute(authId);
  }

  @GetMapping("/complaints-by-status")
  @Operation(
      summary = "Get complaints by status",
      description = "Returns complaints grouped by status.")
  public ComplaintByStatusResponse getComplaintsByStatus() {
    Long authId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintsByStatusUseCase.execute(authId);
  }

  @GetMapping("/average-resolution-time")
  @Operation(
      summary = "Get average complaint resolution time",
      description = "Returns the average resolution time of complaints.")
  public Double getAverageResolutionTime() {
    Long authId = securityContextHelper.getAuthenticatedUserId();
    return getAverageResolutionTimeUseCase.execute(authId);
  }
}
