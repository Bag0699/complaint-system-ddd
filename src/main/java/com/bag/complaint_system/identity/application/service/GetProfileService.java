package com.bag.complaint_system.identity.application.service;

import com.bag.complaint_system.identity.application.dto.response.UserProfileResponse;
import com.bag.complaint_system.identity.application.mapper.UserMapper;
import com.bag.complaint_system.identity.application.ports.input.GetProfileUseCase;
import com.bag.complaint_system.identity.application.ports.output.UserPersistencePort;
import com.bag.complaint_system.identity.domain.model.aggregate.User;
import com.bag.complaint_system.identity.infrastructure.security.CustomUserDetail;
import com.bag.complaint_system.shared.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileService implements GetProfileUseCase {

  private final UserPersistencePort userPersistencePort;
  private final UserMapper userMapper;

  @Override
  public UserProfileResponse execute() {
    var authenticate = SecurityContextHolder.getContext().getAuthentication();

    if (authenticate == null || !authenticate.isAuthenticated()) {
      throw new SecurityException("User is not authenticated");
    }

    CustomUserDetail userDetail = (CustomUserDetail) authenticate.getPrincipal();
    Long userId = userDetail.getUserId();

    User user = userPersistencePort.findById(userId).orElseThrow(UserNotFoundException::new);
    return userMapper.toUserProfileResponse(user);
  }
}
