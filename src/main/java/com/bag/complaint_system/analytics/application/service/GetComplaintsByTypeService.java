package com.bag.complaint_system.analytics.application.service;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByTypeResponse;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByTypeUseCase;
import com.bag.complaint_system.analytics.application.ports.output.AnalyticsPersistencePort;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetComplaintsByTypeService implements GetComplaintsByTypeUseCase {

  private final AnalyticsPersistencePort analyticsPersistencePort;
  private final UserPersistencePort userPersistencePort;

  @Override
  public ComplaintByTypeResponse execute(Long authId) {
    User user = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!user.isAdmin()) {
      throw new RoleAccessDeniedException("Only admins can view complaints by date");
    }

    Map<String, Long> data = analyticsPersistencePort.countComplaintsByViolenceType();
    Long total = data.values().stream().mapToLong(Long::longValue).sum();

    return ComplaintByTypeResponse.builder().data(data).totalComplaints(total).build();
  }
}
