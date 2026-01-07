package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;

public interface GetComplaintDetailUseCase {

  ComplaintDetailResponse execute(Long complaintId, Long authId);
}
