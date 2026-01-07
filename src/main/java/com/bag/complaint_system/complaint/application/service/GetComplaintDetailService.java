package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.input.GetComplaintDetailUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.ComplaintNotFoundException;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import com.bag.complaint_system.shared.exception.VictimNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetComplaintDetailService implements GetComplaintDetailUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final ComplaintMapper complaintMapper;

  @Override
  @Transactional(readOnly = true)
  public ComplaintDetailResponse execute(Long complaintId, Long authId) {

    Complaint complaint =
        complaintPersistencePort.findById(complaintId).orElseThrow(ComplaintNotFoundException::new);

    User userAuth = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    boolean isOwner = complaint.belongsToVictim(authId);
    boolean isAdmin = userAuth.isAdmin();

    if (!isAdmin && !isOwner) {
      throw new RoleAccessDeniedException("You can only view your own complaints");
    }

    User victim =
        userPersistencePort
            .findById(complaint.getVictimId())
            .orElseThrow(VictimNotFoundException::new);

    return complaintMapper.toDetailResponse(
        complaint, victim.getFullName(), victim.getEmail().getValue());
  }
}
