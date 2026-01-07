package com.bag.complaint_system.complaint.application.ports.input;

import com.bag.complaint_system.complaint.application.dto.request.CreateComplaintRequest;
import com.bag.complaint_system.complaint.application.dto.response.ComplaintDetailResponse;

public interface CreateComplaintUseCase {

  ComplaintDetailResponse execute(Long victimId, CreateComplaintRequest request);
}
