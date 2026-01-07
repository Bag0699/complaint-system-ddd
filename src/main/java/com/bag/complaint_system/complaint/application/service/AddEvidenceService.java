package com.bag.complaint_system.complaint.application.service;

import com.bag.complaint_system.complaint.application.dto.response.EvidenceResponse;
import com.bag.complaint_system.complaint.application.mapper.EvidenceMapper;
import com.bag.complaint_system.complaint.application.ports.input.AddEvidenceUseCase;
import com.bag.complaint_system.complaint.application.ports.output.ComplaintPersistencePort;
import com.bag.complaint_system.complaint.application.ports.output.FileStoragePort;
import com.bag.complaint_system.complaint.domain.aggregate.Complaint;
import com.bag.complaint_system.complaint.domain.entity.Evidence;
import com.bag.complaint_system.shared.exception.ComplaintNotFoundException;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddEvidenceService implements AddEvidenceUseCase {

  private final ComplaintPersistencePort complaintPersistencePort;
  private final FileStoragePort fileStoragePort;
  private final EvidenceMapper evidenceMapper;

  @Override
  @Transactional
  public EvidenceResponse execute(Long complaintId, Long authId, MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is required");
    }

    Complaint complaint =
        complaintPersistencePort.findById(complaintId).orElseThrow(ComplaintNotFoundException::new);

    if (!complaint.belongsToVictim(authId)) {
      throw new RoleAccessDeniedException("You can only add evidence to your own complaints");
    }

    if (complaint.isClosed()) {
      throw new IllegalArgumentException("Complaint is already closed");
    }

    log.info("Storing file for complaint {}: {}", complaintId, file.getOriginalFilename());

    String filePath = fileStoragePort.addEvidence(file, complaintId);

    Evidence evidence =
        Evidence.create(
            file.getOriginalFilename(), filePath, file.getContentType(), file.getSize());

    complaint.addEvidence(evidence);

    Complaint updateComplaint = complaintPersistencePort.save(complaint);
    Evidence savedEvidence = updateComplaint.getEvidences().getLast();

    return evidenceMapper.toEvidenceResponse(savedEvidence);
  }
}
