package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.request.CreateComplaintRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.input.CreateComplaintUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.complaint.domain.entity.Aggressor;
import com.bag.complaint_system.complaint.domain.valueobject.VictimRelationship;
import com.bag.complaint_system.complaint.domain.valueobject.ViolenceType;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateComplaintService implements CreateComplaintUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final ComplaintMapper complaintMapper;

  @Override
  @Transactional
  public ComplaintDetailResponse execute(Long victimId, CreateComplaintRequest request) {
    User victim = userPersistencePort.findById(victimId).orElseThrow(UserNotFoundException::new);

    if (!victim.isVictim()) {
      throw new RoleAccessDeniedException("Only victims can create complaints");
    }

    // Parse enums
    ViolenceType violenceType = ViolenceType.valueOf(request.getViolenceType().toUpperCase());
    VictimRelationship relationship =
        VictimRelationship.valueOf(request.getAggressorRelationship().toUpperCase());

    // Create aggressor
    Aggressor aggressor =
        Aggressor.create(
            request.getAggressorFullName(), relationship, request.getAggressorAdditionalDetails());

    // Crate complaint
    Complaint complaint =
        Complaint.create(
            victimId,
            request.getDescription(),
            violenceType,
            request.getIncidentDate(),
            request.getIncidentLocation(),
            aggressor);

    // Save complaint
    Complaint savedComplaint = complaintPersistencePort.save(complaint);

    // Map to response
    return complaintMapper.toDetailResponse(
        savedComplaint, victim.getFullName(), victim.getEmail().getValue());
  }
}
