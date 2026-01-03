package com.bag.complaint_system.support.application.ports.input;

import com.bag.complaint_system.support.application.dto.request.UpdateSupportCenterRequest;
import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;

public interface UpdateSupportCenterUseCase {

  SupportCenterResponse execute(Long authId, Long centerId, UpdateSupportCenterRequest request);
}
