package com.bag.complaint_system.complaint.application.ports.output;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {

  String addEvidence(MultipartFile file, Long complaintId);
}
