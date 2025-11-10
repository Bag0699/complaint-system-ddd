package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.input.GetAllComplaintUseCase;
import com.bag.complaint_system.complaint.application.ports.input.GetComplaintsByVictimUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetComplaintsByVictimService implements GetComplaintsByVictimUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final ComplaintMapper complaintMapper;

  @Override
  public List<ComplaintResponse> execute(Long victimId) {

    User userAuth = userPersistencePort.findById(victimId).orElseThrow(UserNotFoundException::new);

    if (!userAuth.isVictim()) {
      throw new RoleAccessDeniedException("Only victims can view their own complaints");
    }

    List<Complaint> complaints = complaintPersistencePort.findByVictimId(victimId);

    return complaints.stream()
        .map(complaint -> complaintMapper.toComplaintResponse(complaint, userAuth.getFullName()))
        .toList();
  }
}
