package com.bag.complaint_system.analytics.application.service;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByStatusResponse;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByStatusUseCase;
import com.bag.complaint_system.analytics.application.ports.output.AnalyticsPersistencePort;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetComplaintsByStatusService implements GetComplaintsByStatusUseCase {

  private final AnalyticsPersistencePort analyticsPersistencePort;
  private final UserPersistencePort userPersistencePort;

  @Override
  public ComplaintByStatusResponse execute(Long authId) {
    User user = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!user.isAdmin()) {
      throw new IllegalArgumentException("Only admins can view complaints by status");
    }

    Map<String, Long> data = analyticsPersistencePort.countComplaintByStatus();

    Long total = data.values().stream().mapToLong(Long::longValue).sum();

    return ComplaintByStatusResponse.builder().data(data).totalComplaints(total).build();
  }
}
