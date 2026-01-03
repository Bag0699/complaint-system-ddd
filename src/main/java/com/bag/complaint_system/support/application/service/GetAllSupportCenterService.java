package com.bag.complaint_system.support.application.service;

import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;
import com.bag.complaint_system.support.application.mapper.SupportCenterMapper;
import com.bag.complaint_system.support.application.ports.input.GetAllSupportCentersUseCase;
import com.bag.complaint_system.support.application.ports.output.SupportPersistencePort;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllSupportCenterService implements GetAllSupportCentersUseCase {

  private final SupportPersistencePort supportPersistencePort;
  private final SupportCenterMapper mapper;

  @Override
  public List<SupportCenterResponse> execute() {
    List<SupportCenter> centers = supportPersistencePort.findAll();

    return mapper.toResponseList(centers);
  }
}
