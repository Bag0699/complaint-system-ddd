package com.bag.complaint_system.support.application.ports.input;

import com.bag.complaint_system.support.application.dto.request.CreateSupportCenterRequest;
import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;

public interface CreateSupportCenterUseCase {

  SupportCenterResponse execute(Long authId, CreateSupportCenterRequest request);
}
