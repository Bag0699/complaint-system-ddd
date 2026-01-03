package com.bag.complaint_system.support.infrastructure.adapters.output.persistence.repository;

import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.entity.SupportCenterEntity;
import com.bag.complaint_system.support.infrastructure.adapters.output.persistence.utils.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSupportCenterRepository extends JpaRepository<SupportCenterEntity, Long> {
  List<SupportCenterEntity> findByIsActive(Boolean isActive);

  List<SupportCenterEntity> findByDistrict(DistrictEntity district);

  List<SupportCenterEntity> findByDistrictAndIsActive(DistrictEntity district, Boolean isActive);
}
