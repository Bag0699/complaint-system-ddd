package com.bag.complaint_system.analytics.application.ports.input;

import com.bag.complaint_system.analytics.application.dto.response.ComplaintByStatusResponse;

public interface GetComplaintsByStatusUseCase {

  ComplaintByStatusResponse execute(Long authId);
}
