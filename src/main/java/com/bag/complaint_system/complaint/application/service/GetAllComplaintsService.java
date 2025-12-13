package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintResponse;
import com.bag.complaint_system.complaint.application.mapper.ComplaintMapper;
import com.bag.complaint_system.complaint.application.ports.input.GetAllComplaintsUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllComplaintsService implements GetAllComplaintsUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final UserPersistencePort userPersistencePort;
  private final ComplaintMapper complaintMapper;

  @Override
  public List<ComplaintResponse> execute(Long authId) {

    User authUser = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!authUser.isAdmin()) {
      throw new RoleAccessDeniedException("Only admins can view all complaints");
    }

    List<Complaint> complaints = complaintPersistencePort.findAll();

    List<Long> victimIds = complaints.stream().map(Complaint::getVictimId).toList();

    Map<Long, String> victimNamesMap =
        userPersistencePort.findAllById(victimIds).stream()
            .collect(Collectors.toMap(User::getId, User::getFullName));

    List<String> victimNames =
        complaints.stream()
            .map(c -> victimNamesMap.getOrDefault(c.getVictimId(), "Unknow User"))
            .toList();

    return complaintMapper.toComplaintResponseList(complaints, victimNames);
  }
}
