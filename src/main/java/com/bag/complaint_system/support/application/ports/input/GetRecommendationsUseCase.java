package com.bag.complaint_system.support.application.ports.input;

import com.bag.complaint_system.support.application.dto.response.SupportCenterResponse;

import java.util.List;

public interface GetRecommendationsUseCase {

  List<SupportCenterResponse> execute(String districtName);
}
