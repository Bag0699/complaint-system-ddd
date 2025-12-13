package com.bag.complaint_system.complaint.infrastructure.adapters.input.rest;

import com.bag.complaint_system.complaint.application.dto.request.CreateComplaintRequest;
import com.bag.complaint_system.complaint.application.dto.request.UpdateComplaintStatusRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;

import com.bag.complaint_system.complaint.application.ports.input.*;
import com.bag.complaint_system.shared.config.SecurityContextHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/complaints")
public class ComplaintController {

  private final CreateComplaintUseCase createComplaintUseCase;
  private final GetComplaintsByVictimUseCase getComplaintsByVictimUseCase;
  private final GetComplaintDetailUseCase getComplaintDetailUseCase;
  private final GetAllComplaintsUseCase getAllComplaintsUseCase;
  private final UpdateComplaintStatusUseCase updateComplaintStatusUseCase;

  private final SecurityContextHelper securityContextHelper;

  // Para crear una denuncia - solo víctima
  @PostMapping()
  public ResponseEntity<ComplaintDetailResponse> createComplaint(
      @Valid @RequestBody CreateComplaintRequest request) {

    Long victimId = securityContextHelper.getAuthenticatedUserId();
    ComplaintDetailResponse response = createComplaintUseCase.execute(victimId, request);

    return ResponseEntity.created(URI.create("/api/v1/complaints" + response.getId()))
        .body(response);
  }

  // Para obtener todas las denuncias - solo víctima
  @GetMapping("/my-complaints")
  public List<ComplaintResponse> getMyComplaints() {
    Long victimId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintsByVictimUseCase.execute(victimId);
  }

  // Para obtener todas las denuncias - solo admin
  @GetMapping
  public List<ComplaintResponse> getAllComplaints() {
    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    return getAllComplaintsUseCase.execute(authenticatedUserId);
  }

  // Para obtener detalles de una denuncia por ID
  @GetMapping("/{id}")
  public ComplaintDetailResponse getComplaintDetail(@PathVariable Long id) {
    Long victimId = securityContextHelper.getAuthenticatedUserId();
    return getComplaintDetailUseCase.execute(id, victimId);
  }

  // Para actualizar el estado de una denuncia - solo admin
  @PatchMapping("/{id}/status")
  public ComplaintDetailResponse updateComplaintStatus(
      @PathVariable Long id, @Valid @RequestBody UpdateComplaintStatusRequest request) {
    Long authenticatedUserId = securityContextHelper.getAuthenticatedUserId();
    return updateComplaintStatusUseCase.execute(id, authenticatedUserId, request);
  }
}
