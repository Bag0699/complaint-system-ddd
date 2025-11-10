package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.request.UpdateComplaintStatusRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.input.UpdateComplaintStatusUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.complaint.domain.valueobject.ComplaintStatus;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateComplaintStatusService implements UpdateComplaintStatusUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final ComplaintMapper complaintMapper;

  @Override
  public ComplaintDetailResponse execute(
      Long complaintId, Long authId, UpdateComplaintStatusRequest request) {

    User authUser = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!authUser.isAdmin()) {
      throw new RoleAccessDeniedException("The user is not an admin required for this action");
    }

    Complaint complaint =
        complaintPersistencePort.findById(complaintId).orElseThrow(ComplaintNotFoundException::new);

    User victim =
        userPersistencePort
            .findById(complaint.getVictimId())
            .orElseThrow(VictimNotFoundException::new);

    ComplaintStatus newStatus;
    try {
      newStatus = ComplaintStatus.valueOf(request.getNewStatus().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidValueException();
    }

    ComplaintStatus previousStatus = complaint.getStatus();

    complaint.updateStatus(newStatus);

    Complaint updatedComplaint = complaintPersistencePort.save(complaint);

    log.info(
        "Complaint status updated from {} to {} for complaint {}",
        previousStatus,
        newStatus,
        complaintId);

    return complaintMapper.toDetailResponse(
        updatedComplaint, victim.getFullName(), victim.getEmail().getValue());
  }
}
