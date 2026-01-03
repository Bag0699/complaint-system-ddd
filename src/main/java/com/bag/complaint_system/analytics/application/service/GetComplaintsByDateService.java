package com.bag.complaint_system.analytics.application.service;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByDateRangeResponse;
import com.bag.complaint_system.analytics.application.ports.input.GetComplaintsByDateUseCase;
import com.bag.complaint_system.analytics.application.ports.output.AnalyticsPersistencePort;
import com.bag.complaint_system.analytics.domain.valueobject.DateRange;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetComplaintsByDateService implements GetComplaintsByDateUseCase {

  private final AnalyticsPersistencePort analyticsPersistencePort;
  private final UserPersistencePort userPersistencePort;

  @Override
  public ComplaintByDateRangeResponse execute(Long authId, LocalDate startDate, LocalDate endDate) {
    User user = userPersistencePort.findById(authId).orElseThrow(UserNotFoundException::new);

    if (!user.isAdmin()) {
      throw new IllegalArgumentException("Only admins can view complaints by date");
    }

    DateRange dateRange = DateRange.between(startDate, endDate);

    Map<LocalDate, Long> data = analyticsPersistencePort.countComplaintsByDate(dateRange);

    Long total = data.values().stream().mapToLong(Long::longValue).sum();

    return ComplaintByDateRangeResponse.builder()
        .data(data)
        .totalComplaints(total)
        .startDate(startDate)
        .endDate(endDate)
        .build();
  }
}
