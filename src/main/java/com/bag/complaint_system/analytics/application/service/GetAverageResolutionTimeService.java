package com.bag.complaint_system.analytics.application.service;

import com.bag.complaint_system.analytics.application.ports.input.GetAverageResolutionTimeUseCase;
import com.bag.complaint_system.analytics.application.ports.output.AnalyticsPersistencePort;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAverageResolutionTimeService implements GetAverageResolutionTimeUseCase {

  private final AnalyticsPersistencePort analyticsPersistencePort;
  private final UserPersistencePort userPersistencePort;

  @Override
  public Double execute(Long authId) {
    User user = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!user.isAdmin()) {
      throw new IllegalArgumentException("Only admins can view average resolution time");
    }

    return analyticsPersistencePort.getAverageResolutionTime();
  }
}
