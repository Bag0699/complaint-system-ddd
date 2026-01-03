package com.bag.complaint_system.support.application.service;

import com.bag.complaint_system.shared.exception.InvalidDistrictException;
import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;
import com.bag.complaint_system.support.application.mapper.SupportCenterMapper;
import com.bag.complaint_system.support.application.ports.input.GetRecommendationsUseCase;
import com.bag.complaint_system.support.application.ports.output.SupportPersistencePort;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import com.bag.complaint_system.support.domain.valueobject.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRecommendationsService implements GetRecommendationsUseCase {
  private final SupportPersistencePort supportPersistencePort;
  private final SupportCenterMapper mapper;

  @Override
  public List<SupportCenterResponse> execute(String districtName) {
    District district;
    try {
      district = District.valueOf(districtName.toUpperCase().replace(" ", "_"));
    } catch (IllegalArgumentException e) {
      throw new InvalidDistrictException();
    }

    List<SupportCenter> centersRecommended =
        supportPersistencePort.findByDistrict(district).stream()
            .sorted(Comparator.comparing(SupportCenter::getName))
            .collect(Collectors.toList());

    return mapper.toResponseList(centersRecommended);
  }
}
