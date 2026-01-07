package com.bag.complaint_system.support.application.ports.output;

import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import com.bag.complaint_system.support.domain.valueobject.District;

import java.util.List;
import java.util.Optional;

public interface SupportPersistencePort {
  SupportCenter save(SupportCenter supportCenter);

  Optional<SupportCenter> findById(Long id);

  List<SupportCenter> findAllActive();

  List<SupportCenter> findAll();

  List<SupportCenter> findByDistrict(District district);

  List<SupportCenter> findActiveByDistrict(District district);

  void deleteById(Long id);
}
