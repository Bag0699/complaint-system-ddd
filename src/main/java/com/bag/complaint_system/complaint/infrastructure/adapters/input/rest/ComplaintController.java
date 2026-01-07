package com.bag.complaint_system.complaint.infrastructure.adapters.input.rest;

import com.bag.complaint_system.complaint.application.dto.request.CreateComplaintRequest;
import com.bag.complaint_system.complaint.application.dto.request.UpdateComplaintStatusRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;

import com.bag.complaint_system.complaint.application.dto.response.EvidenceResponse;
import com.bag.complaint_system.complaint.application.ports.input.*;
import com.bag.complaint_system.shared.config.SecurityContextHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/complaints")
@Tag(
    name = "Complaints",
    description = "Operations related to complaint management for victims and administrators.")
public class ComplaintController {

  private final CreateComplaintUseCase createComplaintUseCase;
  private final GetComplaintsByVictimUseCase getComplaintsByVictimUseCase;
  private final GetComplaintDetailUseCase getComplaintDetailUseCase;
  private final GetAllComplaintsUseCase getAllComplaintsUseCase;
  private final UpdateComplaintStatusUseCase updateComplaintStatusUseCase;
  private final AddEvidenceUseCase addEvidenceUseCase;

  private final SecurityContextHelper securityContextHelper;

  // Para crear una denuncia - solo víctima
  @PostMapping()
  @Operation(
      summary = "Create a new complaint",
      description = "Allows an authenticated victim to register a new complaint in the system.")
  public ResponseEntity<ComplaintDetailResponse> createComplaint(
      @Valid @RequestBody CreateComplaintRequest request) {

    Long victimId = securityContextHelper.getAuthenticatedUserId();
    ComplaintDetailResponse response = createComplaintUseCase.execute(victimId, request);

    return ResponseEntity.created(URI.create("/api/v1/complaints/" + response.getId()))
        .body(response);
  }

  // Para obtener todas las denuncias - solo víctima
  @GetMapping("/my-complaints")
  @Operation(
      summary = "Get personal complaints",
      description =
          "Retrieves a list of all complaints filed by the currently authenticated victim.")
  public List<ComplaintResponse> getMyComplaints() {
    Long victimId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintsByVictimUseCase.execute(victimId);
  }

  // Para obtener todas las denuncias - solo admin
  @GetMapping
  @Operation(
      summary = "Get all complaints",
      description = "Administrator access only. Retrieves all complaints registered in the system.")
  public List<ComplaintResponse> getAllComplaints() {
    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    return getAllComplaintsUseCase.execute(authenticatedUserId);
  }

  // Para obtener detalles de una denuncia por ID
  @GetMapping("/{id}")
  @Operation(
      summary = "Get complaint details",
      description = "Retrieves detailed information of a specific complaint by its unique ID.")
  public ComplaintDetailResponse getComplaintDetail(@PathVariable Long id) {
    Long victimId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintDetailUseCase.execute(id, victimId);
  }

  // Para actualizar el estado de una denuncia - solo admin
  @PatchMapping("/{id}/status")
  @Operation(
      summary = "Update complaint status",
      description = "Allows an administrator to update the status of a specific complaint.")
  public ComplaintDetailResponse updateComplaintStatus(
      @PathVariable Long id, @Valid @RequestBody UpdateComplaintStatusRequest request) {
    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    return updateComplaintStatusUseCase.execute(id, authenticatedUserId, request);
  }

  @Operation(
      summary = "Adding evidence to a complaint",
      description = "Upload a file as evidence for an existing complaint.")
  @PostMapping(value = "/{id}/evidence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EvidenceResponse> addEvidence(
      @PathVariable Long id, @RequestParam("file") MultipartFile file) {

    Long authUserId = securityContextHelper.getAuthenticatedUserId();
    EvidenceResponse response = addEvidenceUseCase.execute(id, authUserId, file);

    return ResponseEntity.created(URI.create("/api/v1/complaints/" + response.getId()))
        .body(response);
  }
}
