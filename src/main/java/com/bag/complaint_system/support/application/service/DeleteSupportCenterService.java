package com.bag.complaint_system.support.application.service;

import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.shared.exception.RoleAccessDeniedException;
import com.bag.complaint_system.shared.exception.SupportCenterNotFoundException;
import com.bag.complaint_system.support.application.ports.input.DeleteSupportCenterUseCase;
import com.bag.complaint_system.support.application.ports.output.SupportPersistencePort;
import com.bag.complaint_system.support.domain.aggregate.SupportCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSupportCenterService implements DeleteSupportCenterUseCase {

  private final SupportPersistencePort supportPersistencePort;
  private final UserPersistencePort userPersistencePort;

  @Override
  public void execute(Long authId, Long centerId) {
    User authenticatedUser =
        userPersistencePort
            .findById(authId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!authenticatedUser.isAdmin()) {
      throw new RoleAccessDeniedException("Only admins can delete support centers");
    }

    SupportCenter center =
        supportPersistencePort.findById(centerId).orElseThrow(SupportCenterNotFoundException::new);

    center.deactivate();
    supportPersistencePort.save(center);
  }
}
