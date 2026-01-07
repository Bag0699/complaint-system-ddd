package com.bag.complaint_system.support.infrastructure.adapters.input.rest;

import com.bag.complaint_system.shared.config.SecurityContextHelper;
import com.bag.complaint_system.support.application.dto.request.CreateSupportCenterRequest;
import com.bag.complaint_system.support.application.dto.request.UpdateSupportCenterRequest;
import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;
import com.bag.complaint_system.support.application.ports.input.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/support-centers")
@Tag(
    name = "Support Centers",
    description =
        "Management of physical centers providing assistance and recommendations by location.")
public class SupportCenterController {

  private final CreateSupportCenterUseCase createSupportCenterUseCase;
  private final UpdateSupportCenterUseCase updateSupportCenterUseCase;
  private final DeleteSupportCenterUseCase deleteSupportCenterUseCase;
  private final GetAllSupportCentersUseCase getAllSupportCentersUseCase;
  private final GetRecommendationsUseCase getRecommendationsUseCase;
  private final SecurityContextHelper securityContextHelper;

  @PostMapping("/create")
  @Operation(
      summary = "Create a support center",
      description =
          "Registers a new support center in the system. Requires administrator privileges.")
  public ResponseEntity<SupportCenterResponse> createSupportCenter(
      @Valid @RequestBody CreateSupportCenterRequest request) {

    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    SupportCenterResponse response =
        createSupportCenterUseCase.execute(authenticatedUserId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}/edit")
  @Operation(
      summary = "Update support center details",
      description = "Updates the existing information of a support center identified by its ID.")
  public SupportCenterResponse updateSupportCenter(
      @PathVariable Long id, @Valid @RequestBody UpdateSupportCenterRequest request) {

    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    return updateSupportCenterUseCase.execute(id, authenticatedUserId, request);
  }

  @GetMapping
  @Operation(
      summary = "List all support centers",
      description = "Retrieves a complete list of all support centers registered in the database.")
  public List<SupportCenterResponse> getAllSupportCenters() {
    return getAllSupportCentersUseCase.execute();
  }

  @GetMapping("/recommendations/{district}")
  @Operation(
      summary = "Get recommendations by district",
      description =
          "Retrieves a filtered list of recommended support centers based on a specific geographic district.")
  public List<SupportCenterResponse> getRecommendationsByDistrict(@PathVariable String district) {
    return getRecommendationsUseCase.execute(district);
  }

  @DeleteMapping("/{id}/delete")
  @Operation(
      summary = "Delete a support center",
      description =
          "Permanently removes a support center from the system using its unique identifier.")
  public ResponseEntity<Void> deleteSupportCenter(@PathVariable Long id) {
    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    deleteSupportCenterUseCase.execute(id, authenticatedUserId);
    return ResponseEntity.noContent().build();
  }
}
