package com.bag.complaint_system.complaint.infrastructure.adapters;

import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.mapper.ComplaintPersistenceMapper;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity.ComplaintEntity;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.repository.JpaComplaintRepository;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.utils.StatusEntity;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.utils.ViolenceTypeEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ComplaintRepositoryAdapter implements ComplaintPersistencePort {

  private final JpaComplaintRepository jpaComplaintRepository;
  private final ComplaintPersistenceMapper complaintMapper;

  @Override
  public Complaint save(Complaint complaint) {
    ComplaintEntity complaintEntity = complaintMapper.toComplaintEntity(complaint);
    return complaintMapper.toComplaint(jpaComplaintRepository.save(complaintEntity));
  }

  @Override
  public Optional<Complaint> findById(Long id) {
    return jpaComplaintRepository.findById(id).map(complaintMapper::toComplaint);
  }

  @Override
  public List<Complaint> findByVictimId(Long victimId) {
    return jpaComplaintRepository.findByVictimId(victimId).stream()
        .map(complaintMapper::toComplaint)
        .toList();
  }

  @Override
  public List<Complaint> findAll() {
    return jpaComplaintRepository.findAll().stream()
        .map(complaintMapper::toComplaint)
        .toList();
  }

  @Override
  public List<Complaint> findAllByStatus(String status) {
    StatusEntity statusEntity = StatusEntity.valueOf(status);
    return jpaComplaintRepository.findAllByStatus(statusEntity).stream()
        .map(complaintMapper::toComplaint)
        .toList();
  }

  @Override
  public List<Complaint> findAllByViolenceType(String violenceType) {
    ViolenceTypeEntity violenceTypeEntity = ViolenceTypeEntity.valueOf(violenceType);
    return jpaComplaintRepository.findAllByViolenceType(violenceTypeEntity).stream()
        .map(complaintMapper::toComplaint)
        .toList();
  }

  @Override
  public List<Complaint> findAllByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    return jpaComplaintRepository.findByDateRange(startDate, endDate).stream()
        .map(complaintMapper::toComplaint)
        .toList();
  }

  @Override
  public Long countByStatus(String status) {
    StatusEntity statusEntity = StatusEntity.valueOf(status);
    return jpaComplaintRepository.countByStatus(statusEntity);
  }

  @Override
  public Long countByViolenceType(String violenceType) {
    ViolenceTypeEntity violenceTypeEntity = ViolenceTypeEntity.valueOf(violenceType);
    return jpaComplaintRepository.countByViolenceType(violenceTypeEntity);
  }

  @Override
  public Long countAll() {
    return jpaComplaintRepository.count();
  }
}
