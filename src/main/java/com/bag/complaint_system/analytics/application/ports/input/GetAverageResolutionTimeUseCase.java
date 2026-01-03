package com.bag.complaint_system.analytics.application.ports.input;

public interface GetAverageResolutionTimeUseCase {
  Double execute(Long authId);
}
