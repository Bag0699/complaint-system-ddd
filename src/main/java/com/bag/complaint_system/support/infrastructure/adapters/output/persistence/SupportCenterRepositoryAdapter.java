package com.bag.complaint_system.support.infrastructure.adapters.output.persistence;

import com.bag.complaint_system.support.application.ports.output.SupportPersistencePort;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import com.bag.complaint_system.support.domain.valueobject.District;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.entity.SupportCenterEntity;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.mapper.SupportCenterPersistenceMapper;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.repository.JpaSupportCenterRepository;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.utils.DistrictEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SupportCenterRepositoryAdapter implements SupportPersistencePort {

  private final JpaSupportCenterRepository jpaSupportCenterRepository;
  private final SupportCenterPersistenceMapper mapper;

  @Override
  public SupportCenter save(SupportCenter supportCenter) {
    SupportCenterEntity entity = mapper.toSupportCenterEntity(supportCenter);
    return mapper.toSupportCenter(jpaSupportCenterRepository.save(entity));
  }

  @Override
  public Optional<SupportCenter> findById(Long id) {
    return jpaSupportCenterRepository.findById(id).map(mapper::toSupportCenter);
  }

  @Override
  public List<SupportCenter> findAllActive() {
    return jpaSupportCenterRepository.findByIsActive(true).stream()
        .map(mapper::toSupportCenter)
        .toList();
  }

  @Override
  public List<SupportCenter> findAll() {
    return jpaSupportCenterRepository.findAll().stream().map(mapper::toSupportCenter).toList();
  }

  @Override
  public List<SupportCenter> findByDistrict(District district) {
    DistrictEntity districtEntity = DistrictEntity.valueOf(district.name());

    return jpaSupportCenterRepository.findByDistrict(districtEntity).stream()
        .map(mapper::toSupportCenter)
        .toList();
  }

  @Override
  public List<SupportCenter> findActiveByDistrict(District district) {
    DistrictEntity districtEntity = DistrictEntity.valueOf(district.name());

    return jpaSupportCenterRepository.findByDistrictAndIsActive(districtEntity, true).stream()
        .map(mapper::toSupportCenter)
        .toList();
  }

  @Override
  public void deleteById(Long id) {
    if (!jpaSupportCenterRepository.existsById(id)) {
      throw new IllegalArgumentException("SupportCenter not found");
    }
    jpaSupportCenterRepository.deleteById(id);
  }
}
