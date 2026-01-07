package com.bag.complaint_system.analytics.infrastructure.adapters;

import com.bag.complaint_system.analytics.application.ports.output.AnalyticsPersistencePort;
import com.bag.complaint_system.analytics.domain.valueobject.DateRange;
import com.bag.complaint_system.analytics.infrastructure.adapters.output.mapper.AnalyticsMapper;
import com.bag.complaint_system.analytics.infrastructure.adapters.output.persistence.repository.JpaAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnalyticsRepositoryAdapter implements AnalyticsPersistencePort {

  private final JpaAnalyticsRepository jpaAnalyticsRepository;
  private final AnalyticsMapper analyticsMapper;

  @Override
  public Map<LocalDate, Long> countComplaintsByDate(DateRange dateRange) {
    List<Object[]> results =
        jpaAnalyticsRepository.countComplaintsByDate(
            dateRange.getStartDate(), dateRange.getEndDate());

    Map<LocalDate, Long> map = new HashMap<>();
    for (Object[] result : results) {
      LocalDate date = ((Date) result[0]).toLocalDate();
      Long count = (Long) result[1];
      map.put(date, count);
    }
    return map;
  }

  @Override
  public Map<String, Long> countComplaintsByViolenceType() {
    List<Object[]> results = jpaAnalyticsRepository.countComplaintsByViolenceType();
    return analyticsMapper.converToMap(results);
  }

  @Override
  public Map<String, Long> countComplaintsByViolenceType(DateRange dateRange) {
    List<Object[]> results =
        jpaAnalyticsRepository.countComplaintsByViolenceTypeInDateRange(
            dateRange.getStartDate(), dateRange.getEndDate());

    return analyticsMapper.converToMap(results);
  }

  @Override
  public Map<String, Long> countComplaintByStatus() {
    List<Object[]> results = jpaAnalyticsRepository.countComplaintsByStatus();
    return analyticsMapper.converToMap(results);
  }

  @Override
  public Map<String, Long> countComplaintsByStatus(DateRange dateRange) {
    List<Object[]> results =
        jpaAnalyticsRepository.countComplaintsByStatusInDateRange(
            dateRange.getStartDate(), dateRange.getEndDate());
    return analyticsMapper.converToMap(results);
  }

  @Override
  public Long countTotalComplaints() {
    return jpaAnalyticsRepository.count();
  }

  @Override
  public Long countComplaintsInDateRange(DateRange dateRange) {
    List<Object[]> results =
        jpaAnalyticsRepository.countComplaintsByDate(
            dateRange.getStartDate(), dateRange.getEndDate());

    return results.stream().mapToLong(result -> (Long) result[0]).sum();
  }

  @Override
  public Double getAverageResolutionTime() {
    Double avg = jpaAnalyticsRepository.getAverageResolutionTime();
    return avg != null ? avg : 0.0;
  }
}
