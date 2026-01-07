package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.request.UpdateComplaintStatusRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;

public interface UpdateComplaintStatusUseCase {

  ComplaintDetailResponse execute(
      Long complaintId, Long authId, UpdateComplaintStatusRequest request);
}
