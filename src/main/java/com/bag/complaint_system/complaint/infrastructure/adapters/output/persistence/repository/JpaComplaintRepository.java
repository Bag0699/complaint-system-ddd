package com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.repository;

import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.entity.ComplaintEntity;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.utils.StatusEntity;
import com.bag.complaint_system.complaint.infrastructure.adapters.output.persistence.utils.ViolenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaComplaintRepository extends JpaRepository<ComplaintEntity, Long> {

  List<ComplaintEntity> findByVictimId(Long victimId);

  List<ComplaintEntity> findAllByStatus(StatusEntity status);

  List<ComplaintEntity> findAllByViolenceType(ViolenceTypeEntity violenceType);

  @Query("SELECT c FROM ComplaintEntity c WHERE c.createdAt BETWEEN :startDate AND :endDate")
  List<ComplaintEntity> findByDateRange(
      @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

  Long countByStatus(StatusEntity status);

  Long countByViolenceType(ViolenceTypeEntity violenceType);
}
