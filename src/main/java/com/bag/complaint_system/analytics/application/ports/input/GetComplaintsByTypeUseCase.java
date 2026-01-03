package com.bag.complaint_system.analytics.application.ports.input;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByTypeResponse;

public interface GetComplaintsByTypeUseCase {
  ComplaintByTypeResponse execute(Long authId);
}
