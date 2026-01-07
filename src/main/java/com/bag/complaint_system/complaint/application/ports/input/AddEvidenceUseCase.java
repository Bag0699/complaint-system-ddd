package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.response.EvidenceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AddEvidenceUseCase {

  EvidenceResponse execute(Long complaintId, Long authId, MultipartFile file);
}
