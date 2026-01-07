package com.bag.complaint_system.support.application.ports.input;

public interface DeleteSupportCenterUseCase {

  void execute(Long authId, Long centerId);
}
